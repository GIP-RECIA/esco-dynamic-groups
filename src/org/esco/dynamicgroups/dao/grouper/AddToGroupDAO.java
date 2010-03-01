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

import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;


/**
* Callback used to add a list of users to a group with a safe use of Grouper session.
* @author GIP RECIA - A. Deman
* 10 août 2009
*
*/
class AddToGroupCallback implements GrouperSessionHandler {

   /** The grouper DAO Instance.*/
   private AddToGroupDAO addToGroupDAO;

   /** The GrouperUUID of the group. */
   private String groupUUID;

   /** The ids of the users to add to the group. */
   private Set<String> userIds;

   /**
    * Builds an instance of AddToGroupCallback.
    * @param addToGroupDAO The instance of Grouper DAO used to ad users to groups.
    * @param groupUUID The uuid of the considered group.
    * @param userIds The users to add to the group.
    */
   public AddToGroupCallback(final AddToGroupDAO addToGroupDAO, 
           final String groupUUID, 
           final Set<String> userIds) {

       this.addToGroupDAO = addToGroupDAO;
       this.groupUUID = groupUUID;
       this.userIds = userIds;
   }

   /**
    * Calls the DAO method to add the users to the group.
    * @param session The Grouper session.
    * @return null.
    * @throws GrouperSessionException
    * @see edu.internet2.middleware.grouper.misc.GrouperSessionHandler#callback(GrouperSession)
    */
   public Object callback(final GrouperSession session)
   throws GrouperSessionException {
       addToGroupDAO.addToGroupInternal(session, groupUUID, userIds);
       return null;
   }
}

/**
 * DAO used to add users to a group.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
public class AddToGroupDAO extends BaseGrouperDAO implements Serializable {

    /** Serial Version UID.*/
    private static final long serialVersionUID = 8759472783807424758L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(AddToGroupDAO.class);
    
    /**
     * Builds an instance of AddToGroupDAO.
     */
    public AddToGroupDAO() {
        super();
    }
    
    /**
     * Adds a user to a group.
     * @param session The grouper session to use.
     * @param groupUUID The uuid of the group.
     * @param userIds The ids of the users to add to the group.
     */
    protected void addToGroupInternal(final GrouperSession session, 
            final String groupUUID, final Set<String> userIds) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding users: " + userIds + " to the group: " + groupUUID + ".");
        }

        final Group group = retrieveGroup(session, groupUUID);

        if (group == null) {
            LOGGER.error("Error the group " + groupUUID + " can't be found.");

        } else {

            for (String userId : userIds) {
                try {
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
                }   catch (SubjectNotUniqueException e) {
                    LOGGER.error(e, e);
                    throw new DynamicGroupsException(e);
                } catch (SubjectNotFoundException e) {
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

    /**
     * Adds a user to a group.
     * @param groupUUID The uuid of the group.
     * @param userIds The ids of the users to add to the group.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#addToGroup(String, Set)
     */
    public void addToGroup(final String groupUUID, final Set<String> userIds) {
        GrouperSession session = null;
        try {
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);
            GrouperSession.callbackGrouperSession(session, new AddToGroupCallback(this, groupUUID, userIds));
        } catch (SessionException e) {
            LOGGER.error(e, e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
        

    }

}
