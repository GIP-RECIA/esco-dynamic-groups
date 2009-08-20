/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import java.util.Map;
import java.util.Set;

import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;

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
     * Creates a group if it does not exist, removes all its members otherwise.
     * @param definition The definition of the group.
     */
    void resetGroupMembers(final DynamicGroupDefinition definition);
    
    /**
     * Tests if a groupName denotes a dynamic group.
     * @param groupName The name of the group/
     * @return True if the group is dynamic.
     */
//    boolean isDynamicGroup(final String groupName);
    
    /**
     * Gives the list of the dynamic group names whitout a memebership defintion.
     * @return The list of the group names.
     */
    Set<String> getUndefinedDynamicGroups();
    
    /**
     * Checks the members of a group.
     * @param groupDefinition The definition of the group to check.
     * @param expectedMembers The expected members of the group according to 
     * its logic definition.
     */
    void checkGroupMembers(final DynamicGroupDefinition groupDefinition, 
            final Set<String> expectedMembers);
    
    /**
     * Gives all the dynamic group definitnions.
     * @return The sset of dynamic group definitions.
     */
    Set<DynamicGroupDefinition> getAllDynamicGroupDefinitions();
    
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
    void createMemberships(final String userId, final Map<String, DynGroup> groups);
}
