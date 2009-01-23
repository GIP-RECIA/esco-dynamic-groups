package org.esco.dynamicgroups.dao.ldap.syncrepl.client;


import com.novell.ldap.LDAPControl;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPMessage;
import com.novell.ldap.LDAPSearchResult;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Reference implementation for the LDAP Messages handler.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public class ESCOSyncReplMessagesHandler implements ISyncReplMessagesHandler, InitializingBean {

    /** Serial version UID.*/
    private static final long serialVersionUID = -6625007312797809743L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCOSyncReplMessagesHandler.class); 
    
    /** Add Action.*/
    private ISyncReplTriggeredAction addAction;

    /** Modify Action.*/
    private ISyncReplTriggeredAction modifyAction;

    /** Delete Action.*/
    private ISyncReplTriggeredAction deleteAction;

    /** Present Action.*/
    private ISyncReplTriggeredAction presentAction;
    
    /** The string representation of the message handler. */
    private String stringRepresentation;
    
    /** Initialization flag. */
    private boolean initialized;
    
    /**
     * Constructor for ESCOSyncReplMessagesHandler.
     */
    public ESCOSyncReplMessagesHandler() {
      super();
    }
    
    /**
     * Checks the properties after the Spring injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        
        Assert.notNull(this.addAction, 
                "The property addAction in the class " + this.getClass().getName() 
                + " can't be null.");
        
        Assert.notNull(this.modifyAction, 
                "The property modifyAction in the class " + this.getClass().getName() 
                + " can't be null.");

        Assert.notNull(this.deleteAction, 
                "The property deleteAction in the class " + this.getClass().getName() 
                + " can't be null.");

        Assert.notNull(this.presentAction, 
                "The property presentAction in the class " + this.getClass().getName() 
                + " can't be null.");
        
        setInitialized(true);
    }
    
    /**
     * Gives the string representation of the handler.
     * @return The string representation.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (stringRepresentation == null) {
            stringRepresentation = getClass().getSimpleName() + "#{Add action: "
                + addAction + "; Modify action: " + modifyAction 
                + "; Delete action: " + deleteAction 
                + "; Present action: " + presentAction + "}";
        }
        return stringRepresentation;
    }
    
    /**
     * Handles a given message.
     * @param message The message to handle.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.ISyncReplMessagesHandler
     * #processLDAPMessage(com.novell.ldap.LDAPMessage)
     */
    public void processLDAPMessage(final LDAPMessage message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.trace("Handling the message: " + message);
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
            LOGGER.trace("A SyncStateControl is present in the message.");
            final LDAPEntry entry = searchResultMessage.getEntry();
            if (control.isAdd()) {
                addAction.trigger(entry);
            } else if (control.isModify()) {
                modifyAction.trigger(entry);
            } else if (control.isDelete()) {
                deleteAction.trigger(entry);    
            } else if (control.isPresent()) {
                presentAction.trigger(entry);
            }
        } else {
            LOGGER.trace("No SyncStateControl in the message.");
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

    /**
     * Getter for addAction.
     * @return the addAction
     */
    public ISyncReplTriggeredAction getAddAction() {
        return addAction;
    }

    /**
     * Getter for modifyAction.
     * @return the modifyAction
     */
    public ISyncReplTriggeredAction getModifyAction() {
        return modifyAction;
 
    }

    /**
     * Getter for deleteAction.
     * @return the deleteAction
     */
    public ISyncReplTriggeredAction getDeleteAction() {
        return deleteAction;
    }

    /**
     * Getter for presentAction.
     * @return the presentAction
     */
    public ISyncReplTriggeredAction getPresentAction() {
        return presentAction;
    }

    /**
     * Getter for stringRepresentation.
     * @return stringRepresentation.
     */
    public String getStringRepresentation() {
        return stringRepresentation;
    }

    /**
     * Setter for stringRepresentation.
     * @param stringRepresentation the new value for stringRepresentation.
     */
    public void setStringRepresentation(final String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    /**
     * Setter for addAction.
     * @param addAction the new value for addAction.
     */
    public void setAddAction(final ISyncReplTriggeredAction addAction) {
        this.addAction = addAction;
    }

    /**
     * Setter for modifyAction.
     * @param modifyAction the new value for modifyAction.
     */
    public void setModifyAction(final ISyncReplTriggeredAction modifyAction) {
        this.modifyAction = modifyAction;
    }

    /**
     * Setter for deleteAction.
     * @param deleteAction the new value for deleteAction.
     */
    public void setDeleteAction(final ISyncReplTriggeredAction deleteAction) {
        this.deleteAction = deleteAction;
    }

    /**
     * Setter for presentAction.
     * @param presentAction the new value for presentAction.
     */
    public void setPresentAction(final ISyncReplTriggeredAction presentAction) {
        this.presentAction = presentAction;
    }

    
    /**
     * Tests if the handler is initialized.
     * @return True if the handler is initialized.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.ISyncReplMessagesHandler#isInitialized()
     */
    public synchronized boolean isInitialized() {
        if (!initialized) {
            return false;
        }
        return addAction.isInitialized() 
            && deleteAction.isInitialized() 
            && modifyAction.isInitialized()
            && presentAction.isInitialized();
    }

    /**
     * Setter for initialized.
     * @param initialized the new value for initialized.
     */
    public synchronized void setInitialized(final boolean initialized) {
        this.initialized = initialized;
    }

    
}
