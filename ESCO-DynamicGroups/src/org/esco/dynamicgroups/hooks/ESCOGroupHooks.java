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
import java.util.Map;

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

    private static final String ATTRIBUTE_PREFIX = "attribute__";

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
        LOGGER.info("Creation of an hooks of class: " + getClass().getSimpleName());
        dynamicType = ESCODynamicGroupsParameters.instance().getGrouperType();
        definitionField = ESCODynamicGroupsParameters.instance().getGrouperDefinitionField();
        definitionFieldInternal = ATTRIBUTE_PREFIX + definitionField;
    }

    /**
     * Handles a dynamic group.
     * @param group The group to handle.
     */
    protected void handleDynamicGroup(final Group group) { 

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Handles dynamic type for the group: " + group.getName());
        }

        String membersDef = null;
        try {
            membersDef = group.getAttribute(definitionField);
        } catch (AttributeNotFoundException e) {
            LOGGER.error("Unable to retrieve the attribute " + definitionField 
                    + " for the group: " + group.getName() + ".");
        }

        final DynamicGroupDefinition groupDef = new DynamicGroupDefinition(group.getName(), membersDef);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The dynamic group " + group.getName() 
                    + " will be initialized with the definition: " + groupDef);
        }
        DomainRegistry.instance().getDomainService().handleNewOrModifiedDynamicGroup(groupDef);
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
        final String currentName = group.getAttributeOrNull(EXTENSION);
        final String previousName = group.dbVersion().getAttributeOrNull(EXTENSION);
        if (!currentName.equals(previousName)) {
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
                return true;
            }
        }
        return false;
    }
    
    

    /**
     * Tests if a group has to be handled, i.e. tests if the group has the dynamic type
     * and the extension or the field that contains the definition has changed.
     * @param group The group to test.
     * @return True if the group has to be handled as a dynamic group.
     */
    protected boolean groupHasTobeHandled(final Group group) {
        LOGGER.trace("Tests if the group has to be handled as a dynamic one.");
        
        if (!isDynamicGroup(group)) {
            return false;
        }
        final Group previous = group.dbVersion();
        final String currentName = group.getAttributeOrNull(EXTENSION);
        final String previousName = previous.getAttributeOrNull(EXTENSION);
        boolean result = false;
        if (!currentName.equals(previousName)) {
            LOGGER.info(" EXTENSION has changed: current: " + currentName 
                    + " previous: " + previousName);
            result = true;
        }

        final String currentDynAtt = group.getAttributeOrNull(definitionField);
        final String previousDynAtt = previous.getAttributeOrNull(definitionField);

        if (!currentDynAtt.equals(previousDynAtt)) {
            LOGGER.info(" Definition field changed");
            result = true;
        }
        return result;

    }

    /**
     * Displays the differences between the current state of a group and its previous state.
     * @param label  A label to use for the logs.
     * @param g The group.
     */
protected void displayDifferences(final String label, final Group g) {
        LOGGER.info("---------" + label + "-------------");
        LOGGER.info("   g.dbVersionIsDifferent(): " + g.dbVersionIsDifferent());
        LOGGER.info("  g.dbVersionDifferentFields(true): " + g.dbVersionDifferentFields(true));
        final Map<String, String> attributes = g.getAttributes();
        LOGGER.info("Current attributes: ");
        for (String key : attributes.keySet()) {
            LOGGER.debug("   " + key + " = " + attributes.get(key));
        }

        final Map<String, String> prevAttributes = g.dbVersion().getAttributes();
        LOGGER.info("Previous attributes: ");
        for (String key : prevAttributes.keySet()) {
            LOGGER.debug("   " + key + " = " + prevAttributes.get(key));
        }

        LOGGER.info("----------------------");
    }


    /**
     * @param hooksContext
     * @param postDeleteBean
     * @see edu.internet2.middleware.grouper.hooks.GroupHooks#groupPostDelete(HooksContext, HooksGroupBean)
     */
    @Override
    public void groupPostDelete(final HooksContext hooksContext, final HooksGroupBean postDeleteBean) {
        final Group group = postDeleteBean.getGroup();

        if (groupHasTobeHandled(group)) {
            DomainRegistry.instance().getDomainService().handleDeletedGroup(group.getName());
        }
    }

    /**
     * @param hooksContext
     * @param preUpdateBean
     * @see edu.internet2.middleware.grouper.hooks.GroupHooks#groupPreUpdate(HooksContext, HooksGroupBean)
     */
    @Override
    public void groupPreUpdate(final HooksContext hooksContext, final HooksGroupBean preUpdateBean) {
        LOGGER.trace("groupPreUpdate");
        
        displayDifferences("groupPreUpdate", preUpdateBean.getGroup());
//      final Group group = preUpdateBean.getGroup();
//
//      if (groupHasTobeHandled(group)) {
//          handleDynamicGroup(group);
//      }
    }


    /**
     * @param hooksContext
     * @param postUpdateBean
     * @see edu.internet2.middleware.grouper.hooks.GroupHooks#groupPostUpdate(HooksContext, HooksGroupBean)
     */
    @Override
    public void groupPostUpdate(final HooksContext hooksContext, final HooksGroupBean postUpdateBean) {
        LOGGER.trace("groupPostUpdate");
        displayDifferences("groupPostUpdate", postUpdateBean.getGroup());
        LOGGER.info(" isExtensionUpdate: " + isExtensionUpdate(postUpdateBean.getGroup()));
        LOGGER.info(" isDefinitionUpdate: " + isDefinitionUpdate(postUpdateBean.getGroup()));
    }

  

}
