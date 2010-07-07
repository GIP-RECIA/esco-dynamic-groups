/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.AttributeNotFoundException;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;


/**
 * Callback used to retrieve all the dynamic group definitions with a safe use of Grouper session.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
class GetAllDynamicGroupDefinitionsCallback implements GrouperSessionHandler {

    /** The grouper DAO Instance.*/
    private GetAllDynamicGroupDefintionsDAO getAllDynamicGroupDefintionsDAO;

    /**
     * Builds an instance of ResetGroupMembersCallBack.
     * @param getAllDynamicGroupDefintionsDAO The instance of Grouper DAO.
     */
    public GetAllDynamicGroupDefinitionsCallback(
            final GetAllDynamicGroupDefintionsDAO getAllDynamicGroupDefintionsDAO) {

        this.getAllDynamicGroupDefintionsDAO = getAllDynamicGroupDefintionsDAO;
    }

    /**
     * Calls the DAO method to retrieve the dynamic group definitions.
     * @param session The Grouper session.
     * @return The definitions.
     * @throws GrouperSessionException
     * @see edu.internet2.middleware.grouper.misc.GrouperSessionHandler#callback(GrouperSession)
     */
    public Object callback(final GrouperSession session)
    throws GrouperSessionException {
        final Object defintions = getAllDynamicGroupDefintionsDAO.getAllDynamicGroupDefinitionsInternal(session);
        return defintions;
    }

}

/**
 * DAO used to retrieve the dynamic group definitions.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
public class GetAllDynamicGroupDefintionsDAO extends BaseGrouperDAO {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = -1299580502179654717L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GetAllDynamicGroupDefintionsDAO.class);
    

    /** The grouper parameters. */
    private GroupsParametersSection grouperParameters;
    
    /**
     * Builds an instance of GetAllDynamicGroupDefintionsDAO.
     * @param grouperParameters The parameters associated to the grouper backend.
     */
    public GetAllDynamicGroupDefintionsDAO(final  GroupsParametersSection grouperParameters) {
        this.grouperParameters = grouperParameters;
    }

    /**
     * Gives the set of definitions for all the dynamic groups in grouper.
     * @return The set of definitions.
     * @see org.esco.dynamicgroups.dao.grouper.IGroupsDAOService#getAllDynamicGroupDefinitions()
     */
    public Set<DynamicGroupDefinition> getAllDynamicGroupDefinitions() {
        GrouperSession session = null;
        try {
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);

            @SuppressWarnings("unchecked")
            final Set<DynamicGroupDefinition> definitions = 
                (Set<DynamicGroupDefinition>) GrouperSession.callbackGrouperSession(session, 
                        new GetAllDynamicGroupDefinitionsCallback(this));

            return definitions;
        } catch (SessionException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }
    
    /**
     * Gives the set of definitions for all the dynamic groups in grouper.
     * @param session The Grouper session to use.
     * @return The set of definitions.
     */
    public Set<DynamicGroupDefinition> getAllDynamicGroupDefinitionsInternal(final GrouperSession session) {

        final String grouperDynamicType = grouperParameters.getGrouperType();
        final String definitionField = grouperParameters.getGrouperDefinitionField();
        final Set<DynamicGroupDefinition> definitions = new HashSet<DynamicGroupDefinition>();
        final GroupType type = retrieveType(grouperDynamicType);
        if (type != null) {
            final Set<Group> groups = GroupFinder.findAllByType(session, type);
            for (Group group : groups) {
                String membersDef = null;
                try {
                    membersDef = group.getAttributeValue(definitionField,true,true);
                    definitions.add(new DynamicGroupDefinition(group.getUuid(), membersDef));
                } catch (AttributeNotFoundException e) {
                    LOGGER.error("Unable to retrieve the attribute "  
                            + definitionField + " for the group: " + group.getName() + ".");
                }
            }
        } else {
            LOGGER.error("Error unable to retrieve the dynamic type: " + grouperDynamicType);
        }
        return definitions;
    }

}
