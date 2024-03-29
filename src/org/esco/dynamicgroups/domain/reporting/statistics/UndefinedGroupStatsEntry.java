/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.util.HashSet;
import java.util.Set;

import org.esco.dynamicgroups.dao.grouper.IGroupsDAOService;
import org.esco.dynamicgroups.domain.beans.II18NManager;

/**
 * Stats for the groups whithout a membership definition.
 * @author GIP RECIA - A. Deman
 * 5 mai 2009
 *
 */
public class UndefinedGroupStatsEntry implements IUndefinedGroupStatsEntry {

    /** Serial version UID.*/
    private static final long serialVersionUID = 2289834357420086979L;

    /** Key for the label of this entry. */
    private static final String UNDEF_GROUPS_KEY = "stats.groups.undefined";

    /** Key for the label for the list of undefined groups. */
    private static final String UNDEF_GROUPS_LIST_KEY = "stats.groups.undefined.list";

    /** The group service. */
    private transient IGroupsDAOService groupsService;

    /** The I18N Manager. */
    private transient II18NManager i18n;

    /** Names of the groups whithout a membership definition. */
    private Set<String> undefinedGroups;

    /**
     * Builds an instance of UndefinedGroupStatsEntry.
     * @param groupsService The groups service.
     * @param  i18n Th I18n manager.
     */
    public UndefinedGroupStatsEntry(final IGroupsDAOService groupsService, final II18NManager i18n) {
        this.groupsService = groupsService;
        this.i18n = i18n;
    }

    /**
     * Gives the list of the dynamic groups whithout a membership definition.
     * @return The list of the group names.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IUndefinedGroupStatsEntry#getUndefinedGroupNames()
     */
    public Set<String> getUndefinedGroupNames() {
        if (undefinedGroups == null) {
            undefinedGroups = groupsService.getUndefinedDynamicGroups();
        }
        return undefinedGroups;
    }

    /**
     * Gives the string that represents the entry.
     * @return The text that represents the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getEntry()
     */
    public String getEntry() {
        if (undefinedGroups == null) {
            undefinedGroups = groupsService.getUndefinedDynamicGroups();
        }
        return String.valueOf(undefinedGroups.size());
    }

    /**
     * Gives the label associated to the entry.
     * @return The string that contains the label for the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getLabel()
     */
    public String getLabel() {
        return i18n.getI18nMessage(UNDEF_GROUPS_KEY);
    }


    /**
     * Gives the label for the undefined groups names list.
     * @return The label for the list of undefined groups.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IUndefinedGroupStatsEntry#getUndefGroupNamesLabel()
     */
    public String getUndefGroupNamesLabel() {
        return i18n.getI18nMessage(UNDEF_GROUPS_LIST_KEY);  
    }

    /**
     * 
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#reset()
     */
    public void reset() {
        undefinedGroups = null;
    }

    /**
     * Initializes an instance.
     * @param initializationValues The instance that contains 
     * the initialization values.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#initializeFrom(IStatisticsEntry)
     */
    public void initializeFrom(final IStatisticsEntry initializationValues) {

        if (!(initializationValues instanceof UndefinedGroupStatsEntry)) {
            throw new IllegalArgumentException("The parameter is not an instance of " + getClass().getName());
        }
        
        final UndefinedGroupStatsEntry other = (UndefinedGroupStatsEntry) initializationValues;
        if (other.undefinedGroups != null) {
            this.undefinedGroups = new HashSet<String>();
            this.undefinedGroups.addAll(other.undefinedGroups);
        }
    }
    
    /**
     * Tests if there is some undefined group.
     * @return True if at least one group is undefined.
     */
    public boolean hasUndefinedGroup() {
        return undefinedGroups.size() > 0;
    }
}
