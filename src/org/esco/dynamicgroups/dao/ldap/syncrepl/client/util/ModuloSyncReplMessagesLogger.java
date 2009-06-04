/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client.util;

import com.novell.ldap.LDAPMessage;

import org.apache.log4j.Logger;

/**
 * Logger for the LDAP SyncRepl messages that logs messages every modulo calls.
 * @author GIP RECIA - A. Deman
 * 3 juin 2009
 *
 */
public class ModuloSyncReplMessagesLogger implements ISyncReplMessagesLogger {

    /** Serial version UID.*/
    private static final long serialVersionUID = 486020724553020162L;
    
    /** The logger to use. */
    private Logger logger = Logger.getLogger(ModuloSyncReplMessagesLogger.class);
    
    /** The modulo used to determine when the messages should be
     * logged. */
    private int modulo;
    
    /** The number of calls. */
    private int callsCount;

    /**
     * Builds an instance of DisabledSyncReplMessageLogger.
     * @param modulo The modulo to determine when the ldap message soud be logged.
     */
    public ModuloSyncReplMessagesLogger(final int  modulo) {
        this.modulo = modulo;
    }
    
    /**
     * Log the message every modulo call.
     * @param message The LDAP message.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.ISyncReplMessagesLogger#log(LDAPMessage)
     */
    @Override
    public void log(final LDAPMessage message) {
        callsCount = (callsCount + 1) % modulo;  
       if (callsCount == 0) {
           logger.info(modulo + " SyncReplMessages recieved.");
       }
    }

}
