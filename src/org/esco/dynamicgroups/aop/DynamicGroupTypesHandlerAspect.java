/**
 * 
 */
package org.esco.dynamicgroups.aop;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupType;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.codehaus.aspectwerkz.joinpoint.CodeRtti;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.esco.dynamicgroups.DynamicGroupsMapping;




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

    /**
     * Constructor for DynamicGroupTypesHandlerAspect.
     */
    public DynamicGroupTypesHandlerAspect() {
       LOGGER.info("Creation of an aspect of class: " + getClass().getSimpleName());
    }
    
    /**
     * Handles the custom types that corresponds to a dynamic type.
     * @param joinPoint The join point.
     * @throws Throwable if there is an error during the invocation of the code associated
     * to the join point.
     * @return The result of the underlying join point execution.
     */
    public Object handleCustomType(final JoinPoint joinPoint) throws Throwable {
        final Object result = joinPoint.proceed();
        
            
        final Group group = (Group) joinPoint.getCaller();
        final GroupType type = (GroupType) ((CodeRtti) joinPoint.getRtti()).getParameterValues()[0];
        final String typeName = type.getName();
        LOGGER.debug("Adding custom type " + typeName 
                + "for group: " + group.getName() 
                + " dynamic? " + DynamicGroupsMapping.instance().containsDefinitionForGroupType(typeName));
        return result;
    }

}
