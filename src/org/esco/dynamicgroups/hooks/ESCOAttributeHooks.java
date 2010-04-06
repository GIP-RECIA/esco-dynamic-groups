/**
 * 
 */
package org.esco.dynamicgroups.hooks;

import edu.internet2.middleware.grouper.Attribute;
import edu.internet2.middleware.grouper.hooks.AttributeHooks;
import edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean;
import edu.internet2.middleware.grouper.hooks.beans.HooksContext;
import edu.internet2.middleware.grouper.hooks.logic.HookVeto;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.DomainServiceProviderForHooks;
import org.esco.dynamicgroups.domain.IDomainService;
import org.esco.dynamicgroups.domain.definition.DecodedPropositionResult;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ParametersProviderForHooks;

/**
 * Hooks to handle the field associated to the dynamic group type.
 * @author GIP RECIA - A. Deman
 * 18 May 2009
 *
 */
public class ESCOAttributeHooks extends AttributeHooks implements Serializable {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = 9122480003264627999L;

    /** The logger to use. */
    private static final Logger LOGGER = Logger.getLogger(ESCOAttributeHooks.class);

    /** The definition field. */
    private String definitionField;
    
    /** The domain service, used to handle the operations associated to the dynamic groups. */
    private transient IDomainService domainService;


    /**
     * Builds an instance of ESCOAttributeHooks.
     */
    public ESCOAttributeHooks() {
        final ParametersProvider parametersProvider = ParametersProviderForHooks.instance().getParametersProvider();
        final GroupsParametersSection grouperParameters = 
            (GroupsParametersSection) parametersProvider.getGroupsParametersSection();
        definitionField = grouperParameters.getGrouperDefinitionField();
        domainService = DomainServiceProviderForHooks.instance().getDomainService();
        
        if (LOGGER.isInfoEnabled()) {
            final StringBuilder sb = new StringBuilder("Creation of an hooks of class: ");
            sb.append(getClass().getSimpleName());
            sb.append(" - Definition field: ");
            sb.append(definitionField);
            sb.append(".");
            LOGGER.info(sb.toString());
        }
    }

    /**
     * Tests and veto if needed a modification of an attribute that contains the logic definition of a group.
     * @param hooksContext The hook context.
     * @param preUpdateBean The available Grouper information.
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePreUpdate(HooksContext, HooksAttributeBean)
     */
    @Override
    public void attributePreUpdate(final HooksContext hooksContext, final HooksAttributeBean preUpdateBean) {
        checkAttribute(preUpdateBean.getAttribute());
    }
    

    /**
     * Handles the fact that a dynamic groups becomes a static group.
     * @param hooksContext The hook context.
     * @param postDeleteBean The available Grouper information.
     */
    @Override
    public void attributePostDelete(final HooksContext hooksContext, 
            final HooksAttributeBean postDeleteBean) {
        
        final Attribute attribute = postDeleteBean.getAttribute();
        
        if (definitionField.equals(attribute.getAttrName())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Dynamic group - attribute deleted for the group: " 
                        + attribute.getGroupUuid());
            }
            
            domainService.handleDeletedGroup(attribute.getGroupUuid());
        }
    }

  
    /**
     * Tests if the coded proposition is valid.
     * @param attribute The attribute that contains the coded definition.
     */
    private void checkAttribute(final Attribute attribute) {
        if (definitionField.equals(attribute.getAttrName())) {
            final DecodedPropositionResult result = PropositionCodec.instance().decode(attribute.getValue());
            if (!result.isValid()) {
                LOGGER.debug("Check Attribute: " + result.getErrorMessage());
                throw new HookVeto("", result.getErrorMessage());
            }

        }
    }
    

    /**
     * 
     * @param hooksContext
     * @param preInsertBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePreInsert(HooksContext, HooksAttributeBean)
     */
    @Override
    public void attributePreInsert(final HooksContext hooksContext, final HooksAttributeBean preInsertBean) {
        checkAttribute(preInsertBean.getAttribute());
    }
   
}
