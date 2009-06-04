/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client.util;

import com.novell.ldap.LDAPMessage;


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
     * Do nothing.
     * @param message The LDAP message.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.ISyncReplMessagesLogger#log(LDAPMessage)
     */
    @Override
    public void log(final LDAPMessage message) {
       /* Nothing to do.*/
    }

}
