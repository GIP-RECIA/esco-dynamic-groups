/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.parameters.LDAPPersonsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Used to manage the connections to the LDAP.
 * @author GIP RECIA - A. Deman
 * 28 avr. 2009
 *
 */
public class LDAPConnectionManager implements InitializingBean, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -9019129173624950239L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(LDAPConnectionManager.class);

    /** The user parameters provider. */
    private ParametersProvider parametersProvider;
    
    /** LDAP parameters. */
    private LDAPPersonsParametersSection ldapParameters;
    /**
     * Builds an instance of LDAPConnectionManager.
     */
    private LDAPConnectionManager() {
       super();
    }

    /**
     * Checks the bean injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(parametersProvider, "The property parametersProvider in the class " 
                + getClass().getName() + " can't be null.");
       ldapParameters = (LDAPPersonsParametersSection) parametersProvider.getPersonsParametersSection();
        
    } 
   
    /**
     * Creates the connexion to the ldap. 
     * @return The LDAP Connexion.
     */
    private LDAPConnection connectInternal() {

        final int ldapPort = ldapParameters.getLdapPort(); 
        final int ldapVersion =  ldapParameters.getLdapVersion();        
        final String ldapHost = ldapParameters.getLdapHost();
        final String bindDN = ldapParameters.getLdapBindDN();
        final String credentials = ldapParameters.getLdapCredentials();
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
        final int reconnectionIdle = ldapParameters.getLdapReconnectionIdle();
        final int nbMaxAttempts = ldapParameters.getLdapReconnectionAttemptsNb();
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
}

