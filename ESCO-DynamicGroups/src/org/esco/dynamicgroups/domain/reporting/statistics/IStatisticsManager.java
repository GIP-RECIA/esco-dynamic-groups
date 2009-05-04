/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.io.Serializable;
import java.util.List;

import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;

/**
 * Base interface for the statistics manager instances that reports the dynamic groups activities.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public interface IStatisticsManager extends Serializable {
    
    /**
     * Generates a report.
     * @return The lines of the report. 
     */
    List<String> generateReport();
    
    /**
     * Resets the manager.
     */
    void reset();
    
    /**
     * Handles a definition modification.
     * @param groupName The name of the group.
     * @param previousDefinition The previous defintion.
     * @param newDefinition The new definition.
     */
    void handleDefinitionModification(final String groupName,
            final String previousDefinition,
            final String newDefinition);
    
    /**
     * Handles the SyncRepl notification.
     * @param control The control in the LDAP search result.
     */
    void handleSyncReplNotifications(final SyncStateControl control);

}
