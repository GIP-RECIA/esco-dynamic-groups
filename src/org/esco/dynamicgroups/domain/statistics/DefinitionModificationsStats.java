/**
 * 
 */
package org.esco.dynamicgroups.domain.statistics;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Entry for the statistics about the dynamic groups definition change.
 * Informations: the number of modifications.
 * @author GIP RECIA - A. Deman
 * 24 févr. 2009
 *
 */
public class DefinitionModificationsStats extends BaseStatsEntry {

    /** Serial version UID.*/
    private static final long serialVersionUID = -711243293922481685L;

    /** Number of modified definitions. */
    private int nbModifiedDefintions;
    
    



    /**
     * Gives the hash code for this entry.
     * @return The hash code for the entry.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return nbModifiedDefintions; 
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
        if (!(obj instanceof DefinitionModificationsStats)) {
            return false;
        }
        return ((DefinitionModificationsStats) obj).nbModifiedDefintions == this.nbModifiedDefintions;
    }

    /**
     * Gives the string representation of the entry.
     * @return The string that represents the entry.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "#{" + nbModifiedDefintions + "}";  
    }

    /**
     * Gives the string that represents the entry.
     * @param format The format of the output.
     * @return Tghe text that represents the entry in the specified format
     * @see org.esco.dynamicgroups.domain.statistics.BaseStatsEntry#
     * getEntry(org.esco.dynamicgroups.domain.statistics.BaseStatsEntry.OutputFormat)
     */
    public String getEntry(final OutputFormat format) {
        return null;
        
    }

    /**
     * Gives the label associated to the entry.
     * @param format The format of the output.
     * @return The string that contains the label for the entry
     * in the specified format.
     * @see org.esco.dynamicgroups.domain.statistics.BaseStatsEntry#
     * getLabel(org.esco.dynamicgroups.domain.statistics.BaseStatsEntry.OutputFormat)
     */
    public String getLabel(final OutputFormat format) {
        return null;
    }

    /**
     * Resets the entry.
     * @see org.esco.dynamicgroups.domain.statistics.BaseStatsEntry#reset()
     */
    public void reset() {
        
    }

   


   
}
