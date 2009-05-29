/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.parameters.LDAPPersonsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;
import org.esco.dynamicgroups.util.ServletContextResourcesUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Cookie manager for the LdapSync Protocol.
 * @author GIP RECIA - A. Deman
 * 4 févr. 2009
 *
 */
public class CookieManager implements InitializingBean, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 7772446526416999140L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(CookieManager.class);

    /** Value used to denote an invalid RID. */
    private static final int INVALID_RID = -1;
    
    /** CSN entry in the cookie. */
    private static final String CSN_ENTRY = "csn=";

    /** RID entry in the cookie. */
    private static final String RID_ENTRY = "rid=";

    /** End of the default csn entry in the cookie. */
    private static final String DEFAULT_CSN_SUFFIX = "#000000#00#000000";

    /** Separator for the fields CSN and RID. */
    private static final String CSN_RID_FIELDS_SEP = ",";

    /** Pattern for the date in the csn entry of a new cookie. */
    private static final String DATE_PATTERN = "yyyyddMMhhmmss";

    /** Formatter for the date in the csn entry of a new cookie. */
    private static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat(DATE_PATTERN);

    /** Suffix for the date field in the entry csn. */
    private static final String TIMEZONE_SUFFIX = "Z";
   
    /** The user parameters provider. */
    private ParametersProvider parametersProvider;
    
    /** The LDAP Parameters. */
    private LDAPPersonsParametersSection ldapParameters;

    /** The cookie file. */
    private File cookieFile;
    
    /** Change counter to determine when to save the cookie. */
    private int changesCount;

    /** The last cookie. */
    private byte[] currentCookie;
    
    /** Util class to read and write the cookie under the servlet context. */
    private ServletContextResourcesUtil resourceUtil;

    /**
     * Builds an instance of CookieManager.
     */
    private CookieManager() {
       super();
    }
    
    /**
     * Checks the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.parametersProvider, "The property parametersProvider in the class " 
                + getClass().getName() + " can't be null.");
        Assert.notNull(this.resourceUtil, "The property resourceUtil in the class " 
                + getClass().getName() + " can't be null.");
        ldapParameters = (LDAPPersonsParametersSection) parametersProvider.getPersonsParametersSection();
        
        initializeCookie(); 
    }
    
    /**
     * Gives the String representation of this instance.
     * @return The string representation of this instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + new String(currentCookie) + "}";
    }

    /**
     * Writes the cookie into the target file. 
     * @param cookie The cookie to write.
     */
    private void write(final byte[] cookie) {
        try {
            
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Writing the cookie into the file: " + getCookieFile().getCanonicalPath());
            }

            final PrintWriter writer = new PrintWriter(getCookieFile());
            writer.println(new String(cookie));
            writer.close();
        } catch (IOException e) {
            LOGGER.error(e, e);
        }
    }

    /**
     * Tries to load the las cookie from the file system.
     * @return the cookie if a file can be loaded, null otherwise.
     */
    private byte[] read() {
        final String cookieFileName = ldapParameters.getSyncReplCookieFile();
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Loading the cookie from the file: " + getCookieFile().getCanonicalPath());
            }
            final BufferedReader reader = new BufferedReader(new FileReader(getCookieFile()));
            final String line = reader.readLine();
            reader.close();
            if (line != null) {
                return line.getBytes();
            } 
            LOGGER.warn("Empty cookie file: " + cookieFileName);
        } catch (FileNotFoundException e) {
            LOGGER.info("No cookie loaded from the file: " + cookieFileName);
        } catch (IOException e) {
            LOGGER.error(e, e);
        }

        return null;
    }

    /**
     * Initialization of the cookie.
     */
    private synchronized void initializeCookie() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Initializing the cookie - file: " + getCookieFile().getAbsolutePath());
        }
        
        currentCookie = read();
        final int rid = ldapParameters.getSyncReplRID();
        final String cookieFileName = ldapParameters.getSyncReplCookieFile();
        if (currentCookie == null) {
            currentCookie = buildNewCookie();
        } else {

            // Checks that the rid is not changed. 
            final int previousRID = extractRID();
            if (rid != previousRID) {
                LOGGER.warn(" The rid extract from the cookie file: " + cookieFileName 
                        + " (" + previousRID + ") is different from the rid given as a parameter ("
                        + rid + ") using the rid: " + rid + ".");
                currentCookie = buildNewCookie();
            } 
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Cookie initialized: " + new String(currentCookie) + ".");
        }
    }

    /**
     * Tries to retrieve the rid value in the cookie file.
     * @return The previous rid value if it can be read in the cookie file,
     * -1 otherwise.
     */
    private Integer extractRID() {
        final String cookieText = new String(currentCookie);
        final int ridPos = cookieText.indexOf(RID_ENTRY);
        if (ridPos > 0) {
            final String ridValue = cookieText.substring(ridPos + RID_ENTRY.length());
            try {
                return Integer.parseInt(ridValue);
            } catch (NumberFormatException e) {
                LOGGER.error("Unable to retrieve the rid in the cookie file: " 
                        + ldapParameters.getSyncReplCookieFile());
                LOGGER.error(e, e);
            }
        }
        return INVALID_RID;
    }

    /**
     * Builds a new cookie.
     * @return The new cookie.
     */
    private byte[] buildNewCookie() {
        final Date date = Calendar.getInstance().getTime();
        final String dateEntry = DATE_FORMATER.format(date) + TIMEZONE_SUFFIX;
        final String cookie = CSN_ENTRY + dateEntry + DEFAULT_CSN_SUFFIX 
            + CSN_RID_FIELDS_SEP + RID_ENTRY + ldapParameters.getSyncReplRID();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating a new cookie: " + cookie);
        }

        return cookie.getBytes();
    }

    /**
     * Updates the las cookie.
     * @param newCookie The new value for the cookie.
     */
    public synchronized void updateCurrentCookie(final byte[] newCookie) {
        if (newCookie != null) {
            this.currentCookie = newCookie;
            changesCount++;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Number of changes: " + changesCount + " Cookie updated: " + new String(currentCookie) );
                LOGGER.debug("Cookie updated: " + new String(currentCookie));
            }
            
            if (changesCount % ldapParameters.getSyncReplCookieSaveModulo() == 0) {
                write(currentCookie);
                changesCount = 0;
            }
        }
    }

    /**
     * Saves the cookie associated to a SyncDone control.
     */
    public synchronized void saveCurrentCookie() {
        write(currentCookie);
    }

    /**
     * Loads the cookie from a file into a SyncDone control.
     * @return The current value of currentCookie.
     */
    public synchronized byte[] getCurrentCookie() {
        return currentCookie;
    }

    /**
     * Getter for cookieFile.
     * @return cookieFile.
     */
    public File getCookieFile() {
        if (cookieFile == null) {
            try {
                cookieFile = resourceUtil.getResource(ldapParameters.getSyncReplCookieFile()).getFile();
            } catch (IOException e) {
               LOGGER.fatal(e, e);
               throw new DynamicGroupsException(e);
            }
        }
        return cookieFile;
    }

    /**
     * Getter for parametersProvider.
     * @return parametersProvider.
     */
    public ParametersProvider getParametersProvider() {
        return parametersProvider;
    }

    /**
     * Setter for parametersProvider.
     * @param parametersProvider the new value for parametersProvider.
     */
    public void setParametersProvider(final ParametersProvider parametersProvider) {
        this.parametersProvider = parametersProvider;
    }

       /**
     * Getter for resourceUtil.
     * @return resourceUtil.
     */
    public ServletContextResourcesUtil getResourceUtil() {
        return resourceUtil;
    }

    /**
     * Setter for resourceUtil.
     * @param resourceUtil the new value for resourceUtil.
     */
    public void setResourceUtil(final ServletContextResourcesUtil resourceUtil) {
        this.resourceUtil = resourceUtil;
    }

 
}
