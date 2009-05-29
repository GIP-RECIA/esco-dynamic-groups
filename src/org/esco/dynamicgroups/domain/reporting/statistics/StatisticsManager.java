/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.grouper.IGroupsDAOService;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ReportingParametersSection;
import org.esco.dynamicgroups.domain.reporting.IReportFormatter;
import org.esco.dynamicgroups.util.ServletContextResourcesUtil;
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

    /** Serialization file name. */
    private static final String SER_FILE_NAME = StatisticsManager.class.getSimpleName() + ".ser";

    /** LOGGER. */
    private static final Logger LOGGER = Logger.getLogger(StatisticsManager.class);

    /** The user parameters provider instance. */
    private transient ParametersProvider parametersProvider;

    /** The user parameters for the reporting. */
    private transient ReportingParametersSection reportingParameters;

    /** The I18N Manager. */
    private transient I18NManager i18n;

    /** The statistics regarding the modifications of definition. */
    private IDefinitionModificationsStatsEntry definitionModifications;

    /** The SyncRepl notifications statistics. */
    private ISyncReplNotificationsStats syncReplNotifications;

    /** The groups statistics. */
    private IGroupAddOrDeletedStatsEntry groupsStats;

    /** Statistics for the undefined groups. */
    private IUndefinedGroupStatsEntry undefGroupsStats;

    /** Statistics for the groups activity. */
    private IGroupsActivityStatsEntry groupsActivityStats;

    /** The report formatter to use. */
    private transient IReportFormatter reportFormatter;

    /** The group service. */
    private transient IGroupsDAOService groupsService;

    /**Util class to serialize and load the instance.*/
    private transient ServletContextResourcesUtil resourceUtil;

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

        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " + this.getClass().getName() 
                + " can't be null.");

        Assert.notNull(this.i18n, 
                "The property i18n in the class " + this.getClass().getName() 
                + " can't be null.");

        Assert.notNull(this.reportFormatter, 
                "The property reportFormatter in the class " + this.getClass().getName() 
                + " can't be null.");

        Assert.notNull(this.resourceUtil, 
                "The property resourceUtil in the class " + this.getClass().getName() 
                + " can't be null.");

        reportingParameters = (ReportingParametersSection) parametersProvider.getReportingParametersSection();

        if (reportingParameters.getCountUndefiedGroups()) {
            Assert.notNull(this.groupsService, 
                    "The property groupsService in the class " + this.getClass().getName() 
                    + " can't be null.");
            undefGroupsStats = new UndefinedGroupStatsEntry(groupsService, i18n);
        }

        if (reportingParameters.getCountDefinitionModifications()) {
            definitionModifications = new DefinitionModificationsStats(i18n);
        }

        if (reportingParameters.getCountSyncReplNotifications()) {
            syncReplNotifications = new SyncReplNotificationsStats(i18n);
        }

        if (reportingParameters.getCountGroupCreationDeletion()) {
            groupsStats = new GroupsAddOrDeletedStatsEntry(i18n);
        }

        if (reportingParameters.getCountGroupsActivity()) {
            groupsActivityStats = new GroupsAcrivityStatsEntry(i18n);
        }
    }
    
        /**
     * Getter for parametersProvider.
     * @return parametersProvider.
     */
    public ParametersProvider getParametersProvider() {
        return parametersProvider;
    }

    /**
     * Setter for parametersProvider.
     * @param parametersProvider the new value for parametersProvider.
     */
    public void setParametersProvider(final ParametersProvider parametersProvider) {
        this.parametersProvider = parametersProvider;
    }

    /**
     * Generates the report.
     * @return The lines of the report.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#generateReport()
     */
    @Override
    public String generateReport() {
        String report = "";
        boolean separateGroupsActivity = false;

        if (syncReplNotifications != null) {
            synchronized (syncReplNotifications) {
                LOGGER.info(syncReplNotifications.getLabel() + syncReplNotifications.getEntry());
                report += reportFormatter.formatEntry(syncReplNotifications.getLabel(), "");
                report += reportFormatter.formatList(syncReplNotifications.getNotifications());
            }
            report += reportFormatter.getNewLine();
            report += reportFormatter.getSeparation();
            report += reportFormatter.getNewLine();
        }

        if (groupsStats != null) {
            separateGroupsActivity = true;
            synchronized (groupsStats) {
                LOGGER.info(groupsStats.getLabel() + groupsStats.getEntry());
                report += reportFormatter.formatEntry(groupsStats.getLabel(), 
                        groupsStats.getEntry());
                report += reportFormatter.getNewLine();
                report += reportFormatter.getNewLine();
            }
        }

        if (definitionModifications != null) {
            separateGroupsActivity = true;
            synchronized (definitionModifications) {
                LOGGER.info(definitionModifications.getLabel() + definitionModifications.getEntry());
                report += reportFormatter.formatEntry(definitionModifications.getLabel(), 
                        definitionModifications.getEntry());
                report += reportFormatter.getNewLine();
                report += reportFormatter.getNewLine();
            }
        }

        if (undefGroupsStats != null) {
            separateGroupsActivity = true;
            synchronized (undefGroupsStats) {
                LOGGER.info(undefGroupsStats.getLabel() + undefGroupsStats.getEntry());
                LOGGER.info(undefGroupsStats.getUndefinedGroupNames());
                report += reportFormatter.formatEntry(undefGroupsStats.getLabel(), 
                        undefGroupsStats.getEntry());
                report += reportFormatter.getNewLine();
                report += reportFormatter.format(undefGroupsStats.getUndefGroupNamesLabel());
                report += reportFormatter.formatList(undefGroupsStats.getUndefinedGroupNames());
            }
            report += reportFormatter.getNewLine();
            report += reportFormatter.getNewLine();
        }

        if (groupsActivityStats != null) {
            if (separateGroupsActivity) {
                report += reportFormatter.getNewLine();
                report += reportFormatter.getSeparation();
                report += reportFormatter.getNewLine();
            }

            synchronized (groupsActivityStats) {
                LOGGER.info(groupsActivityStats.getLabel() + groupsActivityStats.getEntry());

                report += reportFormatter.formatEntry(groupsActivityStats.getLabel(), groupsActivityStats.getEntry());
                report += reportFormatter.getNewLine();
                report += reportFormatter.format(groupsActivityStats.getActiveGroupsLabel());
                report += reportFormatter.formatList(groupsActivityStats.getActiveGroups());
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
        if (undefGroupsStats != null) {
            synchronized (undefGroupsStats) {
                undefGroupsStats.reset();
            }
        }

        if (groupsActivityStats != null) {
            synchronized (groupsActivityStats) {
                groupsActivityStats.reset();
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
     * Handles the add of a memebr in a group.
     * @param groupName The name of the group.
     * @param userId The id of the user.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#handleMemberAdded(String, String)
     */
    public void handleMemberAdded(final String groupName, final String userId) {
        if (groupsActivityStats != null) {
            synchronized (groupsActivityStats) {
                groupsActivityStats.handleAddedUser(groupName, userId);
            }
        }
    }

    /**
     * Handles the action of removing a member in a group.
     * @param groupName The name of the group.
     * @param userId The id of the user.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#handleMemberRemoved(String, String)
     */
    public void handleMemberRemoved(final String groupName, final String userId) {
        if (groupsActivityStats != null) {
            synchronized (groupsActivityStats) {
                groupsActivityStats.handleRemovedUser(groupName, userId);
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

    /**
     * Getter for groupsService.
     * @return groupsService.
     */
    public IGroupsDAOService getGroupsService() {
        return groupsService;
    }

    /**
     * Setter for groupsService.
     * @param groupsService the new value for groupsService.
     */
    public void setGroupsService(final IGroupsDAOService groupsService) {
        this.groupsService = groupsService;
    }

    /**
     * Loads the values from a serialized instance if it exists.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#load()
     */
    public void load() { 
        try {
            final File file = resourceUtil.getResource(SER_FILE_NAME).getFile();
            if (file.exists()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Loading serialized instance from: " + file.getAbsolutePath());
                }
                final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                final StatisticsManager previousInstance = (StatisticsManager) ois.readObject();
                if (previousInstance != null) {
                    this.definitionModifications = previousInstance.definitionModifications;
                    this.syncReplNotifications = previousInstance.syncReplNotifications;
                    this.groupsStats = previousInstance.groupsStats;
                    this.undefGroupsStats = previousInstance.undefGroupsStats;
                    this.groupsActivityStats = previousInstance.groupsActivityStats;
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Serialization file not found: " + file.getAbsolutePath());
                }
                
            }
        } catch (IOException ioe) {
            LOGGER.error(ioe, ioe);
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error(cnfe, cnfe);
        }

    }

    /**
     * Serializes the instance.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#save()
     */
    public void save() {
        try {
            final File file = resourceUtil.getResource(SER_FILE_NAME).getFile();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Serialization of the instance intothe file: " + file.getAbsolutePath());
            }
            final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(this);
        } catch (IOException ioe) {
            LOGGER.error(ioe, ioe);
        }
    }

    /**
     * Getter for resourceUtil.
     * @return resourceUtil.
     */
    public ServletContextResourcesUtil getResourceUtil() {
        return resourceUtil;
    }

    /**
     * Setter for resourceUtil.
     * @param resourceUtil the new value for resourceUtil.
     */
    public void setResourceUtil(final ServletContextResourcesUtil resourceUtil) {
        this.resourceUtil = resourceUtil;
    }

}
