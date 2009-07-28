package org.esco.dynamicgroups.dao.ldap.syncrepl.client;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPControl;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPIntermediateResponse;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchQueue;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.LDAPConnectionManager;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.CookieManager;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncDoneControl;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncInfoMessage;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncRequestControl;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
import org.esco.dynamicgroups.domain.parameters.LDAPPersonsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Test class for a Ldap SyncRepl client.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public class ESCOSyncReplClient implements Serializable, InitializingBean {

    /** Serial version UID.*/
    private static final long serialVersionUID = -3854237483388730172L;

    /** Number of idle loops between two marks. */
    private static final int MARK_INTERVAL = 10;
    
    /** To convert seconds into milliseconds. */
    private static final int SECONDS_TO_MILLIS_FACTOR = 1000;

    /** Idle duration for the initialization step. */
    private static final int REFRESH_STAGE_IDLE = 1000;

    /** Prefix for the marks. */
    private static final String MARK_PREFIX = "--- MARK --- ";

    /** Suffix for the marks. */
    private static final String MARK_SUFFIX = " ---";

    /** Logger. */
    private  Logger logger = Logger.getLogger(ESCOSyncReplClient.class);
    
    /** The user parameters provider. */
    private ParametersProvider parametersProvider;
    
    /** The LDAP Parameters. */
    private LDAPPersonsParametersSection ldapParameters;

    /** Messages handler. */
    private ISyncReplMessagesHandler messagesHandler;
    
    
    /** The LDAP connection manager. */
    private LDAPConnectionManager connectionManager;

    /** The connection to the LDAP. */
    private transient LDAPConnection lc;

    /** The LDAP search queue. */
    private transient LDAPSearchQueue queue;

    /** Counter for the idle loops. */
    private int idleCount;

    /** Running flag. */
    private boolean running;

    /** Flag for a stop request. */
    private boolean stopRequest;
    
    /** The cookie manager. */
    private CookieManager cookieManager;

    /**
     * Builds an instance of ESCOSyncReplClient.
     */
    public ESCOSyncReplClient() {

        // Registers the new protocol implementation elements.
        LDAPIntermediateResponse.register(SyncInfoMessage.OID, SyncInfoMessage.class);
        LDAPControl.register(SyncDoneControl.OID, SyncDoneControl.class);
        LDAPControl.register(SyncStateControl.OID, SyncStateControl.class);

        logger.debug(getClass().getSimpleName() + " created.");
    }

    /**
     * Checks the properties after the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.messagesHandler, 
                "The property messagesHandler in the class " + this.getClass().getName() 
                + " can't be null.");
        
        Assert.notNull(this.connectionManager, 
                "The property connectionManager in the class " + this.getClass().getName() 
                + " can't be null.");
        
        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " + this.getClass().getName() 
                + " can't be null.");
        
        Assert.notNull(this.cookieManager, 
                "The property cookieManager in the class " + this.getClass().getName() 
                + " can't be null.");
        
        ldapParameters = (LDAPPersonsParametersSection) parametersProvider.getPersonsParametersSection();
       
    }

    /**
     * @param args Not used.
     * @throws IOException
     */
    public static void main(final String[] args ) throws IOException {
        ESCOSyncReplClient client  = new ESCOSyncReplClient();
        client.launch();
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
     * Connects to the ldap and initializes the search.
     */
    private void connect() {

        lc = connectionManager.connect();

        if (!connectionManager.isActiveConnection(lc)) {
            logger.fatal("Client requested to stop.");
            requestToStop();
        } else {
            final String searchFilter = ldapParameters.getLdapSearchFilter();
            final String searchBase = ldapParameters.getLdapSearchBase();
            final Set<String> attributesSet = ldapParameters.getLdapSearchAttributes();
            final int nbAtt = attributesSet.size(); 
            final String[] attributes = attributesSet.toArray(new String[nbAtt]);

            try {

                // Creates the search constraint.
                final LDAPSearchConstraints constraints =  new LDAPSearchConstraints();
                final LDAPControl syncRequestCtrl = 
                    new SyncRequestControl(SyncRequestControl.REFRESH_AND_PERSIST, 
                            false, cookieManager.getCookie());
                constraints.setControls(syncRequestCtrl);
                constraints.setMaxResults(0);

                queue = lc.search(searchBase, 
                        LDAPConnection.SCOPE_SUB, 
                        searchFilter, 
                        attributes, 
                        false,                 
                        null, 
                        constraints);
                logger.info("---------------------------------");
                logger.info("SyncRepl Client connected.");
                logger.info("---------------------------------");
            } catch (LDAPException e) {
                logger.fatal(e, e);

            } catch (IOException e) {
                logger.fatal(e, e);
            }

        }
    }


    /**
     * Checks the connection and tries to reconnect if needed.
     */
    private void checkConnection() {
        if (!connectionManager.isActiveConnection(lc)) {
            connect();
        }
    } 

    /**
     * Launches the client.
     * @throws IOException 
     */
    public void launch() throws IOException {

        logger.info("Starting the SyncRepl Client.");
        setRunning(true);
        setStopRequest(false);
        connect();

        final int idle = ldapParameters.getSyncreplClientIdle() * SECONDS_TO_MILLIS_FACTOR;

        if (queue != null) {
            int contextualIdle = REFRESH_STAGE_IDLE;
            while (!isStopRequest()) {
                if (queue != null) {
                    if (!queue.isResponseReceived()) {
                        sleepSafe(contextualIdle);
                        idleCount++;
                        mark();

                    } else {
                        try {
                            contextualIdle = idle;
                            idleCount = 0;
                            messagesHandler.processLDAPMessage(queue.getResponse());
                        } catch (LDAPException e) {
                            e.printStackTrace();
                            logger.error(e, e);
                        }
                    }
                }
                checkConnection();
            }
        }

        //Disconnect from the server before exiting.
        try {

            lc.abandon(queue);
            lc.disconnect();
        }  catch (LDAPException e) {
            logger.error(e, e);
        }
        cookieManager.saveCurrentCookie();
        logger.info("SyncRepl Client stopped.");
        setRunning(false);

    }

    /**
     * Writes a mark in the log if needed.
     */
    private void mark() {
        if (idleCount == MARK_INTERVAL) {
            idleCount = 0;
            logger.info(MARK_PREFIX + Calendar.getInstance().getTime() + MARK_SUFFIX); 
        }
    }

    /**
     * Getter for messagesHandler.
     * @return messagesHandler.
     */
    public ISyncReplMessagesHandler getMessagesHandler() {
        return messagesHandler;
    }

    /**
     * Setter for messagesHandler.
     * @param messagesHandler the new value for messagesHandler.
     */
    public void setMessagesHandler(final ISyncReplMessagesHandler messagesHandler) {
        this.messagesHandler = messagesHandler;
    }

    /**
     * Getter for running.
     * @return running.
     */
    public synchronized boolean isRunning() {
        return running;
    }

    /**
     * Request the client to stop.
     */
    public synchronized void requestToStop() {
        stopRequest = true;
    }

    /**
     * Setter for stop request.
     * @param stopRequest The new value for stop request.
     */
    protected void setStopRequest(final boolean stopRequest) {
        this.stopRequest = stopRequest;
    }

    /**
     * Getter for stopRequest.
     * @return stopRequest.
     */
    protected synchronized boolean isStopRequest() {
        return stopRequest;
    }

    /**
     * Setter for running.
     * @param running the new value for running.
     */
    protected synchronized void setRunning(final boolean running) {
        this.running = running;
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
     * Getter for cookieManager.
     * @return cookieManager.
     */
    public CookieManager getCookieManager() {
        return cookieManager;
    }

    /**
     * Setter for cookieManager.
     * @param cookieManager the new value for cookieManager.
     */
    public void setCookieManager(final CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }
}

