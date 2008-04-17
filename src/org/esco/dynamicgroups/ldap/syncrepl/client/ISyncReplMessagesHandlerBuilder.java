/**
 * 
 */
package org.esco.dynamicgroups.ldap.syncrepl.client;

import java.io.Serializable;

/**
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public interface ISyncReplMessagesHandlerBuilder extends Serializable {
    
    /**
     * Builds an handler.
     * @return The SyncRepl messages handler.
     */
    ISyncReplMessagesHandler buildHandler();
}
