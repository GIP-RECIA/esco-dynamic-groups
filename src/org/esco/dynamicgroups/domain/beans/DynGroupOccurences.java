/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;

/**
 * Used to associate a DynGroup instance to a number of occurences.
 * @author GIP RECIA - A. Deman
 * 15 janv. 2009
 *
 */
public class DynGroupOccurences implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 3550110210684973324L;
    
    /** The considered group. */
    private DynGroup group;
    
    /** The number of occurences.*/
    private int occurrences;
    
    /**
     * Builds an instance of DynGroupOccurences.
     * @param group The considered group.
     */
    public DynGroupOccurences(final DynGroup group) {
        this.group = group; 
    }
    
    /**
     * Gives the string that represents this instance.
     * @return The String that represents this instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "#{" + group + ", occurences: " 
        + occurrences + "}"; 
    }
    
    /**
     * Tests if an object is equal to this instance.
     * @param obj The object to test.
     * @return True if the object is equal to this instance.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (!(obj instanceof DynGroupOccurences)) {
            return false;
        }
        
        final DynGroupOccurences other =  (DynGroupOccurences) obj;
        if (other.getOccurrences() != getOccurrences()) {
            return false;
        }
        return group.equals(other.getGroup());
        
    }
    
    /**
     * Gives the hash value for this instance.
     * @return The hash value.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return group.hashCode() + occurrences;
    }
    
    
    /**
     * Increments the number of occurences.
     */
    public void incrementOccurences() {
        this.occurrences++;
    }

    /**
     * Getter for occurrences.
     * @return occurrences.
     */
    public int getOccurrences() {
        return occurrences;
    }

    /**
     * Setter for occurrences.
     * @param occurrences the new value for occurrences.
     */
    public void setOccurrences(final int occurrences) {
        this.occurrences = occurrences;
    }

    /**
     * Getter for group.
     * @return group.
     */
    public DynGroup getGroup() {
        return group;
    }

    /**
     * Setter for group.
     * @param group the new value for group.
     */
    public void setGroup(final DynGroup group) {
        this.group = group;
    }
}
