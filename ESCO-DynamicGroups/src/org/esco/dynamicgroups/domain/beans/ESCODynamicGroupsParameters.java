/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.util.PropertyParser;



/**
 * Contains the user parameters.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public class ESCODynamicGroupsParameters implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = -186884409502387377L;

    /** Prefix for the properties. */
    private static final String PROPERTIES_PREFIX = "esco.dynamic.groups.";

    /** Name of the file to use for the ldap sync cookie. */
    private static final String DEF_SYNCREPL_COOKIE_FILE = "esco_dg.cookie";
   
    /** Default modulo for saving the cookie. */
    private static final int DEF_SYNCREPL_MODULO = 100;
    
    /** Default value for the idle loop when trying to reconnect to the ldap. */
    private static final int DEF_LDAP_RECONNECT_IDLE = 30;
    
    /** Default value for the number of retry for the reconnections. */
    private static final int DEF_LDAP_RECONNECT_ATTEMPTS_NB = 5;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCODynamicGroupsParameters.class);
    
    /** Singleton instance. */
    private static ESCODynamicGroupsParameters instance;

    /** To convert milliseconds into seconds.*/
    private static final  int MILLIS_TO_SECONDS_FACTOR = 1000; 
    
    /** The configuration file to use. */
    private String configurationFile;

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
    
    /** Idle value in seconds when trying to reconnect. */
    private int ldapReconnectionIdle;
    
    /** Number of retries for the ldap reconnections. */
    private int ldapReconnectionAttemptsNb;

    /** The LDAP base for the search. */
    private String ldapSearchBase;

    /** Search filter. */
    private String ldapSearchFilter;

    /** Attributes returned by the search operations. */
    private Set<String> ldapSearchAttributes;

    /** LDAP id attribute. */
    private String ldapUidAttribute;
    
    /** Replicat id. */
    private int syncReplRID;
    
    /** Name of the file to use for the cookie file. */
    private String syncReplCookieFile;
    
    /** The modulo for saving the cookie in its file. */
    private int syncReplCookieSaveModulo;

    /** Idle duration for the SyncRepl client. */
    private int syncreplClientIdle;

    /** The id of the subject source. */
    private String grouperSubjectsSourceId;

    /** The type in grouper associated to the dynamic groups. */
    private String grouperType;

    /** Flag to determine if the dynamic grouper type should be created
     * if it does not exist. */
    private boolean createGrouperType;

    /** Flag to determine if the dynamic groups should be reseted. */
    private boolean resetOnStartup;

    /** The field used in grouper to define the members. */
    private String grouperDefinitionField;

    /** The Grouper user used to open the Grouper sessions. */
    private String grouperUser;

    /** Flag used to detemine if a deleted user should be removed from
     * all the groups or only from the dynamic ones.*/
    private boolean removeFromAllGroups;

    /**
     * Constructor for ESCODynamicGroupsParameters.
     * @param configurationFile The configuration file to use.
     */
    private ESCODynamicGroupsParameters(final String configurationFile) {
        this.configurationFile = configurationFile;
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loading ESCODynamicGroupsParameters from file " + configurationFile);
        }
        
        initialize();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loaded values: " + this);
        }
        setInstance(this);
    }

    /**
     * Gives the singleton.
     * @return The singleton.
     */
    public static ESCODynamicGroupsParameters instance() {
        return instance;
    }

    /**
     * Initialization.
     */
    private void initialize() {
        try {
            final ClassLoader cl = getClass().getClassLoader();
            final InputStream is  = cl.getResourceAsStream(configurationFile);
            final Properties params = new Properties();
            if (is == null) {
                LOGGER.fatal("Unable to load (from classpath) " + configurationFile);
            }
            LOGGER.fatal("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Properties p = System.getProperties();
            for (Object k : p.keySet()) {
                LOGGER.fatal(k + " ====> " + p.get(k));
            }
            
            LOGGER.fatal("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            
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
    private void loadFromProperties(final Properties params) {
        // keys used to retrieve the values in the properties instance. 
        final String ldapHostKey = PROPERTIES_PREFIX + "ldap.host";
        final String ldapPortKey = PROPERTIES_PREFIX + "ldap.port";
        final String ldapVersionKey = PROPERTIES_PREFIX + "ldap.version";
        final String ldapBindDNKey = PROPERTIES_PREFIX + "ldap.bind.dn";
        final String ldapCredentialsKey = PROPERTIES_PREFIX + "ldap.credentials";
        final String ldapReconnectionIdleKey = PROPERTIES_PREFIX + "ldap.reconnection.idle";
        final String ldapReconnectionAttemptsNbKey = PROPERTIES_PREFIX + "reconnection.nb.attempts";
        final String ldapSearchBaseKey = PROPERTIES_PREFIX + "ldap.search.base";
        final String ldapSearchFilterKey = PROPERTIES_PREFIX + "ldap.search.filter";
        final String ldapSearchAttributesKey = PROPERTIES_PREFIX + "ldap.search.attributes";
        final String ldapUidAttributeKey = PROPERTIES_PREFIX + "ldap.uid.attribute";
        final String synreplClientIDLEKey = PROPERTIES_PREFIX + "syncrepl.client.idle";
        final String synreplRIDKey = PROPERTIES_PREFIX + "syncrepl.rid";
        final String synreplCookieFileKey = PROPERTIES_PREFIX + "syncrepl.cookie.file";
        final String synreplCookieSaveModuloKey = PROPERTIES_PREFIX + "syncrepl.cookie.save.modulo";
        final String grouperTypeKey = PROPERTIES_PREFIX + "grouper.type";
        final String grouperSubjSourceKey = PROPERTIES_PREFIX + "grouper.subjects.source";
        final String createGrouperTypeKey = PROPERTIES_PREFIX + "grouper.create.type";
        final String resetOnStartupKey = PROPERTIES_PREFIX + "grouper.reset.on.startup";
        final String grouperDefKey = PROPERTIES_PREFIX + "grouper.definiton.field";
        final String grouperUserKey = PROPERTIES_PREFIX + "grouper.user";
        final String removeFromAllGroupsKey = PROPERTIES_PREFIX + "grouper.remove.from.all.groups";

        // Retrieves the values.
        setLdapHost(parseStringFromProperty(params, ldapHostKey));
        setLdapPort(parseIntegerFromProperty(params, ldapPortKey));
        setLdapVersion(parseIntegerFromProperty(params, ldapVersionKey));
        setLdapBindDN(parseStringFromProperty(params, ldapBindDNKey));
        setLdapCredentials(parseStringFromProperty(params, ldapCredentialsKey));
        setLdapReconnectionIdle(parseStrictPositiveIntegerSafeFromProperty(params, ldapReconnectionIdleKey, 
                DEF_LDAP_RECONNECT_IDLE) * MILLIS_TO_SECONDS_FACTOR);
        setLdapReconnectionAttemptsNb(parseStrictPositiveIntegerSafeFromProperty(params, ldapReconnectionAttemptsNbKey, 
                DEF_LDAP_RECONNECT_ATTEMPTS_NB));
        setLdapSearchBase(parseStringFromProperty(params, ldapSearchBaseKey));
        setLdapSearchFilter(parseStringFromProperty(params, ldapSearchFilterKey));
        setLdapSearchAttributesFromArray(parseStringArrayFromProperty(params, ldapSearchAttributesKey));
        setLdapUidAttribute(parseStringFromProperty(params, ldapUidAttributeKey));
        setSyncReplRID(parsePositiveIntegerSafeFromProperty(params, synreplRIDKey, 0));
        setSyncreplClientIdle(parseIntegerFromProperty(params, synreplClientIDLEKey) * MILLIS_TO_SECONDS_FACTOR);
        setSyncReplCookieFile(parseStringSafeFromProperty(params, synreplCookieFileKey, DEF_SYNCREPL_COOKIE_FILE));
        setSyncReplCookieSaveModulo(parseStrictPositiveIntegerSafeFromProperty(params, synreplCookieSaveModuloKey, 
                DEF_SYNCREPL_MODULO));
        setGrouperSubjectsSourceId(parseStringFromProperty(params, grouperSubjSourceKey));
        setGrouperType(parseStringFromProperty(params, grouperTypeKey));
        setCreateGrouperType(parseBooleanFromProperty(params, createGrouperTypeKey)); 
        setResetOnStartup(parseBooleanFromProperty(params, resetOnStartupKey)); 
        setGrouperDefinitionField(parseStringFromProperty(params, grouperDefKey));
        setGrouperUser(parseStringFromProperty(params, grouperUserKey));
        setRemoveFromAllGroups(parseBooleanFromProperty(params, removeFromAllGroupsKey)); 

        // Adds the LDAP id attributes in the search attributes.
        ldapSearchAttributes.add(ldapUidAttribute);
    }

    /**
     * Retrieves the Boolean value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The Boolean value if available in the properties, null otherwise.
     */
    private Boolean parseBooleanFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseBooleanFromProperty(LOGGER, configurationFile, properties, key);
    }

    /**
     * Retrieves the integer value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The Integer value if available in the properties, null otherwise.
     */
    private Integer parseIntegerFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseIntegerFromProperty(LOGGER, configurationFile, properties, key);
    }
    
    /**
     * Retrieves a positive integer value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @param defaultValue The default value to use if the property is not found.
     * @return The positive  Integer value if available and valid in the properties, 0 otherwise.
     */
    private Integer parsePositiveIntegerSafeFromProperty(final Properties properties, 
            final String key, final int defaultValue) {
        Integer value =  PropertyParser.instance().parsePositiveIntegerFromPropertySafe(LOGGER, 
                configurationFile, properties, key);
        
        if (value == null) {
            LOGGER.warn("Unable to retrieve a valid value in the file: " + configurationFile
                    + " for the property: " + key
                    + " - Using the default value: " + defaultValue + ".");
            return defaultValue;
        }
        
        return value;
    }
    
    
    /**
     * Retrieves a positive integer value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @param defaultValue The default value to use if the property is not found.
     * @return The positive  Integer value if available and valid in the properties, 0 otherwise.
     */
    private Integer parseStrictPositiveIntegerSafeFromProperty(final Properties properties, 
            final String key, final int defaultValue) {
        Integer value =  PropertyParser.instance().parseStrictPositiveIntegerFromPropertySafe(LOGGER, 
                configurationFile, properties, key);
        
        if (value == null) {
            LOGGER.warn("Unable to retrieve a valid value in the file: " + configurationFile
                    + " for the property: " + key
                    + " - Using the default value: " + defaultValue + ".");
            return defaultValue;
        }
        
        return value;
    }

    /**
     * Retrieves the string value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String value if available in the properties, null otherwise.
     */
    private String parseStringFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseStringFromProperty(LOGGER, configurationFile, properties, key);
    }
    /**
     * Retrieves the string value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @param defaultValue The default value to use if the key is not in the Properties instance.
     * @return The String value if available in the properties, null otherwise.
     */
    private String parseStringSafeFromProperty(final Properties properties, 
            final String key, 
            final String defaultValue) {
        return PropertyParser.instance().parseStringFromPropertySafe(properties, key, defaultValue);
    }

    /**
     * Retrieves the string array value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String array value if available in the properties, null otherwise.
     */
    private String[] parseStringArrayFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseStringArrayFromProperty(LOGGER, configurationFile, properties, key);
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
        sb.append("; LDAP reconnection idle: ");
        sb.append(getLdapReconnectionIdle());
        sb.append("; LDAP reconnection attempts nb: ");
        sb.append(getLdapReconnectionAttemptsNb());
        
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

        sb.append("; SyncRepl cookie file: ");
        sb.append(getSyncReplCookieFile());

        sb.append("; SyncRepl cookie save modulo: ");
        sb.append(getSyncReplCookieSaveModulo());

        sb.append("; Grouper Subjects source: ");
        sb.append(getGrouperSubjectsSourceId());

        sb.append("; Grouper Type: ");
        sb.append(getGrouperType());

        sb.append("; Grouper definition field: ");
        sb.append(getGrouperDefinitionField());

        sb.append("; Create grouper type (if not present): ");
        sb.append(getCreateGrouperType());

        sb.append("; Reset dynamic groups on startup: ");
        sb.append(getResetOnStartup());

        sb.append("; Grouper user: ");
        sb.append(getGrouperUser());

        sb.append("; remove from all groups: ");
        sb.append(getRemoveFromAllGroups());

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
     * Getter for ldapUidAttribute.
     * @return the ldapUidAttribute
     */
    public String getLdapUidAttribute() {
        return ldapUidAttribute;
    }

    /**
     * Setter for ldapUidAttribute.
     * @param ldapUidAttribute the ldapUidAttribute to set
     */
    public void setLdapUidAttribute(final String ldapUidAttribute) {
        this.ldapUidAttribute = ldapUidAttribute;
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
     * Getter for grouperType.
     * @return grouperType.
     */
    public String getGrouperType() {
        return grouperType;
    }

    /**
     * Setter for grouperType.
     * @param grouperType the new value for grouperType.
     */
    public void setGrouperType(final String grouperType) {
        this.grouperType = grouperType;
    }

    /**
     * Getter for grouperUser.
     * @return grouperUser.
     */
    public String getGrouperUser() {
        return grouperUser;
    }

    /**
     * Setter for grouperUser.
     * @param grouperUser the new value for grouperUser.
     */
    public void setGrouperUser(final String grouperUser) {
        this.grouperUser = grouperUser;
    }

    /**
     * Getter for removeFromAllGroups.
     * @return removeFromAllGroups.
     */
    public boolean getRemoveFromAllGroups() {
        return removeFromAllGroups;
    }

    /**
     * Setter for removeFromAllGroups.
     * @param removeFromAllGroups the new value for removeFromAllGroups.
     */
    public void setRemoveFromAllGroups(final boolean removeFromAllGroups) {
        this.removeFromAllGroups = removeFromAllGroups;
    }

    /**
     * Getter for grouperDefinitionField.
     * @return grouperDefinitionField.
     */
    public String getGrouperDefinitionField() {
        return grouperDefinitionField;
    }

    /**
     * Setter for grouperDefinitionField.
     * @param grouperDefinitionField the new value for grouperDefinitionField.
     */
    public void setGrouperDefinitionField(final String grouperDefinitionField) {
        this.grouperDefinitionField = grouperDefinitionField;
    }

    /**
     * Getter for createGrouperType.
     * @return createGrouperType.
     */
    public boolean getCreateGrouperType() {
        return createGrouperType;
    }

    /**
     * Setter for createGrouperType.
     * @param createGrouperType the new value for createGrouperType.
     */
    public void setCreateGrouperType(final boolean createGrouperType) {
        this.createGrouperType = createGrouperType;
    }

    /**
     * Getter for resetOnStartup.
     * @return resetOnStartup.
     */
    public boolean getResetOnStartup() {
        return resetOnStartup;
    }

    /**
     * Setter for resetOnStartup.
     * @param resetOnStartup the new value for resetOnStartup.
     */
    public void setResetOnStartup(final boolean resetOnStartup) {
        this.resetOnStartup = resetOnStartup;
    }

    /**
     * Getter for grouperSubjectsSourceId.
     * @return grouperSubjectsSourceId.
     */
    public String getGrouperSubjectsSourceId() {
        return grouperSubjectsSourceId;
    }

    /**
     * Setter for grouperSubjectsSourceId.
     * @param grouperSubjectsSourceId the new value for grouperSubjectsSourceId.
     */
    public void setGrouperSubjectsSourceId(final String grouperSubjectsSourceId) {
        this.grouperSubjectsSourceId = grouperSubjectsSourceId;
    }

    /**
     * Getter for syncReplCookieFile.
     * @return syncReplCookieFile.
     */
    public String getSyncReplCookieFile() {
        return syncReplCookieFile;
    }

    /**
     * Setter for syncReplCookieFile.
     * @param syncReplCookieFile the new value for syncReplCookieFile.
     */
    public void setSyncReplCookieFile(final String syncReplCookieFile) {
        this.syncReplCookieFile = syncReplCookieFile;
    }

    /**
     * Getter for syncReplRID.
     * @return syncReplRID.
     */
    public int getSyncReplRID() {
        return syncReplRID;
    }

    /**
     * Setter for syncReplRID.
     * @param syncReplRID the new value for syncReplRID.
     */
    public void setSyncReplRID(final int syncReplRID) {
        this.syncReplRID = syncReplRID;
    }

    /**
     * Getter for syncReplCookieSaveModulo.
     * @return syncReplCookieSaveModulo.
     */
    public int getSyncReplCookieSaveModulo() {
        return syncReplCookieSaveModulo;
    }

    /**
     * Setter for syncReplCookieSaveModulo.
     * @param syncReplCookieSaveModulo the new value for syncReplCookieSaveModulo.
     */
    public void setSyncReplCookieSaveModulo(final int syncReplCookieSaveModulo) {
        this.syncReplCookieSaveModulo = syncReplCookieSaveModulo;
    }

    /**
     * Getter for ldapReconnectionIdle.
     * @return ldapReconnectionIdle.
     */
    public int getLdapReconnectionIdle() {
        return ldapReconnectionIdle;
    }

    /**
     * Setter for ldapReconnectionIdle.
     * @param ldapReconnectionIdle the new value for ldapReconnectionIdle.
     */
    public void setLdapReconnectionIdle(final int ldapReconnectionIdle) {
        this.ldapReconnectionIdle = ldapReconnectionIdle;
    }

    /**
     * Getter for ldapReconnectionAttemptsNb.
     * @return ldapReconnectionAttemptsNb.
     */
    public int getLdapReconnectionAttemptsNb() {
        return ldapReconnectionAttemptsNb;
    }

    /**
     * Setter for ldapReconnectionAttemptsNb.
     * @param ldapReconnectionAttemptsNb the new value for ldapReconnectionAttemptsNb.
     */
    public void setLdapReconnectionAttemptsNb(final int ldapReconnectionAttemptsNb) {
        this.ldapReconnectionAttemptsNb = ldapReconnectionAttemptsNb;
    }

    /**
     * Getter for configurationFile.
     * @return configurationFile.
     */
    public String getConfigurationFile() {
        return configurationFile;
    }

    /**
     * Setter for configurationFile.
     * @param configurationFile the new value for configurationFile.
     */
    public void setConfigurationFile(final String configurationFile) {
        this.configurationFile = configurationFile;
    }

    /**
     * Getter for instance.
     * @return instance.
     */
    protected static synchronized ESCODynamicGroupsParameters getInstance() {
        return instance;
    }

    /**
     * Setter for instance.
     * @param instance the new value for instance.
     */
    protected static synchronized void setInstance(final ESCODynamicGroupsParameters instance) {
        ESCODynamicGroupsParameters.instance = instance;
    }

   
}
