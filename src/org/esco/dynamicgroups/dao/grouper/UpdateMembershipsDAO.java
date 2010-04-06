/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.MemberFinder;
import edu.internet2.middleware.grouper.Membership;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.GroupNotFoundException;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.exception.MemberAddException;
import edu.internet2.middleware.grouper.exception.MemberDeleteException;
import edu.internet2.middleware.grouper.exception.MemberNotFoundException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectNotUniqueException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;


/**
 * Callback used update the memberships for a given user with a safe use of Grouper session.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
class UpdateMembershipsCallback implements GrouperSessionHandler {

    /** The grouper DAO Instance.*/
    private UpdateMembershipsDAO updateMembershipsDAO;

    /** The id of the considered user. */
   private String userId;
   
   /** Map of the new dynamic groups for this user (groupUUID=>DynGroup).*/
   private Map<String, DynGroup> newGroups;

    /**
     * Builds an instance of ResetGroupMembersCallBack.
     * @param updateMembershipsDAO The instance of Grouper DAO.
     * @param userId The id of the user to update.
     * @param newGroups The new dynamic groups for the user.
     */
    public UpdateMembershipsCallback(final UpdateMembershipsDAO updateMembershipsDAO, 
            final String userId, final Map<String, DynGroup> newGroups) {

        this.updateMembershipsDAO = updateMembershipsDAO;
        this.userId = userId;
        this.newGroups = newGroups;
    }

    /**
     * Calls the DAO method to reset the group.
     * @param session The Grouper session.
     * @return null.
     * @throws GrouperSessionException
     * @see edu.internet2.middleware.grouper.misc.GrouperSessionHandler#callback(GrouperSession)
     */
    public Object callback(final GrouperSession session)
    throws GrouperSessionException {
        updateMembershipsDAO.updateMembershipsInternal(session, userId, newGroups);
        return null;
    }

}

/**
 * @author GIP RECIA - A. Deman
 * 11 août 2009
 *
 */
public class UpdateMembershipsDAO extends BaseGrouperDAO {

    /** Serial version UID.*/
    private static final long serialVersionUID = -8276432943151676065L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(UpdateMembershipsDAO.class);

    /** The grouper parameters. */
    private GroupsParametersSection grouperParameters;
    
    /** The statistics manager. */
    private IStatisticsManager statisticsManager;
    
    /**
     * Builds an instance of UpdateMembershipsDAO.
     * @param grouperParameters The grouper parameters.
     * @param statisticsManager The statistics manager.
     */
    public UpdateMembershipsDAO(final GroupsParametersSection grouperParameters, 
            final IStatisticsManager statisticsManager) {
        this.grouperParameters = grouperParameters;
        this.statisticsManager = statisticsManager;
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
            if (isDynamicGroup(g, grouperParameters)) {
                dynamicGroups.put(g.getUuid(), g); 
            }
        }

        return dynamicGroups;
    }
    
    /**
     * Updates the memberships of an user.
     * @param session The grouper session.
     * @param userId The id of the user.
     * @param newGroups The new groups.
     */
    protected void updateMembershipsInternal(final GrouperSession session,
            final String userId, final Map<String, DynGroup> newGroups) {
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

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Memberships for the user: " + userId + " updated.");
            }

        } catch (SubjectNotFoundException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (SubjectNotUniqueException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberNotFoundException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (GroupNotFoundException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (InsufficientPrivilegeException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberDeleteException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (MemberAddException e) {
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
        GrouperSession session = null;
        try {
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);
            GrouperSession.callbackGrouperSession(session, new UpdateMembershipsCallback(this, userId, newGroups));
        } catch (SessionException e) {
            LOGGER.error(e, e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }
}
