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
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.grouper.IGroupsDAOService;
import org.esco.dynamicgroups.dao.ldap.syncrepl.ldapsync.protocol.SyncStateControl;
import org.esco.dynamicgroups.domain.IDomainService;
import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ReportingParametersSection;
import org.esco.dynamicgroups.domain.reporting.IReportFormatter;
import org.esco.dynamicgroups.util.IResourceProvider;
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
    private IGroupCreatedOrDeletedStatsEntry groupsStats;

    /** Statistics for the undefined groups. */
    private IUndefinedGroupStatsEntry undefGroupsStats;

    /** Statistics for the groups activity. */
    private IGroupsActivityStatsEntry groupsActivityStats;

    /** Statistics for the check of invalid or missing members. */
    private ICheckedMembersStatsEntry checkedMembersStats;

    /** The report formatter to use. */
    private transient IReportFormatter reportFormatter;

    /** The domain service used to check the members of the
     * dynamic groups. */
    private transient IDomainService domainService;

    /** The group service. */
    private transient IGroupsDAOService groupsService;

    /**Util class to serialize and load the instance.*/
    private transient IResourceProvider resourceProvider;

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

        Assert.notNull(this.resourceProvider, 
                "The property resourceProvider in the class " + this.getClass().getName() 
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
                undefGroupsStats = new UndefinedGroupStatsEntry(groupsService, i18n);
            }

            if (reportingParameters.getCountDefinitionModifications()) {
                definitionModifications = new DefinitionModificationsStatsEntry(i18n);
            }

            if (reportingParameters.getCountSyncReplNotifications()) {
                syncReplNotifications = new SyncReplNotificationsStatsEntry(i18n);
            }

            if (reportingParameters.getCountGroupCreationDeletion()) {
                groupsStats = new GroupsCreatedOrDeletedStatsEntry(i18n);
            }

            if (reportingParameters.getCountGroupsActivity()) {
                groupsActivityStats = new GroupsActivityStatsEntry(i18n);
            }

            if (reportingParameters.getCountInvalidOrMissingMembers()) {
                checkedMembersStats = new CheckedMembersStatsEntry(i18n);
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

        if (checkedMembersStats != null) {
            synchronized (checkedMembersStats) {

                if (checkedMembersStats.checkPerformed()) {

                    LOGGER.info(checkedMembersStats.getLabel() + checkedMembersStats.getEntry());

                    report += reportFormatter.formatEntry(checkedMembersStats.getLabel(), 
                            checkedMembersStats.getEntry());
                    report += reportFormatter.getNewLine();
                    final Set<String> invalidGroups = checkedMembersStats.getInvalidGroups();
                    final int paddSimple = 3;
                    final int paddDouble = paddSimple * 2;


                    for (String invalidGroup : invalidGroups) {
                        report += reportFormatter.padd(invalidGroup, paddSimple);
                        report += reportFormatter.getNewLine();

                        MissingOrInvalidMembersEntry invalidMbInfos = 
                            checkedMembersStats.getCheckingResult(invalidGroup);

                        if (invalidMbInfos.hasInvalidMembers()) {
                            report += reportFormatter.padd(checkedMembersStats.getInvalidMembersLabel() 
                                    + invalidMbInfos.getInvalidMembers(), paddDouble);
                        }

                        if (invalidMbInfos.hasMissingMembers()) {
                            report += reportFormatter.padd(checkedMembersStats.getMissingMembersLabel() 
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
    @Override
    public String generateReport() {
        String report = "";
        boolean separateGroupsActivity = false;

        // --- SyncRepl Notifications section.
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


        // -- Groups stats section.    
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

        // --- Modifications of definition section.
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

        // --- Dynamic groups without a members definitions.
        if (undefGroupsStats != null) {
            separateGroupsActivity = true;
            synchronized (undefGroupsStats) {
                LOGGER.info(undefGroupsStats.getLabel() + undefGroupsStats.getEntry());
                LOGGER.info(undefGroupsStats.getUndefinedGroupNames());
                report += reportFormatter.formatEntry(undefGroupsStats.getLabel(), 
                        undefGroupsStats.getEntry());

                if (undefGroupsStats.hasUndefinedGroup()) {
                    report += reportFormatter.getNewLine();
                    report += reportFormatter.format(undefGroupsStats.getUndefGroupNamesLabel());
                    report += reportFormatter.formatList(undefGroupsStats.getUndefinedGroupNames());
                }
            }
            report += reportFormatter.getNewLine();
            report += reportFormatter.getNewLine();
        }

        boolean separateMembersCheck = separateGroupsActivity;

        // --- Activity of dynamic groups section.
        if (groupsActivityStats != null) {
            separateMembersCheck = true;
            if (separateGroupsActivity) {
                report += reportFormatter.getNewLine();
                report += reportFormatter.getSeparation();
                report += reportFormatter.getNewLine();
            }

            synchronized (groupsActivityStats) {
                LOGGER.info(groupsActivityStats.getLabel() + groupsActivityStats.getEntry());

                report += reportFormatter.formatEntry(groupsActivityStats.getLabel(), groupsActivityStats.getEntry());

                if (groupsActivityStats.hasActiveGroup()) {
                    report += reportFormatter.getNewLine();
                    report += reportFormatter.format(groupsActivityStats.getActiveGroupsLabel());
                    report += reportFormatter.formatList(groupsActivityStats.getActiveGroups());
                }
            }
            report += reportFormatter.getNewLine();
        }


        // --- Memebers check section.
        if (checkedMembersStats != null) {

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

        if (checkedMembersStats != null) {
            synchronized (checkedMembersStats) {
                checkedMembersStats.reset();
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
     * Handles a group with an invalid member.
     * @param groupName The name of the group.
     * @param userId The id of the invalid memeber.
     */
    public void handleInvalidMember(final String groupName, final String userId) {
        if (checkedMembersStats != null) {
            synchronized (checkedMembersStats) {
                checkedMembersStats.handleInvalidMember(groupName, userId);
            }
        }
    }

    /** 
     * Handles a group with a missing member.
     * @param groupName The name of the group.
     * @param userId The id of the missing member.
     */
    public void handleMissingMember(final String groupName, final String userId) {
        if (checkedMembersStats != null) {
            synchronized (checkedMembersStats) {
                checkedMembersStats.handleMissingMember(groupName, userId);
            }
        }
    }

    /**
     * Handles the notification of the verification of the members of a group.
     * @param groupName The name of the checked group.
     * @see org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager#handleGroupMembersChecked(String)
     */
    public void handleGroupMembersChecked(final String groupName) {
        if (checkedMembersStats != null) {
            synchronized (checkedMembersStats) {
                checkedMembersStats.handleCheckedGroup(groupName);
            }
        }
    }

    /**
     * Handles the start of the members verification process.
     */
    public void handleStartOfMembersCheckingProcess() {
        if (checkedMembersStats != null) {
            synchronized (checkedMembersStats) { 
                checkedMembersStats.handleStartOfProcess();
            }
        }
    }

    /**
     * Handles the end of the  members verification process.
     */
    public void handleEndOfMembersCheckingProcess() {
        if (checkedMembersStats != null) {
            synchronized (checkedMembersStats) {
                checkedMembersStats.handleEndOfProcess();
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
                    final File file = resourceProvider.getResource(SER_FILE_NAME).getFile();
                    if (file.exists()) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Loading serialized instance from: " + file.getAbsolutePath());
                        }
                        final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                        final StatisticsManager previousInstance = (StatisticsManager) ois.readObject();
                        if (previousInstance != null) {
                            this.definitionModifications.initializeFrom(previousInstance.definitionModifications);
                            this.syncReplNotifications.initializeFrom(previousInstance.syncReplNotifications);
                            this.groupsStats.initializeFrom(previousInstance.groupsStats);
                            this.undefGroupsStats.initializeFrom(previousInstance.undefGroupsStats);
                            this.groupsActivityStats.initializeFrom(previousInstance.groupsActivityStats);
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
                    final File file = resourceProvider.getResource(SER_FILE_NAME).getFile();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Serialization of the instance into the file: " + file.getAbsolutePath());
                    }
                    final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                    oos.writeObject(this);
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
     * Getter for resourceProvider.
     * @return resourceProvider.
     */
    public IResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    /**
     * Setter for resourceProvider.
     * @param resourceProvider the new value for resourceProvider.
     */
    public void setResourceProvider(final IResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }
}
