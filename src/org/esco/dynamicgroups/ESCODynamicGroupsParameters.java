/**
 * 
 */
package org.esco.dynamicgroups;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.log4j.Logger;



/**
 * Contains the user parameters.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public class ESCODynamicGroupsParameters implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = -186884409502387377L;

    /** Files that contains the parameters, loaded from the classpath. */
    private static final String ESCO_DG_PARAMETERS_FILE = "esco-dynamicGroups.properties";

    /** Prefix for the properties. */
    private static final String PROPERTIES_PREFIX = "esco.dynamic.groups.";

    /** Separator for the attributes. */
    private static final String ATTRIBUTES_SEP = " ";

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCODynamicGroupsParameters.class);

    /** Singleton instance. loaded at the first use. */
    private static ESCODynamicGroupsParameters instance = new ESCODynamicGroupsParameters();

    /** Initialization flag. */
    private boolean loadedFromProperties;

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
    private String[] ldapSearchAttributes;

    /** The String representation of the instance. */
    private String stringRepresentation;


    /**
     * Constructor for ESCODynamicGroupsParameters.
     */
    private ESCODynamicGroupsParameters() {
        /* Private constructor. */
    }
    
    /**
     * Gives the singleton.
     * @return The singleton.
     */
    public static ESCODynamicGroupsParameters instance() {
        synchronized (instance) {
            if (!instance.isLoadedFromProperties()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Loading ESCODynamicGroupsParameters from file " + ESCO_DG_PARAMETERS_FILE);
                    instance.initialize();
                    LOGGER.debug("Loaded values: " + instance);
                }
            }

            return instance;
        }
    }

    /**
     * Initialization.
     */
    private void initialize() {
        try {
            stringRepresentation = null;
            final ClassLoader cl = getClass().getClassLoader();
            final InputStream is  = cl.getResourceAsStream(ESCO_DG_PARAMETERS_FILE);
            final Properties params = new Properties();
            if (is == null) {
                LOGGER.error("Unable to load (from classpath) " + ESCO_DG_PARAMETERS_FILE);
            }
            params.load(is);
            loadFromProperties(params);
            loadedFromProperties = true;

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
        final String ldapHostKey = PROPERTIES_PREFIX + "ldap.host";
        final String ldapPortKey = PROPERTIES_PREFIX + "ldap.port";
        final String ldapVersionKey = PROPERTIES_PREFIX + "ldap.version";
        final String ldapBindDNKey = PROPERTIES_PREFIX + "ldap.bind.dn";
        final String ldapCredentialsKey = PROPERTIES_PREFIX + "ldap.credentials";
        final String ldapSearchBaseKey = PROPERTIES_PREFIX + "ldap.search.base";
        final String ldapSearchFilterKey = PROPERTIES_PREFIX + "ldap.search.filter";
        final String ldapSearchAttributesKey = PROPERTIES_PREFIX + "ldap.search.attributes";

        setLdapHost(parseStringFromProperty(params, ldapHostKey));
        setLdapPort(parseIntegerFromProperty(params, ldapPortKey));
        setLdapVersion(parseIntegerFromProperty(params, ldapVersionKey));
        setLdapBindDN(parseStringFromProperty(params, ldapBindDNKey));
        setLdapCredentials(parseStringFromProperty(params, ldapCredentialsKey));
        setLdapSearchBase(parseStringFromProperty(params, ldapSearchBaseKey));
        setLdapSearchFilter(parseStringFromProperty(params, ldapSearchFilterKey));
        setLdapSearchAttributes(parseStringArrayFromProperty(params, ldapSearchAttributesKey));
    }

    /**
     * Retrieves the integer value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The Integer value if available in the properties, null otherwise.
     */
    private Integer parseIntegerFromProperty(final Properties properties, final String key) {
        final String strValue = properties.getProperty(key);
        if (strValue == null) {
            LOGGER.fatal("Unalble to find a value for " + key + " in file: " + ESCO_DG_PARAMETERS_FILE);
            return null;
        }
        try {
            final int intValue = Integer.parseInt(strValue.trim());
            return intValue;
        } catch (NumberFormatException e) {
            LOGGER.fatal("Invalid value for " + key + ": " + strValue + " in file: " + ESCO_DG_PARAMETERS_FILE);
            LOGGER.fatal(e, e);
            return null;
        }
    }

    /**
     * Retrieves the string value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String value if available in the properties, null otherwise.
     */
    private String parseStringFromProperty(final Properties properties, final String key) {
        final String strValue = properties.getProperty(key);
        if (strValue == null) {
            LOGGER.fatal("Unalble to find a value for " + key + " in file: " + ESCO_DG_PARAMETERS_FILE);
            return null;
        }
        return strValue.trim();
    }

    /**
     * Retrieves the string array value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String array value if available in the properties, null otherwise.
     */
    private String[] parseStringArrayFromProperty(final Properties properties, final String key) {
        final String strValue = properties.getProperty(key);
        if (strValue == null) {
            LOGGER.fatal("Unalble to find a value for " + key + " in file: " + ESCO_DG_PARAMETERS_FILE);
            return null;
        }
        final String[] arrayValue = strValue.split(ATTRIBUTES_SEP);
        for (int i = 0; i < arrayValue.length; i++) {
            arrayValue[i] = arrayValue[i].trim();
        }
        return arrayValue;
    }

    /**
     * Gives the string representation of the instance.
     * @return The String that denotes the vlues of the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (stringRepresentation == null) {
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
                sb.append(Arrays.toString(getLdapSearchAttributes()));
            }
            sb.append("}");
            stringRepresentation = sb.toString();
        }
        return stringRepresentation;
    }

    /**
     * Getter for loadedFromProperties.
     * @return the loadedFromProperties
     */
    public boolean isLoadedFromProperties() {
        return loadedFromProperties;
    }


    /**
     * Setter for loadedFromProperties.
     * @param loadedFromProperties the loadedFromProperties to set
     */
    public void setLoadedFromProperties(final boolean loadedFromProperties) {
        this.loadedFromProperties = loadedFromProperties;
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
    public String[] getLdapSearchAttributes() {
        return ldapSearchAttributes;
    }

    /**
     * Setter for ldapSearchAttributes.
     * @param ldapSearchAttributes the ldapSearchAttributes to set
     */
    public void setLdapSearchAttributes(final String[] ldapSearchAttributes) {
        this.ldapSearchAttributes = ldapSearchAttributes;
    }
}
