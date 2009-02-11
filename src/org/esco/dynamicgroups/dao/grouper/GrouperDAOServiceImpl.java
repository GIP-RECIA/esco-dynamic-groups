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
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
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
import org.esco.dynamicgroups.domain.IDynamicGroupInitializer;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;
import org.esco.dynamicgroups.util.GrouperSessionUtil;
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

    /** The groups initializer. */
    private IDynamicGroupInitializer initializer;

    /** The dynamic type in grouper. */
    private String grouperDynamicType;

    /** Flag used to determine if a deleted user should be removed from all groups 
     * or only from the dynamic ones.
     */
    private boolean removeFromAllGroups;
    
    /** Flag to determine if the dynamic groups shoudl be reseted. */
    private boolean resetOnStartup;
    
    /** Initialization flag. */
    private boolean initialized;



    /**
     * Builds an instance of GrouperDAOServiceImpl.
     */
    public GrouperDAOServiceImpl() {
        sessionUtil = new GrouperSessionUtil(ESCODynamicGroupsParameters.instance().getGrouperUser());
        grouperDynamicType = ESCODynamicGroupsParameters.instance().getGrouperType();
        removeFromAllGroups = ESCODynamicGroupsParameters.instance().getRemoveFromAllGroups();
        resetOnStartup = ESCODynamicGroupsParameters.instance().getResetOnStartup();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creates an instance of GrouperDAOServiceImpl - Grouper user: "  
                    + ESCODynamicGroupsParameters.instance().getGrouperUser() 
                    + " - Grouper dynamic type " + grouperDynamicType 
                    + " - deleted users are removed from all groups: " + removeFromAllGroups 
                    + " - reset on startup: " + resetOnStartup + ".");
        }


    }


    /**
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(this.initializer, 
                "The property initializer in the class " + this.getClass().getName() 
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
            if (!initialized) {
                checkDynamicType();
                resetAllDynamicGroups();
                initialized = true;
            }
        }
    }
    
    /**
     * Adds a user to a group.
     * @param groupName The name of the group.
     * @param userIds The ids of the users to add to the group.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#addToGroup(String, Set)
     */
    public void addToGroup(final String groupName, final Set<String> userIds) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding users: " + userIds + " to the group: " + groupName + ".");
        }

        final GrouperSession session  = sessionUtil.createSession();
        final Group group = retrieveGroup(session, groupName);

        if (group == null) {
            LOGGER.error("Error the group " + groupName + " can't be found.");

        } else {
            try {
                for (String userId : userIds) {
                    final Subject subject = SubjectFinder.findById(userId);
                    if (!group.hasImmediateMember(subject)) {
                        group.addMember(subject);

                        if (LOGGER.isTraceEnabled()) {
                            LOGGER.trace("The user: " + userId + " has been added to the group: " 
                                    + groupName  + ".");
                        }
                    } else {
                        LOGGER.warn("The user: " + userId 
                                + " is already an immediate member of the group: " + groupName + ".");
                    }
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

    }

    /** 
     * Creates a group if it does not exist, removes all its members otherwise and ther initializes
     * the members.
     * @param definition The definition of the group.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#resetGroupMembers(DynamicGroupDefinition)
     */
    public void resetGroupMembers(final DynamicGroupDefinition definition) {
        final GrouperSession session  = sessionUtil.createSession();

        final Group group = retrieveGroup(session, definition.getGroupName());

        if (group == null) {
            LOGGER.error("Error the group " + definition.getGroupName() + " can't be found.");

        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Resetting group: " + definition.getGroupName());
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
        initializer.initialize(definition);
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
     * Resets all the dynamic groups.
     */
    private void resetAllDynamicGroups() {
        if (ESCODynamicGroupsParameters.instance().getResetOnStartup()) {
            LOGGER.info("Resetting all the dynamic groups.");
            final GroupType type = retrieveType(grouperDynamicType);
            if (type != null) {

                final GrouperSession session = sessionUtil.createSession();
                resetDynamicGroups(StemFinder.findRootStem(session));
            }
        }
    }

    /**
     * Resets all the dynamic groups under a stem.
     * @param stem The current stem.
     */
    private void resetDynamicGroups(final Stem stem) {
        LOGGER.info("Resetting the dynamic groups under the stem " + stem + ".");
        @SuppressWarnings("unchecked")
        final Set<Group> groups = stem.getChildGroups();

        // initializes all the dynamic groups under the stem.
        for (Group group : groups) {
            if (isDynamicGroup(group)) {
                String membersDef = null;
                try {
                    membersDef = group.getAttribute(ESCODynamicGroupsParameters.instance().getGrouperDefinitionField());
                } catch (AttributeNotFoundException e) {
                    LOGGER.error("Unable to retrieve the attribute "  
                            + ESCODynamicGroupsParameters.instance().getGrouperDefinitionField()
                            + " for the group: " + group.getName() + ".");
                }

                final DynamicGroupDefinition groupDef = new DynamicGroupDefinition(group.getName(), membersDef);
                resetGroupMembers(groupDef);
            }
        }
        @SuppressWarnings("unchecked")
        final Set<Stem> childStems = stem.getChildStems();

        for (Stem childStem : childStems) {
            resetDynamicGroups(childStem);
        }
    }

    /**
     * Checks the dynamic types.
     */
    private void checkDynamicType() {

        final boolean create = ESCODynamicGroupsParameters.instance().getCreateGrouperType();
        LOGGER.info("Checking the dynamic types.");
        final String definitionField = ESCODynamicGroupsParameters.instance().getGrouperDefinitionField();
        // Checks the group types.
        final GrouperSession session = sessionUtil.createSession();
        try {
            GroupType type = retrieveType(grouperDynamicType);

            if (type == null) {
                if (create) {
                    type = GroupType.createType(session, grouperDynamicType);
                    type.addAttribute(session, definitionField, Privilege.getInstance("admin"), 
                            Privilege.getInstance("admin"), true);
                    LOGGER.info("Group type " + grouperDynamicType + " created.");
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
                    //                        final Field field = it.next();
                    //                        final String fieldName = field.getName();
                    defFieldFound = it.next().getName().equals(definitionField);
                }
                if (!defFieldFound) {
                    LOGGER.fatal("The group type " + grouperDynamicType 
                            + " does not contain the definition field: " + definitionField);
                    System.exit(1);
                } else {
                    LOGGER.info("Group type " + grouperDynamicType + " is valid.");
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
            if (grouperDynamicType.equals(type.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a Group from its name.
     * @param session The Grouper session to use.
     * @param groupName The name of the group to retrieve.
     * @return The group if it can be retrieved, null oterwise.
     */
    private Group retrieveGroup(final GrouperSession session, final String groupName) {
        Group group = null;
        try {
            group = GroupFinder.findByName(session, groupName);
        } catch (GroupNotFoundException e) {
            LOGGER.warn("The group: " + groupName + " can't be retrieved from Grouper.");
        }
        return group;
    }

    /**
     * Retrieves the dynamic groups for an user.
     * @param session The Grouper session to use.
     * @param subject The subject associated to the considered user.
     * @return The map of the dynamic groups the user belongs to 
     * (association: Group name => Grouper group >).
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
                dynamicGroups.put(g.getName(), g); 
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
            if (removeFromAllGroups) {
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
            final Set<Group> groups = new HashSet<Group>();
            final Member member = MemberFinder.findBySubject(session, subject);

            @SuppressWarnings("unchecked")
            final Set memberships = member.getImmediateMemberships();

            for (Object o : memberships) {
                final Membership m = (Membership) o;
                final Group group = m.getGroup();
                if (isDynamicGroup(group) || removeFromAllGroups) {
                    groups.add(group);
                }
            }

            for (Group group : groups) {
                group.deleteMember(subject);
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(" Removing user: " + userId + " from the group: " + group.getName());
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
     * Updates the memberships of an user.
     * @param userId The id of the user.
     * @param newGroups The new groups.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#updateMemberships(java.lang.String, java.util.Map)
     */
    public void updateMemberships(final String userId, final Map<String, DynGroup> newGroups) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Updating the memberships for the user: " + userId);
        }

        final GrouperSession session = sessionUtil.createSession();
        try {
            final Subject subject = SubjectFinder.findById(userId);
            final Map<String, Group> previousGroups = retrieveDynamicGroupsForUser(session, subject);

            // Removes the obsolet memberships.
            for (String previousGroup : previousGroups.keySet()) {

                if (!newGroups.containsKey(previousGroup)) {
                    previousGroups.get(previousGroup).deleteMember(subject);

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Update memberships user: " + userId 
                                + " removed from the group: " + previousGroup);
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
                                    + " added to the group: " + newGroup);
                        }

                        group.addMember(subject);
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
                                + " added to the group: " + newGroup);
                    }
                    if (!group.hasImmediateMember(subject)) {
                        group.addMember(subject);
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
     * Getter for initializer.
     * @return initializer.
     */
    public IDynamicGroupInitializer getInitializer() {
        return initializer;
    }


    /**
     * Setter for initializer.
     * @param initializer the new value for initializer.
     */
    public void setInitializer(final IDynamicGroupInitializer initializer) {
        this.initializer = initializer;
    }


 
}
