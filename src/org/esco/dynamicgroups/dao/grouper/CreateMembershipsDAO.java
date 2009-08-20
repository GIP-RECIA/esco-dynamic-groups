/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.exception.MemberAddException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectNotUniqueException;

import java.util.Map;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;



/**
 * Callback used update the memberships for a given user with a safe use of Grouper session.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
class CreateMembershipsCallback implements GrouperSessionHandler {

    /** The grouper DAO Instance.*/
    private CreateMembershipsDAO createMembershipsDAO;

    /** The id of the considered user. */
   private String userId;
   
   /** Map of the dynamic groups for this user (groupUUID=>DynGroup).*/
   private Map<String, DynGroup> groups;

    /**
     * Builds an instance of ResetGroupMembersCallBack.
     * @param createMembershipsDAO The instance of Grouper DAO.
     * @param userId The id of the user for wich the memberships are created.
     * @param groups The dynamic groups for the user.
     */
    public CreateMembershipsCallback(final CreateMembershipsDAO createMembershipsDAO, 
            final String userId, final Map<String, DynGroup> groups) {
        this.createMembershipsDAO = createMembershipsDAO;
        this.userId = userId;
        this.groups = groups;
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
        createMembershipsDAO.createMembershipsInternal(session, userId, groups);
        return null;
    }

}

/**
 * DAO used to create the memberships for a given user.
 * @author GIP RECIA - A. Deman
 * 11 août 2009
 *
 */
public class CreateMembershipsDAO extends BaseGrouperDAO {

    
    /** Serial version UID.*/
    private static final long serialVersionUID = -4105345546598046669L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(CreateMembershipsDAO.class);

    /** The statistics manager. */
    private IStatisticsManager statisticsManager;
    
    /**
     * Builds an instance of CreateMembershipsDAO.
     * @param statisticsManager The statistics manager.
     */
    public CreateMembershipsDAO(final IStatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }
    

    /**
     * Creates the memberships for a given user.
     * @param userId The id of the user.
     * @param groups The groups to which the user should become a member.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#createMemberships(String, Map)
     */
    public void createMemberships(final String userId, final Map<String, DynGroup> groups) {
        GrouperSession session = null; 
        try {
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);
            GrouperSession.callbackGrouperSession(session, new CreateMembershipsCallback(this, userId, groups));
        } catch (SessionException e) {
            LOGGER.error(e, e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }
    
    /**
     * Creates the memberships for a given user.
     * @param session The Grouper session.
     * @param userId The id of the user.
     * @param groups The groups to which the user should become a member.
     */
    public void createMembershipsInternal(final GrouperSession session,
            final String userId, final Map<String, DynGroup> groups) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creates the memberships for the user: " + userId);
        }
        if (groups.size() > 0) {

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

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Memberships for the user: " + userId + " created.");
                }

            } catch (SubjectNotFoundException e) {
                LOGGER.error(e, e);
                throw new DynamicGroupsException(e);
            } catch (SubjectNotUniqueException e) {
                LOGGER.error(e, e);
                throw new DynamicGroupsException(e);
            } catch (InsufficientPrivilegeException e) {
                LOGGER.error(e, e);
                throw new DynamicGroupsException(e);
            } catch (MemberAddException e) {
                LOGGER.error(e, e);
                throw new DynamicGroupsException(e);
            }
        }
    }
}
