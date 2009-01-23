/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client;



import com.novell.ldap.LDAPEntry;

import java.io.Serializable;

/**
 * Interface for the action triggered by an SyncRepl notification.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public interface ISyncReplTriggeredAction extends Serializable {

    /**
     * Trigger an action relative to an entry.
     * @param entry The entry associated to the triggered action.
     */
    void trigger(final LDAPEntry entry);
    
    /**
     * Tests if the action is initialized.
     * @return True if the action is initialized
     */
    boolean isInitialized();
    
}
