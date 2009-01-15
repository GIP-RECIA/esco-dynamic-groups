/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client;



import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPEntry;
import org.apache.log4j.Logger;
import org.esco.dynamicgroups.IEntryDTO;

/**
 * Modify action triggered by an LDAP Synchronization Replication notification.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public class ModifySyncReplTriggeredAction extends AbstractSyncReplsTriggeredAction {

    /** Serial version UID. */
    private static final long serialVersionUID = -8354685420509950293L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(AddSyncReplTriggeredAction.class);
        
    /** The String representation of the action. */
    private String stringRepresentation;

    /**
     * Constructor for ModifySyncReplTriggeredAction.
     */
    public ModifySyncReplTriggeredAction() {
        super();
        LOGGER.debug("Creation of an instance of " + getClass().getSimpleName() + ".");
    }
    
    /**
     * Gives the string representation of the action.
     * @return The string representation of the action.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (stringRepresentation == null) {
            stringRepresentation = getClass().getSimpleName() + "#{"
                + "LDAP id attribute: " + getIdAttribute() + "}";
        }
        return stringRepresentation;
    }
    
    /**
     * Triggers the action.
     * @param ldapEntry The LDAP informations about the entry.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.ISyncReplTriggeredAction#trigger(com.novell.ldap.LDAPEntry)
     */
    public void trigger(final LDAPEntry ldapEntry) {
        final LDAPAttribute id = ldapEntry.getAttribute(getIdAttribute());
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Modify action - id of the entry:" + id);
        }
        if (id != null) {
            final IEntryDTO entryDTO = getEntryDTOFactory().createEntryDTO(ldapEntry);
            LOGGER.debug(entryDTO);
        }
    }
}
