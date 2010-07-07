/**
 * 
 */
package org.esco.dynamicgroups.hooks;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.DomainServiceProviderForHooks;
import org.esco.dynamicgroups.domain.IDomainService;
import org.esco.dynamicgroups.domain.definition.DecodedPropositionResult;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ParametersProviderForHooks;

import edu.internet2.middleware.grouper.Attribute;
import edu.internet2.middleware.grouper.hooks.AttributeHooks;
import edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean;
import edu.internet2.middleware.grouper.hooks.beans.HooksContext;
import edu.internet2.middleware.grouper.hooks.logic.HookVeto;

/**
 * Hooks to handle the field associated to the dynamic group type.
 * @author GIP RECIA - A. Deman - T.Bizouerne
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
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("attribute modification detected");
    	}
    	/* 
    	 * If the attribute going to be modified is the attribute associated to the dynamic group type,
    	 * then we are updating the dynamic group definition 
    	 */
    	if (definitionField.equals(preUpdateBean.getAttribute().getAttrName())) {
        	if (LOGGER.isDebugEnabled()) {
        		LOGGER.debug("modification of the " +definitionField+ " attribute");
        	}
        	String previousValue = preUpdateBean.getAttribute().dbVersion().getValue();
        	String newValue = PropositionCodec.instance().nomalize(preUpdateBean.getAttribute().getValue());
        	
        	if (previousValue.equalsIgnoreCase((newValue))) {
        		if(LOGGER.isDebugEnabled()) {
        		LOGGER.debug("Nothing to do, previous definion is equal to the new one. Previous: " + previousValue + " new: " + newValue + ".");
        		}
        	}
        	else {
        		if(LOGGER.isDebugEnabled()) {
            		LOGGER.debug("Definition has changed. Previous: " + previousValue + " new: " + newValue + ".");
        		}
        		
        		if (newValue != null) {
        			// The defition field of the dynamic group has been normalized and is valid
        			final DynamicGroupDefinition def = buildDefinition(preUpdateBean.getAttribute().getGroupUuid(), newValue);
        			domainService.handleNewOrModifiedDynamicGroup(def);
        		}
        	}
    	}
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
     * Check if the attribute going to be inserted is the attribute related to our custom dynamic type. 
     * @param hooksContext
     * @param preInsertBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePreInsert(HooksContext, HooksAttributeBean)
     */
    @Override
    public void attributePreInsert(final HooksContext hooksContext, final HooksAttributeBean preInsertBean) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("attribute insertion detected");
    	}
    	/* 
    	 * If the attribute going to be inserted is the attribute associated to the dynamic group type,
    	 * then we are creating a dynamic group 
    	 */
    	if (definitionField.equals(preInsertBean.getAttribute().getAttrName())) {
        	if (LOGGER.isDebugEnabled()) {
        		LOGGER.debug("insertion of the " +definitionField+ " attribute");
        	}
    		final String newDefinitionAtt = PropositionCodec.instance().nomalize(preInsertBean.getAttribute().getValue());
    		if (newDefinitionAtt != null) {
    			// The defition field of the dynamic group has been normalized and is valid
    			// A DynamicGroupDefinition is built
                final DynamicGroupDefinition def = buildDefinition(preInsertBean.getAttribute().getGroupUuid(), newDefinitionAtt);
                // the new group is created using the definition (members are calculated and writen in grouper)
                domainService.handleNewOrModifiedDynamicGroup(def);
            }
    	}
    	/*
    	 * The attribute is then inserted in grouper.
    	 */
    }
    
    /**
     * Builds the definition of a dynamic group.
     * @param groupUUID The uuid of the group.
     * @param definition The logic expression of the memberships.
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