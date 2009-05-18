/**
 * 
 */
package org.esco.dynamicgroups.hooks;

import edu.internet2.middleware.grouper.hooks.AttributeHooks;
import edu.internet2.middleware.grouper.hooks.beans.HooksAttributeBean;
import edu.internet2.middleware.grouper.hooks.beans.HooksContext;
import edu.internet2.middleware.grouper.hooks.logic.HookVeto;

import org.apache.log4j.Logger;

/**
 * @author GIP RECIA - A. Deman
 * 18 mai 2009
 *
 */
public class ESCOAttributeHooks extends AttributeHooks {
    
    private static final Logger LOGGER = Logger.getLogger(ESCOAttributeHooks.class);

    /**
     * Builds an instance of ESCOAttributeHooks.
     */
    public ESCOAttributeHooks() {
        // TODO Auto-generated constructor stub
    }
    public void attributePreUpdate(final HooksContext hooksContext, final HooksAttributeBean preUpdateBean) {
       LOGGER.info("==> attributePreUpdate");
       LOGGER.info("==> preUpdateBean.getAttribute().getAttrName() " 
                + preUpdateBean.getAttribute().getAttrName());
       LOGGER.info("==> preUpdateBean.getAttribute().getValue() " 
                + preUpdateBean.getAttribute().getValue());
       throw new HookVeto("toto tutu trallalal", "Yeeeeaaaahhaaahha çaaa maaaaaaarche !!!!!");
        
        
    }

    /**
     * called right after a attribute update
     * @param hooksContext
     * @param postUpdateBean
     */
    public void attributePostUpdate(HooksContext hooksContext, HooksAttributeBean postUpdateBean) {
        LOGGER.info("==> attributePostUpdate");
    }
    
    public void attributePreInsert(HooksContext hooksContext, HooksAttributeBean preInsertBean) {
        LOGGER.info("==> attributePreInsert");
        LOGGER.info("==> preInsertBean.getAttribute().getAttrName() " 
                + preInsertBean.getAttribute().getAttrName());
        LOGGER.info("==> preInsertBean.getAttribute().getValue() " 
                + preInsertBean.getAttribute().getValue());
        
    }
    public void attributePostInsert(HooksContext hooksContext, HooksAttributeBean postInsertBean) {
        LOGGER.info("==> attributePostInsert");
    }
}
