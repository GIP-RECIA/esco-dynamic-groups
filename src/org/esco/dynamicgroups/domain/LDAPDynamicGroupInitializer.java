/**
 * 
 */
package org.esco.dynamicgroups.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.definition.AtomicProposition;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.IProposition;
import org.esco.dynamicgroups.util.ESCODynamicGroupsParameters;

/**
 * @author GIP RECIA - A. Deman
 * 19 janv. 2009
 *
 */
public class LDAPDynamicGroupInitializer implements IDynamicGroupInitializer, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 3555266918663719714L;
    
    /** Factory for the ldap context.*/
    private static final String LDAP_CTX_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(LDAPDynamicGroupInitializer.class); 
    
    /** And constant fot the LDAP filters. */
    private static final String AND = "&";
    
    /** OR constant fot the LDAP filters. */
    private static final String OR = "|";
    
    /** Not constant fot the LDAP filters. */
    private static final String NOT = "!";
    
    /** Equal constant. */
    private static final String EQUAL = "=";
    
    /** Open bracket constant. */
    private static final char OPEN_BRACKET = '(';
    
    /** Close bracket constant. */
    private static final char CLOSE_BRACKET = ')';
    
    /** The LDAP ldapContext. */
    private DirContext ldapContext;


    /**
     * Builds an instance of LDAPDynamicGroupInitializer.
     */
    public LDAPDynamicGroupInitializer() {
        connectToLDAP();
        
    }
    
    /**
     * Initializes the group : removes all the existing members and retrieves the 
     * new members from the group definition.
     * @param definition The dfinition associated to the group to initialize.
     * @see org.esco.dynamicgroups.domain.IDynamicGroupInitializer#initialize(DynamicGroupDefinition)
     */
    public void initialize(final DynamicGroupDefinition definition) {
        SearchControls ctls = new SearchControls();
        String filter = translateToLdapFilter(definition);

        try {
           NamingEnumeration<SearchResult> answer = ldapContext.search(ESCODynamicGroupsParameters.instance().getLdapSearchBase(), filter, ctls);
          while (answer.hasMore()) {
               System.out.println("=> " + answer.next());
           }
        } catch (NamingException e) {
          
           e.printStackTrace();
       }
    }
    
    /**
     * Performs the LDAP connexion.
     */
    protected void connectToLDAP() {
        final ESCODynamicGroupsParameters params = ESCODynamicGroupsParameters.instance();
        final String ldapURL = "ldap://" + params.getLdapHost() + ":" + params.getLdapPort() + "/"; 
        
        LOGGER.info("Connecting ldap: " + ldapURL);
        
        Properties ldapEnv = new Properties();

        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CTX_FACTORY);
        ldapEnv.put(Context.PROVIDER_URL, ldapURL);
        ldapEnv.put(Context.SECURITY_PRINCIPAL, params.getLdapBindDN());
        ldapEnv.put(Context.SECURITY_CREDENTIALS, params.getLdapCredentials());

        try {
            ldapContext = new InitialDirContext(ldapEnv);

        } catch (NameAlreadyBoundException e ) {
            LOGGER.fatal(e, e);
            System.exit(1);
        } catch (NamingException e) {
            LOGGER.fatal(e, e);
            System.exit(1);
        } 
    }


    /**
     * Translates a conjunctive proposition into an ldap filter.
     * @param proposition The conjunctive proposition.
     * @return The ldap filter string.
     */
    protected String translateConjPropToLDAPFilter(final IProposition proposition) {
        
        List<IProposition> atoms = proposition.getAtomicPropositions();
        final StringBuilder sb = new StringBuilder();
        if (atoms.size() > 0) {
            if (atoms.size() == 1) {
                final AtomicProposition prop = (AtomicProposition) atoms.get(0);
                sb.append(OPEN_BRACKET);
                sb.append(prop.getAttribute());
                sb.append(EQUAL);
                sb.append(prop.getValue());
                sb.append(CLOSE_BRACKET);
            } else {
            sb.append(OPEN_BRACKET);
            sb.append(AND);
           
            for (IProposition atom : atoms) {
                final AtomicProposition prop = (AtomicProposition) atom;
                StringBuilder sb2 =  new StringBuilder();
                sb2.append(OPEN_BRACKET);
                sb2.append(prop.getAttribute());
                sb2.append(EQUAL);
                sb2.append(prop.getValue());
                sb2.append(CLOSE_BRACKET);
                if (prop.isNegative()) {
                    sb2.insert(0, NOT);
                    sb2.insert(0, OPEN_BRACKET);
                    sb2.append(CLOSE_BRACKET);
                }
                sb.append(sb2);  
            }
            sb.append(CLOSE_BRACKET);
        }
        }
        return sb.toString();
    }

    /**
     * Translates a dynamic group definition into an ldap filter.
     * @param definition The dynamic group definition.
     * @return The LDAP filter string that corresponds to the group.
     */
    public String translateToLdapFilter(final DynamicGroupDefinition definition) {
        final StringBuilder sb = new StringBuilder();
        List<IProposition> conjProps = definition.getConjunctiveProposition();
        if (conjProps.size() > 0) {
            if (conjProps.size() == 1) {
                sb.append(translateConjPropToLDAPFilter(conjProps.get(0)));
            } else {
                sb.append(OPEN_BRACKET);
                sb.append(OR);
                
                for (IProposition conjProp : conjProps) {
                    sb.append(translateConjPropToLDAPFilter(conjProp));
                }
                sb.append(CLOSE_BRACKET);
            }
        }
        return sb.toString();
    }
    
}
