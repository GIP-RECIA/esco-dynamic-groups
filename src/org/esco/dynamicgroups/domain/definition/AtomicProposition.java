/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.util.ArrayList;
import java.util.List;


/**
 * Base element for a dynamic definition.
 * It consists in a pair attribute = value.
 * @author GIP RECIA - A. Deman
 * 16 janv. 2009
 *
 */
public class AtomicProposition implements IProposition {

    /** Serial version UID.*/
    private static final long serialVersionUID = 9107364247898347133L;
    
    /** The considered attribute. */
    private String attribute;
    
    /** The value associated to the attribute. */
    private String value;
    
    /** Flag to determine if the proposition is negative. */
    private boolean negative;

    /**
     * Builds an instance of AtomicProposition.
     * @param attribute The considered attribute.
     * @param value The value associated to the attribute.
     * @param negative Flag to determine if the position is negative.
     */
    public AtomicProposition(final String attribute, 
            final String value, 
            final boolean negative) {
        this.attribute = attribute;
        this.value = value;
        this.negative = negative;
    }

    /**
     * Computes the hash code for the instance.
     * @return The hash code for the instance.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
       return attribute.hashCode() + value.hashCode();
    }

    /**
     * Test the equality with another object.
     * @param obj The considered object.
     * @return True if the two instance are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        
        if (!(obj instanceof AtomicProposition)) {
            return false;
        }
        
        final AtomicProposition other = (AtomicProposition) obj;
        
        return attribute.equals(other.attribute) 
            && value.equals(other.value) 
            && (negative == other.negative);
    }
    
    /**
     * Gives the string representation of the instance.
     * @return The string that represents the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return PropositionCodec.instance().encode(this);
    }

    /**
     * Gives the disjonctive nomrmal form of the definition. 
     * @return The definition in a normal disjunctive form.
     * @see org.esco.dynamicgroups.domain.definition.IProposition#toDisjunctiveNormalForm()
     */
    public IProposition toDisjunctiveNormalForm() {
        return this;
    }
    
    /**
     * Gives the conjunctive components of a proposition.
     * @return The conjunctive components of the proposition.
     * @see org.esco.dynamicgroups.domain.definition.IProposition#getConjunctivePropositions()
     */
    public List<IProposition> getConjunctivePropositions() {
        List<IProposition> components = new ArrayList<IProposition>();
        components.add(this);
        return components;
    }
    
    /**
     * Gives the disjunctive components of a proposition.
     * @return The disjunctive components of the proposition.
     * @see org.esco.dynamicgroups.domain.definition.IProposition#getDisjunctivePropositions()
     */
    public List<IProposition> getDisjunctivePropositions() {
        List<IProposition> components = new ArrayList<IProposition>();
        components.add(this);
        return components;
    }
    
    /**
     * Gives the negative form of the definition.
     * @return The negative form of the definition.
     * @see org.esco.dynamicgroups.domain.definition.IProposition#toNegativeForm()
     */
    public IProposition toNegativeForm() {
        return new AtomicProposition(attribute, value, !negative);
    }
    
    /**
     * Getter for attribute.
     * @return attribute.
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Setter for attribute.
     * @param attribute the new value for attribute.
     */
    public void setAttribute(final String attribute) {
        this.attribute = attribute;
    }

    /**
     * Getter for value.
     * @return value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter for value.
     * @param value the new value for value.
     */
    public void setValue(final String value) {
        this.value = value;
    }


    /**
     * Getter for negative.
     * @return negative.
     */
    public boolean isNegative() {
        return negative;
    }
}
