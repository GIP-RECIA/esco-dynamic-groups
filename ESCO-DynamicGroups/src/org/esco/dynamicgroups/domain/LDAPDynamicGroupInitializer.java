/**
 * 
 */
package org.esco.dynamicgroups.domain;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchResults;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.grouper.IGroupsDAOService;
import org.esco.dynamicgroups.dao.ldap.LDAPConnectionManager;
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.esco.dynamicgroups.domain.definition.AtomicProposition;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.IProposition;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * Initializer for the new created dynamic groups. 
 * The group definition is translated into an LDAP filter in order
 * to retrieve the initial members in the LDAP.
 * This initializer is also responsible to register or not the group in the base,
 * depending if their definition is valid.
 * @author GIP RECIA - A. Deman
 * 19 janv. 2009
 *
 */
public class LDAPDynamicGroupInitializer implements IDynamicGroupInitializer, InitializingBean, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 3555266918663719714L;

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

    /** The group service to use. */
    private IGroupsDAOService groupsService;

    /** The LDAP search base. */
    private String ldapSearchBase;

    /** The name of the uid attribute. */
    private String uidAttribute;
    
    /** The uidAttribute as a String array. */
    private String[] uidAttributeArray;
    
    /** The LDAP Connection. */
    private LDAPConnection connection;
    
    /** The LDAP search constaints. */
    private LDAPSearchConstraints constraints;

    /**
     * Builds an instance of LDAPDynamicGroupInitializer.
     */
    public LDAPDynamicGroupInitializer() {
        this(false);
    }

    /**
     * Builds an instance of LDAPDynamicGroupInitializer.
     * @param offline Flag to determine if a connection to the LDAP should be established.
     */
    public LDAPDynamicGroupInitializer(final boolean offline) {
        if (!offline) {
            
            ldapSearchBase = ESCODynamicGroupsParameters.instance().getLdapSearchBase();
            uidAttribute = ESCODynamicGroupsParameters.instance().getLdapUidAttribute();
            uidAttributeArray = new String[] {uidAttribute};
            connection = LDAPConnectionManager.instance().connect();
            constraints = new LDAPSearchConstraints();
            constraints.setMaxResults(0);
        }
    }
    
    /**
     * Checks the connection and tries to reconnect if needed.
     */
    private void checkConnection() {
        synchronized (connection) {
            if (!LDAPConnectionManager.instance().isActiveConnection(connection)) {
                connection = LDAPConnectionManager.instance().connect();
            }
        }
    } 
    /**
     * Checks the bean injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.groupsService, 
                "The property groupService in the class " + this.getClass().getName() 
                + " can't be null.");
    }

    /**
     * Initializes the group : removes all the existing members and retrieves the 
     * new members from the group definition.
     * @param definition The dfinition associated to the group to initialize.
     * @see org.esco.dynamicgroups.domain.IDynamicGroupInitializer#initialize(DynamicGroupDefinition)
     */
    public void initialize(final DynamicGroupDefinition definition) {
        if (definition.isValid()) {
            checkConnection();
            
            final String filter = translateToLdapFilter(definition);
           
            try {
                final LDAPSearchResults result = getConnection().search(ldapSearchBase, 
                        LDAPConnection.SCOPE_SUB, 
                        filter, 
                        uidAttributeArray, 
                        false, 
                        constraints);
                
                final Set<String> userIds = new HashSet<String>();
                while (result.hasMore()) {
                    final LDAPEntry entry = result.next();
                    userIds.add(entry.getAttribute(uidAttribute).getStringValue());
                }

                // Adds the members to the list.
                groupsService.addToGroup(definition.getGroupName(), userIds);

            } catch (LDAPException e) {
                LOGGER.error(e, e);
            }
        }
    }
   
    /**
     * Translates a conjunctive proposition into an ldap filter.
     * @param proposition The conjunctive proposition.
     * @return The ldap filter string.
     */
    protected String translateConjPropToLDAPFilter(final IProposition proposition) {

        List<AtomicProposition> atoms = proposition.getAtomicPropositions();
        final StringBuilder sb = new StringBuilder();
        if (atoms.size() > 0) {
            if (atoms.size() == 1) {
                final AtomicProposition atom = atoms.get(0);
                sb.append(OPEN_BRACKET);
                sb.append(atom.getAttribute());
                sb.append(EQUAL);
                sb.append(atom.getValue());
                sb.append(CLOSE_BRACKET);

                if (atom.isNegative()) {
                    sb.insert(0, NOT);
                    sb.insert(0, OPEN_BRACKET);
                    sb.append(CLOSE_BRACKET);
                }

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
        List<IProposition> conjProps = definition.getConjunctivePropositions();
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

    /**
     * Getter for groupsService.
     * @return groupsService.
     */
    public IGroupsDAOService getGroupsService() {
        return groupsService;
    }

    /**
     * Setter for groupsService.
     * @param groupsService the new value for groupsService.
     */
    public void setGroupsService(final IGroupsDAOService groupsService) {
        this.groupsService = groupsService;
    }

    /**
     * Getter for connection.
     * @return connection.
     */
    protected LDAPConnection getConnection() {
        synchronized (connection) {
            return connection;
        }
    }


}
