/**
 * 
 */
package org.esco.dynamicgroups.domain.statistics;

/**
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public interface IDefinitionModificationsStatsEntry extends IStatisticsEntry {
    
  /**
   * Handles a modification of a dynamic definition.
   * @param groupName The name of the group.
   * @param previousDefinition The previous defintion.
   * @param newDefinition The new definition. 
   */
   void handleDefinitionModification(final String groupName,
            final String previousDefinition,
            final String newDefinition);
}
