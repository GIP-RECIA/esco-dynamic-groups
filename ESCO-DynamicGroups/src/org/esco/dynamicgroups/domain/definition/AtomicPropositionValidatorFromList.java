/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.io.Serializable;
import java.util.Set;

import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Checks that the attribute in an atomic proposition corresponds to
 * the ones associated to the SyncRepl request.
 * @author GIP RECIA - A. Deman
 * 19 mai 2009
 *
 */
public class AtomicPropositionValidatorFromList 
    implements IAtomicPropositionValidator, Serializable, InitializingBean {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = -3715339692875717955L;
    
    /** The user parameters. */
    private ESCODynamicGroupsParameters parameters;
    
    /** The LDAP search attributes to use. */
    private String[] ldapAttributes;
    
    /**
     * Builds an instance of AtomicPropositionValidatorFromList.
     */
    public AtomicPropositionValidatorFromList() {
        super();
    }

    /**
     * Checks the atom.
     * @param atom The atom to check.
     * @return True if the attribute in the atom is one of the LDAP attributes
     * of the SyncRepl request.
     * @see org.esco.dynamicgroups.domain.definition.IAtomicPropositionValidator#isValid(AtomicProposition)
     */
    @Override
    public boolean isValid(final AtomicProposition atom) {
        for (String attribute : ldapAttributes) {
            if (attribute.equals(atom.getAttribute())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(parameters, "The property parameters in the class " 
                + getClass().getName() + " can't be null.");
        
        final Set<String> ldapAttributesSet = parameters.getAttributesForDynamicDefinition(); 
        ldapAttributes = ldapAttributesSet.toArray(new String[ldapAttributesSet.size()]);
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
