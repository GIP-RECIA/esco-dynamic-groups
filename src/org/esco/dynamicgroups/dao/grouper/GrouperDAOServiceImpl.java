/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.grouper.Field;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GroupTypeFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.MemberFinder;
import edu.internet2.middleware.grouper.Membership;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.AttributeNotFoundException;
import edu.internet2.middleware.grouper.exception.GroupNotFoundException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.exception.MemberAddException;
import edu.internet2.middleware.grouper.exception.MemberDeleteException;
import edu.internet2.middleware.grouper.exception.MemberNotFoundException;
import edu.internet2.middleware.grouper.exception.SchemaException;
import edu.internet2.middleware.grouper.privs.Privilege;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectNotUniqueException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;
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
public class GrouperDAOServiceImpl implements IGroupsDAOService, InitializingBean, ApplicationListener, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 2185838538636654415L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GrouperDAOServiceImpl.class);

    /** The Grouper session util instance. */
    private GrouperSessionUtil sessionUtil;

    //    /** The dynamic type in grouper. */
    //    private String grouperDynamicType;
    //    
    /** The user parameters provider. */
    private ParametersProvider parametersProvider;

    /** The grouper parameters. */
    private GroupsParametersSection grouperParameters;

    //    /** Flag used to determine if a deleted user should be removed from all groups 
    //     * or only from the dynamic ones.
    //     */
    //    private boolean removeFromAllGroups;

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
        sessionUtil = new GrouperSessionUtil(grouperParameters.getGrouperUser());
        ESCODeletedSubjectImpl.setSourceId(grouperParameters.getGrouperSubjectsSourceId());

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

        final GrouperSession session  = sessionUtil.createSession();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding users: " + userIds + " to the group: " + groupUUID + ".");
        }

        final Group group = retrieveGroup(session, groupUUID);

        if (group == null) {
            LOGGER.error("Error the group " + groupUUID + " can't be found.");

        } else {
            try {
                for (String userId : userIds) {
                    final Subject subject = SubjectFinder.findById(userId);
                    if (!group.hasImmediateMember(subject)) {
                        group.addMember(subject);

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("The user: " + userId + " has been added to the group: " 
                                    + groupUUID  + ".");
                        }
                    } else {
                        LOGGER.warn("The user: " + userId 
                                + " is already an immediate member of the group: " + groupUUID + ".");
                    }
                }
                sessionUtil.stopSession(session);


            } catch (SubjectNotFoundException e) {
                sessionUtil.stopSession(session);
                LOGGER.error(e, e);
                throw new DynamicGroupsException(e);
            } catch (SubjectNotUniqueException e) {
                sessionUtil.stopSession(session);
                LOGGER.error(e, e);
                throw new DynamicGroupsException(e);
            } catch (InsufficientPrivilegeException e) {
                sessionUtil.stopSession(session);
                LOGGER.error(e, e);
                throw new DynamicGroupsException(e);
            } catch (MemberAddException e) {
                sessionUtil.stopSession(session);
                LOGGER.error(e, e);
                throw new DynamicGroupsException(e);
            }  
        }

    }

    /** 
     * Creates a group if it does not exist, removes all its members otherwise .
     * @param definition The definition of the group.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#resetGroupMembers(DynamicGroupDefinition)
     */
    public void resetGroupMembers(final DynamicGroupDefinition definition) {
        final GrouperSession session  = sessionUtil.createSession();

        final Group group = retrieveGroup(session, definition.getGroupUUID());

        if (group == null) {
            LOGGER.error("Error the group " + definition.getGroupUUID() + " can't be found.");

        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Resetting group: " + definition.getGroupUUID());
            }
            @SuppressWarnings("unchecked") 
            final Set<Member> members = group.getImmediateMembers();
            for (Member member : members) {
                try {
                    group.deleteMember(new ESCODeletedSubjectImpl(member.getSubjectId()));
                } catch (InsufficientPrivilegeException e) {
                    sessionUtil.stopSession(session);
                    LOGGER.error(e, e);
                    throw new DynamicGroupsException(e);
                } catch (MemberDeleteException e) {
                    sessionUtil.stopSession(session);
                    LOGGER.error(e, e);
                    throw new DynamicGroupsException(e);
                } 
            }


        }
        sessionUtil.stopSession(session);
    }


    /**
     * Try to retrieve a Grouper Type.
     * @param typeName The name of the type to retrieve.
     * @return The Grouper Type if found, null otherwise.
     */
    private GroupType retrieveType(final String typeName) {
        GroupType type;
        try {
            type = GroupTypeFinder.find(typeName);
        } catch (SchemaException e) {
            type = null;
        }

        return type;
    }

    /**
     * Gives the set of definitions for all the dynamic groups in grouper.
     * @return The set of definitions.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#getAllDynamicGroupDefinitions()
     */
    @Override
    public Set<DynamicGroupDefinition> getAllDynamicGroupDefinitions() {
        final GrouperSession session = sessionUtil.createSession();

        final String grouperDynamicType = grouperParameters.getGrouperType();
        final String definitionField = grouperParameters.getGrouperDefinitionField();
        final Set<DynamicGroupDefinition> definitions = new HashSet<DynamicGroupDefinition>();
        final GroupType type = retrieveType(grouperDynamicType);
        if (type != null) {
            final Set<Group> groups = GroupFinder.findAllByType(session, type);
            for (Group group : groups) {
                String membersDef = null;
                try {
                    membersDef = group.getAttribute(definitionField);
                    definitions.add(new DynamicGroupDefinition(group.getUuid(), membersDef));
                } catch (AttributeNotFoundException e) {
                    LOGGER.error("Unable to retrieve the attribute "  
                            + definitionField + " for the group: " + group.getName() + ".");
                }
            }
        } else {
            LOGGER.error("Error unable to retrieve the dynamic type: " + grouperDynamicType);
        }
        sessionUtil.stopSession(session);
        return definitions;
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


        final GrouperSession session = sessionUtil.createSession();

        final Group group = retrieveGroup(session, groupDefinition.getGroupUUID());
        
        if (group != null) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Checking group: " + group.getName());
            }


            // Builds the actual members of the group in order to compare
            // with the expected one.
            final Set<String> actualMembers = new HashSet<String>(); 

            @SuppressWarnings("unchecked") 
            final Set members = group.getImmediateMembers();
            for (Object o : members) {
                try {
                    final Subject subj = ((Member) o).getSubject();
                    actualMembers.add(subj.getId());
                   
                } catch (SubjectNotFoundException e) {
                    LOGGER.error(e, e);
                }
            }

            // Checks that all the expected members are actually members of the group.
            for (String expectedMember : expectedMembers) {
                if (!actualMembers.contains(expectedMember)) {
                    LOGGER.warn("Checking group: " 
                            + group.getName() + " member " + expectedMember + " not found (added).");
                    try {
                        final Subject subj = SubjectFinder.findById(expectedMember);
                        group.addMember(subj);
                    } catch (SubjectNotFoundException e) {
                        LOGGER.error(e, e);
                    } catch (SubjectNotUniqueException e) {
                        LOGGER.error(e, e);
                    } catch (InsufficientPrivilegeException e) {
                        LOGGER.error(e, e);
                    } catch (MemberAddException e) {
                        LOGGER.error(e, e);
                    }
                }
                actualMembers.remove(expectedMember);
            }

            // All the remaining uid in the actual list are invalid, so the corresponding subjects
            // are removed from the group.
            for (String invalidMemeber : actualMembers) {
                LOGGER.warn("Checking group: " 
                        + group.getName() + " member " + invalidMemeber + " is invalid (removed).");

                try {
                    Subject subj = SubjectFinder.findById(invalidMemeber);
                    group.deleteMember(subj);
                } catch (SubjectNotFoundException e) {
                    LOGGER.error(e, e);
                } catch (SubjectNotUniqueException e) {
                    LOGGER.error(e, e);
                } catch (InsufficientPrivilegeException e) {
                    LOGGER.error(e, e);
                } catch (MemberDeleteException e) {
                    LOGGER.error(e, e);
                }
            }
        }

        sessionUtil.stopSession(session);

        if (LOGGER.isDebugEnabled() && group != null) {
            LOGGER.debug("Group: " + group.getName() + " checked.");
        }

    }

    /**
     * Checks the dynamic types.
     */
    private void checkDynamicType() {

        final boolean create = grouperParameters.getCreateGrouperType();
        LOGGER.info("Checking the dynamic type: " + grouperParameters.getGrouperType());
        final String definitionField = grouperParameters.getGrouperDefinitionField();
        final String grouperDynamicType = grouperParameters.getGrouperType();
        // Checks the group types.
        final GrouperSession session = sessionUtil.createSession();
        try {
            GroupType type = retrieveType(grouperDynamicType);

            if (type == null) {
                if (create) {
                    type = GroupType.createType(session, grouperDynamicType);
                    type.addAttribute(session, definitionField, Privilege.getInstance("read"), 
                            Privilege.getInstance("admin"), false);
                    LOGGER.info("Group type " + grouperDynamicType 
                            + " created with the field: " + definitionField + ".");
                } else {
                    LOGGER.error("The group type: " + grouperDynamicType 
                            + " can't be found and the configuration "
                            + " doesn't not allow to create it. Change the configuration or create it manually.");
                }
            } else {
                boolean defFieldFound = false;
                final Set<Field> fields = type.getFields();
                final Iterator<Field> it = fields.iterator();
                while (it.hasNext() && !defFieldFound) {
                    defFieldFound = it.next().getName().equals(definitionField);
                }
                if (!defFieldFound) {
                    LOGGER.error("The group type " + grouperDynamicType 
                            + " does not contain the definition field: " 
                            + definitionField
                            + ".");
                } else {
                    LOGGER.info("Group type " + grouperDynamicType + " with field " + definitionField + " is valid.");
                }

            }
        } catch (InsufficientPrivilegeException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (SchemaException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        }
        sessionUtil.stopSession(session);
    }

    /**
     * Tests if a group has a dynamic type.
     * @param group The group to test.
     * @return True if the dynamic type is found in the types of the group.
     */
    private boolean isDynamicGroup(final Group group) {
        final Set<GroupType> types = group.getTypes();

        for (GroupType type : types) {
            if (grouperParameters.getGrouperType().equals(type.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a Group from its name.
     * @param session The Grouper session to use.
     * @param groupUUID The uuid of the group to retrieve.
     * @return The group if it can be retrieved, null oterwise.
     */
    private Group retrieveGroup(final GrouperSession session, final String groupUUID) {
        Group group = null;
        try {
            group = GroupFinder.findByUuid(session, groupUUID);
        } catch (GroupNotFoundException e) {
            LOGGER.warn("The group: " + groupUUID + " can't be retrieved from Grouper.");
        }
        return group;
    }

    /**
     * Retrieves the dynamic groups for an user.
     * @param session The Grouper session to use.
     * @param subject The subject associated to the considered user.
     * @return The map of the dynamic groups the user belongs to 
     * (association: Group uuid => Grouper group >).
     * @throws MemberNotFoundException 
     * @throws GroupNotFoundException 
     */
    private Map<String, Group> retrieveDynamicGroupsForUser(final GrouperSession session, final Subject subject) 
    throws MemberNotFoundException, GroupNotFoundException {
        final Map<String, Group> dynamicGroups = new HashMap<String, Group>();

        final Member member = MemberFinder.findBySubject(session, subject);
        @SuppressWarnings("unchecked")
        final Set memberships = member.getImmediateMemberships();

        for (Object o : memberships) {
            final Membership m = (Membership) o;
            final Group g = m.getGroup();
            if (isDynamicGroup(g)) {
                dynamicGroups.put(g.getUuid(), g); 
            }
        }

        return dynamicGroups;
    }

    /**
     * Removes a user from its groups.
     * All the groups or only the dynamic ones may be considered, depending on the configuration.
     * @param userId The id of the user.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#removeFromGroups(java.lang.String)
     */
    public void removeFromGroups(final String userId) {

        if (LOGGER.isDebugEnabled()) {
            String msg = "Removing the user from ";
            if (grouperParameters.getRemoveFromAllGroups()) {
                msg += " all its groups.";
            } else {
                msg += " its dynamic groups.";
            }
            LOGGER.debug(msg);
        }


        final GrouperSession session = sessionUtil.createSession();

        try {

            final Subject subject = new ESCODeletedSubjectImpl(userId); 

            // Retrieves the groups the subject belongs to.
            final Member member = MemberFinder.findBySubject(session, subject);

            @SuppressWarnings("unchecked")
            final Set memberships = member.getImmediateMemberships();

            for (Object o : memberships) {
                final Membership m = (Membership) o;
                final Group group = m.getGroup();
                if (isDynamicGroup(group)) {
                    // The group is dynamic.
                    group.deleteMember(subject);
                    statisticsManager.handleMemberRemoved(group.getName(), userId);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(" Removing user: " + userId + " from the dynamic group: " + group.getName());

                    }
                } else if (grouperParameters.getRemoveFromAllGroups()) {
                    // The group is not dynamic but all the groups have to be processed.
                    group.deleteMember(subject);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(" Removing user: " + userId + " from the group (not dynamic): " + group.getName());
                    }
                }
            }



            sessionUtil.stopSession(session);

        }  catch (GroupNotFoundException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (InsufficientPrivilegeException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberDeleteException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        }
    }

    /**
     * Gives the list of groups with no membership defintions.
     * @return The group names.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#getUndefinedDynamicGroups()
     */
    public Set<String> getUndefinedDynamicGroups() {
        final GrouperSession session = sessionUtil.createSession();
        GroupType type = retrieveType(grouperParameters.getGrouperType());
        final Set<Group> groups = GroupFinder.findAllByType(session, type);
        final Set<String> undefGroups = new HashSet<String>();
        for (Group group : groups) {
            String membersDef = null;
            try {
                membersDef = group.getAttribute(grouperParameters.getGrouperDefinitionField());
                if ("".equals(membersDef)) {
                    undefGroups.add(group.getName());
                }
            } catch (AttributeNotFoundException e) {
                LOGGER.error("Unable to retrieve the attribute "  
                        + grouperParameters.getGrouperDefinitionField()
                        + " for the group: " + group.getName() + ".");
            }
        }
        return undefGroups;
    }

    /**
     * Updates the memberships of an user.
     * @param userId The id of the user.
     * @param newGroups The new groups.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#updateMemberships(java.lang.String, java.util.Map)
     */
    public void updateMemberships(final String userId, final Map<String, DynGroup> newGroups) {
        final GrouperSession session = sessionUtil.createSession();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Updating the memberships for the user: " + userId);
        }


        try {
            final Subject subject = SubjectFinder.findById(userId);
            final Map<String, Group> previousGroups = retrieveDynamicGroupsForUser(session, subject);

            // Removes the obsolet memberships.
            for (String previousGroup : previousGroups.keySet()) {

                if (!newGroups.containsKey(previousGroup)) {
                    final Group group = previousGroups.get(previousGroup); 
                    group.deleteMember(subject);
                    statisticsManager.handleMemberRemoved(group.getName(), userId);

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Update memberships user: " + userId 
                                + " removed from the group: " + group.getName());
                    }
                } 
            }

            // Adds the new memberships.
            for (String  newGroup : newGroups.keySet()) {
                if (!previousGroups.containsKey(newGroup)) {
                    final Group group = retrieveGroup(session, newGroup);
                    if (group != null) {

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Update memberships user: " + userId 
                                    + " added to the group: " + group.getName());
                        }

                        group.addMember(subject);
                        statisticsManager.handleMemberAdded(group.getName(), userId);
                    }
                }
            }
            sessionUtil.stopSession(session);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Memberships for the user: " + userId + " updated.");
            }

        } catch (SubjectNotFoundException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (SubjectNotUniqueException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberNotFoundException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (GroupNotFoundException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (InsufficientPrivilegeException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberDeleteException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberAddException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        }
    }

    /**
     * Creates the memberships for a given user.
     * @param userId The id of the user.
     * @param groups The groups to which the user should become a member.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#createMemeberShips(String, Map)
     */
    public void createMemeberShips(final String userId, final Map<String, DynGroup> groups) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creates the memberships for the user: " + userId);
        }
        final GrouperSession session = sessionUtil.createSession();


        try {
            final Subject subject = SubjectFinder.findById(userId);
            // Adds the new memberships.
            for (String  newGroup : groups.keySet()) {
                final Group group = retrieveGroup(session, newGroup);
                if (group != null) {

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Create memberships user: " + userId 
                                + " added to the group: " + group.getName());
                    }
                    if (!group.hasImmediateMember(subject)) {
                        group.addMember(subject);
                        statisticsManager.handleMemberAdded(group.getName(), userId);
                    }
                }
            }
            sessionUtil.stopSession(session);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Memberships for the user: " + userId + " created.");
            }

        } catch (SubjectNotFoundException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (SubjectNotUniqueException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (InsufficientPrivilegeException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberAddException e) {
            sessionUtil.stopSession(session);
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        }

    }

    /**
     * Tests if a groupName denotes a dynamic group.
     * @param groupName The name of the group/
     * @return True if the group is dynamic.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#isDynamicGroup(java.lang.String)
     */
    public boolean isDynamicGroup(final String groupName) {
        final GrouperSession session = sessionUtil.createSession();
        final Group group = retrieveGroup(session, groupName);
        boolean dynamic = false;
        if (group != null) {
            dynamic = isDynamicGroup(group);
        }
        sessionUtil.stopSession(session);
        return dynamic;
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
