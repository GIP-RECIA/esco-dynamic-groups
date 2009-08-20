/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;


import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;





/**
 * DAO Service for Grouper.
 * @author GIP RECIA - A. Deman
 * 15 janv. 2009
 *
 */
public class GrouperDAOServiceImpl extends BaseGrouperDAO 
implements IGroupsDAOService, InitializingBean, ApplicationListener, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 2185838538636654415L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GrouperDAOServiceImpl.class);

    
    /** The DAO to add to groups. */
    private AddToGroupDAO addToGroupDAO;
    
    /** The DAO used to check the dynamic type. */
    private CheckDynamicTypeDAO checkDynamicTypeDAO;
    
    /** DAO used to check the members of dynamic groups. */
    private CheckGroupMembersDAO checkGroupMembersDAO;
    
    /** DAO used to retrieve the dynamic group definitions. */
    private GetAllDynamicGroupDefintionsDAO getAllDynamicGroupDefDAO;
    
    /** DAO to retrieve the undefined groups. */
    private GetUndefinedGroupsDAO getUndefinedGroupsDAO;
    
    /** DAO to remove a subject from its groups. */
    private RemoveFromGroupsDAO removeFromGroupsDAO;
    
    /** DAO to initialize the members of a group. */
    private ResetGroupMembersDAO resetGroupMembersDAO;
    
    /** DAO to update the memberships. */
    private UpdateMembershipsDAO updateMembershipsDAO;
    
    /** DAO used to create membherships. */
    private CreateMembershipsDAO createMembershipsDAO;

    /** The user parameters provider. */
    private ParametersProvider parametersProvider;

    /** The grouper parameters. */
    private GroupsParametersSection grouperParameters;

    /** Initialization flag. */
    private boolean initialized;

    /** The statistics manager service. */
    private IStatisticsManager statisticsManager;

    /**
     * Builds an instance of GrouperDAOServiceImpl.
     */
    public GrouperDAOServiceImpl() {
        super();
    }

    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(this.statisticsManager, 
                "The property statisticsManager in the class " + this.getClass().getName() 
                + " can't be null.");
        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " + this.getClass().getName() 
                + " can't be null.");

        grouperParameters = (GroupsParametersSection) parametersProvider.getGroupsParametersSection();
        
        ESCODeletedSubjectImpl.setSourceId(grouperParameters.getGrouperSubjectsSourceId());

        // Builds the internal dao that use Grouper session callbacks.
        addToGroupDAO = new AddToGroupDAO();
        checkDynamicTypeDAO = new CheckDynamicTypeDAO(grouperParameters);
        checkGroupMembersDAO = new CheckGroupMembersDAO(statisticsManager);
        getAllDynamicGroupDefDAO = new GetAllDynamicGroupDefintionsDAO(grouperParameters);
        getUndefinedGroupsDAO = new GetUndefinedGroupsDAO(grouperParameters);
        removeFromGroupsDAO = new RemoveFromGroupsDAO(grouperParameters, statisticsManager);
        resetGroupMembersDAO = new ResetGroupMembersDAO();
        updateMembershipsDAO = new UpdateMembershipsDAO(grouperParameters, statisticsManager);
        createMembershipsDAO = new CreateMembershipsDAO(statisticsManager);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Instance of GrouperDAOServiceImpl initialized - Grouper user: "  
                    + grouperParameters.getGrouperUser() 
                    + " - Grouper dynamic type " + grouperParameters.getGrouperType() 
                    + " - deleted users are removed from all groups: " 
                    + grouperParameters.getRemoveFromAllGroups() + ".");
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
            if (!initialized) {
                checkDynamicType();
                initialized = true;
            }
        }
    }

    /**
     * Adds a user to a group.
     * @param groupUUID The uuid of the group.
     * @param userIds The ids of the users to add to the group.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#addToGroup(String, Set)
     */
    public void addToGroup(final String groupUUID, final Set<String> userIds) {
        addToGroupDAO.addToGroup(groupUUID, userIds);
    }

    /** 
     * Creates a group if it does not exist, removes all its members otherwise .
     * @param definition The definition of the group.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#resetGroupMembers(DynamicGroupDefinition)
     */
    public void resetGroupMembers(final DynamicGroupDefinition definition) {
       resetGroupMembersDAO.resetGroupMembers(definition);
    }

    /**
     * Gives the set of definitions for all the dynamic groups in grouper.
     * @return The set of definitions.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#getAllDynamicGroupDefinitions()
     */
    public Set<DynamicGroupDefinition> getAllDynamicGroupDefinitions() {
       return getAllDynamicGroupDefDAO.getAllDynamicGroupDefinitions();
    }
    /** 
     * Checks the members of a group.
     * @param groupDefinition The definition of the group to check.
     * @param expectedMembers The expected members of the group according to 
     * its logic definition.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#checkGroupMembers(DynamicGroupDefinition, Set)
     */
    public void checkGroupMembers(final DynamicGroupDefinition groupDefinition, 
            final Set<String> expectedMembers) {
        checkGroupMembersDAO.checkGroupMembers(groupDefinition, expectedMembers);
    }

    /**
     * Checks the dynamic types.
     * This method probably doesn't need to use the sessions callbacks.
     */
    public void checkDynamicType() {
        checkDynamicTypeDAO.checkDynamicType();
    }
    

    

    /**
     * Removes a user from its groups.
     * All the groups or only the dynamic ones may be considered, depending on the configuration.
     * @param userId The id of the user.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#removeFromGroups(java.lang.String)
     */
    public void removeFromGroups(final String userId) {
        removeFromGroupsDAO.removeFromGroups(userId);
    }
    
    

    /**
     * Updates the memberships of an user.
     * @param userId The id of the user.
     * @param newGroups The new groups.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#updateMemberships(java.lang.String, java.util.Map)
     */
    public void updateMemberships(final String userId, final Map<String, DynGroup> newGroups) {
        updateMembershipsDAO.updateMemberships(userId, newGroups);
    }
    /**
     * Gives the list of groups with no membership defintions.
     * @return The group names.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#getUndefinedDynamicGroups()
     */
    public Set<String> getUndefinedDynamicGroups() {
        return getUndefinedGroupsDAO.getUndefinedDynamicGroups();
    }

    /**
     * Creates the memberships for a given user.
     * @param userId The id of the user.
     * @param groups The groups to which the user should become a member.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#createMemberships(String, Map)
     */
    public void createMemberships(final String userId, final Map<String, DynGroup> groups) {
       createMembershipsDAO.createMemberships(userId, groups);
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
}
