/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger;

import java.io.Serializable;


/**
 * Interface for the factories of Loggers for the SyncRepl Messages.
 * @author GIP RECIA - A. Deman
 * 3 juin 2009
 *
 */
public interface ISyncReplMessagesLoggerFactory extends Serializable {
    
    /**
     * Creates a logger.
     * @return The new SyncRepl messages logger.
     */
    ISyncReplMessagesLogger createLogger();

}
