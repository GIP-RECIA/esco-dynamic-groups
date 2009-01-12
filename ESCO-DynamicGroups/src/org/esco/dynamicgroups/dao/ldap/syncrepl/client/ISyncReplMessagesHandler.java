package org.esco.dynamicgroups.dao.ldap.syncrepl.client;


import com.novell.ldap.LDAPMessage;

import java.io.Serializable;

/**
 * Base interface for the LDAP messages handlers.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 *
 */
public interface ISyncReplMessagesHandler extends Serializable {

    /**
     * Handles a given LDAP message.
     * @param message the LDAP message to handle.
     */
    void processLDAPMessage(final LDAPMessage message);
    
}
