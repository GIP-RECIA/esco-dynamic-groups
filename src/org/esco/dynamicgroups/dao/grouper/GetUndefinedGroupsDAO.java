/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.Stem.Scope;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.AttributeNotFoundException;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;


/**
 * Callback used to dynamic groups with no members definition,  with a safe use of Grouper session.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
 class GetUndefinedGroupsCallback implements GrouperSessionHandler {
    
    /** The grouper DAO Instance.*/
    private GetUndefinedGroupsDAO grouperDAO;
    
    /**
     * Builds an instance of ResetGroupMembersCallBack.
     * @param grouperDAO The instance of Grouper DAO.
     */
    public GetUndefinedGroupsCallback(final GetUndefinedGroupsDAO grouperDAO) {
        this.grouperDAO = grouperDAO;
    }
    
    /**
     * Calls the DAO method to retrive the undefined groups.
     * @param session The Grouper session.
     * @return The undefined groups.
     * @throws GrouperSessionException
     * @see edu.internet2.middleware.grouper.misc.GrouperSessionHandler#callback(GrouperSession)
     */
    public Object callback(final GrouperSession session)
    throws GrouperSessionException {
        final Set<String> undefinedGroups = grouperDAO.getUndefinedDynamicGroupsInternal(session);
        return undefinedGroups;
    }
}


/**
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
public class GetUndefinedGroupsDAO extends BaseGrouperDAO {

    /** Serial version UID.*/
    private static final long serialVersionUID = -1168430642129902735L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GetUndefinedGroupsDAO.class);
 
    /** The grouper parameters. */
    private GroupsParametersSection grouperParameters;
    
    /**
     * Builds an instance of GetUndefinedGroupsDAO.
     * @param grouperParameters The grouper parameters.
     */
    public GetUndefinedGroupsDAO(final GroupsParametersSection grouperParameters) {
        this.grouperParameters = grouperParameters;
    }
    
    /**
     * Builds recursively the set of undefined groups.
     * @param session The grouper session to use.
     * @param type The Grouper's custom type associated to the dynamic groups.
     * @param setToPopulate The set to populate with the name of the undefined groups.
     * @param stem The current stem.
     */
    private void populateUndefinedGroupsSet(final GrouperSession session, 
            final GroupType type, 
            final Set<String> setToPopulate, 
            final Stem stem) {
        
        final Set<Stem> children = stem.getChildStems(Scope.ONE);
        
        for (Stem child : children) {
            populateUndefinedGroupsSet(session, type, setToPopulate, child);
        }
        
        final Set<Group> groups = stem.getChildGroups(Scope.ONE);
        for (Group group : groups) {
            if (group.getTypes().contains(type)) {
                String membersDef = null;
                try {
                    membersDef = group.getAttribute(grouperParameters.getGrouperDefinitionField());
                    if ("".equals(membersDef)) {
                        setToPopulate.add(group.getName());
                    }
                } catch (AttributeNotFoundException e) {
                    LOGGER.error("Unable to retrieve the attribute "  
                            + grouperParameters.getGrouperDefinitionField()
                            + " for the group: " + group.getName() + ".");
                }
            }
        }
    }

    /**
     * Gives the list of groups with no membership defintions.
     * @param session The Grouper session.
     * @return The group names.
     */
    protected Set<String> getUndefinedDynamicGroupsInternal(final GrouperSession session) {
        final GroupType type = retrieveType(grouperParameters.getGrouperType());
        final Stem root = StemFinder.findRootStem(session);
        final Set<String> undefGroups = new HashSet<String>();
        populateUndefinedGroupsSet(session, type, undefGroups, root);
        return undefGroups;
    }
    /**
     * Gives the list of groups with no membership defintions.
     * @return The group names.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#getUndefinedDynamicGroups()
     */
    public Set<String> getUndefinedDynamicGroups() {
        GrouperSession session = null;
        try {
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);

            @SuppressWarnings("unchecked")
            final Set<String> undefinedGroups = (Set<String>) GrouperSession.callbackGrouperSession(session, 
                        new GetUndefinedGroupsCallback(this));

            return undefinedGroups;
        } catch (SessionException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }

}
