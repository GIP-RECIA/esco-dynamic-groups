/**
 * 
 */
package org.esco.dynamicgroups.ldap.syncrepl.client;

import java.io.Serializable;

import com.novell.ldap.LDAPEntry;

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
    
}
