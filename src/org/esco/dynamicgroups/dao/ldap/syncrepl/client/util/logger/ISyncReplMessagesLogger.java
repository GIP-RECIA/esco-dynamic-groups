/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger;

import com.novell.ldap.LDAPMessage;

import java.io.Serializable;

import org.apache.log4j.Level;

/**
 * Interface for the classes used to log the syncRepl messages.
 * @author GIP RECIA - A. Deman
 * 3 juin 2009
 *
 */
public interface ISyncReplMessagesLogger extends Serializable {

    /**
     * Log the SyncRepl message.
     * @param level The Priority for the logging. 
     * @param ldapMessage The LDAP Message. 
     * @param logMessage The log message.
     */
    void log(final Level level, final LDAPMessage ldapMessage, final String logMessage);

    /**
     * Log the SyncRepl message.
     * @param ldapMessage The LDAP Message. 
     */
    void log(final LDAPMessage ldapMessage);
    
}
