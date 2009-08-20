/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap;

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
import org.esco.dynamicgroups.domain.definition.AtomicProposition;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.IProposition;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.PersonsParametersSection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * DAO for the LDAP. Used for the initialization of the dynamic groups.
 * @author GIP RECIA - A. Deman
 * 19 janv. 2009
 *
 */
public class LDAPMembersFromDefinitionDAO implements IMembersFromDefinitionDAO, InitializingBean, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 3555266918663719714L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(LDAPMembersFromDefinitionDAO.class); 

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
    
    /** The user parameters. */
    private ParametersProvider parametersProvider;
    
    /** The user parameters for the LDAP. */
    private PersonsParametersSection ldapParameters;

    /** The uidAttribute as a String array. */
    private String[] uidAttributeArray;
    
    /** The ldap connection manager. */
    private LDAPConnectionManager connectionManager;

    /** The LDAP Connection. */
    private transient LDAPConnection connection;

    /** The LDAP search constaints. */
    private transient LDAPSearchConstraints constraints;

    /**
     * Builds an instance of DynamicGroupInitializer.
     */
    public LDAPMembersFromDefinitionDAO() {
       super();
    }

    /**
     * Checks the connection and tries to reconnect if needed.
     */
    private void checkConnection() {
        synchronized (connection) {
            if (!connectionManager.isActiveConnection(connection)) {
                connection = connectionManager.connect();
            }
        }
    } 
    
    /**
     * Checks the bean injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        
        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " + this.getClass().getName() 
                + " can't be null.");
        
        Assert.notNull(this.connectionManager, 
                "The property connectionManager in the class " + this.getClass().getName() 
                + " can't be null.");
        
        ldapParameters = (PersonsParametersSection) parametersProvider.getPersonsParametersSection();
        uidAttributeArray = new String[] {ldapParameters.getLdapUidAttribute()};
        connection = connectionManager.connect();
        constraints = new LDAPSearchConstraints();
        constraints.setMaxResults(0);
    }

    /**
     * Gives the members, in the ldap, that are corresponding to the logic definition of a group.
     * @param definition The logic definition of the members of the group.
     * @return The set of the members ids.
     * @see org.esco.dynamicgroups.dao.ldap.IMembersFromDefinitionDAO#getMembers(DynamicGroupDefinition)
     */
    public Set<String> getMembers(final DynamicGroupDefinition definition) {
        final Set<String> userIds = new HashSet<String>();
        if (definition.isValid()) {
            checkConnection();

            final String filter = translateToLdapFilter(definition);

            try {
                final LDAPSearchResults result = getConnection().search(ldapParameters.getLdapSearchBase(), 
                        LDAPConnection.SCOPE_SUB, 
                        filter, 
                        uidAttributeArray, 
                        false, 
                        constraints);
                
                while (result.hasMore()) {
                    final LDAPEntry entry = result.next();
                    userIds.add(entry.getAttribute(ldapParameters.getLdapUidAttribute()).getStringValue());
                }

            } catch (LDAPException e) {
                LOGGER.error("Error while trying to retrieve the members for the definition: " + definition 
                        + " - associated filter: " + filter);
                LOGGER.error(e, e);
            }
        }
        return userIds;
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
     * Getter for connection.
     * @return connection.
     */
    protected LDAPConnection getConnection() {
        synchronized (connection) {
            return connection;
        }
    }

    /**
     * Getter for parametersProvider.
     * @return parametersProvider.
     */
    public ParametersProvider getParametersProvider() {
        return parametersProvider;
    }

    /**
     * Setter for parameters.
     * @param parametersProvider the new value for parameters.
     */
    public void setParametersProvider(final ParametersProvider parametersProvider) {
        this.parametersProvider = parametersProvider;
    }

    /**
     * Getter for connectionManager.
     * @return connectionManager.
     */
    public LDAPConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Setter for connectionManager.
     * @param connectionManager the new value for connectionManager.
     */
    public void setConnectionManager(final LDAPConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


}
