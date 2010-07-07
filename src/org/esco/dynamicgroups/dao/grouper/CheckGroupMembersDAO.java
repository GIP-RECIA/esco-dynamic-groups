/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.exception.MemberAddException;
import edu.internet2.middleware.grouper.exception.MemberDeleteException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectNotUniqueException;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;


/**
 * Callback used to check the members of a dynamic group with a safe use of Grouper session.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
class CheckGroupMembersCallback implements GrouperSessionHandler {

    /** The grouper DAO Instance.*/
    private CheckGroupMembersDAO checkGroupMembersDAO;

    /** The GrouperUUID of the group. */
    private String groupUUID;

    /** The expected members of the group. */
    private Set<String> expectedMembers;

    /**
     * Builds an instance of ResetGroupMembersCallBack.
     * @param checkGroupMembersDAO The instance of Grouper DAO.
     * @param groupUUID The uuid of the group in Grouper.
     * @param expectedMembers  The ids of the expected members of the group.
     */
    public CheckGroupMembersCallback(final CheckGroupMembersDAO checkGroupMembersDAO,
            final String groupUUID, 
            final Set<String> expectedMembers) {
        this.checkGroupMembersDAO = checkGroupMembersDAO;
        this.groupUUID = groupUUID;
        this.expectedMembers = expectedMembers;
    }

    /**
     * Calls the DAO method to check the members of the group.
     * @param session The Grouper session.
     * @return null.
     * @throws GrouperSessionException
     * @see edu.internet2.middleware.grouper.misc.GrouperSessionHandler#callback(GrouperSession)
     */
    public Object callback(final GrouperSession session)
    throws GrouperSessionException {
        checkGroupMembersDAO.checkGroupMembersInternal(session, groupUUID, expectedMembers);
        return null;
    }
}


/**
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
public class CheckGroupMembersDAO extends BaseGrouperDAO {

    /** Serial version UID.*/
    private static final long serialVersionUID = -8075673978389571024L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(CheckGroupMembersDAO.class);
 
    /** Statistics manager used to report the result of the checks. */
    private IStatisticsManager statisticsManager;
    
    /**
     * Builds an instance of CheckGroupMembersDAO.
     * @param statisticsManager A statistics manager use to report the result of the checks.
     */
    public CheckGroupMembersDAO(final IStatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    /** 
     * Checks the members of a group.
     * @param session  The Grouper session to use.
     * @param groupUUID The uuid of the group in Grouper.
     * @param expectedMembers The expected members of the group according to 
     * its logic definition.
     */
    public void checkGroupMembersInternal(final GrouperSession session, final String groupUUID, 
            final Set<String> expectedMembers) {

        final Group group = retrieveGroup(session, groupUUID);

        if (group != null) {

            final String groupName = group.getName();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Checking group: " + groupName);
            }

            statisticsManager.handleGroupMembersChecked(groupName);


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
                            + groupName + " member " + expectedMember + " not found (added).");
                    statisticsManager.handleMissingMember(groupName, expectedMember);
                    try {
                        final Subject subj = SubjectFinder.findById(expectedMember,true);
                        group.addMember(subj);
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
                actualMembers.remove(expectedMember);
            }

            // All the remaining uid in the actual list are invalid, so the corresponding subjects
            // are removed from the group.
            for (String invalidMember : actualMembers) {
                LOGGER.warn("Checking group: " 
                        + groupName + " member " + invalidMember + " is invalid (removed).");
                statisticsManager.handleInvalidMember(groupName, invalidMember);
                try {
                    Subject subj = SubjectFinder.findById(invalidMember,true);
                    group.deleteMember(subj);
                } catch (SubjectNotFoundException e) {
                    LOGGER.error(e, e);
                    throw new DynamicGroupsException(e);
                } catch (SubjectNotUniqueException e) {
                    LOGGER.error(e, e);
                    throw new DynamicGroupsException(e);
                } catch (InsufficientPrivilegeException e) {
                    LOGGER.error(e, e);
                    throw new DynamicGroupsException(e);
                } catch (MemberDeleteException e) {
                    LOGGER.error(e, e);
                    throw new DynamicGroupsException(e);
                }
            }
        }


        if (LOGGER.isDebugEnabled() && group != null) {
            LOGGER.debug("Group: " + group.getName() + " checked.");
        }
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
        GrouperSession session = null;
        try {
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);
            GrouperSession.callbackGrouperSession(session, 
                    new CheckGroupMembersCallback(this, groupDefinition.getGroupUUID(), expectedMembers));
        } catch (SessionException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }

}
