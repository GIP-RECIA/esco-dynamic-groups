/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client.util;

import com.novell.ldap.LDAPMessage;

import java.io.Serializable;

/**
 * Interface for the classes used to log the syncRepl messages.
 * @author GIP RECIA - A. Deman
 * 3 juin 2009
 *
 */
public interface ISyncReplMessagesLogger extends Serializable {

    /**
     * Log the SyncRepl message.
     * @param message The LDAP Message.
     */
    void log(LDAPMessage message);
    
}
