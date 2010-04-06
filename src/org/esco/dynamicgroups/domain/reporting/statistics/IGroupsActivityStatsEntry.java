package org.esco.dynamicgroups.domain.reporting.statistics;

import java.util.Set;

/**
 * Interface for the statistics about the groups activity.
 * @author GIP RECIA - A. Deman
 * 6 mai 2009
 *
 */
public interface IGroupsActivityStatsEntry extends IStatisticsEntry {
    
    
    
    /** 
     * Gives the active groups.
     * @return The active groups.
     */
    Set<String> getActiveGroups();
    
    /**
     * Handles the add of a memebrs.
     * @param groupName The name of the group.
     * @param userId The id of the group.
     */
    void handleAddedUser(final String groupName, final String userId);
    
    /**
     * Handles the remove member operation for a group member.
     * @param groupName The name of the group.
     * @param userId The id of the user.
     */
    void handleRemovedUser(final String groupName, final String userId);
    
    /**
     * Tests if some groups are active.
     * @return True if at leastone group is active.
     */
    boolean hasActiveGroup();
    
    /**
     * Gives the label for the active groups.
     * @return The label.
     */
    String getActiveGroupsLabel();
    
}
