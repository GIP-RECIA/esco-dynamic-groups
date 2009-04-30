/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * Manager for the internationalization.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public class I18NManager implements Serializable, ApplicationContextAware, InitializingBean {

    /** Serial version UIDs.*/
    private static final long serialVersionUID = 8053619606448404446L;

    /** Used to build the default values. */
    private static final String UNDEF_I18N =  "Undefined I18n entry for the key: ";
    
    /** The user parameters. */
    private ESCODynamicGroupsParameters parameters;
    
    /** The application context ued to handle the I18n. */
    private ApplicationContext appCtx;
    
    
    /**
     * Builds an instance of I18NManager.
     */
    public I18NManager() {
        super();
    }
    /**
     * Checks the properties after the Spring injection.
     * @throws Exception If one of the injected property is null.
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.parameters, 
                "The property parameters in the class " + this.getClass().getName() 
                + " can't be null.");
        
    }
    
    /**
     * @param ctx The application context.
     * @throws BeansException
     * @see org.springframework.context.ApplicationContextAware#
     * setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(final ApplicationContext ctx)
            throws BeansException {
        appCtx = ctx;
        
    }
    
    /**
     * Gives the messge that corresponds to an i18n key.
     * @param key The i18n key.
     * @return The message.
     */
    public String getI18nMessage(final String key) {
        return appCtx.getMessage(key, null, UNDEF_I18N + key, parameters.getLocale());
    }
    /**
     * Getter for parameters.
     * @return parameters.
     */
    public ESCODynamicGroupsParameters getParameters() {
        return parameters;
    }
    /**
     * Setter for parameters.
     * @param parameters the new value for parameters.
     */
    public void setParameters(final ESCODynamicGroupsParameters parameters) {
        this.parameters = parameters;
    }
    
}
