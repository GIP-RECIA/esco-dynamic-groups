/**
 * 
 */
package org.esco.dynamicgroups;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Interface of the objects used to transfer the entry informations.
 * @author GIP RECIA - A. Deman
 * 18 avr. 08
 *
 */
public interface IEntryDTO extends Serializable {
    
    /**
     * Gives the Id of the subject.
     * @return The id of the subject.
     */
    String getId();
    
    /**
     * Gets the value of an attribute.
     * @param key The key of the attribute to retrieve.
     * @return The value of the attribute.
     */
    String[] getAttributeValue(final String key);
    
    /**
     * Gives the list of the values of the attributes.
     * @return The list of the values of the attributes.
     */
    Collection<String[]> getAttributeValues();
    
    /**
     * Gives the list of the names of the attributes.
     * @return The list of the values of the attributes.
     */
    Set<String> getAttributeNames();
    
    /**
     * Gives the number of attributes.
     * @return The number of attributes.
     */
    int countAttributes();
    
    /**
     * Gives the name of the attribute at the position index.
     * @param index The position of the attribute.
     * @return The name of the attribute.
     */
    String getAttibuteName(final int index);
    
    /**
     * Gives the value of the attribute at the position index.
     * @param index The position of the attribute.
     * @return The value of the attribute.
     */
    String[] getAttributeValue(final int index);
    
    /**
     * The if the DTO contains a given attribute.
     * @param attributeName The name of the attribute to look for.
     * @return True if the DTO contains the attribute, false otherwise.
     */
    boolean hasAttribute(final String attributeName);

}
