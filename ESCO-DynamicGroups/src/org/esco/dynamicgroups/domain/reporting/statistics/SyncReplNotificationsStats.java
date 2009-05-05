/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import org.esco.dynamicgroups.domain.beans.I18NManager;

/**
 * Sync Repl notifications.
 * This class is not thread safe, as it is the responsability of the statistics manager.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public class SyncReplNotificationsStats implements ISyncReplNotificationsStats {

    /** Serial version UID. */
    private static final long serialVersionUID = -1657137526790547062L;

    /** The key for the label. */
    private static final String LABEL_KEY = "stats.syncrepl.notifications";

    /** The key for the add notification. */
    private static final String ADD_KEY = "stats.syncrepl.notifications.add";

    /** The key for the delete notification. */
    private static final String DELETE_KEY = "stats.syncrepl.notifications.delete";

    /** The key for the modify notification. */
    private static final String MODIFY_KEY = "stats.syncrepl.notifications.modify";

    /** The key for the present notification. */
    private static final String PRESENT_KEY = "stats.syncrepl.notifications.present";



    /** The add count. */
    private int addCount;

    /** The delete count. */
    private int deleteCount;

    /** The modify count. */
    private int modifyCount;

    /** The present count. */
    private int presentCount;

    /** The I18N manager. */
    private I18NManager i18n;

    /**
     * Builds an instance of SyncReplNotificationsStats.
     * @param i18n The i18N manager.
     */
    public SyncReplNotificationsStats(final I18NManager i18n) {
        this.i18n = i18n;
    }

    /**
     * Handles an Add action.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ISyncReplNotificationsStats#handeAddAction()
     */
    public void handeAddAction() {
        addCount++;
    }

    /**
     * Handles a Delete action.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ISyncReplNotificationsStats#handeDeleteAction()
     */
    public void handeDeleteAction() {
        deleteCount++;
    }

    /**
     * Handles a Modify action.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ISyncReplNotificationsStats#handeModifyAction()
     */
    public void handeModifyAction() {
        modifyCount++;
    }

    /**
     * Handles a Present action.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.ISyncReplNotificationsStats#handePresentAction()
     */
    public void handePresentAction() {
        presentCount++;
    }

    /**
     * Gives the string that represents the entry.
     * @return The text that represents the entry.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsEntry#getEntry()
     */
    public String getEntry() {
        return i18n.getI18nMessage(ADD_KEY) + addCount 
        + " - " + i18n.getI18nMessage(DELETE_KEY) + deleteCount 
        + " - " + i18n.getI18nMessage(MODIFY_KEY) + modifyCount 
        + " - " + i18n.getI18nMessage(PRESENT_KEY) + presentCount;
    }

    /**
     * Gives the label associated to the entry.
     * @return The string that contains the label for the entry.
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
        addCount = 0;
        deleteCount = 0;
        modifyCount = 0;
        presentCount = 0;
    }

}