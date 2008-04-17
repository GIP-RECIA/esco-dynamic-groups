package org.esco.dynamicgroups.ldap.syncrepl.client;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPControl;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPIntermediateResponse;
import com.novell.ldap.LDAPMessage;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchQueue;
import com.novell.ldap.LDAPSearchResult;
import com.novell.ldap.LDAPSearchResultReference;
import com.novell.ldap.controls.LDAPEntryChangeControl;
import com.novell.ldap.controls.LDAPPersistSearchControl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.ESCODynamicGroupsParameters;
import org.esco.dynamicgroups.ldap.syncrepl.ldapsync.protocol.SyncDoneControl;
import org.esco.dynamicgroups.ldap.syncrepl.ldapsync.protocol.SyncInfoMessage;
import org.esco.dynamicgroups.ldap.syncrepl.ldapsync.protocol.SyncRequestControl;
import org.esco.dynamicgroups.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
/**
 * Test class for a Ldap SyncRepl client.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public class ESCOSyncReplClient {
    
    /** Number of idle loops between two marks. */
    private static final int MARK_INTERVAL = 2;
    
    /** Prefix for the marks. */
    private static final String MARK_PREFIX = "--- MARK --- ";
    
    /** Suffix for the marks. */
    private static final String MARK_SUFFIX = " ---";
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCOSyncReplClient.class);
    
    /** Message handler. */
    private ISyncReplMessagesHandler messageHandler;
    
    /** Counter for the idle loops. */
    private int idleCount;
    
    /**
     * Constructor for ESCOSyncReplClient.
     */
    private ESCOSyncReplClient() {
        /* private */
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
     * Launches the client.
     * @throws IOException 
     */
    public void launch() throws IOException {

        final ESCODynamicGroupsParameters parameters = ESCODynamicGroupsParameters.instance();

        final int ldapPort = parameters.getLdapPort(); 
        final int ldapVersion =  parameters.getLdapVersion();        
        final String ldapHost = parameters.getLdapHost();
        final String bindDN = parameters.getLdapBindDN();
        final String credentials = parameters.getLdapCredentials();
        final String searchFilter = parameters.getLdapSearchFilter();
        final String searchBase = parameters.getLdapSearchBase();
        final String[] attributes = parameters.getLdapSearchAttributes();
        final int idle = parameters.getSyncreplClientIdle();
        try {
            final ISyncReplMessagesHandlerBuilder messageHandlerBuilder = 
                parameters.getSyncReplMessageHandlerBuilderClass().newInstance();
            messageHandler = messageHandlerBuilder.buildHandler();
        } catch (InstantiationException e1) {
            LOGGER.fatal(e1, e1);
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            LOGGER.fatal(e1, e1);
        }

        LDAPConnection lc = new LDAPConnection();
        final LDAPSearchConstraints constraints = new LDAPSearchConstraints();
        LDAPSearchQueue queue = null;
        final SyncRequestControl syncRequestCtrl = 
            new SyncRequestControl(SyncRequestControl.REFRESH_AND_PERSIST, null, false);

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

        } catch (LDAPException e) {
            System.out.println("Error: " + e.toString());
            try { 
                lc.disconnect(); 
            } catch (LDAPException e2) {  
                e2.printStackTrace();
            }
            System.exit(1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {   
            while (true) {
                if (!queue.isResponseReceived()) {
                        Thread.sleep(idle);
                        idleCount++;
                        mark();
                    
                } else {
                    try {
                        idleCount = 0;
                        messageHandler.processLDAPMessage(queue.getResponse());
                    } catch (LDAPException e) {
                        e.printStackTrace();
                    }
                }
//                if (!checkForAChange(queue)) {
//                    break;
//                }
//                Thread.sleep(IDLE_LOOP);
            }
        } catch (InterruptedException e) {
            /* Noting to do */
        }

        //disconnect from the server before exiting
        try {
            System.out.println("Abandon the search");
            lc.abandon(queue);
            lc.disconnect();
        }  catch (LDAPException e) {
            System.out.println();
            System.out.println("Error: " + e.toString());
        }
        System.exit(0);
    }

    /**
     * 
     * @param queue
     * @return true if there is a change.
     */
    private static boolean checkForAChange(final LDAPSearchQueue queue) {

        LDAPMessage message;
        boolean result = true;

        try {
            message = queue.getResponse();
            System.out.println("MESSAGE =>>> " + message + "<=== " + queue.isResponseReceived());
             
            if (queue.isResponseReceived()) {

                //message = queue.getResponse();
                if (message == null) {
                    System.out.println("MESSAGE NUL.");
                } else {
                    System.out.println("MESSAGE: " + message.getClass().getSimpleName() + " => " + message);

                    // is the response a search result reference?

                    if (message instanceof SyncInfoMessage) {
                        System.out.println("INSTANCE of intermediate message");
                    } else if (message instanceof LDAPSearchResultReference) {

                        String[] urls = ((LDAPSearchResultReference) message).getReferrals();
                        System.out.println("\nSearch result references:");
                        for (int i = 0; i < urls.length; i++) {
                            System.out.println(urls[i]);
                        }
                    } else if (message instanceof LDAPSearchResult) {
                        System.out.println("--LDAPSEARCHRESULT--");
                        LDAPControl[] controls = message.getControls();

                        for (int i = 0; i < controls.length; i++) {
                            System.out.println("CONTROL " + controls[i].getClass().getSimpleName() 
                                    + "==>" + controls[i]);
                            if (controls[i] instanceof LDAPEntryChangeControl) {
                                LDAPEntryChangeControl ecCtrl = (LDAPEntryChangeControl) controls[i];

                                int changeType = ecCtrl.getChangeType();
                                System.out.println("\n\nchange type: " + getChangeTypeString(changeType));
                                if (changeType == LDAPPersistSearchControl.MODDN) {
                                    System.out.println("Prev. DN: " + ecCtrl.getPreviousDN());
                                }

                                if (ecCtrl.getHasChangeNumber()) {
                                    System.out.println("Change Number: " + ecCtrl.getChangeNumber());
                                }

                                LDAPEntry entry = ((LDAPSearchResult) message).getEntry();
                                System.out.println("entry: " + entry.getDN());
                            }

                        }
                    }
                }

            } else {
                System.out.println("No message");
            }
        } catch (LDAPException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * Writes a mark in the log if needed.
     */
    private void mark() {
        if (idleCount == MARK_INTERVAL) {
            idleCount = 0;
            LOGGER.info(MARK_PREFIX + Calendar.getInstance().getTime() + MARK_SUFFIX);
        }
    }
    
    /**
     * Return a string indicating the type of change represented by the
     * changeType parameter.
     * @param changeType The change type.
     * @return The string indicating the type of change.
     */
    private static String getChangeTypeString(final int changeType) {

        String changeTypeString;
        switch (changeType) {
        case LDAPPersistSearchControl.ADD:
            changeTypeString = "ADD";
            break;
        case LDAPPersistSearchControl.MODIFY:
            changeTypeString = "MODIFY";
            break;
        case LDAPPersistSearchControl.MODDN:
            changeTypeString = "MODDN";
            break;
        case LDAPPersistSearchControl.DELETE:
            changeTypeString = "DELETE";
            break;
        default:
            changeTypeString = "Unknown change type: " + String.valueOf(changeType);
        break;
        }
        return changeTypeString;
    }
}

