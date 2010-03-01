/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger;

import com.novell.ldap.LDAPMessage;

import org.apache.log4j.Level;



/**
 * Disabled Logger for the LDAP SyncRepl messages.
 * @author GIP RECIA - A. Deman
 * 3 juin 2009
 *
 */
public class DisabledSyncReplMessageLogger implements ISyncReplMessagesLogger {

    /** Serial version UID.*/
    private static final long serialVersionUID = 486020724553020162L;

    /**
     * Builds an instance of DisabledSyncReplMessageLogger.
     */
    public DisabledSyncReplMessageLogger() {
        super();
    }
    
    /**
     * Does nothing.
     * @param message The LDAP message.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger.ISyncReplMessagesLogger#log(LDAPMessage)
     */
    public void log(final LDAPMessage message) {
       /* Nothing to do.*/
    }
    /**
     * This logger does not log the messages.
     * @param level The Priority for the logging. 
     * @param ldapMessage The LDAP Message. 
     * @param logMessage The log message.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger.ISyncReplMessagesLogger#
     * log(org.apache.log4j.Level, com.novell.ldap.LDAPMessage, java.lang.String)
     */
    public void log(final Level level, final LDAPMessage ldapMessage, final String logMessage) {
        /* NOTHINg to do.*/
    }

}
