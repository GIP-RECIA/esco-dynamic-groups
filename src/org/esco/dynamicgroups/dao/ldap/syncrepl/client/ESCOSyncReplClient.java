package org.esco.dynamicgroups.dao.ldap.syncrepl.client;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPControl;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPIntermediateResponse;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchQueue;

import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.LDAPConnectionManager;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.CookieManager;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncDoneControl;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncInfoMessage;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncRequestControl;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Test class for a Ldap SyncRepl client.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public class ESCOSyncReplClient implements InitializingBean {

    /** Number of idle loops between two marks. */
    private static final int MARK_INTERVAL = 10;

    /** Idle duration for the initialization step. */
    private static final int REFRESH_STAGE_IDLE = 1000;

    /** Prefix for the marks. */
    private static final String MARK_PREFIX = "--- MARK --- ";

    /** Suffix for the marks. */
    private static final String MARK_SUFFIX = " ---";

    /** Logger. */
    private  Logger logger = Logger.getLogger(ESCOSyncReplClient.class);

    /** Messages handler. */
    private ISyncReplMessagesHandler messagesHandler;

    /** The connection to the LDAP. */
    private LDAPConnection lc;

    /** The LDAP search queue. */
    private LDAPSearchQueue queue;

    /** Counter for the idle loops. */
    private int idleCount;

    /** Running flag. */
    private boolean running;

    /** Flag for a stop request. */
    private boolean stopRequest;

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

        lc = LDAPConnectionManager.instance().connect();

        if (!LDAPConnectionManager.instance().isActiveConnection(lc)) {
            logger.fatal("Client requested to stop.");
            requestToStop();
        } else {
            final ESCODynamicGroupsParameters parameters = ESCODynamicGroupsParameters.instance();
            final String searchFilter = parameters.getLdapSearchFilter();
            final String searchBase = parameters.getLdapSearchBase();
            final String[] attributes = parameters.getLdapSearchAttributesAsArray();

            try {

                // Creates the search constraint.
                final LDAPSearchConstraints constraints =  new LDAPSearchConstraints();
                final LDAPControl syncRequestCtrl = 
                    new SyncRequestControl(SyncRequestControl.REFRESH_AND_PERSIST, false);
                constraints.setControls(syncRequestCtrl);
                constraints.setMaxResults(0);

                queue = lc.search(searchBase, 
                        LDAPConnection.SCOPE_SUB, 
                        searchFilter, 
                        attributes, 
                        false,                 
                        null, 
                        constraints);
                logger.info("SyncRepl Client connected.");
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
        if (!LDAPConnectionManager.instance().isActiveConnection(lc)) {
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

        final int idle = ESCODynamicGroupsParameters.instance().getSyncreplClientIdle();

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
        CookieManager.instance().saveCurrentCookie();
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
}

