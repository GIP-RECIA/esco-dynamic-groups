/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.esco.dynamicgroups.domain.parameters.IDynamicAttributesProvider;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
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
    
    /** Key for an invalid attribute. */
    private static final String ATTRIBUTE_NOT_IN_LIST = "proposition.decoding.error.attribute.notinlist";
    
    /** The user parameters provider. */
    private ParametersProvider parametersProvider;
    
    /** The i18n manager. */
    private I18NManager i18n;
    
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
        Assert.notNull(parametersProvider, "The property parametersProvider in the class " 
                + getClass().getName() + " can't be null.");
        
        Assert.notNull(i18n, "The property i18n in the class " 
                + getClass().getName() + " can't be null.");
        
        final Set<String> ldapAttributesSet = 
            ((IDynamicAttributesProvider) parametersProvider.getPersonsParametersSection()).getDynamicAttributes(); 
        ldapAttributes = ldapAttributesSet.toArray(new String[ldapAttributesSet.size()]);
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
     * Explains why an atom is invalid.
     * @param invalidAtom The invalid atom.
     * @return The explanation.
     * @see org.esco.dynamicgroups.domain.definition.IAtomicPropositionValidator#explainInvalidAtom(AtomicProposition)
     */
    public String explainInvalidAtom(final AtomicProposition invalidAtom) {
        return i18n.getI18nMessage(ATTRIBUTE_NOT_IN_LIST, invalidAtom.getAttribute(), Arrays.toString(ldapAttributes));
    }

    /**
     * Getter for i18n.
     * @return i18n.
     */
    public I18NManager getI18n() {
        return i18n;
    }

    /**
     * Setter for i18n.
     * @param i18n the new value for i18n.
     */
    public void setI18n(final I18NManager i18n) {
        this.i18n = i18n;
    }

}
