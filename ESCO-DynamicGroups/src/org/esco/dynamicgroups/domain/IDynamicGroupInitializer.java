/**
 * 
 */
package org.esco.dynamicgroups.domain;

import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;

/**
 * Interface of the initilizers for the dynamic groups.
 * @author GIP RECIA - A. Deman
 * 19 janv. 2009
 *
 */
public interface IDynamicGroupInitializer {

    /**
     * Initializes the group :  removes all the existing members and retrieves the 
     * new members from the group definition.
     * @param definition The dfinition associated to the group to initialize.
     */
    void initialize(final DynamicGroupDefinition definition);
    
}
