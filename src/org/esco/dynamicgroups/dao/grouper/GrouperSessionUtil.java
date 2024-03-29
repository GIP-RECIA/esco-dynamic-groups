/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectNotUniqueException;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;

/**
 * Util class used to handle the grouper sessions.
 * @author GIP RECIA - A. Deman
 * 28 juil. 08
 *
 */
public class GrouperSessionUtil implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -4531189184916034497L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GrouperSessionUtil.class);

    /** Subject id used to open the sessions. */
    private String subjectId;


    /**
     * Builds an instance of GrouperSessionUtil.
     * @param subjectId The subject id used to open sessions.
     */
    public GrouperSessionUtil(final String subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * Creates a Grouper session instance.
     * @return The session object.
     */
    public GrouperSession createSession() {

        try {
            final Subject subject = SubjectFinder.findById(subjectId, true);
            final GrouperSession session = GrouperSession.start(subject);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Starting a new session: " + session.getSessionId());
            }

            return session;

        } catch (SubjectNotFoundException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (SubjectNotUniqueException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (SessionException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        }
    }



    /**
     * Closes a grouper session.
     * @param session The session to close.
     */
    public void stopSession(final GrouperSession session) {
        try {
            LOGGER.debug("Stopping the session : " + session.getSessionId());
            session.stop();
        } catch (SessionException e) {
            LOGGER.error(e, e);
        }
    }

    /**
     * Getter for subjectId.
     * @return subjectId.
     */
    public String getSubjectId() {
        return subjectId;
    }

    /**
     * Setter for subjectId.
     * @param subjectId the new value for subjectId.
     */
    public void setSubjectId(final String subjectId) {
        this.subjectId = subjectId;
    }
}
