/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

/**
 * Interface for the statistics about the created and deleted groups.
 * @author GIP RECIA - A. Deman
 * 5 mai 2009
 *
 */
public interface IGroupAddOrDeletedStatsEntry extends IStatisticsEntry {

    /**
     * Handles the creation of a group.
     * @param groupName The name of the group.
     */
    void handleCreatedGroup(final String groupName);

    /**
     * Handles the deletion of a group.
     * @param groupName The name of the group.
     */
    void handleDeletedGroup(final String groupName);

}
