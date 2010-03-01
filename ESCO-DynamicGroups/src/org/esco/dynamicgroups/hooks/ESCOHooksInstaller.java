/**
 * 
 */
package org.esco.dynamicgroups.hooks;

import edu.internet2.middleware.grouper.hooks.logic.GrouperHookType;
import edu.internet2.middleware.grouper.hooks.logic.GrouperHooksUtils;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Installs the hooks when Spring initialization stage is finished.
 * @author GIP RECIA - A. Deman
 * 12 févr. 2009
 *
 */
public class ESCOHooksInstaller implements Serializable, ApplicationListener {

    /** Serial version UID.*/
    private static final long serialVersionUID = 5302099526585134702L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCOHooksInstaller.class); 
    
    /** Initialization flag. */
    private transient boolean initialized;
    
    /**
     * Builds an instance of ESCOHooksInstaller.
     */
    public ESCOHooksInstaller() {
        super();
    }
    
    /**
     * Listen for an application event.
     * @param event The event.
     * @see org.springframework.context.ApplicationListener#
     * onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    public void onApplicationEvent(final ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            if (!initialized) {
                LOGGER.info("Installing the group hooks " + ESCOGroupHooks.class);
                GrouperHooksUtils.addHookManual(GrouperHookType.GROUP.getPropertyFileKey(), ESCOGroupHooks.class);
                LOGGER.info("Installing the attributes hooks " + ESCOAttributeHooks.class);
                GrouperHooksUtils.addHookManual(GrouperHookType.ATTRIBUTE.getPropertyFileKey(), 
                        ESCOAttributeHooks.class);
                initialized = true;
            }
        }
    }
}
