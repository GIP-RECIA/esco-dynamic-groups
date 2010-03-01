/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.util.Set;

/**
 * Interface assocated the groups members verification process.
 * @author GIP RECIA - A. Deman
 * 2 juin 2009
 *
 */
public interface ICheckedMembersStatsEntry extends IStatisticsEntry {

    /**
     * Handles an invalid member of a group.
     * @param groupName The name of the group.
     * @param uid The id of the invalid member of the group.
     */
    void handleInvalidMember(final String groupName, final String uid);
    
    /**
     * Handles a missing member of a group.
     * @param groupName The name of the group.
     * @param uid The id of the missing member of the group.
     */
    void handleMissingMember(final String groupName, final String uid);
    
    /**
     * Gives the names of the groups with invalid or missing members.
     * @return The names of the groups.
     */
    Set<String> getInvalidGroups();
    
    /**
     * Gives the result of the verification process of the group.
     * @param groupName The name of the group.
     * @return The result of the verification wich contains the invalid members and/or
     * the missing ones.
     */
    MissingOrInvalidMembersEntry getCheckingResult(final String groupName);
    
    /**
     * Gives the label for the invalid members.
     * @return The label.
     */
    String getInvalidMembersLabel();
    
    /**
     * Gives the label for the missing members.
     * @return The label.
     */
    String getMissingMembersLabel();
    
    
    /**
     * Handles a checked group.
     * @param groupName The name of the checked group.
     */
    void handleCheckedGroup(final String groupName);
    
    /**
     * Tests if groups have been checked.
     * @return true if some groups have been checked.
     */
    boolean checkPerformed();
    
    /**
     * Handles the start of the process.
     */
    void handleStartOfProcess();
    
    /**
     * Handles the end of the process.
     */
    void handleEndOfProcess();
    
    
}
