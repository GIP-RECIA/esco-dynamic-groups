package org.esco.dynamicgroups.domain;

import org.esco.dynamicgroups.IEntryDTO;

/**
 * Interface for the domain service.
 * @author GIP RECIA - A. Deman
 * 13 janv. 2009
 *
 */
public interface IDomainService {

    
   // void createGroup();
    
    /**
     * Updates the dynamic groups for a given user entry.
     * @param entry The user entry used to compute the new groups.
     */
    void updateDynamicGroups(final IEntryDTO entry);
    
    //void removeFromGroups(final String userId);
    
    
}
