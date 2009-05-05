/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.util.Set;

import org.esco.dynamicgroups.dao.grouper.IGroupsDAOService;
import org.esco.dynamicgroups.domain.beans.I18NManager;

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

    /** The group service. */
    private IGroupsDAOService groupsService;
    
    /** The I18N Manager. */
    private I18NManager i18n;
    
    /** Names of the groups whithout a membership definition. */
   private Set<String> undefinedGroups;

    /**
     * Builds an instance of UndefinedGroupStatsEntry.
     * @param groupsService The groups service.
     * @param  i18n Th I18n manager.
     */
    public UndefinedGroupStatsEntry(final IGroupsDAOService groupsService, final I18NManager i18n) {
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
    @Override
    public String getLabel() {
        return i18n.getI18nMessage(UNDEF_GROUPS_KEY);
    }

    /**
     * 
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#reset()
     */
    @Override
    public void reset() {
        undefinedGroups = null;

    }
}
