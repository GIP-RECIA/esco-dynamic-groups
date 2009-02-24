/**
 * 
 */
package org.esco.dynamicgroups.domain.statistics;

import java.io.Serializable;

/**
 * Entry for the statistics about the dynamic groups definition change.
 * @author GIP RECIA - A. Deman
 * 24 févr. 2009
 *
 */
public class DefinitionStatsEntry implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -711243293922481685L;
    
    /** The previous definition of the dynamic group. */
    private String previousDefinition;
    
    /** The new definition of the dynamic group. */
    private String newDefinition;

    /**
     * Builds an instance of DefinitionStatsEntry.
     * @param previousDefinition The previous definition of the dynamic group.
     * @param newDefinition  The new definition of the dynamic group. 
     */
    public DefinitionStatsEntry(final String previousDefinition, final String newDefinition) {
        this.previousDefinition = previousDefinition;
        this.newDefinition = newDefinition;
        
    }

    /**
     * Getter for previousDefinition.
     * @return previousDefinition.
     */
    public String getPreviousDefinition() {
        return previousDefinition;
    }

    /**
     * Getter for newDefinition.
     * @return newDefinition.
     */
    public String getNewDefinition() {
        return newDefinition;
    }
    
    /**
     * Gives the hash code for this entry.
     * @return The hash code for the entry.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return previousDefinition.hashCode() + newDefinition.hashCode(); 
    }
    
    /**
     * Test the equality with another object.
     * @param obj The object to compare this entry with.
     * @return True if the object is equal to this entry.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DefinitionStatsEntry)) {
            return false;
        }
        final DefinitionStatsEntry other = (DefinitionStatsEntry) obj;
        
        return other.getPreviousDefinition().equals(getPreviousDefinition()) 
            && other.getNewDefinition().equals(getNewDefinition());
    }
    
    /**
     * Gives the string representation of the entry.
     * @return The string that represents the entry.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "#{" + getPreviousDefinition() 
            + ", " + getNewDefinition() + "}";  
    }
}
