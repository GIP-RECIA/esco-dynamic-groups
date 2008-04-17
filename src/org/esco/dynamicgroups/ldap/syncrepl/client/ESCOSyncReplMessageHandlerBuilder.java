/**
 * 
 */
package org.esco.dynamicgroups.ldap.syncrepl.client;

import org.apache.log4j.Logger;

/**
 * @author GIP RECIA - A. Deman 
 * 17 avr. 08
 *
 */
public class ESCOSyncReplMessageHandlerBuilder implements ISyncReplMessagesHandlerBuilder {
    /** Serial version UID. */
    private static final long serialVersionUID = 2088800887151034194L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCOSyncReplMessageHandlerBuilder.class);

    /**
     * Constructor for ESCOSyncReplMessageHandlerBuilder.
     */
    public ESCOSyncReplMessageHandlerBuilder() {
        LOGGER.info("Class used as builder for the SyncRepl messages handler: " + getClass());
    }
    /**
     * Builds an handler.
     * @return The SyncRepl messages handler.
    * @see org.esco.dynamicgroups.ldap.syncrepl.client.ISyncReplMessagesHandlerBuilder#buildHandler()
    */
    public ISyncReplMessagesHandler buildHandler() {
        return new ESCOSyncReplMessagesHandler();
    }

}
