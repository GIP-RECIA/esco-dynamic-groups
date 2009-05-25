/**
 * 
 */
package org.esco.dynamicgroups.hooks;

import edu.internet2.middleware.grouper.Attribute;
import edu.internet2.middleware.grouper.hooks.AttributeHooks;
import edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean;
import edu.internet2.middleware.grouper.hooks.beans.HooksContext;
import edu.internet2.middleware.grouper.hooks.logic.HookVeto;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.esco.dynamicgroups.domain.definition.DecodedPropositionResult;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;

/**
 * Hooks to handle the field associated to the dynamic group type.
 * @author GIP RECIA - A. Deman
 * 18 mai 2009
 *
 */
public class ESCOAttributeHooks extends AttributeHooks {
    
    /** The logger to use. */
    private static final Logger LOGGER = Logger.getLogger(ESCOAttributeHooks.class);

    /** The definition field. */
    private String definitionField;

    /**
     * Builds an instance of ESCOAttributeHooks.
     */
    public ESCOAttributeHooks() {
        definitionField = ESCODynamicGroupsParameters.instance().getGrouperDefinitionField();
    }

    /**
     * Tests and veto if needed a modification of an attribute that contains the logic definition of a group.
     * @param hooksContext The hook context.
     * @param preUpdateBean The available Grouper informations.
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePreUpdate(HooksContext, HooksAttributeBean)
     */
    @Override
    public void attributePreUpdate(final HooksContext hooksContext, final HooksAttributeBean preUpdateBean) {
        final Attribute attribute = preUpdateBean.getAttribute();
        final Attribute previous = (Attribute) attribute.dbVersion();
        LOGGER.debug("att =>  " + attribute);
        LOGGER.debug("prev =>  " + previous);
        
        checkAttribute(preUpdateBean.getAttribute());
    }
    
  
    /**
     * Tests if the coded proposition is valid.
     * @param attribute The attribute that contains the coded definition.
     */
    private void checkAttribute(final Attribute attribute) {
        if (definitionField.equals(attribute.getAttrName())) {
            final DecodedPropositionResult result = PropositionCodec.instance().decode(attribute.getValue());
            if (!result.isValid()) {
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
