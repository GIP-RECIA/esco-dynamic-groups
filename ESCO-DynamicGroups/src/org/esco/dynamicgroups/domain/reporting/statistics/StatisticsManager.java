/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;


import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.esco.dynamicgroups.domain.reporting.IReportFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Reference implementation of the statistics manager.
 * The underlying managers are not thread safe, so it is the responsability of this manager. 
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public class StatisticsManager implements IStatisticsManager, InitializingBean {

    /** Serial version UID.*/
    private static final long serialVersionUID = -825908954001800235L;

    /** LOGGER. */
    private static final Logger LOGGER = Logger.getLogger(StatisticsManager.class);

    /** The user parameters instance. */
    private ESCODynamicGroupsParameters parameters;

    /** The I18N Manager. */
    private I18NManager i18n;

    /** The statistics regarding the modifications of definition. */
    private IDefinitionModificationsStatsEntry definitionModifications;

    /** The SyncRepl notifications statistics. */
    private ISyncReplNotificationsStats syncReplNotifications;

    /** The groups statistics. */
    private IGroupAddOrDeletedStatsEntry groupsStats;

    /** The report formatter to use. */
    private IReportFormatter reportFormatter;

    /**
     * Builds an instance of StatisticsManager.
     */
    public StatisticsManager() {
        super();
    }

    /**
     * Checks the bean injection. 
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(this.parameters, 
                "The property parameters in the class " + this.getClass().getName() 
                + " can't be null.");

        Assert.notNull(this.i18n, 
                "The property i18n in the class " + this.getClass().getName() 
                + " can't be null.");
        Assert.notNull(this.reportFormatter, 
                "The property reportFormatter in the class " + this.getClass().getName() 
                + " can't be null.");

        if (parameters.getCountDefinitionModifications()) {
            definitionModifications = new DefinitionModificationsStats(i18n);
        }

        if (parameters.getCountSyncReplNotifications()) {
            syncReplNotifications = new SyncReplNotificationsStats(i18n);
        }

        if (parameters.getCountGroupCreationDeletion()) {
            groupsStats = new GroupsAddOrDeletedStatsEntry(i18n);
        }


    }

    /**
     * Getter for parameters.
     * @return parameters.
     */
    public ESCODynamicGroupsParameters getParameters() {
        return parameters;
    }

    /**
     * Setter for parameters.
     * @param parameters the new value for parameters.
     */
    public void setParameters(final ESCODynamicGroupsParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Generates the report.
     * @return The lines of the report.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#generateReport()
     */
    @Override
    public String generateReport() {
        String report = "";
        
        if (syncReplNotifications != null) {
            synchronized (syncReplNotifications) {
                LOGGER.info(syncReplNotifications.getLabel() + syncReplNotifications.getEntry());
                report += reportFormatter.formatEntry(syncReplNotifications.getLabel(), 
                        syncReplNotifications.getEntry());
                report += reportFormatter.getNewLine();
            }
            report += reportFormatter.getNewLine();
            report += reportFormatter.getNewLine();
            report += reportFormatter.getNewLine();
        }
        
        if (definitionModifications != null) {
            synchronized (definitionModifications) {
                LOGGER.info(definitionModifications.getLabel() + definitionModifications.getEntry());
                report += reportFormatter.formatEntry(definitionModifications.getLabel(), 
                        definitionModifications.getEntry());
                report += reportFormatter.getNewLine();
            }
        }
        if (groupsStats != null) {
            synchronized (groupsStats) {
                LOGGER.info(groupsStats.getLabel() + groupsStats.getEntry());
                report += reportFormatter.formatEntry(groupsStats.getLabel(), 
                        groupsStats.getEntry());
                report += reportFormatter.getNewLine();
            }
        }
        return report;
    }

    /**
     * Handles a modification of a dynamic definition.
     * @param groupName The name of the group.
     * @param previousDefinition The previous defintion.
     * @param newDefinition The new definition.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#
     * handleDefinitionModification(String, String, String)
     */
    @Override
    public void handleDefinitionModification(final String groupName,
            final String previousDefinition,
            final String newDefinition) {
        if (definitionModifications != null) {
            synchronized (definitionModifications) {
                definitionModifications.handleDefinitionModification(groupName, previousDefinition, newDefinition);
            }
        }
    }

    /**
     * Handles the SyncRepl notification.
     * @param control The control in the LDAP search result.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#
     * handleSyncReplNotifications(SyncStateControl)
     */
    public void handleSyncReplNotifications(final SyncStateControl control) {
        if (syncReplNotifications != null) {
            synchronized (syncReplNotifications) {
                if (control.isAdd()) {
                    syncReplNotifications.handeAddAction();
                } else if (control.isModify()) {
                    syncReplNotifications.handeModifyAction();
                } else if (control.isDelete()) {
                    syncReplNotifications.handeDeleteAction();
                } else {
                    syncReplNotifications.handePresentAction();
                }
            }
        }
    }

    /**
     * Resets the statistics manager.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#reset()
     */
    @Override
    public void reset() {

        if (definitionModifications != null) {
            synchronized (definitionModifications) {
                definitionModifications.reset();
            }
        }

        if (syncReplNotifications != null) {
            synchronized (syncReplNotifications) {
                syncReplNotifications.reset();
            }
        }

        if (groupsStats != null) {
            synchronized (groupsStats) {
                groupsStats.reset();
            }
        }
    }


    /**
     * Handles the creation of a group.
     * @param groupName The name of the group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#handleCreatedGroup(String)
     */
    public void handleCreatedGroup(final String groupName) {
        if (groupsStats != null) {
            synchronized (groupsStats) {
                groupsStats.handleCreatedGroup(groupName);
            }
        }
    }

    /**
     * Handles the deletion of a group.
     * @param groupName The name of the group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#handleDeletedGroup(String)
     */
    public void handleDeletedGroup(final String groupName) {
        if (groupsStats != null) {
            synchronized (groupsStats) {
                groupsStats.handleDeletedGroup(groupName);
            }
        }
    }

    /**
     * Getter for i18n.
     * @return i18n.
     */
    public I18NManager getI18n() {
        return i18n;
    }

    /**
     * Setter for i18n.
     * @param i18n the new value for i18n.
     */
    public void setI18n(final I18NManager i18n) {
        this.i18n = i18n;
    }

    /**
     * Getter for reportFormatter.
     * @return reportFormatter.
     */
    public IReportFormatter getReportFormatter() {
        return reportFormatter;
    }

    /**
     * Setter for reportFormatter.
     * @param reportFormatter the new value for reportFormatter.
     */
    public void setReportFormatter(final IReportFormatter reportFormatter) {
        this.reportFormatter = reportFormatter;
    }

    /**
     * Getter for groupsStats.
     * @return groupsStats.
     */
    public IGroupAddOrDeletedStatsEntry getGroupsStats() {
        return groupsStats;
    }

    /**
     * Setter for groupsStats.
     * @param groupsStats the new value for groupsStats.
     */
    public void setGroupsStats(final IGroupAddOrDeletedStatsEntry groupsStats) {
        this.groupsStats = groupsStats;
    }

}
