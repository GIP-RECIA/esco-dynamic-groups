/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;

import java.io.Serializable;

/**
 * @author GIP RECIA - A. Deman
 * 3 juin 2009
 *
 */
public interface IReportingManager extends Serializable {
    
    /**
     * Generates and send the report.
     */
    void doReporting();
    
    /**
     * Generates the report for the check of the groups memebers.
     */
    void doReportingForGroupsMembersCheck();
}
