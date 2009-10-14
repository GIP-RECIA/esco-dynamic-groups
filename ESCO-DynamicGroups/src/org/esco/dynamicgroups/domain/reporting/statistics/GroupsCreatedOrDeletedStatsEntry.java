/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import org.esco.dynamicgroups.domain.beans.II18NManager;


/**
 * Count the number of dynamic groups created or deleted.
 * @author GIP RECIA - A. Deman
 * 5 mai 2009
 *
 */
public class GroupsCreatedOrDeletedStatsEntry implements IGroupCreatedOrDeletedStatsEntry {

    /** Serial version UID.*/
    private static final long serialVersionUID = -4524167486981975737L;
    
    /** I18N key for the label. */
    private static final String GROUPS_KEY = "stats.groups";
    
    /** I18N entry for the added groups. */
    private static final String GROUPS_CREATED_KEY = "stats.groups.created";
    
    /** I18N entry for the deleteed groups. */
    private static final String GROUPS_DELETED_KEY = "stats.groups.deleted";

    /** Number of created groups. */
    private int createdCount;
    
    /** Number of dynamic groups deleted. */
    private int deletedCount;
    
    /** I18N manager. */
    private transient II18NManager i18n;

    /**
     * Builds an instance of GroupsCreatedOrDeletedStatsEntry.
     * @param i18n The i18n manager to use.
     */
    public GroupsCreatedOrDeletedStatsEntry(final II18NManager i18n) {
        this.i18n = i18n;
    }
    
    /**
     * Gives the string that represents the entry.
     * @return The text that represents the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getEntry()
     */
    public String getEntry() {
        return i18n.getI18nMessage(GROUPS_CREATED_KEY) + createdCount + " - "
            + i18n.getI18nMessage(GROUPS_DELETED_KEY) + deletedCount;
    }

    /**
     * Gives the label associated to the entry.
     * @return The string that contains the label for the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getLabel()
     */
    public String getLabel() {
        return i18n.getI18nMessage(GROUPS_KEY);
    }

    /**
     * 
     * Resets the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#reset()
     */
    public void reset() {
       createdCount = 0;
       deletedCount = 0;
    }

    /**
     * Handles the creation of a group.
     * @param groupName The name of the group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IGroupCreatedOrDeletedStatsEntry#
     * handleCreatedGroup(String)
     */
    public void handleCreatedGroup(final String groupName) {
       createdCount++;
    }

    /**
     * Handles the deletion of a group.
     * @param groupName The name of the group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IGroupCreatedOrDeletedStatsEntry#
     * handleDeletedGroup(String)
     */
    public void handleDeletedGroup(final String groupName) {
        deletedCount++;
        
    }
    
    /**
     * Initializes an instance.
     * @param initializationValues The instance that contains 
     * the initialization values.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#initializeFrom(IStatisticsEntry)
     */
    public void initializeFrom(final IStatisticsEntry initializationValues) {

        if (!(initializationValues instanceof GroupsCreatedOrDeletedStatsEntry)) {
            throw new IllegalArgumentException("The parameter is not an instance of " + getClass().getName());
        }
        
        final GroupsCreatedOrDeletedStatsEntry other = (GroupsCreatedOrDeletedStatsEntry) initializationValues;
        this.createdCount = other.createdCount;
        this.deletedCount = other.deletedCount;
    }

}
