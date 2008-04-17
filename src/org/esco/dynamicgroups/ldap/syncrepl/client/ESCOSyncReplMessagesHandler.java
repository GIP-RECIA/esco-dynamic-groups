package org.esco.dynamicgroups.ldap.syncrepl.client;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.ldap.syncrepl.ldapsync.protocol.SyncStateControl;

import com.novell.ldap.LDAPControl;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPMessage;
import com.novell.ldap.LDAPSearchResult;

/**
 * Reference implementation for the LDAP Messages handler.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public class ESCOSyncReplMessagesHandler implements ISyncReplMessagesHandler {

    /** Serial version UID.*/
    private static final long serialVersionUID = -6625007312797809743L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCOSyncReplMessagesHandler.class); 
    
    /**
     * Constructor for ESCOSyncReplMessagesHandler.
     */
    public ESCOSyncReplMessagesHandler() {
       /* */ 
    }
    
    /**
     * Handles a given message.
     * @param message The message to handle.
     * @see org.esco.dynamicgroups.ldap.syncrepl.client.ISyncReplMessagesHandler
     * #processLDAPMessage(com.novell.ldap.LDAPMessage)
     */
    public void processLDAPMessage(final LDAPMessage message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Handling the message: " + message);
        }
        if (message instanceof LDAPSearchResult) {
            handleLDAPSearchResult((LDAPSearchResult) message);
        }
    }
    
    /**
     * Handles an LDAPSearchResult message.
     * @param searchResultMessage The message to handle.
     */
    protected void handleLDAPSearchResult(final LDAPSearchResult searchResultMessage) {
        final SyncStateControl control = retrieveSyncStateControl(searchResultMessage);
        
        if (control != null) {
            final LDAPEntry entry = searchResultMessage.getEntry(); 
            if (control.isAdd()) {
                
                LOGGER.debug("SyncStateControl with state ADD." 
                        +  entry.getAttribute("uid"));
            } else if (control.isDelete()) {
                LOGGER.debug("SyncStateControl with state DELETE.");
            } else if (control.isModify()) {
                LOGGER.debug("SyncStateControl with state MODIFY.");
            } else if (control.isPresent()) {
                LOGGER.debug("SyncStateControl with state PRESENT.");
            }
        } else {
            LOGGER.debug("No SyncStateControl in the message.");
        }
        
    }
    
    /**
     * Retrieves a SyncStateControl if present in a message.
     * @param message The considered message.
     * @return The control if found, null otherwise.
     */
    private SyncStateControl retrieveSyncStateControl(final LDAPMessage message) {
        for (LDAPControl control : message.getControls()) {
            if (control instanceof SyncStateControl) {
                return (SyncStateControl) control;
            }
        }
        return null;
    }
}
