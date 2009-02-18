/**
 * 
 */
package org.esco.dynamicgroups.hooks;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.exception.AttributeNotFoundException;
import edu.internet2.middleware.grouper.hooks.GroupHooks;
import edu.internet2.middleware.grouper.hooks.beans.HooksContext;
import edu.internet2.middleware.grouper.hooks.beans.HooksGroupBean;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.DomainRegistry;
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;

/**
 * @author GIP RECIA - A. Deman
 * 9 févr. 2009
 *
 */
public class ESCOGroupHooks extends GroupHooks implements Serializable {

    /** Prefixe used by grouper for the attributes in the changed fields. */
    private static final String ATTRIBUTE_PREFIX = "attribute__";

    /** Extension field in Grouper. */ 
    private static final String EXTENSION = "extension";

    /** Serial version UID. */
    private static final long serialVersionUID = 8623167847413079614L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCOGroupHooks.class);

    /** The dynamic type to use. */
    private String dynamicType;

    /** The definition field for the dynamic groups. */
    private String definitionField;

    /** The grouper internal name for the definiton field. */
    private String definitionFieldInternal;

    /**
     * Builds an instance of ESCOGroupHooks.
     */
    public ESCOGroupHooks() {
        dynamicType = ESCODynamicGroupsParameters.instance().getGrouperType();
        definitionField = ESCODynamicGroupsParameters.instance().getGrouperDefinitionField();
        definitionFieldInternal = ATTRIBUTE_PREFIX + definitionField;

        if (LOGGER.isInfoEnabled()) {
            final StringBuilder sb = new StringBuilder("Creation of an hooks of class: ");
            sb.append(getClass().getSimpleName());
            sb.append(" - dynamic type: ");
            sb.append(dynamicType);
            sb.append(" Definition field: ");
            sb.append(definitionField);
            sb.append(".");
            LOGGER.info(sb.toString());
        }
    }

    /**
     * @param hooksContext
     * @param postDeleteBean
     * @see edu.internet2.middleware.grouper.hooks.GroupHooks#groupPostDelete(HooksContext, HooksGroupBean)
     */
    @Override
    public void groupPostDelete(final HooksContext hooksContext, final HooksGroupBean postDeleteBean) {
        final Group group = postDeleteBean.getGroup();

        if (isDynamicGroup(group)) {
            DomainRegistry.instance().getDomainService().handleDeletedGroup(group.getName());
        }
    }


    /**
     * @param hooksContext
     * @param postUpdateBean
     * @see edu.internet2.middleware.grouper.hooks.GroupHooks#groupPostUpdate(HooksContext, HooksGroupBean)
     */
    @Override
    public void groupPostUpdate(final HooksContext hooksContext, final HooksGroupBean postUpdateBean) {

        final Group group = postUpdateBean.getGroup();

        if (isDynamicGroup(group)) {

            if (isExtensionUpdate(group)) {
                final String previousName = group.dbVersion().getName();

                // The extension of the group is modified.
                if (LOGGER.isDebugEnabled()) {
                    final String newName = group.getName();
                    final StringBuilder sb = new StringBuilder("Modification of the extension - Previous name: ");
                    sb.append(previousName);
                    sb.append(" - New name: ");
                    sb.append(newName);
                    LOGGER.debug(sb.toString());
                }

                DomainRegistry.instance().getDomainService().handleDeletedGroup(previousName);
                DomainRegistry.instance().getDomainService().handleNewOrModifiedDynamicGroup(buildDefinition(group));

            } else if (isDefinitionUpdate(group)) {

                // The defition field of the dynamic group is modified.
                LOGGER.debug("Modification of the definition field.");

                DomainRegistry.instance().getDomainService().handleNewOrModifiedDynamicGroup(buildDefinition(group));
            }
        }
    }

    /**
     * Tests if the extension of a group is modified.
     * @param group the group to test.
     * @return True if the extension of the group is modified.
     */
    protected boolean isExtensionUpdate(final Group group) {

        if (!group.dbVersionDifferentFields().contains(EXTENSION)) {
            return false;
        }

        final String currentExtension = group.getAttributeOrNull(EXTENSION);
        final String previousExtension = group.dbVersion().getAttributeOrNull(EXTENSION);

        if (!currentExtension.equals(previousExtension)) {
            return true;
        }
        return false;
    }

    /**
     * Test if the definiton field associated to a dynamic group is changed.
     * @param group The group.
     * @return true if the fied that contains the defintion of the group is changed.
     */
    protected boolean isDefinitionUpdate(final Group group) {

        if (!group.dbVersionDifferentFields().contains(definitionFieldInternal)) {
            return false;
        }
        final String currentDefinition = group.getAttributeOrNull(definitionField);
        final String previousDefinition = group.dbVersion().getAttributeOrNull(definitionField);
        if (!currentDefinition.equals(previousDefinition)) {
            return true;
        }
        return false;
    } 

    /**
     * Tests if a group is dynamic.
     * @param group The group to test.
     * @return True if the group has the dynamic type.
     */
    protected boolean isDynamicGroup(final Group group) {
        final Iterator<GroupType> it = group.getTypes().iterator(); 
        while (it.hasNext()) {
            if (dynamicType.equals(it.next().getName())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("The group " + group.getName() + " is dynamic.");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Builds the definition of a dynamic group.
     * @param group The group associated to the dynamic group definition. 
     * @return The Dynamic group definition.
     */
    protected DynamicGroupDefinition buildDefinition(final Group group) {

        String membersDef = null;
        try {
            membersDef = group.getAttribute(definitionField);
        } catch (AttributeNotFoundException e) {
            LOGGER.error("Unable to retrieve the attribute " + definitionField 
                    + " for the group: " + group.getName() + ".");
        }

        final DynamicGroupDefinition groupDef = new DynamicGroupDefinition(group.getName(), membersDef);

        if (LOGGER.isDebugEnabled()) {
            final StringBuilder sb = new StringBuilder("Dynamic group definition built: ");
            sb.append(groupDef);
            LOGGER.debug(sb.toString());
        }

        return groupDef;
    }


}
