/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger;

import com.novell.ldap.LDAPMessage;

import org.apache.log4j.Level;
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
     * Logs the message.
     * @param message The LDAP message.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger.ISyncReplMessagesLogger#log(LDAPMessage)
     */
    public void log(final LDAPMessage message) {
        log(Level.INFO, message, "SyncReplMessage recieved");
    }

    /**
     * Logs an LDAP Message.
     * @param level The Priority for the logging. 
     * @param ldapMessage The LDAP Message. 
     * @param logMessage The log message.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger.ISyncReplMessagesLogger#
     * log(org.apache.log4j.Level, com.novell.ldap.LDAPMessage, java.lang.String)
     */
    public void log(final Level level, final LDAPMessage ldapMessage, final String logMessage) {
        logger.log(level, logMessage);
    }

}
