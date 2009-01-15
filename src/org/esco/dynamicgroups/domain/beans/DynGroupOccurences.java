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
    private int ocurrences;
    
    /**
     * Builds an instance of DynGroupOccurences.
     * @param group The considered group.
     */
    public DynGroupOccurences(final DynGroup group) {
        this.group = group; 
    }
    
    /**
     * Increments the number of occurences.
     */
    public void incrementOccurences() {
        this.ocurrences++;
    }

    /**
     * Getter for ocurrences.
     * @return ocurrences.
     */
    public int getOcurrences() {
        return ocurrences;
    }

    /**
     * Setter for ocurrences.
     * @param ocurrences the new value for ocurrences.
     */
    public void setOcurrences(final int ocurrences) {
        this.ocurrences = ocurrences;
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
