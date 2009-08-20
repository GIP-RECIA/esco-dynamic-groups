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
public class PersonsParametersSection extends DGParametersSection implements IDynamicAttributesProvider {

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
    
    /** Default value for the SyncRepl log modulo. */
    private static final int DEF_SYNC_REPL_MESSAGES_LOG_MODULO = 0;
    
    /** Default value for the use of an ssl connection. */
    private static final boolean DEF_USE_SSL = false;
    
    /** Default value for skip until first cookie flag. */
    private static final boolean DEF_SKIP_UNTIL_COOKIE = false;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(PersonsParametersSection.class);

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
    
    /**  Modulo to determine when the SyncReplNotifications should be logged. */
    private int syncReplMessagesLogModulo;

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
    
    /** Flag to determine if an ssl connection should be used. */
    private boolean useSSLConnection;
    
    /** The path of the keystore to use in the case of an ssl connection. */
    private String keystorePath;
    
    /** Flag used to skip the message until the first cookie.*/
    private boolean skipMessagesUntilFirstCookie;

    /**
     * Constructor for PersonsParametersSection.
     */
    private PersonsParametersSection() {
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
        final String ldapUseSSLKey = PROPERTIES_PREFIX + "ldap.ssl.connection";
        final String ldapKeystorePathKey = PROPERTIES_PREFIX + "ldap.ssl.keystore.path";
        final String ldapBindDNKey = PROPERTIES_PREFIX + "ldap.bind.dn";
        final String ldapCredentialsKey = PROPERTIES_PREFIX + "ldap.credentials";
        final String ldapReconnectionIdleKey = PROPERTIES_PREFIX + "ldap.reconnection.idle.seconds";
        final String ldapReconnectionAttemptsNbKey = PROPERTIES_PREFIX + "ldap.reconnection.nb.attempts";
        final String ldapSearchBaseKey = PROPERTIES_PREFIX + "ldap.search.base";
        final String ldapSearchFilterKey = PROPERTIES_PREFIX + "ldap.search.filter";
        final String ldapSearchAttributesKey = PROPERTIES_PREFIX + "ldap.search.attributes";
        final String ldapUidAttributeKey = PROPERTIES_PREFIX + "ldap.uid.attribute";
        final String synreplClientIDLEKey = PROPERTIES_PREFIX + "syncrepl.client.idle.seconds";
        final String synreplRIDKey = PROPERTIES_PREFIX + "syncrepl.rid";
        final String synreplCookieFileKey = PROPERTIES_PREFIX + "syncrepl.cookie.file";
        final String synreplCookieSaveModuloKey = PROPERTIES_PREFIX + "syncrepl.cookie.save.modulo";
        final String syncReplMessagesLogModuloKey = PROPERTIES_PREFIX + "syncrepl.client.messages.log.modulo";
        final String skipMessagesUntilFirstCookieKey = PROPERTIES_PREFIX 
            + "syncrepl.client.messages.skip.until.first.cookie";

        // Retrieves the values.
        setLdapHost(parseStringFromProperty(params, ldapHostKey));
        setLdapPort(parseIntegerFromProperty(params, ldapPortKey));
        setLdapVersion(parseIntegerFromProperty(params, ldapVersionKey));
        setLdapBindDN(parseStringFromProperty(params, ldapBindDNKey));
        setUseSSLConnection(parseBooleanFromPropertySafe(params, ldapUseSSLKey, DEF_USE_SSL));
        if (getUseSSLConnection()) {
            setKeystorePath(parseStringFromProperty(params, ldapKeystorePathKey));
        }
        
        setLdapCredentials(parseStringFromProperty(params, ldapCredentialsKey));
        setLdapReconnectionIdle(parseStrictPositiveIntegerFromPropertySafe(params, ldapReconnectionIdleKey, 
                DEF_LDAP_RECONNECT_IDLE));
       setSyncReplMessagesLogModulo(parsePositiveIntegerFromPropertySafe(params, 
               syncReplMessagesLogModuloKey, DEF_SYNC_REPL_MESSAGES_LOG_MODULO));
        
        setLdapReconnectionAttemptsNb(parseStrictPositiveIntegerFromPropertySafe(params, ldapReconnectionAttemptsNbKey, 
                DEF_LDAP_RECONNECT_ATTEMPTS_NB));
        setLdapSearchBase(parseStringFromProperty(params, ldapSearchBaseKey));
        setLdapSearchFilter(parseStringFromProperty(params, ldapSearchFilterKey));
        setLdapSearchAttributesFromArray(parseStringArrayFromProperty(params, ldapSearchAttributesKey));
        setLdapUidAttribute(parseStringFromProperty(params, ldapUidAttributeKey));
        setSyncReplRID(parsePositiveIntegerFromPropertySafe(params, synreplRIDKey, 0));
        setSyncreplClientIdle(parseIntegerFromProperty(params, synreplClientIDLEKey));
        setSyncReplCookieFile(parseStringFromPropertySafe(params, synreplCookieFileKey, DEF_COOKIE_FILE));
        setSyncReplCookieSaveModulo(parseStrictPositiveIntegerFromPropertySafe(params, synreplCookieSaveModuloKey, 
                DEF_SYNCREPL_MODULO));
        setSkipMessagesUntilFirstCookie(parseBooleanFromPropertySafe(params, 
                skipMessagesUntilFirstCookieKey, DEF_SKIP_UNTIL_COOKIE));

    }

