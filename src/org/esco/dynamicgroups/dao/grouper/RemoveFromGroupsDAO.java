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
import edu.internet2.middleware.grouper.exception.MemberDeleteException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;
import edu.internet2.middleware.subject.Subject;

import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;

/**
 * Callback used to check the members of a dynamic group with a safe use of Grouper session.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
class RemoveFromGroupsCallback implements GrouperSessionHandler {
    
    /** The grouper DAO Instance.*/
    private RemoveFromGroupsDAO removeFromGroupsDAO;
    
    /** The id of the user. */
    private String userId;
    
    /**
     * Builds an instance of ResetGroupMembersCallBack.
     * @param removeFromGroupsDAO The instance of Grouper DAO.
     * @param userId The id of the user.
     */
    public RemoveFromGroupsCallback(final RemoveFromGroupsDAO removeFromGroupsDAO,
            final String userId) {
        this.removeFromGroupsDAO = removeFromGroupsDAO;
        this.userId = userId;
    }
    
    /**
     * Calls the DAO method to remove a the member from the group.
     * @param session The Grouper session.
     * @return null.
     * @throws GrouperSessionException
     * @see edu.internet2.middleware.grouper.misc.GrouperSessionHandler#callback(GrouperSession)
     */
    public Object callback(final GrouperSession session)
    throws GrouperSessionException {
        removeFromGroupsDAO.removeFromGroupsInternal(session, userId);
        return null;
    }
}

/**
 * DAO used to remove a user from its groups.
 * @author GIP RECIA - A. Deman
 * 11 août 2009
 *
 */
public class RemoveFromGroupsDAO extends BaseGrouperDAO {

    /** Serial version UID.*/
    private static final long serialVersionUID = -171254819722338719L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(RemoveFromGroupsDAO.class);
    
    /** Thge grouper parameters. */
    private GroupsParametersSection grouperParameters;

    /** The statistics manager. */
    private IStatisticsManager statisticsManager;
    
    /**
     * Builds an instance of RemoveFromGroupsDAO.
     * @param grouperParameters The groupers parameters.
     * @param statisticsManager The statistics manager.
     */
    public RemoveFromGroupsDAO(final GroupsParametersSection grouperParameters, 
            final IStatisticsManager statisticsManager) {
        this.grouperParameters = grouperParameters;
        this.statisticsManager = statisticsManager;
    }
    
    /**
     * Removes a user from its groups.
     * All the groups or only the dynamic ones may be considered, depending on the configuration.
     * @param userId The id of the user.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#removeFromGroups(java.lang.String)
     */
    public void removeFromGroups(final String userId) {

        GrouperSession session = null;
        try {
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);
            GrouperSession.callbackGrouperSession(session, new RemoveFromGroupsCallback(this, userId));
        } catch (SessionException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }
    
    /**
     * Removes a user from its groups.
     * All the groups or only the dynamic ones may be considered, depending on the configuration.
     * @param session The grouper session to use.
     * @param userId The id of the user.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#removeFromGroups(java.lang.String)
     */
    public void removeFromGroupsInternal(final GrouperSession session, final String userId) {
        
        if (LOGGER.isDebugEnabled()) {
            String msg = "Removing the user from ";
            if (grouperParameters.getRemoveFromAllGroups()) {
                msg += " all its groups.";
            } else {
                msg += " its dynamic groups.";
            }
            LOGGER.debug(msg);
        }
        
        
        try {
            
            final Subject subject = new ESCODeletedSubjectImpl(userId); 
            
            // Retrieves the groups the subject belongs to.
            final Member member = MemberFinder.findBySubject(session, subject, true);
            
            final Set<Membership> memberships = member.getImmediateMemberships();
            
            for (Object o : memberships) {
                final Membership m = (Membership) o;
                final Group group = m.getGroup();
                if (isDynamicGroup(group, grouperParameters)) {
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
            
            
            
        }  catch (GroupNotFoundException e) {
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
