package org.esco.dynamicgroups.domain;

import org.esco.dynamicgroups.IEntryDTO;

/**
 * Interface for the domain service.
 * @author GIP RECIA - A. Deman
 * 13 janv. 2009
 *
 */
public interface IDomainService {

    /**
     * Removes a deleted user user from its groups. 
     * The whole groups or only the dynmic groups may be considered, depending
     * on the configuration.
     * @param entry The entry associated to the user.
     */
    void removeDeletedUserFromGroups(final IEntryDTO entry);
    
    /**
     * Updates the dynamic groups for a given user entry.
     * @param entry The user entry used to compute the new groups.
     */
    void updateDynamicGroups(final IEntryDTO entry);
    
    /**
     * Adds a user to the dynamic groups.
     * @param entry The entry associated to the user.
     */
    void addToDynamicGroups(final IEntryDTO entry); 
    
    
}
