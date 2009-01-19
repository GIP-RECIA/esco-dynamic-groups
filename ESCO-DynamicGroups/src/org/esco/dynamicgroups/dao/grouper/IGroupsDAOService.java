/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import java.util.Map;

import org.esco.dynamicgroups.domain.beans.DynGroup;

/**
 * @author GIP RECIA - A. Deman
 * 15 janv. 2009
 *
 */
public interface IGroupsDAOService {
    
    /**
     * Removes a user from its groups.
     * All the groups or only the dynamic ones may be considered, depending on the configuration.
     * @param userId The id of the user.
     */
    void removeFromGroups(final String userId);
    
    /**
     * Updates the memberships of an user.
     * @param userId The id of the user.
     * @param newGroups The new groups.
     */
    void updateMemberships(final String userId, final Map<String, DynGroup> newGroups);
    
    /**
     * Creates the memberships for a given user.
     * @param userId The id of the user.
     * @param groups The groups to which the user should become a member.
     */
    void createMemeberShips(final String userId, final Map<String, DynGroup> groups);
}
