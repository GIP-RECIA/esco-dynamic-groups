/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client.util;

import com.novell.ldap.LDAPMessage;

import org.apache.log4j.Logger;

/**
 * Logger for the LDAP SyncRepl messages which logs all the messages.
 * @author GIP RECIA - A. Deman
 * 3 juin 2009
 *
 */
public class AllMessagesSyncReplMessageLogger implements ISyncReplMessagesLogger {

    /** Serial version UID.*/
    private static final long serialVersionUID = 486020724553020162L;

    /** The logger to use. */
    private Logger logger = Logger.getLogger(AllMessagesSyncReplMessageLogger.class);
    
    /**
     * Builds an instance of DisabledSyncReplMessageLogger.
     */
    public AllMessagesSyncReplMessageLogger() {
        super();
    }
    
    /**
     * Do nothing.
     * @param message The LDAP message.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.ISyncReplMessagesLogger#log(LDAPMessage)
     */
    @Override
    public void log(final LDAPMessage message) {
        logger.info(" SyncReplMessage recieved");
    }

}
