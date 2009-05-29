/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;

import java.io.Serializable;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Used to provides the user parameters to the hooks.
 * @author GIP RECIA - A. Deman
 * 29 mai 2009
 *
 */
public class ParametersProviderForHooks implements Serializable, InitializingBean {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = 4395330602220542934L;
    
  
    /** The available instance. */
    private static ParametersProviderForHooks instance;
    
    /** The instance of parameters to provide. */
    private ParametersProvider parametersProvider;
    
    /**
     * Builds an instance of ParametersProviderForHooks.
     */
    public ParametersProviderForHooks() {
        instance = this;
    }
    
    /**
     * Gives the available instance.
     * @return The instance.
     */
    public static ParametersProviderForHooks instance() {
        return instance;
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
     * Checks the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.parametersProvider, "The property parametersProvider in the class"
                + getClass() + " can't be null.");
    }

}
