/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import java.util.Map;
import java.util.Set;

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
     * Create a group if it does not exist, removes all its members otherwise.
     * @param groupName The name of the group.
     */
    void resetGroupMembers(final String groupName);
    
    /**
     * Tests if a groupName denotes a dynamic group.
     * @param groupName The name of the group/
     * @return True if the group is dynamic.
     */
    boolean isDynamicGroup(final String groupName);
    
    /**
     * Adds a user to a group.
     * @param groupName The name of the group.
     * @param userIds The ids of the users to add to the group.
     */
    void addToGroup(final String groupName, final Set<String> userIds);
    
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
