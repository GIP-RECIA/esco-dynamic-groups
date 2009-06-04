/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.esco.dynamicgroups.domain.beans.I18NManager;

/**
 * Statistics about the verification process of the memebers of a group.
 * @author GIP RECIA - A. Deman
 * 2 juin 2009
 *
 */
public class CheckedMembersStatsEntry implements ICheckedMembersStatsEntry {

    /** Serial version UID.*/
    private static final long serialVersionUID = -6891311521808450328L;

    /** Key for the label of the entry. */
    private static final String MEMBERS_CHECK_KEY = "stats.members.checking";

    /** Key for the invalid members. */
    private static final String INVALID_MEMBERS_KEY = "stats.members.checking.invalid";

    /** Key for the missing members. */
    private static final String MISSING_MEMBERS_KEY = "stats.members.checking.missing";

    /** Key for the label of the number of checked groups. */
    private static final String CHECKED_GROUPS_COUNT_KEY = "stats.members.checking.count";

    /** Key for the label of the number of checked groups. */
    private static final String GROUPS_ON_ERROR_KEY = "stats.members.checking.on-error";
    
    /** Key for the label of the number of checked groups. */
    private static final String DURATION_KEY = "stats.members.checking.duration";

    /** The I18N manager to use. */
    private transient I18NManager i18n;

    /** The number of checked groups. */
    private int checkedGroups;
    
    /** Used to compute the duration time of the verification process. */
    private long start;
    
    /** Used to compute the duration time of the verification process. */
    private long end;

    /** Groups with invalid or missing members.     */
    private Map<String, MissingOrInvalidMembersEntry> invalidGroupsInformations = 
        new HashMap<String, MissingOrInvalidMembersEntry>();


    /**
     * Builds an instance of CheckedMembersStatsEntry.
     * @param i18n The i18n manager.
     */
    public CheckedMembersStatsEntry(final I18NManager i18n) {
        this.i18n = i18n;
    }

    /**
     * Gives the result of the verification process of the group.
     * @param groupName The name of the group.
     * @return The result of the verification wich contains the invalid members and/or
     * the missing ones.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#getCheckingResult(String)
     */
    public MissingOrInvalidMembersEntry getCheckingResult(final String groupName) {
        return invalidGroupsInformations.get(groupName);
    }

    /**
     * Gives the names of the groups with invalid or missing members.
     * @return The names of the groups.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#getInvalidGroups()
     */
    public Set<String> getInvalidGroups() {
        return invalidGroupsInformations.keySet();
    }

    /**
     * Handles an invalid member of a group.
     * @param groupName The name of the group.
     * @param uid The id of the invalid member of the group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#
     * handleInvalidMember(String, String)
     */
    public void handleInvalidMember(final String groupName, final String uid) {

        MissingOrInvalidMembersEntry invalidGroup = invalidGroupsInformations.get(groupName);

        if (invalidGroup == null) {
            invalidGroup = new MissingOrInvalidMembersEntry();
            invalidGroupsInformations.put(groupName, invalidGroup);
        }

        invalidGroup.addInvalidMember(uid);
    }

    /**
     * Handles a missing member of a group.
     * @param groupName The name of the group.
     * @param uid The id of the missing member of the group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#
     * handleMissingMember(String, String)
     */
    public void handleMissingMember(final String groupName, final String uid) {
        MissingOrInvalidMembersEntry invalidGroup = invalidGroupsInformations.get(groupName);

        if (invalidGroup == null) {
            invalidGroup = new MissingOrInvalidMembersEntry();
            invalidGroupsInformations.put(groupName, invalidGroup);
        }

        invalidGroup.addMissingMember(uid);
    }

    /**
     * Handles a checked group.
     * @param groupName The name of the checked group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#handleCheckedGroup(String)
     */
    public void handleCheckedGroup(final String groupName) {
        checkedGroups++;
    }

    /**
     * Tests if groups have been checked.
     * @return true if some groups have been checked.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#checkPerformed()
     */
    public boolean checkPerformed() {
        return checkedGroups > 0;
    }

    /**
     * Gives the string that represents the entry.
     * @return  The number of groups with invalid or missing members.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getEntry()
     */
    public String getEntry() {
        String elapsedString = "";
        if (end > 0 && start > 0) {
            final long millisToSecondsFactor = 1000;
            final long elapsed = (end - start) / millisToSecondsFactor;
            elapsedString = " (" + i18n.getI18nMessage(DURATION_KEY) + elapsed + " s.)";
        }
        
        return i18n.getI18nMessage(CHECKED_GROUPS_COUNT_KEY) + checkedGroups 
        + " - " + i18n.getI18nMessage(GROUPS_ON_ERROR_KEY) + invalidGroupsInformations.size() 
        + elapsedString;
    }

    /**
     * Gives the label associated to the entry.
     * @return The label associated to the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getLabel()
     */
    public String getLabel() {
        return i18n.getI18nMessage(MEMBERS_CHECK_KEY);
    }

    /**
     * Resets the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#reset()
     */
    public void reset() {
        invalidGroupsInformations.clear();
        checkedGroups = 0;
        start = 0;
        end = 0;

    }

    /**
     * Gives the label for the invalid members.
     * @return The label.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#getInvalidMembersLabel()
     */
    public String getInvalidMembersLabel() {
        return i18n.getI18nMessage(INVALID_MEMBERS_KEY);
    }

    /**
     * Gives the label for the missing members.
     * @return The label.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#getMissingMembersLabel()
     */
    public String getMissingMembersLabel() {
        return i18n.getI18nMessage(MISSING_MEMBERS_KEY);
    }

    /**
     * Handles the start of the process.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#handleStartOfProcess()
     */
    public void handleStartOfProcess() {
        start = System.currentTimeMillis();
    }
    
    /**
     * Handles the end of the process.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ICheckedMembersStatsEntry#handleEndOfProcess()
     */
    public void handleEndOfProcess() {
        end = System.currentTimeMillis();
    }

    /**
     * Initializes an instance.
     * @param initializationValues The instance that contains 
     * the initialization values.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#initializeFrom(IStatisticsEntry)
     */
    public void initializeFrom(final IStatisticsEntry initializationValues) {

        if (!(initializationValues instanceof CheckedMembersStatsEntry)) {
            throw new IllegalArgumentException("The parameter is not an instance of " + getClass().getName());
        }
        
        final CheckedMembersStatsEntry other = (CheckedMembersStatsEntry) initializationValues;
        this.checkedGroups = other.checkedGroups;
        for (String key : other.invalidGroupsInformations.keySet()) {
            this.invalidGroupsInformations.put(key, other.invalidGroupsInformations.get(key));
        }
    }

}
