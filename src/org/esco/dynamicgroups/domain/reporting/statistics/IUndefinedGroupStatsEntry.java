/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.util.Set;

/**
 * Interface for the statistics about the dynamic groups whithout a membership definition.
 * @author GIP RECIA - A. Deman
 * 5 mai 2009
 *
 */
public interface IUndefinedGroupStatsEntry extends IStatisticsEntry {
    
    /**
     * Gives the list of the dynamic groups whithout a membership definition.
     * @return The list of the group names.
     */
    Set<String> getUndefinedGroupNames();

}
