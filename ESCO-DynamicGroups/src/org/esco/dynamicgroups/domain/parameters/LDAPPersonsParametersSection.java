/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;


import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;



/**
 * Section for the parameters associated to the backen that contains the
 * persons information.
 * @author GIP RECIA - A. Deman
 * 27 mai 2009
 *
 */
public class LDAPPersonsParametersSection extends DGParametersSection implements IDynamicAttributesProvider {

    /** Serial version UID. */
    private static final long serialVersionUID = -4955164877146141642L;

    /** Name of the file to use for the ldap sync cookie. */
    private static final String DEF_COOKIE_FILE = "esco_dg.cookie";

    /** Default modulo for saving the cookie. */
    private static final int DEF_SYNCREPL_MODULO = 1;

    /** Default value for the idle loop when trying to reconnect to the ldap. */
    private static final int DEF_LDAP_RECONNECT_IDLE = 30;

    /** Default value for the number of retry for the reconnections. */
    private static final int DEF_LDAP_RECONNECT_ATTEMPTS_NB = 5;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(LDAPPersonsParametersSection.class);

        /** To convert seconds into milliseconds.*/
    private static final  int SECONDS_TO_MILLIS_FACTOR = 1000; 

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

    /**
     * Constructor for ParametersProvider.
     */
    private LDAPPersonsParametersSection() {
        super();
    }
    
    /**
     * Gives the logger for this class.
     * @return The logger for this class.
     * @see org.esco.dynamicgroups.domain.parameters.DGParametersSection#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    /**
     * 
     * Loads the values from a properties instance.
     * @param params The properties that contains the values to load.
     * @see org.esco.dynamicgroups.domain.parameters.DGParametersSection#loadFromProperties(java.util.Properties)
     */
    @Override
    public void loadFromProperties(final Properties params) {
        // keys used to retrieve the values in the properties instance. 
        final String ldapHostKey = PROPERTIES_PREFIX + "ldap.host";
        final String ldapPortKey = PROPERTIES_PREFIX + "ldap.port";
        final String ldapVersionKey = PROPERTIES_PREFIX + "ldap.version";
        final String ldapBindDNKey = PROPERTIES_PREFIX + "ldap.bind.dn";
        final String ldapCredentialsKey = PROPERTIES_PREFIX + "ldap.credentials";
        final String ldapReconnectionIdleKey = PROPERTIES_PREFIX + "ldap.reconnection.idle";
        final String ldapReconnectionAttemptsNbKey = PROPERTIES_PREFIX + "ldap.reconnection.nb.attempts";
        final String ldapSearchBaseKey = PROPERTIES_PREFIX + "ldap.search.base";
        final String ldapSearchFilterKey = PROPERTIES_PREFIX + "ldap.search.filter";
        final String ldapSearchAttributesKey = PROPERTIES_PREFIX + "ldap.search.attributes";
        final String ldapUidAttributeKey = PROPERTIES_PREFIX + "ldap.uid.attribute";
        final String synreplClientIDLEKey = PROPERTIES_PREFIX + "syncrepl.client.idle";
        final String synreplRIDKey = PROPERTIES_PREFIX + "syncrepl.rid";
        final String synreplCookieFileKey = PROPERTIES_PREFIX + "syncrepl.cookie.file";
        final String synreplCookieSaveModuloKey = PROPERTIES_PREFIX + "syncrepl.cookie.save.modulo";
       
        // Retrieves the values.
        setLdapHost(parseStringFromProperty(params, ldapHostKey));
        setLdapPort(parseIntegerFromProperty(params, ldapPortKey));
        setLdapVersion(parseIntegerFromProperty(params, ldapVersionKey));
        setLdapBindDN(parseStringFromProperty(params, ldapBindDNKey));
        setLdapCredentials(parseStringFromProperty(params, ldapCredentialsKey));
        setLdapReconnectionIdle(parseStrictPositiveIntegerSafeFromProperty(params, ldapReconnectionIdleKey, 
                DEF_LDAP_RECONNECT_IDLE) * SECONDS_TO_MILLIS_FACTOR);
        setLdapReconnectionAttemptsNb(parseStrictPositiveIntegerSafeFromProperty(params, ldapReconnectionAttemptsNbKey, 
                DEF_LDAP_RECONNECT_ATTEMPTS_NB));
        setLdapSearchBase(parseStringFromProperty(params, ldapSearchBaseKey));
        setLdapSearchFilter(parseStringFromProperty(params, ldapSearchFilterKey));
        setLdapSearchAttributesFromArray(parseStringArrayFromProperty(params, ldapSearchAttributesKey));
        setLdapUidAttribute(parseStringFromProperty(params, ldapUidAttributeKey));
        setSyncReplRID(parsePositiveIntegerSafeFromProperty(params, synreplRIDKey, 0));
        setSyncreplClientIdle(parseIntegerFromProperty(params, synreplClientIDLEKey) * SECONDS_TO_MILLIS_FACTOR);
        setSyncReplCookieFile(parseStringSafeFromProperty(params, synreplCookieFileKey, DEF_COOKIE_FILE));
        setSyncReplCookieSaveModulo(parseStrictPositiveIntegerSafeFromProperty(params, synreplCookieSaveModuloKey, 
                DEF_SYNCREPL_MODULO));

    }

    /**
   
    /**
     * Gives the string representation of the instance.
     * @return The String that denotes the values of the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append("#{");
        sb.append("; LDAP Host: ");
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
            sb.append(getLdapCredentials().replaceAll(".", "\\*"));
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

        sb.append("}");
        return sb.toString();
    }

    /**
     * Gives the dynamic attributes.
     * @return The array of the dynamic attributes.
     * @see org.esco.dynamicgroups.domain.parameters.IDynamicAttributesProvider#getDynamicAttributes()
     */
    public Set<String> getDynamicAttributes() {
        return getLdapSearchAttributes();
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
     * Gives the attributes considered in the dynamic defininitions.
     * @return the attributes.
     */
    public Set<String> getAttributesForDynamicDefinition() {
        return getLdapSearchAttributes();
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
}
