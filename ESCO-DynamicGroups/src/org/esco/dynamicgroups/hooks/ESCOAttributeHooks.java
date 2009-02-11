/**
 * 
 */
package org.esco.dynamicgroups.hooks;

import edu.internet2.middleware.grouper.hooks.AttributeHooks;
import edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean;
import edu.internet2.middleware.grouper.hooks.beans.HooksContext;

/**
 * @author GIP RECIA - A. Deman
 * 9 févr. 2009
 *
 */
public class ESCOAttributeHooks extends AttributeHooks {

    /**
     * @param hooksContext
     * @param postCommitDeleteBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePostCommitDelete(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean)
     */
    public void attributePostCommitDelete(HooksContext hooksContext,
            HooksAttributeBean postCommitDeleteBean) {
        System.out.println("attributePostCommitDelete");
    }

    /**
     * @param hooksContext
     * @param postCommitInsertBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePostCommitInsert(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean)
     */
    public void attributePostCommitInsert(HooksContext hooksContext,
            HooksAttributeBean postCommitInsertBean) {
        System.out.println("attributePostCommitInsert");
    }

    /**
     * @param hooksContext
     * @param postCommitUpdateBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePostCommitUpdate(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean)
     */
    public void attributePostCommitUpdate(HooksContext hooksContext,
            HooksAttributeBean postCommitUpdateBean) {
        System.out.println("attributePostCommitUpdate");
    }

    /**
     * @param hooksContext
     * @param postDeleteBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePostDelete(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean)
     */
    public void attributePostDelete(HooksContext hooksContext,
            HooksAttributeBean postDeleteBean) {
        System.out.println("attributePostDelete");
    }

    /**
     * @param hooksContext
     * @param postInsertBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePostInsert(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean)
     */
    public void attributePostInsert(HooksContext hooksContext,
            HooksAttributeBean postInsertBean) {
        System.out.println("attributePostInsert");
    }

    /**
     * @param hooksContext
     * @param postUpdateBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePostUpdate(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean)
     */
    public void attributePostUpdate(HooksContext hooksContext,
            HooksAttributeBean postUpdateBean) {
        System.out.println("attributePostUpdate");
    }

    /**
     * @param hooksContext
     * @param preDeleteBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePreDelete(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean)
     */
    public void attributePreDelete(HooksContext hooksContext,
            HooksAttributeBean preDeleteBean) {
        System.out.println("attributePreDelete");
    }

    /**
     * @param hooksContext
     * @param preInsertBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePreInsert(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean)
     */
    public void attributePreInsert(HooksContext hooksContext,
            HooksAttributeBean preInsertBean) {
        System.out.println("attributePreInsert");
    }

    /**
     * @param hooksContext
     * @param preUpdateBean
     * @see edu.internet2.middleware.grouper.hooks.AttributeHooks#attributePreUpdate(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean)
     */
    public void attributePreUpdate(HooksContext hooksContext,
            HooksAttributeBean preUpdateBean) {
        System.out.println("attributePreUpdate");
    }

}