    /**

    /**
     * Gives the string representation of the instance.
     * @return The String that denotes the values of the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        toStringFormatSingleEntry(sb, getClass().getSimpleName() + "#{\n");

        toStringFormatProperty(sb, "Host: ", getLdapHost());
        toStringFormatProperty(sb, "Port: ", getLdapPort());
        toStringFormatProperty(sb, "Version: ", getLdapVersion());
        toStringFormatProperty(sb, "SSL Connection: ", getUseSSLConnection());
        if (getUseSSLConnection()) {
            toStringFormatProperty(sb, "Path to keystore: ", getKeystorePath());
        }
        toStringFormatProperty(sb, "Bind DN: ", getLdapBindDN());
        toStringFormatProperty(sb, "Credentials: ", toStringFormatPassword(getLdapCredentials()));
        toStringFormatProperty(sb, "Reconnection idle: ", getLdapReconnectionIdle());
        toStringFormatProperty(sb, "SyncRepl Log modulo: ", getSyncReplMessagesLogModulo());
        toStringFormatProperty(sb, "Reconnection attempts nb: ", getLdapReconnectionAttemptsNb());
        toStringFormatProperty(sb, "Search base: ", getLdapSearchBase());
        toStringFormatProperty(sb, "Search filter: ", getLdapSearchFilter());
        toStringFormatProperty(sb, "UID Attribute: ", getLdapUidAttribute());
        toStringFormatProperty(sb, "SyncRepl Search attributes: ", "");
        toStringFormatMultipleEntries(sb, getLdapSearchAttributes());
        toStringFormatProperty(sb, "SyncRepl Client idle: ", getSyncreplClientIdle());
        toStringFormatProperty(sb, "SyncRepl cookie file: ", getSyncReplCookieFile());
        toStringFormatProperty(sb, "Skip messages until first cookie: ", getSkipMessagesUntilFirstCookie());
        
        toStringFormatProperty(sb, "SyncRepl cookie save modulo: ", getSyncReplCookieSaveModulo());
        
        toStringFormatSingleEntry(sb, "}");
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

    /**
     * Getter for useSSLConnection.
     * @return useSSLConnection.
     */
    public boolean getUseSSLConnection() {
        return useSSLConnection;
    }

    /**
     * Setter for useSSLConnection.
     * @param useSSLConnection the new value for useSSLConnection.
     */
    public void setUseSSLConnection(final boolean useSSLConnection) {
        this.useSSLConnection = useSSLConnection;
    }

    /**
     * Getter for keystorePath.
     * @return keystorePath.
     */
    public String getKeystorePath() {
        return keystorePath;
    }

    /**
     * Setter for keystorePath.
     * @param keystorePath the new value for keystorePath.
     */
    public void setKeystorePath(final String keystorePath) {
        this.keystorePath = keystorePath;
    }

    /**
     * Getter for syncReplMessagesLogModulo.
     * @return syncReplMessagesLogModulo.
     */
    public int getSyncReplMessagesLogModulo() {
        return syncReplMessagesLogModulo;
    }

    /**
     * Setter for syncReplMessagesLogModulo.
     * @param syncReplMessagesLogModulo the new value for syncReplMessagesLogModulo.
     */
    public void setSyncReplMessagesLogModulo(final int syncReplMessagesLogModulo) {
        this.syncReplMessagesLogModulo = syncReplMessagesLogModulo;
    }

    /**
     * Getter for skipMessagesUntilFirstCookie.
     * @return skipMessagesUntilFirstCookie.
     */
    public boolean getSkipMessagesUntilFirstCookie() {
        return skipMessagesUntilFirstCookie;
    }

    /**
     * Setter for skipMessagesUntilFirstCookie.
     * @param skipMessagesUntilFirstCookie the new value for skipMessagesUntilFirstCookie.
     */
    public void setSkipMessagesUntilFirstCookie(final boolean skipMessagesUntilFirstCookie) {
        this.skipMessagesUntilFirstCookie = skipMessagesUntilFirstCookie;
    }
}
