/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.io.Serializable;
import java.util.Arrays;

import org.esco.dynamicgroups.domain.beans.II18NManager;

/**
 * Checks that the attribute in an atomic proposition corresponds to
 * the allowed ones. This class add the support for i18n and the spring bean injection.
 * @author GIP RECIA - A. Deman
 * 19 mai 2009
 *
 */
public class AtomicPropositionValidatorFromList implements IAtomicPropositionValidator, Serializable  {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = -3715339692875717955L;
    
    /** Key for an invalid attribute. */
    private static final String ATTRIBUTE_NOT_IN_LIST = "proposition.decoding.error.attribute.notinlist";
   
    /** The i18n manager. */
    private II18NManager i18n;
    
    /** The attributes that can be used in a proposition. */
    private String[] allowedAttributes;
   
    /**
     * Builds an instance of AtomicPropositionValidatorFromListBean.
     */
    public AtomicPropositionValidatorFromList() {
        super();
    }
    /**
     * Builds an instance of AtomicPropositionValidatorFromListBean.
     * @param allowedAttributes The allowed attibutes.
     * @param i18n The Internationalization manager.
     */
    public AtomicPropositionValidatorFromList(final String[] allowedAttributes, final II18NManager i18n) {
        this.allowedAttributes = allowedAttributes;
        this.i18n = i18n;
    }
    /**
     * Checks the atom.
     * @param atom The atom to check.
     * @return True if the attribute in the atom is one of alloawed ones.
     * @see org.esco.dynamicgroups.domain.definition.IAtomicPropositionValidator#isValid(AtomicProposition)
     */
    public boolean isValid(final AtomicProposition atom) {
        for (String attribute : allowedAttributes) {
            if (attribute.equals(atom.getAttribute())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Explains why an atom is invalid.
     * @param invalidAtom The invalid atom.
     * @return The explanation.
     * @see org.esco.dynamicgroups.domain.definition.IAtomicPropositionValidator#explainInvalidAtom(AtomicProposition)
     */
    public String explainInvalidAtom(final AtomicProposition invalidAtom) {
        return i18n.getI18nMessage(ATTRIBUTE_NOT_IN_LIST, invalidAtom.getAttribute(), 
                Arrays.toString(getAllowedAttributes()));
    }

    /**
     * Getter for i18n.
     * @return i18n.
     */
    public II18NManager getI18n() {
        return i18n;
    }

    /**
     * Setter for i18n.
     * @param i18n the new value for i18n.
     */
    public void setI18n(final II18NManager i18n) {
        this.i18n = i18n;
    }

    /**
     * Getter for allowedAttributes.
     * @return allowedAttributes.
     */
    public String[] getAllowedAttributes() {
        return allowedAttributes;
    }

    /**
     * Setter for allowedAttributes.
     * @param allowedAttributes the new value for allowedAttributes.
     */
    public void setAllowedAttributes(final String[] allowedAttributes) {
        this.allowedAttributes = allowedAttributes;
    }
}
