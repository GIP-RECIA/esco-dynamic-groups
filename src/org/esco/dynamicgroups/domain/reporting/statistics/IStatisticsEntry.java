/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.io.Serializable;


/**
 * Base interface for the statistics entries.
 * Defines the informations that can be handled.
 * The implementations have to be thread safe. 
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public interface IStatisticsEntry extends Serializable {
    
    /**
     * Gives the label associated to the entry.
     * @return The string that contains the label for the entry.
     */
    String getLabel();
    
    /**
     * Gives the string that represents the entry.
     * @return The text that represents the entry.
     */
    String getEntry();

    /**
     * Resets the entry.
     */
    void reset();
    
    /**
     * Initializes an instance.
     * @param initializationValues The instance that contains 
     * the initialization values.
     */
    void initializeFrom(final IStatisticsEntry initializationValues);
}
