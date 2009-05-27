/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;

import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

/**
 * Manager for the internationalization.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public class I18NManager implements Serializable, InitializingBean {

    /** Serial version UIDs.*/
    private static final long serialVersionUID = 8053619606448404446L;

    /** Used to build the default values. */
    private static final String UNDEF_I18N =  "Undefined I18n entry for the key: ";
    
    /** The user parameters provider. */
    private ParametersProvider parametersProvider;
    
    /** The message source instance. */
    private MessageSource messageSource;

    
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
        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " + this.getClass().getName() 
                + " can't be null.");
        Assert.notNull(this.messageSource, 
                "The property messageSource in the class " + this.getClass().getName() 
                + " can't be null.");
        
    }
    
    
    /**
     * Gives the messge that corresponds to an i18n key.
     * @param key The i18n key.
     * @return The message.
     */
    public String getI18nMessage(final String key) {
        return getI18nMessage(key, (String[]) null);
    }
    /**
     * Gives the messge that corresponds to an i18n key.
     * @param key The i18n key.
     * @param args The arguments to include in the message.
     * @return The message.
     */
    public String getI18nMessage(final String key, final String...args) {
        return messageSource.getMessage(key, args, UNDEF_I18N + key, parametersProvider.getLocale());
    }
    /**
     * Getter for parametersProvider.
     * @return parametersProvider.
     */
    public ParametersProvider getParametersProvider() {
        return parametersProvider;
    }
    /**
     * Setter for parametersProvider.
     * @param parametersProvider the new value for parametersProvider.
     */
    public void setParametersProvider(final ParametersProvider parametersProvider) {
        this.parametersProvider = parametersProvider;
    }
    /**
     * Getter for messageSource.
     * @return messageSource.
     */
    public MessageSource getMessageSource() {
        return messageSource;
    }
    /**
     * Setter for messageSource.
     * @param messageSource the new value for messageSource.
     */
    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
}
