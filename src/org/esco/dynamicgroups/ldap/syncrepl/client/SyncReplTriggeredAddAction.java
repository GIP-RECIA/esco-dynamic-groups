/**
 * 
 */
package org.esco.dynamicgroups.ldap.syncrepl.client;

import org.apache.log4j.Logger;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPEntry;

/**
 * Add action trigerred by an Ldap Synchronization Replication notification.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public class SyncReplTriggeredAddAction implements ISyncReplTriggeredAction {

    /** Serial version UID. */
    private static final long serialVersionUID = -8354685420509950293L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SyncReplTriggeredAddAction.class);

    /** The ldap attribute for the uid. */
    private String uidAttribute;
    
    /**
     * Constructor for SyncReplTriggeredAddAction.
     * @param uidAttribute The ldap attribute for the uid.
     */
    public SyncReplTriggeredAddAction(final String uidAttribute) {
        this.uidAttribute = uidAttribute;
        LOGGER.debug("Creation of an instance of " + getClass().getSimpleName() + ".");
    }
    
    /**
     * @param entry
     * @see org.esco.dynamicgroups.ldap.syncrepl.client.ISyncReplTriggeredAction#trigger(com.novell.ldap.LDAPEntry)
     */
    public void trigger(final LDAPEntry entry) {
        final LDAPAttribute uid = entry.getAttribute(uidAttribute);
        if (uid != null) {
            
        }

    }

}
