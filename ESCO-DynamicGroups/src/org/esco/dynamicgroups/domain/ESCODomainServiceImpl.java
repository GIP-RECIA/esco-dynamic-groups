/**
 * 
 */
package org.esco.dynamicgroups.domain;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.IEntryDTO;
import org.esco.dynamicgroups.dao.db.IDBDAOService;
import org.esco.dynamicgroups.dao.grouper.IGroupsDAOService;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.DynGroupOccurences;
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
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
    private String[] dynamicAttributes;

    /** The Grouper DAO Service to use. */
    private IGroupsDAOService groupsService;

    /** The Database DAO Service to use. */
    private IDBDAOService daoService;

    /** Listener for the repository. */
    private IRepositoryListener repositoryListener;
    
    /**
     * Builds an instance of ESCODomainServiceImpl.
     */
    public ESCODomainServiceImpl() {
        dynamicAttributes = ESCODynamicGroupsParameters.instance().getLdapSearchAttributesAsArray();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creates an instance of ESCODomainServiceImpl - Attributes: " 
                    + Arrays.toString(dynamicAttributes) + ".");
        }
    }

    /**
     * Checks the properties after the Spring injection.
     * @throws Exception If one of the injected property is null.
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(this.groupsService, 
                "The property groupService in the class " + this.getClass().getName() 
                + " can't be null.");

        Assert.notNull(this.daoService, 
                "The property daoService in the class " + this.getClass().getName() 
                + " can't be null.");

        Assert.notNull(this.repositoryListener, 
                "The property repositoryListener in the class " + this.getClass().getName() 
                + " can't be null.");
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
                
                repositoryListener.listen();
            }
        } else if (event instanceof ContextClosedEvent) {
            if (repositoryListener.isListening()) {
                repositoryListener.stop();
            } 
        }
    }

    /**
     * Adds a user to the dynamic groups.
     * @param entry The entry associated to the user.
     * @see org.esco.dynamicgroups.domain.IDomainService#addToDynamicGroups(org.esco.dynamicgroups.IEntryDTO)
     */
    public void addToDynamicGroups(final IEntryDTO entry) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Request to create the dynamic groups memberships for the user: " + entry.getId());
        }

        final Map<String, DynGroup> retainedCandidatGroups = computeDynGroups(entry);

        // Updates the memberships effectively.
        groupsService.createMemeberShips(entry.getId(), retainedCandidatGroups);
    }

    /**
     * Updates the dynamic groups for a given user entry.
     * @param entry The user entry used to compute the new groups.
     * @see org.esco.dynamicgroups.domain.IDomainService#updateDynamicGroups(IEntryDTO)
     */
    public void updateDynamicGroups(final IEntryDTO entry) {
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
    public void removeDeletedUserFromGroups(final IEntryDTO entry) {
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
            if (attributeValues == null) {
                attributeValues = UNDEF_VALUE;
            }

            final Set<DynGroup> candidatGroupsForAtt = 
                daoService.getGroupsForAttributeValues(attributeName, attributeValues);

            for (DynGroup candidatGroupForAtt : candidatGroupsForAtt) {

                if (!candidatGroups.containsKey(candidatGroupForAtt.getGroupName())) {
                    candidatGroups.put(candidatGroupForAtt.getGroupName(), 
                            new DynGroupOccurences(candidatGroupForAtt));
                }
                candidatGroups.get(candidatGroupForAtt.getGroupName()).incrementOccurences();
            }
        }


        // The retained groups are those which the number of occurences is equal to
        // the number of attributes used in their definition (the group definition are in a conjonctive form).
        //final Subject subject = SubjectFinder.findById(entry.getId());
        final Map<String, DynGroup> retainedCandidatGroups = new HashMap<String, DynGroup>(); 
        final Set<DynGroup> conjunctiveCompInds = new HashSet<DynGroup>();

        for (String candidatGroup : candidatGroups.keySet()) {
            final DynGroupOccurences dynGroupOcc = candidatGroups.get(candidatGroup);
            if (dynGroupOcc.getOcurrences() >= dynGroupOcc.getGroup().getAttributesNb()) {

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
                retainedCandidatGroups.put(resolvedGroup.getGroupName(), resolvedGroup);
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
    public void handleNewOrModifiedDynamicGroup(final DynamicGroupDefinition definition) {
        daoService.storeOrModifyDynGroup(definition);
        groupsService.resetGroupMembers(definition);

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
}
