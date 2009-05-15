/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.esco.dynamicgroups.domain.beans.I18NManager;

/**
 * Statistics about the group activities.
 * REM: This class is not thread safe, it is the responsability of the 
 * statistics manager. 
 * @author GIP RECIA - A. Deman
 * 6 mai 2009
 *
 */
public class GroupsAcrivityStatsEntry implements IGroupsActivityStatsEntry {
    
    /**
     * Stores the activity for a given group.
     * @author GIP RECIA - A. Deman
     * 6 mai 2009
     *
     */
    private class GroupActivity {
        
        /** Number of added members for a group. */
        private int addedMembersCount;
        
        /** Number of removed members for a group. */
        private int removedMembersCount;
        
        /**
         * Builds an instance of ActivityEntry.
         */
        public GroupActivity() {
            super();
        }

        /**
         * Getter for addedMembersCount.
         * @return addedMembersCount.
         */
        public int getAddedMembersCount() {
            return addedMembersCount;
        }

        /**
         * Getter for removedMembersCount.
         * @return removedMembersCount.
         */
        public int getRemovedMembersCount() {
            return removedMembersCount;
        }
        
        /**
         * Increments the number of added members.
         */
        public void incrementAddedMembersCount() {
            addedMembersCount++;
        }
        
        /**
         * Increments the number of removed members.
         */
        public void incrementRemovedMembersCount() {
            removedMembersCount++;
        }
        
      
        
    }
    

    /** Serial version UID.*/
    private static final long serialVersionUID = -7322886458682673817L;
    
    /** I18N key for the label. */
    private static final String GROUPS_ACTIVITY_KEY = "stats.groups.activity";
    
    /** Key for the name of the groups with an activity. */
    private static final String ACTIVE_GROUPS_LABEL_KEY = "stats.groups.activity.groups.active";
    
    /** 18N key for the added members. */
    private static final String ADDED_KEY = "stats.groups.activity.groups.members.added";
    
    /** */
    private static final String REMOVED_KEY = "stats.groups.activity.groups.members.removed";
    
    /** Activities for the groups. */
    private Map<String, GroupActivity> activities = new HashMap<String, GroupActivity>();
    
    /** The I18NManager. */
    private I18NManager i18n;
    
    
    
    /**
     * Builds an instance of GroupsAcrivityStatsEntry.
     * @param i18n The i18n manager.
     */
    public GroupsAcrivityStatsEntry(final I18NManager i18n) {
        this.i18n = i18n;
    }

    /**
     * Gives the name of the groups with an activity.
     * @return The name of the groups with an activity.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IGroupsActivityStatsEntry#getActiveGroups()
     */
    public Set<String> getActiveGroups() {
        
        final Set<String> groupNamesSet = activities.keySet();
        final String[] groupNames = groupNamesSet.toArray(new String[groupNamesSet.size()]);
        Arrays.sort(groupNames);
        final Set<String> activeGroups = new HashSet<String>();
        for (String groupName : groupNames) {
            final GroupActivity activity = activities.get(groupName);
            final String entry = groupName  
                + " - " + i18n.getI18nMessage(ADDED_KEY) +  activity.getAddedMembersCount()
                + " - " + i18n.getI18nMessage(REMOVED_KEY) + activity.getRemovedMembersCount();
            activeGroups.add(entry);
        }
        return activeGroups;
    }
    
    /**
     * Gives the label for the active groups.
     * @return The label.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IGroupsActivityStatsEntry#getActiveGroupsLabel()
     */
    public String getActiveGroupsLabel() {
        return i18n.getI18nMessage(ACTIVE_GROUPS_LABEL_KEY);
    }

    /**
     * Handles the add of a memebrs.
     * @param groupName The name of the group.
     * @param userId The id of the group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IGroupsActivityStatsEntry#handleAddedUser(String, String)
     */
    public void handleAddedUser(final String groupName, final String userId) {
        GroupActivity activity = activities.get(groupName);
        if (activity == null) {
            activity = new GroupActivity();
            activities.put(groupName, activity);
        }
        activity.incrementAddedMembersCount();
    }

    /**
     * Handles the remove member operation for a group member.
     * @param groupName The name of the group.
     * @param userId The id of the user.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IGroupsActivityStatsEntry#
     * handleRemovedUser(String, String)
     */
    public void handleRemovedUser(final String groupName, final String userId) {
        GroupActivity activity = activities.get(groupName);
        if (activity == null) {
            activity = new GroupActivity();
            activities.put(groupName, activity);
        }
        activity.incrementRemovedMembersCount();

    }

    /**
     * Gives the string that represents the entry.
     * @return The text that represents the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getEntry()
     */
    public String getEntry() {
        return String.valueOf(activities.size());
    }

    /**
     * Gives the label associated to the entry.
     * @return The string that contains the label for the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getLabel()
     */
    public String getLabel() {
        return i18n.getI18nMessage(GROUPS_ACTIVITY_KEY);
    }

    /**
     * 
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#reset()
     */
    public void reset() {
       activities.clear();

    }

}
