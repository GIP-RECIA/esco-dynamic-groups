/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.ESCODynamicGroupsParameters;

/**
 * Reference implementation of SyncRepl Message handler.
 * @author GIP RECIA - A. Deman. 
 * 17 avr. 08
 *
 */
public class ESCOSyncReplMessageHandlerBuilder implements ISyncReplMessagesHandlerBuilder {

    /** Serial version UID. */
    private static final long serialVersionUID = 2088800887151034194L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCOSyncReplMessageHandlerBuilder.class);

    /** LDAP id attribute. */ 
    private String idAttribute;  
    
    /** String representation of this builder. */
    private String stringRepresentation;
    
    /**
     * Constructor for ESCOSyncReplMessageHandlerBuilder.
     */
    public ESCOSyncReplMessageHandlerBuilder() {
        this.idAttribute = ESCODynamicGroupsParameters.instance().getLdapIdAttribute();
        LOGGER.info("Builder used for the SyncRepl messages handler: " + this);
    }
    
    /**
     * Gives the string representation of this builder.
     * @return The string representation.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (stringRepresentation == null)  {
            stringRepresentation = getClass().getSimpleName() + "#{"
                + "LDAP id attribute: " + idAttribute + "}";
        }
        return stringRepresentation;
    }
    
    /**
     * Builds an handler.
     * @return The SyncRepl messages handler.
    * @see org.esco.dynamicgroups.ldap.syncrepl.client.ISyncReplMessagesHandlerBuilder#buildHandler()
    */
    public ISyncReplMessagesHandler buildHandler() {
        final AddSyncReplTriggeredAction addAction = new AddSyncReplTriggeredAction(idAttribute);
        final ModifySyncReplTriggeredAction modifyAction = new ModifySyncReplTriggeredAction(idAttribute);
        final DeleteSyncReplTriggeredAction deleteAction = new DeleteSyncReplTriggeredAction(idAttribute);
        final PresentSyncReplTriggeredAction presentAction = new PresentSyncReplTriggeredAction(idAttribute);
        return new ESCOSyncReplMessagesHandler(addAction, modifyAction, deleteAction, presentAction);
    }
}
