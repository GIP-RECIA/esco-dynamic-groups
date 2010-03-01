/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.util.List;

/**
 * Interface for the statistics about the LDAP notifications.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public interface ISyncReplNotificationsStats extends IStatisticsEntry {

    /**
     * Handles an Add action.
     */
    void handeAddAction();
    
    /**
     * Handles a Modify action.
     */
    void handeModifyAction();
    
    /**
     * Handles a Delete action.
     */
    void handeDeleteAction();
    
    /**
     * Handles a Present action.
     */
    void handePresentAction();
    
    /**
     * Gives the notifications.
     * @return The list of the notifications.
     */
     List<String> getNotifications();
     
    
}
