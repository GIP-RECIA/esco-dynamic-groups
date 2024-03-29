/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import org.esco.dynamicgroups.domain.beans.II18NManager;


/**
 * Entry for the statistics about the dynamic groups definition change.
 * Note that this class is not thread safe. This point has to be handled 
 * by the statistics manager implementation.
 * Informations: the number of modifications.
 * @author GIP RECIA - A. Deman
 * 24 févr. 2009
 *
 */
public class DefinitionModificationsStatsEntry implements IDefinitionModificationsStatsEntry {

    /** Serial version UID.*/
    private static final long serialVersionUID = -711243293922481685L;
    
    /** The key for the label. */
    private static final String LABEL_KEY = "stats.modified.definitions";

    /** The I18NManager. */
    private transient II18NManager i18n;
    
    /** Number of modified definitions. */
    private int nbModifiedDefinitions;
    
    /**
     * Builds an instance of DefinitionModificationsStatsEntry.
     * @param i18n The I18N manager.
     */
    public DefinitionModificationsStatsEntry(final II18NManager i18n) {
        this.i18n = i18n;
    }

    /**
     * Gives the hash code for this entry.
     * @return The hash code for the entry.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return nbModifiedDefinitions; 
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
        if (!(obj instanceof DefinitionModificationsStatsEntry)) {
            return false;
        }
        return ((DefinitionModificationsStatsEntry) obj).nbModifiedDefinitions == this.nbModifiedDefinitions;
    }

    /**
     * Gives the string representation of the entry.
     * @return The string that represents the entry.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "#{" + nbModifiedDefinitions + "}";  
    }

    /**
     * Gives the string that represents the entry.
     * @return The text that represents the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getEntry()
     */
    public String getEntry() {
        return String.valueOf(nbModifiedDefinitions);
    }

    /**
     * Gives the label associated to the entry.
     * @return The string that contains the label for the entry
     * in the specified format.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getLabel()
     */
    public String getLabel() {
        return i18n.getI18nMessage(LABEL_KEY);
    }

    /**
     * Resets the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#reset()
     */
    public void reset() {
        nbModifiedDefinitions = 0;
    }

    /**
     * Handles a modification of a dynamic definition.
     * @param groupName The name of the group.
     * @param previousDefinition The previous defintion.
     * @param newDefinition The new definition. 
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IDefinitionModificationsStatsEntry#
     * handleDefinitionModification(String, String, String)
     */
    public void handleDefinitionModification(final String groupName,
            final String previousDefinition, final String newDefinition) {
            nbModifiedDefinitions++;
    }
    
    /**
     * Initializes an instance.
     * @param initializationValues The instance that contains 
     * the initialization values.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#initializeFrom(IStatisticsEntry)
     */
    public void initializeFrom(final IStatisticsEntry initializationValues) {

        if (!(initializationValues instanceof DefinitionModificationsStatsEntry)) {
            throw new IllegalArgumentException("The parameter is not an instance of " + getClass().getName());
        }
        final DefinitionModificationsStatsEntry other = (DefinitionModificationsStatsEntry) initializationValues;
        this.nbModifiedDefinitions = other.nbModifiedDefinitions;
    }

}
