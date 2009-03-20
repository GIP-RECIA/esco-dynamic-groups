/**
 * 
 */
package org.esco.dynamicgroups.domain.statistics;

import java.io.Serializable;

import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * Base class for the statistic elements.
 * @author GIP RECIA - A. Deman
 * 20 mars 2009
 *
 */
public abstract class BaseStatsEntry implements Serializable, ApplicationContextAware, InitializingBean {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = 5753717829592499493L;

    /** Used to build the default values. */
    private static final String UNDEF_I18N =  "Undefined I18n entry for the key: ";
    
    /** The user parameters. */
    private ESCODynamicGroupsParameters parameters;
    
    /**
     * Type of format for the outputs.
     * @author GIP RECIA - A. Deman
     * 20 mars 2009
     */
    public enum OutputFormat {
        /** Text format. */
        text, 
        
        /** XHTML Format. */
        xhtml 
    }
    
    /** The application context ued to handle the I18n. */
    private ApplicationContext appCtx;
    
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
     * Gives the label associated to the entry.
     * @param format The format of the output.
     * @return The string that contains the label for the entry
     * in the specified format.
     */
    public abstract String getLabel(final OutputFormat format);
    
    
    /**
     * Gives the string that represents the entry.
     * @param format The format of the output.
     * @return Tghe text that represents the entry in the specified format.
     */
    public abstract String getEntry(final OutputFormat format);
    
    /**
     * Resets the entry.
     */
    public abstract void reset();
    
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
    protected String getI18nMessage(final String key) {
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
