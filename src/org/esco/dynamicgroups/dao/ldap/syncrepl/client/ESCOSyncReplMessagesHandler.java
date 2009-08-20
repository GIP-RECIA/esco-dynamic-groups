package org.esco.dynamicgroups.dao.ldap.syncrepl.client;


import com.novell.ldap.LDAPControl;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPMessage;
import com.novell.ldap.LDAPSearchResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger.ISyncReplMessagesLogger;
import org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger.ISyncReplMessagesLoggerFactory;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.CookieManager;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncInfoMessage;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.PersonsParametersSection;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
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

    /** The statistics manager. */
    private IStatisticsManager statisticsManager;

    /** The messages logger. */
    private ISyncReplMessagesLogger messagesLogger;

    /** The messages logger factory. */
    private ISyncReplMessagesLoggerFactory messagesLoggerFactory;

    /** The cookie manager. */
    private CookieManager cookieManager;

    /** The string representation of the message handler. */
    private String stringRepresentation;

    /** Parameters provider. */
    private ParametersProvider parametersProvider;

    /** The LDAP Parameters. */
    private PersonsParametersSection ldapParameters;

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

        final String cantBeNull = " can't be null.";
        Assert.notNull(this.addAction, 
                "The property addAction in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.modifyAction, 
                "The property modifyAction in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.deleteAction, 
                "The property deleteAction in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.presentAction, 
                "The property presentAction in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.statisticsManager, 
                "The property statisticsManager in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.cookieManager, 
                "The property cookieManager in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.messagesLoggerFactory, 
                "The property messagesLoggerFactory in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " + this.getClass().getName() 
                + cantBeNull);
        ldapParameters = (PersonsParametersSection) parametersProvider.getPersonsParametersSection();
        messagesLogger = messagesLoggerFactory.createLogger();

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
        if (ldapParameters.getSkipMessagesUntilFirstCookie() && !cookieManager.hasRecievedCookie()) {
            // The messages are skipped until the first cookie.
            if (message instanceof SyncInfoMessage) {
                cookieManager.updateCurrentCookie(((SyncInfoMessage) message).getCookie());
            }
            messagesLogger.log(Level.INFO, message, " (Messages skipped until the first cookie).");
        } else {
            // The message is actually processed.
            
            messagesLogger.log(message);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Handling the message tag: " + message.getTag());
            }
            if (message instanceof LDAPSearchResult) {
                handleLDAPSearchResult((LDAPSearchResult) message);
            } else if (message instanceof SyncInfoMessage) {
                cookieManager.updateCurrentCookie(((SyncInfoMessage) message).getCookie());
            }
        }
    }

    /**
     * Handles an LDAPSearchResult message.
     * @param searchResultMessage The message to handle.
     */
    protected void handleLDAPSearchResult(final LDAPSearchResult searchResultMessage) {
        final SyncStateControl control = retrieveSyncStateControl(searchResultMessage);

        if (control != null) {
            LOGGER.debug("A SyncStateControl is present in the message.");
            cookieManager.updateCurrentCookie(control.getCookie());
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
            statisticsManager.handleSyncReplNotifications(control);
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
     * Getter for statisticsManager.
     * @return statisticsManager.
     */
    public IStatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    /**
     * Setter for statisticsManager.
     * @param statisticsManager the new value for statisticsManager.
     */
    public void setStatisticsManager(final IStatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
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

    /**
     * Getter for messagesLogger.
     * @return messagesLogger.
     */
    public ISyncReplMessagesLogger getMessagesLogger() {
        return messagesLogger;
    }

    /**
     * Setter for messagesLogger.
     * @param messagesLogger the new value for messagesLogger.
     */
    public void setMessagesLogger(final ISyncReplMessagesLogger messagesLogger) {
        this.messagesLogger = messagesLogger;
    }

    /**
     * Getter for messagesLoggerFactory.
     * @return messagesLoggerFactory.
     */
    public ISyncReplMessagesLoggerFactory getMessagesLoggerFactory() {
        return messagesLoggerFactory;
    }

    /**
     * Setter for messagesLoggerFactory.
     * @param messagesLoggerFactory the new value for messagesLoggerFactory.
     */
    public void setMessagesLoggerFactory(final ISyncReplMessagesLoggerFactory messagesLoggerFactory) {
        this.messagesLoggerFactory = messagesLoggerFactory;
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
