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
import org.esco.dynamicgroups.domain.DomainServiceProviderForHooks;
import org.esco.dynamicgroups.domain.IDomainService;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ParametersProviderForHooks;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.esco.dynamicgroups.domain.reporting.statistics.StatisticsManagerProviderForHooks;

/**
 * Dynamic groups hook.
 * This hook managed the creation of the dynamic groups.
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

    /** The statistics manager.*/
    private IStatisticsManager statisticsManager;

    /** The domain service to use to handle thy operations associated to the dynamic groups. */
    private IDomainService domainService;

    /**
     * Builds an instance of ESCOGroupHooks.
     */
    public ESCOGroupHooks() {
        final ParametersProvider parametersProvider = ParametersProviderForHooks.instance().getParametersProvider();
        
        final GroupsParametersSection grouperParameters = 
            (GroupsParametersSection) parametersProvider.getGroupsParametersSection();
        dynamicType = grouperParameters.getGrouperType();
        definitionField = grouperParameters.getGrouperDefinitionField();
        definitionFieldInternal = ATTRIBUTE_PREFIX + definitionField;
        statisticsManager = StatisticsManagerProviderForHooks.instance().getStatisticsManager();
        domainService = DomainServiceProviderForHooks.instance().getDomainService();

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
     * Post commit insert hook point.
     * @param hooksContext The hook context.
     * @param postCommitInsertBean The available Grouper information.
     * @see edu.internet2.middleware.grouper.hooks.GroupHooks#groupPostCommitInsert(HooksContext, HooksGroupBean)
     */
    @Override
    public void groupPostCommitInsert(final HooksContext hooksContext, 
            final HooksGroupBean postCommitInsertBean) {

        final Group group = postCommitInsertBean.getGroup();

        if (isDynamicGroup(group)) {
            statisticsManager.handleCreatedGroup(group.getName());
        }
    }

    /**
     * Post commit Delete hook point.
     * @param hooksContext The hook context.
     * @param postCommitDeleteBean The availbale Grouper information.
     * @see edu.internet2.middleware.grouper.hooks.GroupHooks#groupPostCommitDelete(HooksContext, HooksGroupBean)
     */
    @Override
    public void groupPostCommitDelete(final HooksContext hooksContext, 
            final HooksGroupBean postCommitDeleteBean) {

        final Group group = postCommitDeleteBean.getGroup();

        if (isDynamicGroup(group)) {
            domainService.handleDeletedGroup(group.getName());
            statisticsManager.handleDeletedGroup(group.getName());
        }

    }

    /**
     * Pre update hook point.
     * @param hooksContext The hook context.
     * @param preUpdateBean The available Grouper information.
     * @see edu.internet2.middleware.grouper.hooks.GroupHooks#groupPreUpdate(HooksContext, HooksGroupBean)
     */
    @Override
    public void groupPreUpdate(final HooksContext hooksContext, final HooksGroupBean preUpdateBean) {
        final Group group = preUpdateBean.getGroup();
        if (isDynamicGroup(group)) {
            if (isDefinitionUpdate(group)) {
                final String newDefinitionAtt = PropositionCodec.instance().nomalize(getDefinitionAttribute(group));
                if (newDefinitionAtt != null) {
                    final String prevDefinitionAtt = domainService.getMembershipExpression(group.getUuid());
                    
                    if (newDefinitionAtt.equalsIgnoreCase(prevDefinitionAtt)) {
                        LOGGER.debug("Nothing to do, previous definion is equal to the new one. Previous: " 
                                + prevDefinitionAtt + " new: " + newDefinitionAtt + ".");
                    } else {
                        // The defition field of the dynamic group is modified.
                        LOGGER.debug("Modification of the definition field.");
                        final DynamicGroupDefinition def = buildDefinition(group.getUuid(), newDefinitionAtt);
                        domainService.handleNewOrModifiedDynamicGroup(def);
                    }
                }
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
     * Tests if the definiton field associated to a dynamic group is changed.
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
            statisticsManager.handleDefinitionModification(group.getName(), previousDefinition, currentDefinition);
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
     * Retrieves the definition attribute for a given dynamic group.
     * @param group The considered group.
     * @return The definition attribute.
     */
    protected String getDefinitionAttribute(final Group group) {
        try {
            return group.getAttribute(definitionField);
        } catch (AttributeNotFoundException e) {
            LOGGER.error("Unable to retrieve the attribute " + definitionField 
                    + " for the group: " + group.getName() + ".");
        }
        return null;
    }

    /**
     * Builds the definition of a dynamic group.
     * @param group The group associated to the dynamic group definition. 
     * @return The Dynamic group definition.
     */
    protected DynamicGroupDefinition buildDefinition(final Group group) {
        return buildDefinition(group.getUuid(), getDefinitionAttribute(group));
    }

    /**
     * Builds the definition of a dynamic group.
     * @param groupUUID The uuid of the group.
     * @param definition The logic expresion of the memberships.
     * @return The dynamic group definition.
     */
    protected DynamicGroupDefinition buildDefinition(final String groupUUID, final String definition) {
        final DynamicGroupDefinition groupDef = new DynamicGroupDefinition(groupUUID, definition);

        if (LOGGER.isDebugEnabled()) {
            final StringBuilder sb = new StringBuilder("Dynamic group definition built: ");
            sb.append(groupDef);
            LOGGER.debug(sb.toString());
        }

        return groupDef;
    }


}
