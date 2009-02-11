/**
 * 
 */
package org.esco.dynamicgroups.aop;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.exception.AttributeNotFoundException;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.codehaus.aspectwerkz.joinpoint.CodeRtti;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.esco.dynamicgroups.domain.IDomainService;
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;





/**
 * Aspect used to handles Custom Grouper groups that are dynamic groups.
 * @author GIP RECIA - A. Deman
 * 29 avr. 08
 *
 */
public class DynamicGroupTypesHandlerAspect implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 8423935858631963788L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DynamicGroupTypesHandlerAspect.class);

    /** The dynamic type to use. */
    private String dynamicType;

    /** The definition field for the dynamic groups. */
    private String definitionField;

    /** The dynamic group initializer. */
    private IDomainService domainService;

    /**
     * Constructor for DynamicGroupTypesHandlerAspect.
     */
    public DynamicGroupTypesHandlerAspect() {
        LOGGER.info("Creation of an aspect of class: " + getClass().getSimpleName());
        dynamicType = ESCODynamicGroupsParameters.instance().getGrouperType();
        definitionField = ESCODynamicGroupsParameters.instance().getGrouperDefinitionField();
    }

    /**
     * Retrieves the group service to use.
     * @param joinPoint The join point.
     */
    public void retrieveDomainService(final JoinPoint joinPoint) {
        domainService = (IDomainService) joinPoint.getCallee();
        LOGGER.debug("Domain service retrieved.");
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
        domainService.handleNewOrModifiedDynamicGroup(groupDef);
    }

    /**
     * Handles the attribute that corresponds to the members definition.
     * @param joinPoint The join point.
     */
    public void handleCustomTypeAttribute(final JoinPoint joinPoint) {
        
        // Tests if the modification has to be handled. 
        final Object[] values = ((CodeRtti) joinPoint.getRtti()).getParameterValues();
        boolean handle = false;
        if (values.length > 0) {
            handle = values[0].equals(definitionField);
        } 
        if (handle) {
            final Group group = (Group) joinPoint.getCaller();
            handleDynamicGroup(group);
        }

    }


    /**
     * Handles the custom types that corresponds to a dynamic type.
     * @param joinPoint The join point.
     */
    public void handleCustomType(final JoinPoint joinPoint) {

        // Tests if the modification has to be handled. 
        final Object[] values = ((CodeRtti) joinPoint.getRtti()).getParameterValues();
        boolean handle = false;
        if (values.length > 0) {
            if (values[0] instanceof GroupType) {
                handle = dynamicType.equals(((GroupType) values[0]).getName());
            }
        } 

        if (handle) {
            final Group group = (Group) joinPoint.getCaller();
            handleDynamicGroup(group);
        }
    }
}
