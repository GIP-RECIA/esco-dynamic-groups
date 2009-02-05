package org.esco.dynamicgroups.dao.ldap.syncrepl.client;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPControl;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPIntermediateResponse;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchQueue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.CookieManager;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncDoneControl;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncInfoMessage;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncRequestControl;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
import org.esco.dynamicgroups.util.ESCODynamicGroupsParameters;
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
        super();
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
            Thread.sleep(REFRESH_STAGE_IDLE);
        } catch (InterruptedException e) {
            /* Nothing to do. */
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
        
        final ESCODynamicGroupsParameters parameters = ESCODynamicGroupsParameters.instance();

        final int ldapPort = parameters.getLdapPort(); 
        final int ldapVersion =  parameters.getLdapVersion();        
        final String ldapHost = parameters.getLdapHost();
        final String bindDN = parameters.getLdapBindDN();
        final String credentials = parameters.getLdapCredentials();
        final String searchFilter = parameters.getLdapSearchFilter();
        final String searchBase = parameters.getLdapSearchBase();
        final String[] attributes = parameters.getLdapSearchAttributesAsArray();
        final int idle = parameters.getSyncreplClientIdle();


        LDAPConnection lc = new LDAPConnection();
        final LDAPSearchConstraints constraints = new LDAPSearchConstraints();
        LDAPSearchQueue queue = null;
        final SyncRequestControl syncRequestCtrl = 
            new SyncRequestControl(SyncRequestControl.REFRESH_AND_PERSIST, false);

        // Registers the new protocol implementation elements.
        LDAPIntermediateResponse.register(SyncInfoMessage.OID, SyncInfoMessage.class);
        LDAPControl.register(SyncDoneControl.OID, SyncDoneControl.class);
        LDAPControl.register(SyncStateControl.OID, SyncStateControl.class);

        try {
            lc.connect(ldapHost, ldapPort);
            lc.bind(ldapVersion, bindDN, credentials.getBytes("UTF8") );
            constraints.setControls(syncRequestCtrl);

            queue = lc.search(searchBase, 
                    LDAPConnection.SCOPE_SUB, 
                    searchFilter, 
                    attributes, 
                    false,                 
                    null, 
                    constraints);

            logger.info("SyncRepl Client connected.");

        } catch (LDAPException e) {
            logger.error(e, e);

            try { 
                lc.disconnect(); 
            } catch (LDAPException e2) {  
                e2.printStackTrace();
            }
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (queue != null) {
            int contextualIdle = REFRESH_STAGE_IDLE;
            while (!isStopRequest()) {
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
        }

        //disconnect from the server before exiting
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

