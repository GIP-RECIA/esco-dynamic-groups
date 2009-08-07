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
import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.grouper.IGroupsDAOService;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
import org.esco.dynamicgroups.domain.IDomainService;
import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ReportingParametersSection;
import org.esco.dynamicgroups.domain.reporting.IReportFormatter;
import org.esco.dynamicgroups.util.IResourcesProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Bundle for the statistics informations.
 * @author GIP RECIA - A. Deman
 * 24 juil. 2009
 *
 */
class StatsBundle implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -4096407571735607701L;

    /** The statistics regarding the modifications of definition. */
    private IDefinitionModificationsStatsEntry definitionModifications;

    /** The SyncRepl notifications statistics. */
    private ISyncReplNotificationsStats syncReplNotifications;

    /** The groups statistics. */
    private IGroupCreatedOrDeletedStatsEntry groupsStats;

    /** Statistics for the undefined groups. */
    private IUndefinedGroupStatsEntry undefGroupsStats;

    /** Statistics for the groups activity. */
    private IGroupsActivityStatsEntry groupsActivityStats;

    /** Statistics for the check of invalid or missing members. */
    private ICheckedMembersStatsEntry checkedMembersStats;

    /**
     * Builds an instance of StatsBundle.
     */
    public StatsBundle() {
        super();
    }
    
    /**
     * Getter for definitionModifications.
     * @return definitionModifications.
     */
    public IDefinitionModificationsStatsEntry getDefinitionModifications() {
        return definitionModifications;
    }

    /**
     * Setter for statsBundle.getDefinitionModifications().
     * @param definitionModifications the new value for statsBundle.getDefinitionModifications().
     */
    public void setDefinitionModifications(final IDefinitionModificationsStatsEntry definitionModifications) {
        this.definitionModifications = definitionModifications;
    }

    /**
     * Getter for syncReplNotifications.
     * @return syncReplNotifications.
     */
    public ISyncReplNotificationsStats getSyncReplNotifications() {
        return syncReplNotifications;
    }

    /**
     * Setter for syncReplNotifications.
     * @param syncReplNotifications the new value for syncReplNotifications.
     */
    public void setSyncReplNotifications(final ISyncReplNotificationsStats syncReplNotifications) {
        this.syncReplNotifications = syncReplNotifications;
    }

    /**
     * Getter for groupsStats.
     * @return groupsStats.
     */
    public IGroupCreatedOrDeletedStatsEntry getGroupsStats() {
        return groupsStats;
    }

    /**
     * Setter for groupsStats.
     * @param groupsStats the new value for groupsStats.
     */
    public void setGroupsStats(final IGroupCreatedOrDeletedStatsEntry groupsStats) {
        this.groupsStats = groupsStats;
    }

    /**
     * Getter for undefGroupsStats.
     * @return undefGroupsStats.
     */
    public IUndefinedGroupStatsEntry getUndefGroupsStats() {
        return undefGroupsStats;
    }

    /**
     * Setter for undefGroupsStats.
     * @param undefGroupsStats the new value for undefGroupsStats.
     */
    public void setUndefGroupsStats(final IUndefinedGroupStatsEntry undefGroupsStats) {
        this.undefGroupsStats = undefGroupsStats;
    }

    /**
     * Getter for groupsActivityStats.
     * @return groupsActivityStats.
     */
    public IGroupsActivityStatsEntry getGroupsActivityStats() {
        return groupsActivityStats;
    }

    /**
     * Setter for groupsActivityStats.
     * @param groupsActivityStats the new value for groupsActivityStats.
     */
    public void setGroupsActivityStats(final IGroupsActivityStatsEntry groupsActivityStats) {
        this.groupsActivityStats = groupsActivityStats;
    }

    /**
     * Getter for checkedMembersStats.
     * @return checkedMembersStats.
     */
    public ICheckedMembersStatsEntry getCheckedMembersStats() {
        return checkedMembersStats;
    }

    /**
     * Setter for checkedMembersStats.
     * @param checkedMembersStats the new value for checkedMembersStats.
     */
    public void setCheckedMembersStats(final ICheckedMembersStatsEntry checkedMembersStats) {
        this.checkedMembersStats = checkedMembersStats;
    }

   
}
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

    /** Bundle of statistic data. */
    private StatsBundle statsBundle = new StatsBundle();

    /** The report formatter to use. */
    private transient IReportFormatter reportFormatter;

    /** The domain service used to check the members of the
     * dynamic groups. */
    private transient IDomainService domainService;

    /** The group service. */
    private transient IGroupsDAOService groupsService;

    /**Util class to serialize and load the instance.*/
    private transient IResourcesProvider resourcesProvider;

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

        final String canNotBeNull = " can't be null.";

        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " + this.getClass().getName() 
                + canNotBeNull);

        Assert.notNull(this.i18n, 
                "The property i18n in the class " + this.getClass().getName() 
                + canNotBeNull);

        Assert.notNull(this.reportFormatter, 
                "The property reportFormatter in the class " + this.getClass().getName() 
                + canNotBeNull);

        Assert.notNull(this.resourcesProvider, 
                "The property resourcesProvider in the class " + this.getClass().getName() 
                + canNotBeNull);

        Assert.notNull(this.groupsService, 
                "The property groupsService in the class " + this.getClass().getName() 
                + canNotBeNull);

        Assert.notNull(this.domainService, 
                "The property domainService in the class "  + this.getClass().getName() 
                + canNotBeNull);

        reportingParameters = (ReportingParametersSection) parametersProvider.getReportingParametersSection();

        if (reportingParameters.getEnabled()) {
            if (reportingParameters.getCountUndefiedGroups()) {
                statsBundle.setUndefGroupsStats(new UndefinedGroupStatsEntry(groupsService, i18n));
            }

            if (reportingParameters.getCountDefinitionModifications()) {
                statsBundle.setDefinitionModifications(new DefinitionModificationsStatsEntry(i18n));
            }

            if (reportingParameters.getCountSyncReplNotifications()) {
                statsBundle.setSyncReplNotifications(new SyncReplNotificationsStatsEntry(i18n));
            }

            if (reportingParameters.getCountGroupCreationDeletion()) {
                statsBundle.setGroupsStats(new GroupsCreatedOrDeletedStatsEntry(i18n));
            }

            if (reportingParameters.getCountGroupsActivity()) {
                statsBundle.setGroupsActivityStats(new GroupsActivityStatsEntry(i18n));
            }

            if (reportingParameters.getCountInvalidOrMissingMembers()) {
                statsBundle.setCheckedMembersStats(new CheckedMembersStatsEntry(i18n));
            }
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
     * Generates the report for the verification of the members of the dynamic groups.
     * For this report all the members are checked, without a time limit.
     * @return The Report string.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#generateGroupsMembersCheckReport()
     */
    public String generateGroupsMembersCheckReport() {
        handleStartOfMembersCheckingProcess();
        domainService.startMembersCheckingProcess();

        handleEndOfMembersCheckingProcess();

        String report = "";

        if (statsBundle.getCheckedMembersStats() != null) {
            synchronized (statsBundle.getCheckedMembersStats()) {

                if (statsBundle.getCheckedMembersStats().checkPerformed()) {

                    LOGGER.info(statsBundle.getCheckedMembersStats().getLabel() 
                            + statsBundle.getCheckedMembersStats().getEntry());

                    report += reportFormatter.formatEntry(statsBundle.getCheckedMembersStats().getLabel(), 
                            statsBundle.getCheckedMembersStats().getEntry());
                    report += reportFormatter.getNewLine();
                    final Set<String> invalidGroups = statsBundle.getCheckedMembersStats().getInvalidGroups();
                    final int paddSimple = 3;
                    final int paddDouble = paddSimple * 2;


                    for (String invalidGroup : invalidGroups) {
                        report += reportFormatter.padd(invalidGroup, paddSimple);
                        report += reportFormatter.getNewLine();

                        MissingOrInvalidMembersEntry invalidMbInfos = 
                            statsBundle.getCheckedMembersStats().getCheckingResult(invalidGroup);

                        if (invalidMbInfos.hasInvalidMembers()) {
                            report += reportFormatter.padd(
                                    statsBundle.getCheckedMembersStats().getInvalidMembersLabel() 
                                    + invalidMbInfos.getInvalidMembers(), paddDouble);
                        }

                        if (invalidMbInfos.hasMissingMembers()) {
                            report += reportFormatter.padd(
                                    statsBundle.getCheckedMembersStats().getMissingMembersLabel() 
                                    + invalidMbInfos.getMissingMembers(), paddDouble);
                        }
                        report += reportFormatter.getNewLine();
                    }
                }
            }
        }
        report += reportFormatter.getNewLine();

        return report;
    }

    /**
     * Generates the report.
     * @return The lines of the report.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#generateReport()
     */
    public String generateReport() {
        String report = "";
        boolean separateGroupsActivity = false;

        // --- SyncRepl Notifications section.
        if (statsBundle.getSyncReplNotifications() != null) {
            synchronized (statsBundle.getSyncReplNotifications()) {
                LOGGER.info(statsBundle.getSyncReplNotifications().getLabel() 
                        + statsBundle.getSyncReplNotifications().getEntry());
                report += reportFormatter.formatEntry(statsBundle.getSyncReplNotifications().getLabel(), "");
                report += reportFormatter.formatList(statsBundle.getSyncReplNotifications().getNotifications());
            }
            report += reportFormatter.getNewLine();
            report += reportFormatter.getSeparation();
            report += reportFormatter.getNewLine();
        }


        // -- Groups statsBundle section.    
        if (statsBundle.getGroupsStats() != null) {
            separateGroupsActivity = true;
            synchronized (statsBundle.getGroupsStats()) {
                LOGGER.info(statsBundle.getGroupsStats().getLabel() + statsBundle.getGroupsStats().getEntry());
                report += reportFormatter.formatEntry(statsBundle.getGroupsStats().getLabel(), 
                        statsBundle.getGroupsStats().getEntry());
                report += reportFormatter.getNewLine();
                report += reportFormatter.getNewLine();
            }
        }

        // --- Modifications of definition section.
        if (statsBundle.getDefinitionModifications() != null) {
            separateGroupsActivity = true;
            synchronized (statsBundle.getDefinitionModifications()) {
                LOGGER.info(statsBundle.getDefinitionModifications().getLabel()
                        + statsBundle.getDefinitionModifications().getEntry());
                report += reportFormatter.formatEntry(statsBundle.getDefinitionModifications().getLabel(), 
                        statsBundle.getDefinitionModifications().getEntry());
                report += reportFormatter.getNewLine();
                report += reportFormatter.getNewLine();
            }
        }

        // --- Dynamic groups without a members definitions.
        if (statsBundle.getUndefGroupsStats() != null) {
            separateGroupsActivity = true;
            synchronized (statsBundle.getUndefGroupsStats()) {
                LOGGER.info(statsBundle.getUndefGroupsStats().getLabel() 
                        + statsBundle.getUndefGroupsStats().getEntry());
                LOGGER.info(statsBundle.getUndefGroupsStats().getUndefinedGroupNames());
                report += reportFormatter.formatEntry(statsBundle.getUndefGroupsStats().getLabel(), 
                        statsBundle.getUndefGroupsStats().getEntry());

                if (statsBundle.getUndefGroupsStats().hasUndefinedGroup()) {
                    report += reportFormatter.getNewLine();
                    report += reportFormatter.format(statsBundle.getUndefGroupsStats().getUndefGroupNamesLabel());
                    report += reportFormatter.formatList(statsBundle.getUndefGroupsStats().getUndefinedGroupNames());
                }
            }
            report += reportFormatter.getNewLine();
            report += reportFormatter.getNewLine();
        }

        boolean separateMembersCheck = separateGroupsActivity;

        // --- Activity of dynamic groups section.
        if (statsBundle.getGroupsActivityStats() != null) {
            separateMembersCheck = true;
            if (separateGroupsActivity) {
                report += reportFormatter.getNewLine();
                report += reportFormatter.getSeparation();
                report += reportFormatter.getNewLine();
            }

            synchronized (statsBundle.getGroupsActivityStats()) {
                LOGGER.info(statsBundle.getGroupsActivityStats().getLabel() 
                        + statsBundle.getGroupsActivityStats().getEntry());

                report += reportFormatter.formatEntry(statsBundle.getGroupsActivityStats().getLabel(), 
                        statsBundle.getGroupsActivityStats().getEntry());

                if (statsBundle.getGroupsActivityStats().hasActiveGroup()) {
                    report += reportFormatter.getNewLine();
                    report += reportFormatter.format(statsBundle.getGroupsActivityStats().getActiveGroupsLabel());
                    report += reportFormatter.formatList(statsBundle.getGroupsActivityStats().getActiveGroups());
                }
            }
            report += reportFormatter.getNewLine();
        }


        // --- Memebers check section.
        if (statsBundle.getCheckedMembersStats() != null) {

            // Creates a timer to stop the processus if needed.
            final int durationMinutes = reportingParameters.getMembersCheckingDuration();
            if (durationMinutes > 0) {
                new CheckingMembersProcessStopper(domainService, durationMinutes);
            }

            // generate the report.
            final String membersCheckReport = generateGroupsMembersCheckReport();

            if (separateMembersCheck && !"".equals(membersCheckReport)) {
                report += reportFormatter.getNewLine();
                report += reportFormatter.getSeparation();
                report += reportFormatter.getNewLine();
            }

            report += membersCheckReport;
        }
        report += reportFormatter.getNewLine();

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
    public void handleDefinitionModification(final String groupName,
            final String previousDefinition,
            final String newDefinition) {
        if (statsBundle.getDefinitionModifications() != null) {
            synchronized (statsBundle.getDefinitionModifications()) {
                statsBundle.getDefinitionModifications().handleDefinitionModification(groupName, 
                        previousDefinition, newDefinition);
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
        if (statsBundle.getSyncReplNotifications() != null) {
            synchronized (statsBundle.getSyncReplNotifications()) {
                if (control.isAdd()) {
                    statsBundle.getSyncReplNotifications().handeAddAction();
                } else if (control.isModify()) {
                    statsBundle.getSyncReplNotifications().handeModifyAction();
                } else if (control.isDelete()) {
                    statsBundle.getSyncReplNotifications().handeDeleteAction();
                } else {
                    statsBundle.getSyncReplNotifications().handePresentAction();
                }
            }
        }
    }

    /**
     * Resets the statistics manager.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#reset()
     */
    public void reset() {

        if (statsBundle.getDefinitionModifications() != null) {
            synchronized (statsBundle.getDefinitionModifications()) {
                statsBundle.getDefinitionModifications().reset();
            }
        }

        if (statsBundle.getSyncReplNotifications() != null) {
            synchronized (statsBundle.getSyncReplNotifications()) {
                statsBundle.getSyncReplNotifications().reset();
            }
        }

        if (statsBundle.getGroupsStats() != null) {
            synchronized (statsBundle.getGroupsStats()) {
                statsBundle.getGroupsStats().reset();
            }
        }
        if (statsBundle.getUndefGroupsStats() != null) {
            synchronized (statsBundle.getUndefGroupsStats()) {
                statsBundle.getUndefGroupsStats().reset();
            }
        }

        if (statsBundle.getGroupsActivityStats() != null) {
            synchronized (statsBundle.getGroupsActivityStats()) {
                statsBundle.getGroupsActivityStats();
            }
        }

        if (statsBundle.getCheckedMembersStats() != null) {
            synchronized (statsBundle.getCheckedMembersStats()) {
                statsBundle.getCheckedMembersStats().reset();
            }
        }
    }

    /**
     * Handles the creation of a group.
     * @param groupName The name of the group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#handleCreatedGroup(String)
     */
    public void handleCreatedGroup(final String groupName) {
        if (statsBundle.getGroupsStats() != null) {
            synchronized (statsBundle.getGroupsStats()) {
                statsBundle.getGroupsStats().handleCreatedGroup(groupName);
            }
        }
    }

    /**
     * Handles the deletion of a group.
     * @param groupName The name of the group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#handleDeletedGroup(String)
     */
    public void handleDeletedGroup(final String groupName) {
        if (statsBundle.getGroupsStats() != null) {
            synchronized (statsBundle.getGroupsStats()) {
                statsBundle.getGroupsStats().handleDeletedGroup(groupName);
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
        if (statsBundle.getGroupsActivityStats() != null) {
            synchronized (statsBundle.getGroupsActivityStats()) {
                statsBundle.getGroupsActivityStats().handleAddedUser(groupName, userId);
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
        if (statsBundle.getGroupsActivityStats() != null) {
            synchronized (statsBundle.getGroupsActivityStats()) {
                statsBundle.getGroupsActivityStats().handleRemovedUser(groupName, userId);
            }
        }
    }

    /** 
     * Handles a group with an invalid member.
     * @param groupName The name of the group.
     * @param userId The id of the invalid memeber.
     */
    public void handleInvalidMember(final String groupName, final String userId) {
        if (statsBundle.getCheckedMembersStats() != null) {
            synchronized (statsBundle.getCheckedMembersStats()) {
                statsBundle.getCheckedMembersStats().handleInvalidMember(groupName, userId);
            }
        }
    }

    /** 
     * Handles a group with a missing member.
     * @param groupName The name of the group.
     * @param userId The id of the missing member.
     */
    public void handleMissingMember(final String groupName, final String userId) {
        if (statsBundle.getCheckedMembersStats() != null) {
            synchronized (statsBundle.getCheckedMembersStats()) {
                statsBundle.getCheckedMembersStats().handleMissingMember(groupName, userId);
            }
        }
    }

    /**
     * Handles the notification of the verification of the members of a group.
     * @param groupName The name of the checked group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#handleGroupMembersChecked(String)
     */
    public void handleGroupMembersChecked(final String groupName) {
        if (statsBundle.getCheckedMembersStats() != null) {
            synchronized (statsBundle.getCheckedMembersStats()) {
                statsBundle.getCheckedMembersStats().handleCheckedGroup(groupName);
            }
        }
    }

    /**
     * Handles the start of the members verification process.
     */
    public void handleStartOfMembersCheckingProcess() {
        if (statsBundle.getCheckedMembersStats() != null) {
            synchronized (statsBundle.getCheckedMembersStats()) { 
                statsBundle.getCheckedMembersStats().handleStartOfProcess();
            }
        }
    }

    /**
     * Handles the end of the  members verification process.
     */
    public void handleEndOfMembersCheckingProcess() {
        if (statsBundle.getCheckedMembersStats() != null) {
            synchronized (statsBundle.getCheckedMembersStats()) {
                statsBundle.getCheckedMembersStats().handleEndOfProcess();
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
        if (reportingParameters.getEnabled()) {
            if (reportingParameters.getPersistent()) {
                try {
                    final File file = resourcesProvider.getResource(SER_FILE_NAME).getFile();
                    if (file.exists()) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Loading serialized instance from: " + file.getAbsolutePath());
                        }
                        final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                        final StatisticsManager previousInstance = (StatisticsManager) ois.readObject();
                        ois.close();
                        if (previousInstance != null) {
                            statsBundle.getDefinitionModifications().initializeFrom(
                                    previousInstance.statsBundle.getDefinitionModifications());
                            statsBundle.getSyncReplNotifications().initializeFrom(
                                    previousInstance.statsBundle.getSyncReplNotifications());
                            statsBundle.getGroupsStats().initializeFrom(
                                    previousInstance.statsBundle.getGroupsStats());
                            statsBundle.getUndefGroupsStats().initializeFrom(
                                    previousInstance.statsBundle.getUndefGroupsStats());
                            statsBundle.getGroupsActivityStats().initializeFrom(
                                    previousInstance.statsBundle.getGroupsActivityStats());
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
            } else {
                LOGGER.debug("Reporting is not persistent : loading disabled.");
            }
        }
    }

    /**
     * Serializes the instance.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#save()
     */
    public void save() {
        if (reportingParameters.getEnabled()) {
            if (reportingParameters.getPersistent()) {
                try {
                    final File file = resourcesProvider.getResource(SER_FILE_NAME).getFile();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Serialization of the instance into the file: " + file.getAbsolutePath());
                    }
                    final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                    oos.writeObject(this);
                    oos.close();
                } catch (IOException ioe) {
                    LOGGER.error(ioe, ioe);
                }
            } else {
                LOGGER.debug("Reporting is not persistent : saving disabled.");
            }
        }
    }

    /**
     * Getter for domainService.
     * @return domainService.
     */
    public IDomainService getDomainService() {
        return domainService;
    }

    /**
     * Setter for domainService.
     * @param domainService the new value for domainService.
     */
    public void setDomainService(final IDomainService domainService) {
        this.domainService = domainService;
    }

    /**
     * Getter for resourcesProvider.
     * @return resourcesProvider.
     */
    public IResourcesProvider getResourcesProvider() {
        return resourcesProvider;
    }

    /**
     * Setter for resourcesProvider.
     * @param resourcesProvider the new value for resourcesProvider.
     */
    public void setResourcesProvider(final IResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }
}
