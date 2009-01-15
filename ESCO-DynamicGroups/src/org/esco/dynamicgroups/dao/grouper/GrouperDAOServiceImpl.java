/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.MemberAddException;
import edu.internet2.middleware.grouper.MemberDeleteException;
import edu.internet2.middleware.grouper.MemberFinder;
import edu.internet2.middleware.grouper.MemberNotFoundException;
import edu.internet2.middleware.grouper.Membership;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectNotUniqueException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;
import org.esco.dynamicgroups.util.ESCODynamicGroupsParameters;
import org.esco.dynamicgroups.util.GrouperSessionUtil;


/**
 * DAO Service for Grouper.
 * @author GIP RECIA - A. Deman
 * 15 janv. 2009
 *
 */
public class GrouperDAOServiceImpl implements IGroupsDAOService, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 2185838538636654415L;

    /** The Grouper session util instance. */
    private GrouperSessionUtil sessionUtil;

    /** The dynamic types in grouper. */
    private String[] grouperDynamicTypes;

    /** Logger. */
    private Logger logger = Logger.getLogger(GrouperDAOServiceImpl.class);
    
    /** Flag used to determine if a deleted user should be removed from all groups 
     * or only from the dynamic ones.
     */
    private boolean removeFromAllGroups;

    /**
     * Builds an instance of GrouperDAOServiceImpl.
     */
    public GrouperDAOServiceImpl() {
        sessionUtil = new GrouperSessionUtil(ESCODynamicGroupsParameters.instance().getGrouperUser());
        grouperDynamicTypes = ESCODynamicGroupsParameters.instance().getGrouperTypes();
        removeFromAllGroups = ESCODynamicGroupsParameters.instance().getRemoveFromAllGroups();
        
        if (logger.isDebugEnabled()) {
            logger.debug("Creates an instance of GrouperDAOServiceImpl - Grouper user: "  
                    + ESCODynamicGroupsParameters.instance().getGrouperUser() 
                    + " - Grouper dynamic types " + Arrays.toString(grouperDynamicTypes) 
                    + " - deleted users are removed from all groups: " + removeFromAllGroups + ".");
        }
    }
    
    /**
     * Tests if a group has a dynamic type.
     * @param group The group to test.
     * @return True if one of the dynamic type is found in the type of the group.
     */
    private boolean isDynamicGroup(final Group group) {
        @SuppressWarnings("unchecked")
        Set types = group.getTypes();

        for (Object oType : types) {
            for (String dynType : grouperDynamicTypes) {
                if (dynType.equals(oType)) {
                    return true;
                }
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
            logger.warn("The dynamic group: " + groupName + " can be retrieved from Grouper.");
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
     */
    public void removeFromGroups(final String userId) {
        
        if (logger.isDebugEnabled()) {
            String msg = "Removing the user from ";
            if (removeFromAllGroups) {
                 msg += " all its groups.";
            } else {
                msg += " its dynamic groups.";
            }
            logger.debug(msg);
        }
        
        
        final GrouperSession session = sessionUtil.createSession();
        
        try {
            final Subject subject = SubjectFinder.findById(userId);
            
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
                if (logger.isTraceEnabled()) {
                    logger.trace(" Removing user: " + userId + " from the group: " + group.getName());
                }
            }
            
            sessionUtil.stopSession(session);
            
        } catch (SubjectNotFoundException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (SubjectNotUniqueException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberNotFoundException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (GroupNotFoundException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (InsufficientPrivilegeException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberDeleteException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
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
        
        if (logger.isDebugEnabled()) {
            logger.debug("Updating the memberships for the user: " + userId);
        }
        
        final GrouperSession session = sessionUtil.createSession();
        try {
            final Subject subject = SubjectFinder.findById(userId);
            final Map<String, Group> previousGroups = retrieveDynamicGroupsForUser(session, subject);

            // Removes the obsolet memberships.
            for (String previousGroup : previousGroups.keySet()) {

                if (!newGroups.containsKey(previousGroup)) {
                    previousGroups.get(previousGroup).deleteMember(subject);
                    
                    if (logger.isDebugEnabled()) {
                        logger.debug("Update memberships user: " + userId 
                                + " removed from the group: " + previousGroup);
                    }
                } 
            }

            // Adds the new memberships.
            for (String  newGroup : newGroups.keySet()) {
                if (previousGroups.containsKey(newGroup)) {
                    final Group group = retrieveGroup(session, newGroup);
                    if (group != null) {
                        
                        if (logger.isDebugEnabled()) {
                            logger.debug("Update memberships user: " + userId 
                                    + " added to the group: " + newGroup);
                        }
                        
                        group.addMember(subject);
                    }
                }
            }
            sessionUtil.stopSession(session);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Memberships for the user: " + userId + " updated.");
            }
            
        } catch (SubjectNotFoundException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (SubjectNotUniqueException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberNotFoundException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (GroupNotFoundException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (InsufficientPrivilegeException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberDeleteException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberAddException e) {
            sessionUtil.stopSession(session);
            logger.error(e, e);
            throw new DynamicGroupsException(e);
        }
    }
}
