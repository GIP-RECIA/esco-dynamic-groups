/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.util.Set;

import org.esco.dynamicgroups.domain.parameters.IDynamicAttributesProvider;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Checks that the attribute in an atomic proposition corresponds to
 * the allowed ones. This class add the support for spring bean injection.
 * @author GIP RECIA - A. Deman
 * 19 mai 2009
 *
 */
public class AtomicPropositionValidatorFromListBean extends AtomicPropositionValidatorFromList
    implements InitializingBean {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = -3715339692875717955L;
    
    /** The user parameters provider. */
    private ParametersProvider parametersProvider;
     
    /**
     * Builds an instance of AtomicPropositionValidatorFromListBean.
     */
    public AtomicPropositionValidatorFromListBean() {
        super();
    }

    /**
     * Checks the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(parametersProvider, "The property parametersProvider in the class " 
                + getClass().getName() + " can't be null.");
        
        Assert.notNull(getI18n(), "The property i18n in the class " 
                + getClass().getName() + " can't be null.");
        
        final Set<String> attributesSet = 
            ((IDynamicAttributesProvider) parametersProvider.getPersonsParametersSection()).getDynamicAttributes(); 
        setAllowedAttributes(attributesSet.toArray(new String[attributesSet.size()]));
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
    
   

}
