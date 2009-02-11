/**
 * 
 */
package org.esco.dynamicgroups.hooks;

import edu.internet2.middleware.grouper.hooks.FieldHooks;
import edu.internet2.middleware.grouper.hooks.beans.HooksContext;
import edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean;

/**
 * @author GIP RECIA - A. Deman
 * 10 févr. 2009
 *
 */
public class ESCOFieldHooks extends FieldHooks {

    /**
     * @param hooksContext
     * @param postCommitDeleteBean
     * @see edu.internet2.middleware.grouper.hooks.FieldHooks#fieldPostCommitDelete(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean)
     */
    @Override
    public void fieldPostCommitDelete(HooksContext hooksContext,
            HooksFieldBean postCommitDeleteBean) {
        // TODO Auto-generated method stub
        super.fieldPostCommitDelete(hooksContext, postCommitDeleteBean);
    }

    /**
     * @param hooksContext
     * @param postCommitInsertBean
     * @see edu.internet2.middleware.grouper.hooks.FieldHooks#fieldPostCommitInsert(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean)
     */
    @Override
    public void fieldPostCommitInsert(HooksContext hooksContext,
            HooksFieldBean postCommitInsertBean) {
        // TODO Auto-generated method stub
        super.fieldPostCommitInsert(hooksContext, postCommitInsertBean);
    }

    /**
     * @param hooksContext
     * @param postCommitUpdateBean
     * @see edu.internet2.middleware.grouper.hooks.FieldHooks#fieldPostCommitUpdate(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean)
     */
    @Override
    public void fieldPostCommitUpdate(HooksContext hooksContext,
            HooksFieldBean postCommitUpdateBean) {
        // TODO Auto-generated method stub
        super.fieldPostCommitUpdate(hooksContext, postCommitUpdateBean);
    }

    /**
     * @param hooksContext
     * @param postDeleteBean
     * @see edu.internet2.middleware.grouper.hooks.FieldHooks#fieldPostDelete(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean)
     */
    @Override
    public void fieldPostDelete(HooksContext hooksContext,
            HooksFieldBean postDeleteBean) {
        // TODO Auto-generated method stub
        super.fieldPostDelete(hooksContext, postDeleteBean);
    }

    /**
     * @param hooksContext
     * @param postInsertBean
     * @see edu.internet2.middleware.grouper.hooks.FieldHooks#fieldPostInsert(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean)
     */
    @Override
    public void fieldPostInsert(HooksContext hooksContext,
            HooksFieldBean postInsertBean) {
        // TODO Auto-generated method stub
        super.fieldPostInsert(hooksContext, postInsertBean);
    }

    /**
     * @param hooksContext
     * @param postUpdateBean
     * @see edu.internet2.middleware.grouper.hooks.FieldHooks#fieldPostUpdate(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean)
     */
    @Override
    public void fieldPostUpdate(HooksContext hooksContext,
            HooksFieldBean postUpdateBean) {
        // TODO Auto-generated method stub
        super.fieldPostUpdate(hooksContext, postUpdateBean);
    }

    /**
     * @param hooksContext
     * @param preDeleteBean
     * @see edu.internet2.middleware.grouper.hooks.FieldHooks#fieldPreDelete(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean)
     */
    @Override
    public void fieldPreDelete(HooksContext hooksContext,
            HooksFieldBean preDeleteBean) {
        // TODO Auto-generated method stub
        super.fieldPreDelete(hooksContext, preDeleteBean);
    }

    /**
     * @param hooksContext
     * @param preInsertBean
     * @see edu.internet2.middleware.grouper.hooks.FieldHooks#fieldPreInsert(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean)
     */
    @Override
    public void fieldPreInsert(HooksContext hooksContext,
            HooksFieldBean preInsertBean) {
        // TODO Auto-generated method stub
        super.fieldPreInsert(hooksContext, preInsertBean);
    }

    /**
     * @param hooksContext
     * @param preUpdateBean
     * @see edu.internet2.middleware.grouper.hooks.FieldHooks#fieldPreUpdate(edu.internet2.middleware.grouper.hooks.beans.HooksContext, edu.internet2.middleware.grouper.hooks.beans.HooksFieldBean)
     */
    @Override
    public void fieldPreUpdate(HooksContext hooksContext,
            HooksFieldBean preUpdateBean) {
        // TODO Auto-generated method stub
        super.fieldPreUpdate(hooksContext, preUpdateBean);
    }

}
