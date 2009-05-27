/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;

import java.util.Set;

/**
 * Interface for the parameters section that can be used to retrieve which attrbutes 
 * may be used in the logic definition of a dynamic group.
 * @author GIP RECIA - A. Deman
 * 27 mai 2009
 *
 */
public interface IDynamicAttributesProvider {

    /**
     * Gives the dynamic attributes.
     * @return The array of the dynamic attributes.
     */
    Set<String> getDynamicAttributes();
    
}
