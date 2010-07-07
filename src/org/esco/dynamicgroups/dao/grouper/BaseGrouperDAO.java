/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GroupTypeFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.exception.GroupNotFoundException;
import edu.internet2.middleware.grouper.exception.SchemaException;

import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;

/**
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
public abstract class BaseGrouperDAO implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 6126692856846983220L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(BaseGrouperDAO.class);
    

    /**
     * Retrieves a Group from its name.
     * @param session The Grouper session to use.
     * @param groupUUID The uuid of the group to retrieve.
     * @return The group if it can be retrieved, null oterwise.
     */
    protected Group retrieveGroup(final GrouperSession session, final String groupUUID) {
        Group group = null;
        try {
            group = GroupFinder.findByUuid(session, groupUUID,true);
        } catch (GroupNotFoundException e) {
            LOGGER.warn("The group: " + groupUUID + " can't be retrieved from Grouper.");
        }
        return group;
    }
    
    /**
     * Try to retrieve a Grouper Type.
     * @param typeName The name of the type to retrieve.
     * @return The Grouper Type if found, null otherwise.
     */
    protected GroupType retrieveType(final String typeName) {
        GroupType type;
        try {
            type = GroupTypeFinder.find(typeName,true);
        } catch (SchemaException e) {
            LOGGER.fatal(e, e);
            type = null;
        }

        return type;
    }
    
    /**
     * Tests if a group has a dynamic type.
     * @param group The group to test.
     * @param grouperParameters The grouper parameters.
     * @return True if the dynamic type is found in the types of the group.
     */
    protected boolean isDynamicGroup(final Group group, final GroupsParametersSection grouperParameters ) {
        final Set<GroupType> types = group.getTypes();

        for (GroupType type : types) {
            if (grouperParameters.getGrouperType().equals(type.getName())) {
                return true;
            }
        }
        return false;
    }
}
