/**
 * 
 */
package org.esco.dynamicgroups.domain;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.IEntryDTO;
import org.esco.dynamicgroups.dao.db.IDBDAOService;
import org.esco.dynamicgroups.dao.grouper.IGroupsDAOService;
import org.esco.dynamicgroups.dao.ldap.IMembersFromDefinitionDAO;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.DynGroupOccurences;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.IDynamicAttributesProvider;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ReportingParametersSection;
import org.esco.dynamicgroups.domain.reporting.IReportingManager;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;


/**
 * Implementation of the domain service for the ESCO context.
 * @author GIP RECIA - A. Deman
 * 13 janv. 2009
 *
 */
public class ESCODomainServiceImpl 
implements IDomainService, ApplicationListener, 
InitializingBean {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCODomainServiceImpl.class);

    /** Value of an undefined attribute. */
    private static final String[] UNDEF_VALUE = {"___UNDEF_VALUE_DYNGRP___"}; 

    /** The dynamic attributes. */
    private Set<String> dynamicAttributes;

    /** The users parameters provider. */
    private ParametersProvider parametersProvider;

    /** The Grouper DAO Service to use. */
    private IGroupsDAOService groupsService;

    /** The Database DAO Service to use. */
    private IDBDAOService daoService;

    /** Service used to retrives the members from the logic defintion 
     * of the group. */
    private IMembersFromDefinitionDAO membersFromDefinitionService;

    /** Listener for the repository. */
    private IRepositoryListener repositoryListener;

    /** The statistics manager to use. */
    private IStatisticsManager statisticsManager;

    /** Reporting manager. */
    private IReportingManager reportingManager; 

    /** Flag to determine if the memebrs of the dynamic groups should be checked on startup. */
    private boolean checkMembersOnStartup;

    /** Flag to determine if the verification of the dynamic group memebrs
     * should be reported. */
    private boolean reportCheckMemebersOnStartup;

    /** Stop the checking process for the members of dynamic groups. */
    private AtomicBoolean stopCheckingProcess  = new AtomicBoolean();

    /**
     * Builds an instance of ESCODomainServiceImpl.
     */
    public ESCODomainServiceImpl() {
        super();
    }

    /**
     * Checks the properties after the Spring injection.
     * @throws Exception If one of the injected property is null.
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

        final String cantBeNull = " can't be null.";

        Assert.notNull(this.groupsService, 
                "The property groupService in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.daoService, 
                "The property daoService in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.repositoryListener, 
                "The property repositoryListener in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.membersFromDefinitionService, 
                "The property membersFromDefinitionService in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.statisticsManager, 
                "The property statisticsManager in the class " + this.getClass().getName() 
                + cantBeNull);

        Assert.notNull(this.reportingManager, 
                "The property reportingManager in the class " + this.getClass().getName() 
                + cantBeNull);

        IDynamicAttributesProvider dynAttProvider = 
            (IDynamicAttributesProvider) parametersProvider.getPersonsParametersSection();
        dynamicAttributes = dynAttProvider.getDynamicAttributes();

        final GroupsParametersSection groupsParameters = 
            (GroupsParametersSection) parametersProvider.getGroupsParametersSection();

        checkMembersOnStartup = groupsParameters.getCheckMembersOnStartup();

        final ReportingParametersSection reportingParameters =
            (ReportingParametersSection) parametersProvider.getReportingParametersSection();

        reportCheckMemebersOnStartup = reportingParameters.getCountInvalidOrMissingMembers();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Instance of ESCODomainServiceImpl initialized - Attributes: " 
                    + dynamicAttributes + ".");
        }
    }


    /**
     * Listen for an application event.
     * @param event The event.
     * @see org.springframework.context.ApplicationListener#
     * onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    public void onApplicationEvent(final ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            if (!repositoryListener.isListening()) {
                statisticsManager.load();

                if (checkMembersOnStartup) {
                    LOGGER.info("Starting to check the members of dynamic groups.");

                    startMembersCheckingProcess();
                    LOGGER.info("Members of dynamic groups checked.");
                    if (reportCheckMemebersOnStartup) {
                        reportingManager.doReportingForGroupsMembersCheck();
                    }
                }

                repositoryListener.listen();
                LOGGER.info("-----------------------------------------------");
                LOGGER.info("- Dynamic groups system for grouper started.");
                LOGGER.info("-----------------------------------------------");

            }
        } else if (event instanceof ContextClosedEvent) {
            if (repositoryListener.isListening()) {
                repositoryListener.stop();
                statisticsManager.save();
                LOGGER.info("-----------------------------------------------");
                LOGGER.info("- Dynamic groups system for grouper stopped.");
                LOGGER.info("-----------------------------------------------");
            } 
        }
    }

    /**
     * Adds a user to the dynamic groups.
     * @param entry The entry associated to the user.
     * @see org.esco.dynamicgroups.domain.IDomainService#addToDynamicGroups(org.esco.dynamicgroups.IEntryDTO)
     */
    public synchronized void addToDynamicGroups(final IEntryDTO entry) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Request to create the dynamic groups memberships for the user: " + entry.getId());
        }

        final Map<String, DynGroup> retainedCandidatGroups = computeDynGroups(entry);

        // Updates the memberships effectively.
        groupsService.createMemberships(entry.getId(), retainedCandidatGroups);
    }

    /**
     * Updates the dynamic groups for a given user entry.
     * @param entry The user entry used to compute the new groups.
     * @see org.esco.dynamicgroups.domain.IDomainService#updateDynamicGroups(IEntryDTO)
     */
    public synchronized void updateDynamicGroups(final IEntryDTO entry) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Request to Update the dynamic groups for the user: " + entry.getId());
        }

        final Map<String, DynGroup> retainedCandidatGroups = computeDynGroups(entry);

        // Updates the memberships effectively.
        groupsService.updateMemberships(entry.getId(), retainedCandidatGroups);
    }

    /**
     * Removes a deleted user user from its groups. 
     * The whole groups or only the dynmic groups may be considered, depending
     * on the configuration.
     * @param entry The entry associated to the user.
     * @see org.esco.dynamicgroups.domain.IDomainService#removeDeletedUserFromGroups(org.esco.dynamicgroups.IEntryDTO)
     */
    public synchronized void removeDeletedUserFromGroups(final IEntryDTO entry) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Request to remove from groups the deleted user: " + entry.getId());
        }
        getGroupsService().removeFromGroups(entry.getId());
    }

    /**
     * Computes the dynamic groups for a given user.
     * @param entry The entry associated to the user.
     * @return The map groupName=>DynGroup for the dynamic groups of the user.
     */
    private Map<String, DynGroup> computeDynGroups(final IEntryDTO entry) {

        final Map<String, DynGroupOccurences> candidatGroups = new HashMap<String, DynGroupOccurences>();

        // For each value of attribute, computes the candidat groups and their number of occurences.
        // If the attribute is a and the value is v, a candidat group is a group 
        // wich uses a=v in its definition.
        for (String attributeName : dynamicAttributes) {
            String[] attributeValues = entry.getAttributeValues(attributeName);
            if (attributeValues != null) {


                final Set<DynGroup> candidatGroupsForAtt = 
                    daoService.getGroupsForAttributeValues(attributeName, attributeValues);

                for (DynGroup candidatGroupForAtt : candidatGroupsForAtt) {

                    if (!candidatGroups.containsKey(candidatGroupForAtt.getGroupUUID())) {
                        candidatGroups.put(candidatGroupForAtt.getGroupUUID(), 
                                new DynGroupOccurences(candidatGroupForAtt));
                    }
                    candidatGroups.get(candidatGroupForAtt.getGroupUUID()).incrementOccurences();
                }
            }
        }


        // The retained groups are those which the number of occurences is equal to
        // the number of attributes used in their definition (the group definition are in a conjonctive form).
        final Map<String, DynGroup> retainedCandidatGroups = new HashMap<String, DynGroup>(); 
        final Set<DynGroup> conjunctiveCompInds = new HashSet<DynGroup>();

        for (String candidatGroup : candidatGroups.keySet()) {
            final DynGroupOccurences dynGroupOcc = candidatGroups.get(candidatGroup);
            if (dynGroupOcc.getOccurrences() >= dynGroupOcc.getGroup().getAttributesNb()) {

                if (dynGroupOcc.getGroup().isConjunctiveComponentIndirection()) {
                    // The group is only a conjunctive component of another group
                    // so it will have to be resolved before adding it.
                    conjunctiveCompInds.add(dynGroupOcc.getGroup());
                } else {
                    // The group is not an indirection so it is retained.
                    retainedCandidatGroups.put(candidatGroup, dynGroupOcc.getGroup());
                }
            }
        }

        // Resolves the indirections.
        // The groups which are a conjunctive component of another
        // one will be replaced by the original group.
        if (conjunctiveCompInds.size() > 0) {
            final Set<DynGroup> resolvedGroups = daoService.resolvConjunctiveComponentIndirections(conjunctiveCompInds);
            for (DynGroup resolvedGroup : resolvedGroups) {
                retainedCandidatGroups.put(resolvedGroup.getGroupUUID(), resolvedGroup);
            }
        }


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Number of candidat groups: " + candidatGroups.size());
            LOGGER.debug("Number of retained candidat groups: " + retainedCandidatGroups.size());
            LOGGER.debug("All the candidat Groups:" + candidatGroups);
            LOGGER.debug("Retained candidat Groups:" + retainedCandidatGroups);
        }

        return retainedCandidatGroups;
    }

    /**
     * Initializes the group : removes all the existing members and retrieves the 
     * new members from the group definition.
     * @param definition The dfinition associated to the group to initialize.
     */
    private void initialize(final DynamicGroupDefinition definition) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialization of the group with the definition: " + definition);
        }

        final Set<String> userIds = membersFromDefinitionService.getMembers(definition);
        groupsService.resetGroupMembers(definition);
        // Adds the members to the list.
        if (userIds.size() > 0) {
            groupsService.addToGroup(definition.getGroupUUID(), userIds);
        }

    }

    /**
     * Checks all the groups.
     * @see org.esco.dynamicgroups.domain.IDomainService#startMembersCheckingProcess()
     */
    public synchronized  void startMembersCheckingProcess() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Starting the checking of the dynamic groups.");
        }
        stopCheckingProcess.set(false);



        // Gets the list of the dynamic groups definitions.
        // This list is shuffled for the case that only a part of the list is control.
        final List<DynamicGroupDefinition> definitions = new ArrayList<DynamicGroupDefinition>();
        definitions.addAll(groupsService.getAllDynamicGroupDefinitions());
        Collections.shuffle(definitions);

        final Iterator<DynamicGroupDefinition> definitionsIter = definitions.iterator();

        // The definitions are checked until the end of the list or the processus is stopped.
        while (definitionsIter.hasNext() && !stopCheckingProcess.get()) {
            final DynamicGroupDefinition definition = definitionsIter.next();
            final Set<String> expectedMembers = membersFromDefinitionService.getMembers(definition);
            groupsService.checkGroupMembers(definition, expectedMembers);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Dynamic groups checking finished.");
        }
    }

    /**
     * Stops the checking of the members ah the dynamic groups.
     */
    public void stopMembersCheckingProcess() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Stoping the checking of the dynamic groups.");
        }
        stopCheckingProcess.set(true);
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
     * Getter for daoService.
     * @return daoService.
     */
    public IDBDAOService getDaoService() {
        return daoService;
    }

    /**
     * Setter for daoService.
     * @param daoService the new value for daoService.
     */
    public void setDaoService(final IDBDAOService daoService) {
        this.daoService = daoService;
    }


    /**
     * Handles a new or modified group.
     * If the deinition is not valid the entry in the DB is deleted if exist else
     * the entry is creted or modified.
     * @param definition The dynamic group definition.
     * @see org.esco.dynamicgroups.domain.IDomainService#handleNewOrModifiedDynamicGroup(DynamicGroupDefinition)
     */
    public synchronized  void handleNewOrModifiedDynamicGroup(final DynamicGroupDefinition definition) {
        daoService.storeOrModifyDynGroup(definition);
        initialize(definition);
    }

    /**
     * Gives the members definition for a dynamic group.
     * @param groupUUID The uuid of the dynamic group.
     * @return The definition if it exists, null oterwise.
     * @see org.esco.dynamicgroups.domain.IDomainService#getMembershipExpression(java.lang.String)
     */
    public synchronized  String getMembershipExpression(final String groupUUID) {
        final DynGroup group = daoService.getDynGroupByUUID(groupUUID);
        if (group == null) {
            return null;
        }
        return group.getGroupDefinition();
    }

    /**
     * Getter for repositoryListener.
     * @return repositoryListener.
     */
    public IRepositoryListener getRepositoryListener() {
        return repositoryListener;
    }

    /**
     * Setter for repositoryListener.
     * @param repositoryListener the new value for repositoryListener.
     */
    public void setRepositoryListener(final IRepositoryListener repositoryListener) {
        this.repositoryListener = repositoryListener;
    }

    /**
     * Deletes a group.
     * @param groupName The name of the group to delete.
     * @see org.esco.dynamicgroups.domain.IDomainService#handleDeletedGroup(java.lang.String)
     */
    public void handleDeletedGroup(final String groupName) {
        this.daoService.deleteDynGroup(groupName);
    }

    /**
     * Getter for membersFromDefinitionService.
     * @return membersFromDefinitionService.
     */
    public IMembersFromDefinitionDAO getMembersFromDefinitionService() {
        return membersFromDefinitionService;
    }

    /**
     * Setter for membersFromDefinitionService.
     * @param membersFromDefinitionService the new value for membersFromDefinitionService.
     */
    public void setMembersFromDefinitionService(final IMembersFromDefinitionDAO membersFromDefinitionService) {
        this.membersFromDefinitionService = membersFromDefinitionService;
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
     * Getter for statisticsManager.
     * @return statisticsManager.
     */
    public IStatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    /**
     * Setter for statisticsManager.
     * @param statisticsManager the new value for statisticsManager.
     */
    public void setStatisticsManager(final IStatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    /**
     * Getter for reportingManager.
     * @return reportingManager.
     */
    public IReportingManager getReportingManager() {
        return reportingManager;
    }

    /**
     * Setter for reportingManager.
     * @param reportingManager the new value for reportingManager.
     */
    public void setReportingManager(final IReportingManager reportingManager) {
        this.reportingManager = reportingManager;
    }
}
