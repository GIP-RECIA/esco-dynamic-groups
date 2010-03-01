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
import edu.internet2.middleware.grouper.exception.MemberDeleteException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;

import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;


/**
 * Callback used to reset a group with a safe use of Grouper session.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
class ResetGroupMembersCallback implements GrouperSessionHandler {

    /** The grouper DAO Instance.*/
    private ResetGroupMembersDAO resetGroupMembersDAO;

    /** The GrouperUUID of the group. */
    private String groupUUID;

    /**
     * Builds an instance of ResetGroupMembersCallBack.
     * @param resetGroupMembersDAO The instance of Grouper DAO.
     * @param groupUUID The uuid of the considered group.
     */
    public ResetGroupMembersCallback(final ResetGroupMembersDAO resetGroupMembersDAO, 
            final String groupUUID) {

        this.resetGroupMembersDAO = resetGroupMembersDAO;
        this.groupUUID = groupUUID;
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
        resetGroupMembersDAO.resetGroupMembersInternal(session, groupUUID);
        return null;
    }

}


/**
 * Resets the members of a group.
 * @author GIP RECIA - A. Deman
 * 11 août 2009
 *
 */
public class ResetGroupMembersDAO extends BaseGrouperDAO {

    /** Serial version UID.*/
    private static final long serialVersionUID = 5519931176378570682L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ResetGroupMembersDAO.class);
    
    /**
     * 
     * Builds an instance of ResetGroupMembersDAO.
     */
    public ResetGroupMembersDAO() {
        super();
    }
    

    /** 
     * Creates a group if it does not exist, removes all its members otherwise .
     * @param definition The definition of the group.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#resetGroupMembers(DynamicGroupDefinition)
     */
    public void resetGroupMembers(final DynamicGroupDefinition definition) {
        GrouperSession session = null;
        try {
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);
            GrouperSession.callbackGrouperSession(session, 
                    new ResetGroupMembersCallback(this, definition.getGroupUUID()));
        } catch (SessionException e) {
            LOGGER.error(e, e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }


    /** 
     * Creates a group if it does not exist, removes all its members otherwise .
     * @param session The Grouper session to use.
     * @param groupUUID The UUID of the group.
     */
    public void resetGroupMembersInternal(final GrouperSession session, final String groupUUID) {

        final Group group = retrieveGroup(session, groupUUID);

        if (group == null) {
            LOGGER.error("Error the group " + groupUUID + " can't be found.");

        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Resetting group: " + groupUUID);
            }
            @SuppressWarnings("unchecked") 
            final Set<Member> members = group.getImmediateMembers();
            for (Member member : members) {
                try {
                    group.deleteMember(new ESCODeletedSubjectImpl(member.getSubjectId()));
                } catch (InsufficientPrivilegeException e) {
                    LOGGER.error(e, e);
                    throw new DynamicGroupsException(e);
                } catch (MemberDeleteException e) {
                    LOGGER.error(e, e);
                    throw new DynamicGroupsException(e);
                } 
            }
        }
    }


}
