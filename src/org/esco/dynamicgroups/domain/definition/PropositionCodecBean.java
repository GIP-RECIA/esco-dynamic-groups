/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;


import org.esco.dynamicgroups.domain.beans.II18NManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Used to code and decode a String that represents an IProposition.
 * @author GIP RECIA - A. Deman
 * 16 janv. 2009
 *
 */
public class PropositionCodecBean extends PropositionCodec implements InitializingBean {
    
    /** Serial Version UID.*/
    private static final long serialVersionUID = -6173365831189113977L;

    /**
     * Builds an instance of PropositionCodec.
     */
    public PropositionCodecBean() {
        super();
    }
    
    /**
     * Builds an instance of PropositionCodec.
     * @param atomValidator The atom validator to use.
     * @param i18n The I18N manager.
     */
    public PropositionCodecBean(final IAtomicPropositionValidator atomValidator, 
            final II18NManager i18n) {
        super(atomValidator, i18n);
    }
    

    /**
     * Checks the bean injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(getAtomValidator(), 
                "The property atomValidator in the class " + getClass().getName()
                + " can't be null.");
        
        Assert.notNull(getI18n(), 
                "The property i18n in the class " + getClass().getName()
                + " can't be null.");
    }
    
    
    
}
