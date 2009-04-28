/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;

/**
 * @author GIP RECIA - A. Deman
 * 28 avr. 2009
 *
 */
public class LDAPConnectionManager implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -9019129173624950239L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(LDAPConnectionManager.class);

    /** singleton. */
    private static LDAPConnectionManager instance;

    /** Idle interval when trying to reconnect to the ldap. */
    private int reconnectionIdle;

    /** Number of attempts when trying to reconnect to the ldap. */
    private int nbMaxAttempts;

    /**
     * Builds an instance of LDAPConnectionManager.
     */
    private LDAPConnectionManager() {
        reconnectionIdle = ESCODynamicGroupsParameters.instance().getLdapReconnectionIdle();
        nbMaxAttempts = ESCODynamicGroupsParameters.instance().getLdapReconnectionAttemptsNb();
    }


    /**
     * Gives the available instance of LDAP connextion manager.
     * @return The singleton.
     */
    public static LDAPConnectionManager instance() {
        if (instance == null) {
            instance = new LDAPConnectionManager();
        }
        return instance;
    }

    /**
     * Creates the connexion to the ldap. 
     * @return The LDAP Connexion.
     */
    private LDAPConnection connectInternal() {
        final ESCODynamicGroupsParameters parameters = ESCODynamicGroupsParameters.instance();

        final int ldapPort = parameters.getLdapPort(); 
        final int ldapVersion =  parameters.getLdapVersion();        
        final String ldapHost = parameters.getLdapHost();
        final String bindDN = parameters.getLdapBindDN();
        final String credentials = parameters.getLdapCredentials();
        final LDAPConnection lc = new LDAPConnection();
        try {

            lc.connect(ldapHost, ldapPort);
            lc.bind(ldapVersion, bindDN, credentials.getBytes("UTF8") );
        } catch (LDAPException e) {
            LOGGER.error(e, e);

            try { 
                lc.disconnect(); 
            } catch (LDAPException e2) {  
                LOGGER.error(e2, e2);
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e, e);
        } 
        return lc;
    }

    /**
     * Tests if a connection is active.
     * @param lc The connection to test.
     * @return True if the connection is valid and active.
     */
    public boolean isActiveConnection(final LDAPConnection lc) {
        if (lc == null) {
            return false;
        }
        return lc.isConnected() && lc.isConnectionAlive();
    }
    
    /**
     * Sleep method without interruption notifiction.
     * @param millis The sleep duration time.
     */
    private void sleepSafe(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            /* Nothing to do. */
        }
    }

    /**
     * Tries to connect to the LDAP.
     * @return A new connection or null if there is a problem.
     */
    public LDAPConnection connect() {
        int nbAttempts = 0;
        LDAPConnection newConnection = null;
        while (!isActiveConnection(newConnection) && nbAttempts++ < nbMaxAttempts) {
            if (nbAttempts > 1) {
                sleepSafe(reconnectionIdle);
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Connection to the LDAP seems to be closed. Trying to connect - attempt: " 
                        + nbAttempts + "/" + nbMaxAttempts + ".");
            }
            newConnection = connectInternal();
        }
        if (nbAttempts == nbMaxAttempts) {
            LOGGER.fatal("Unable to connect to the ldap.");
        }
        return newConnection;
    } 

}

