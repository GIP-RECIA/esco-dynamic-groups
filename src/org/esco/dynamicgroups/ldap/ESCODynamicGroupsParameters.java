/**
 * 
 */
package org.esco.dynamicgroups.ldap;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.ldap.syncrepl.client.ISyncReplMessagesHandlerBuilder;
import org.esco.dynamicgroups.util.PropertyParser;



/**
 * Contains the user parameters.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public class ESCODynamicGroupsParameters implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = -186884409502387377L;

    /** Files that contains the parameters, loaded from the class path. */
    private static final String ESCO_DG_PARAMETERS_FILE = "esco-dynamicGroups.properties";

    /** Prefix for the properties. */
    private static final String PROPERTIES_PREFIX = "esco.dynamic.groups.";

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCODynamicGroupsParameters.class);

    /** Singleton instance. */
    private static final ESCODynamicGroupsParameters INSTANCE = new ESCODynamicGroupsParameters();

    /** To convert milliseconds into seconds.*/
    private static final  int MILLIS_TO_SECONDS_FACTOR = 1000; 

    /** Properties instance used to initialize this instance. */
    private Properties parametersProperties;

    /** The LDAP host. */
    private String ldapHost;

    /** The LDAP port. */
    private int ldapPort;

    /** LDAP version. */
    private int ldapVersion;

    /** The bind DN to use for the ldap connection. */
    private String ldapBindDN;

    /** Credentials associated to the bind DN.*/
    private String ldapCredentials;

    /** The LDAP base for the search. */
    private String ldapSearchBase;

    /** Search filter. */
    private String ldapSearchFilter;

    /** Attributes returned by the search operations. */
    private Set<String> ldapSearchAttributes;

    /** LDAP id attribute. */
    private String ldapIdAttribute;

    /** Idle duration for the SyncRepl client. */
    private int syncreplClientIdle;

    /** Builder class for the SyncRepl messages Handler. */
    private Class<ISyncReplMessagesHandlerBuilder> syncReplMessageHandlerBuilderClass;

    /**
     * Constructor for ESCODynamicGroupsParameters.
     */
    private ESCODynamicGroupsParameters() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loading ESCODynamicGroupsParameters from file " + ESCO_DG_PARAMETERS_FILE);
            initialize();
            LOGGER.debug("Loaded values: " + this);
        }
    }

    /**
     * Gives the singleton.
     * @return The singleton.
     */
    public static ESCODynamicGroupsParameters instance() {
        return INSTANCE;
    }

    /**
     * Initialization.
     */
    private void initialize() {
        try {
            final ClassLoader cl = getClass().getClassLoader();
            final InputStream is  = cl.getResourceAsStream(ESCO_DG_PARAMETERS_FILE);
            final Properties params = new Properties();
            if (is == null) {
                LOGGER.error("Unable to load (from classpath) " + ESCO_DG_PARAMETERS_FILE);
            }
            params.load(is);
            loadFromProperties(params);
            setParametersProperties(params);

        } catch (InvalidPropertiesFormatException e) {
            LOGGER.error(e, e);
        } catch (IOException e) {
            LOGGER.error(e, e);
        }
    }

    /**
     * Loads the values from a properties instance.
     * @param params The properties that contains the values to load.
     */
    @SuppressWarnings("unchecked")
    private void loadFromProperties(final Properties params) {
        // keys used to retrieve the values in the properties instance. 
        final String ldapHostKey = PROPERTIES_PREFIX + "ldap.host";
        final String ldapPortKey = PROPERTIES_PREFIX + "ldap.port";
        final String ldapVersionKey = PROPERTIES_PREFIX + "ldap.version";
        final String ldapBindDNKey = PROPERTIES_PREFIX + "ldap.bind.dn";
        final String ldapCredentialsKey = PROPERTIES_PREFIX + "ldap.credentials";
        final String ldapSearchBaseKey = PROPERTIES_PREFIX + "ldap.search.base";
        final String ldapSearchFilterKey = PROPERTIES_PREFIX + "ldap.search.filter";
        final String ldapSearchAttributesKey = PROPERTIES_PREFIX + "ldap.search.attributes";
        final String ldapIdAttributeKey = PROPERTIES_PREFIX + "ldap.id.attribute";
        final String synreplClientIDLEKey = PROPERTIES_PREFIX + "syncrepl.client.idle";
        final String synreplHandlerBuilderKey = PROPERTIES_PREFIX + "syncrepl.handler.builder.class";

        // Retrieves the values.
        setLdapHost(parseStringFromProperty(params, ldapHostKey));
        setLdapPort(parseIntegerFromProperty(params, ldapPortKey));
        setLdapVersion(parseIntegerFromProperty(params, ldapVersionKey));
        setLdapBindDN(parseStringFromProperty(params, ldapBindDNKey));
        setLdapCredentials(parseStringFromProperty(params, ldapCredentialsKey));
        setLdapSearchBase(parseStringFromProperty(params, ldapSearchBaseKey));
        setLdapSearchFilter(parseStringFromProperty(params, ldapSearchFilterKey));
        setLdapSearchAttributesFromArray(parseStringArrayFromProperty(params, ldapSearchAttributesKey));
        setLdapIdAttribute(parseStringFromProperty(params, ldapIdAttributeKey));
        setSyncreplClientIdle(parseIntegerFromProperty(params, synreplClientIDLEKey) * MILLIS_TO_SECONDS_FACTOR);

        // Retrieves the class for the builder of SyncRepl Messages.
        final String builderClass = parseStringFromProperty(params, synreplHandlerBuilderKey);
        if (builderClass != null) {
            try {
                syncReplMessageHandlerBuilderClass = 
                    (Class<ISyncReplMessagesHandlerBuilder>) getClass().getClassLoader().loadClass(builderClass);

            } catch (ClassNotFoundException e) {
                LOGGER.fatal(e, e);
            }
        }

        // Adds the LDAP id attributes in the search attributes.
        ldapSearchAttributes.add(ldapIdAttribute);


    }

    /**
     * Retrieves the integer value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The Integer value if available in the properties, null otherwise.
     */
    private Integer parseIntegerFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseIntegerFromProperty(LOGGER, ESCO_DG_PARAMETERS_FILE, properties, key);
    }

    /**
     * Retrieves the string value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String value if available in the properties, null otherwise.
     */
    private String parseStringFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseStringFromProperty(LOGGER, ESCO_DG_PARAMETERS_FILE, properties, key);
    }

    /**
     * Retrieves the string array value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String array value if available in the properties, null otherwise.
     */
    private String[] parseStringArrayFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseStringArrayFromProperty(LOGGER, ESCO_DG_PARAMETERS_FILE, properties, key);
    }

    /**
     * Gives the string representation of the instance.
     * @return The String that denotes the values of the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append("#{LDAP Host: ");
        sb.append(getLdapHost());
        sb.append("; LDAP Port: ");
        sb.append(getLdapPort());
        sb.append("; LDAP Version: ");
        sb.append(getLdapVersion());
        sb.append("; LDAP Bind DN: ");
        sb.append(getLdapBindDN());
        sb.append("; LDAP Credentials: ");
        if (getLdapCredentials() == null) {
            sb.append(getLdapCredentials());
        } else {
            for (int i = 0; i < getLdapCredentials().length(); i++) {
                sb.append("*");
            }
        }
        sb.append("; LDAP Search base: ");
        sb.append(getLdapSearchBase());
        sb.append("; LDAP Search filter: ");
        sb.append(getLdapSearchFilter());
        sb.append("; LDAP Search attributes: ");
        if (getLdapSearchAttributes() == null) {
            sb.append(getLdapSearchAttributes());
        } else {
            sb.append(getLdapSearchAttributes());
        }

        sb.append("; SyncRepl Client idle: ");
        sb.append(getSyncreplClientIdle());

        sb.append("; SyncRepl Messages Handler builder: ");
        sb.append(getSyncReplMessageHandlerBuilderClass());

        sb.append("}");
        return sb.toString();
    }

    /**
     * Getter for ldapPort.
     * @return the ldapPort
     */
    public int getLdapPort() {
        return ldapPort;
    }

    /**
     * Setter for ldapPort.
     * @param ldapPort the ldapPort to set
     */
    public void setLdapPort(final int ldapPort) {
        this.ldapPort = ldapPort;
    }

    /**
     * Getter for ldapVersion.
     * @return the ldapVersion
     */
    public int getLdapVersion() {
        return ldapVersion;
    }

    /**
     * Setter for ldapVersion.
     * @param ldapVersion the ldapVersion to set
     */
    public void setLdapVersion(final int ldapVersion) {
        this.ldapVersion = ldapVersion;
    }

    /**
     * Getter for ldapBindDN.
     * @return the ldapBindDN
     */
    public String getLdapBindDN() {
        return ldapBindDN;
    }

    /**
     * Setter for ldapBindDN.
     * @param ldapBindDN the ldapBindDN to set
     */
    public void setLdapBindDN(final String ldapBindDN) {
        this.ldapBindDN = ldapBindDN;
    }

    /**
     * Getter for ldapCredentials.
     * @return the ldapCredentials
     */
    public String getLdapCredentials() {
        return ldapCredentials;
    }

    /**
     * Setter for ldapCredentials.
     * @param ldapCredentials the ldapCredentials to set
     */
    public void setLdapCredentials(final String ldapCredentials) {
        this.ldapCredentials = ldapCredentials;
    }

    /**
     * Getter for ldapSearchBase.
     * @return the ldapSearchBase
     */
    public String getLdapSearchBase() {
        return ldapSearchBase;
    }

    /**
     * Setter for ldapSearchBase.
     * @param ldapSearchBase the ldapSearchBase to set
     */
    public void setLdapSearchBase(final String ldapSearchBase) {
        this.ldapSearchBase = ldapSearchBase;
    }

    /**
     * Getter for ldapSearchFilter.
     * @return the ldapSearchFilter
     */
    public String getLdapSearchFilter() {
        return ldapSearchFilter;
    }

    /**
     * Setter for ldapSearchFilter.
     * @param ldapSearchFilter the ldapSearchFilter to set
     */
    public void setLdapSearchFilter(final String ldapSearchFilter) {
        this.ldapSearchFilter = ldapSearchFilter;
    }

    /**
     * Getter for ldapHost.
     * @return the ldapHost
     */
    public String getLdapHost() {
        return ldapHost;
    }

    /**
     * Setter for ldapHost.
     * @param ldapHost the ldapHost to set
     */
    public void setLdapHost(final String ldapHost) {
        this.ldapHost = ldapHost;
    }

    /**
     * Getter for ldapSearchAttributes.
     * @return the ldapSearchAttributes
     */
    public Set<String> getLdapSearchAttributes() {
        return ldapSearchAttributes;
    }

    /**
     * Gives the LDAP search attributes as an array.
     * @return The array of LDAP search attributes.
     */
    public String[] getLdapSearchAttributesAsArray() {
        return ldapSearchAttributes.toArray(new String[ldapSearchAttributes.size()]);
    }

    /**
     * Setter for ldapSearchAttributes.
     * @param ldapSearchAttributes the ldapSearchAttributes to set
     */
    public void setLdapSearchAttributes(final Set<String> ldapSearchAttributes) {
        this.ldapSearchAttributes = ldapSearchAttributes;
    }

    /**
     * Setter for ldapSearchAttributes.
     * @param ldapSearchAttributes the ldapSearchAttributes to set
     */
    protected void setLdapSearchAttributesFromArray(final String[] ldapSearchAttributes) {
        this.ldapSearchAttributes = new HashSet<String>(ldapSearchAttributes.length);
        for (String ldapSearchAttribute : ldapSearchAttributes) {
            this.ldapSearchAttributes.add(ldapSearchAttribute);
        }
    }

    /**
     * Getter for syncreplClientIdle.
     * @return the syncreplClientIdle
     */
    public int getSyncreplClientIdle() {
        return syncreplClientIdle;
    }

    /**
     * Setter for syncreplClientIdle.
     * @param syncreplClientIdle the syncreplClientIdle to set
     */
    public void setSyncreplClientIdle(final int syncreplClientIdle) {
        this.syncreplClientIdle = syncreplClientIdle;
    }

    /**
     * Getter for ldapIdAttribute.
     * @return the ldapIdAttribute
     */
    public String getLdapIdAttribute() {
        return ldapIdAttribute;
    }

    /**
     * Setter for ldapIdAttribute.
     * @param ldapIdAttribute the ldapIdAttribute to set
     */
    public void setLdapIdAttribute(final String ldapIdAttribute) {
        this.ldapIdAttribute = ldapIdAttribute;
    }

    /**
     * Getter for parametersProperties.
     * @return the parametersProperties
     */
    public Properties getParametersProperties() {
        return parametersProperties;
    }

    /**
     * Setter for parametersProperties.
     * @param parametersProperties the parametersProperties to set
     */
    public void setParametersProperties(final Properties parametersProperties) {
        this.parametersProperties = parametersProperties;
    }

    /**
     * Getter for syncReplMessageHandlerBuilderClass.
     * @return the syncReplMessageHandlerBuilderClass
     */
    public Class<ISyncReplMessagesHandlerBuilder> getSyncReplMessageHandlerBuilderClass() {
        return syncReplMessageHandlerBuilderClass;
    }

    /**
     * Setter for syncReplMessageHandlerBuilderClass.
     * @param syncReplMessageHandlerBuilderClass the syncReplMessageHandlerBuilderClass to set
     */
    public void setSyncReplMessageHandlerBuilderClass(
            final Class<ISyncReplMessagesHandlerBuilder> syncReplMessageHandlerBuilderClass) {
        this.syncReplMessageHandlerBuilderClass = syncReplMessageHandlerBuilderClass;
    }
}
