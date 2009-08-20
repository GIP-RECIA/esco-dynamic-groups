/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;


import edu.internet2.middleware.grouper.Field;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.exception.SchemaException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;
import edu.internet2.middleware.grouper.privs.Privilege;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.exceptions.DynamicGroupsException;

/**
 * Callback used to check the dynamic type with a safe use of Grouper session.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
class CheckDynamicTypeCallback implements GrouperSessionHandler {

    /** The grouper DAO Instance.*/
    private CheckDynamicTypeDAO checkDynamicTypeDAO;

    /**
     * Builds an instance of ResetGroupMembersCallBack.
     * @param checkDynamicTypeDAO The instance of Grouper DAO.
     */
    public CheckDynamicTypeCallback(final CheckDynamicTypeDAO checkDynamicTypeDAO) {

        this.checkDynamicTypeDAO = checkDynamicTypeDAO;
    }

    /**
     * Calls the DAO method check the dynamic type.
     * @param session The Grouper session.
     * @return null.
     * @throws GrouperSessionException
     * @see edu.internet2.middleware.grouper.misc.GrouperSessionHandler#callback(GrouperSession)
     */
    public Object callback(final GrouperSession session)
    throws GrouperSessionException {
        checkDynamicTypeDAO.checkDynamicTypeInternal(session);
        return null;
    }

}


/**
 * DAO Used to check the Grouper's custom type associated to the dynamic groups.
 * @author GIP RECIA - A. Deman
 * 10 août 2009
 *
 */
public class CheckDynamicTypeDAO extends BaseGrouperDAO {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = 7673846598777896337L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(CheckDynamicTypeDAO.class);
    
    /** The grouper parameters. */
    private GroupsParametersSection grouperParameters;

    /**
     * Builds an instance of CheckDynamicTypeDAO.
     * @param grouperParameters The parameters associated to the grouper backend.
     */
    public CheckDynamicTypeDAO(final  GroupsParametersSection grouperParameters) {
        this.grouperParameters = grouperParameters;
    }
    
    /**
     * Checks the dynamic types.
     * This method probably doesn't need to use the sessions callbacks.
     */
    public void checkDynamicType() {
        GrouperSession session = null;
        try {
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);
            GrouperSession.callbackGrouperSession(session, 
                    new CheckDynamicTypeCallback(this));
        } catch (SessionException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }
    
    /**
     * Checks the dynamic types.
     * @param session The grouperSession.
     */
    protected void checkDynamicTypeInternal(final GrouperSession session) {
        
        final boolean create = grouperParameters.getCreateGrouperType();
        LOGGER.info("Checking the dynamic type: " + grouperParameters.getGrouperType());
        final String definitionField = grouperParameters.getGrouperDefinitionField();
        final String grouperDynamicType = grouperParameters.getGrouperType();
        // Checks the group types.
        try {
            GroupType type = retrieveType(grouperDynamicType);
            
            if (type == null) {
                if (create) {
                    type = GroupType.createType(session, grouperDynamicType);
                    type.addAttribute(session, definitionField, Privilege.getInstance("read"), 
                            Privilege.getInstance("admin"), false);
                    LOGGER.info("Group type " + grouperDynamicType 
                            + " created with the field: " + definitionField + ".");
                } else {
                    LOGGER.error("The group type: " + grouperDynamicType 
                            + " can't be found and the configuration "
                            + " doesn't not allow to create it. Change the configuration or create it manually.");
                }
            } else {
                boolean defFieldFound = false;
                final Set<Field> fields = type.getFields();
                final Iterator<Field> it = fields.iterator();
                while (it.hasNext() && !defFieldFound) {
                    defFieldFound = it.next().getName().equals(definitionField);
                }
                if (!defFieldFound) {
                    LOGGER.error("The group type " + grouperDynamicType 
                            + " does not contain the definition field: " 
                            + definitionField
                            + ".");
                } else {
                    LOGGER.info("Group type " + grouperDynamicType + " with field " + definitionField + " is valid.");
                }
            }
        } catch (InsufficientPrivilegeException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        } catch (SchemaException e) {
            LOGGER.error(e, e);
            throw new DynamicGroupsException(e);
        }
    }

}
