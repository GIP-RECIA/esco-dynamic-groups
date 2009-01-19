/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.util.ArrayList;
import java.util.List;


/**
 * Conjunction of DefinitionElements.
 * @author GIP RECIA - A. Deman
 * 16 janv. 2009
 *
 */
public class Conjunction implements IProposition {

    /** Serial version UID.*/
    private static final long serialVersionUID = 3553775544514275109L;

    /** The first definiton element in the conjunction. */
    private IProposition first;

    /** The second definiton element in the conjunction. */
    private IProposition second;

    /**
     * Builds an instance of Conjunction.
     * @param first The first definition element in the conjunction.
     * @param second The second definition element in the conjunction.
     */
    public Conjunction(final IProposition first, final IProposition second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Computes the hash code for the instance.
     * @return The hash code for the instance.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode();
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

        if (!(obj instanceof Conjunction)) {
            return false;
        }

        final Conjunction other = (Conjunction) obj;
        return first.equals(other.first) && second.equals(other.second);
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
        final IProposition firstDFN = first.toDisjunctiveNormalForm();
        final IProposition secondDFN = second.toDisjunctiveNormalForm();
        final List<IProposition> firstConjProps = firstDFN.getConjunctivePropositions();
        final List<IProposition> secondConjProps = secondDFN.getConjunctivePropositions();
        IProposition dfn = null;
        
        for (IProposition firstConjProp : firstConjProps) {
            for (IProposition secondConjProp : secondConjProps) {
                if (dfn == null) {
                    dfn = new Conjunction(firstConjProp, secondConjProp);
                } else {
                    dfn = new Disjunction(dfn, new Conjunction(firstConjProp, secondConjProp));
                }
            }
        }
        
        return dfn;
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
        components.addAll(first.getDisjunctivePropositions());
        components.addAll(second.getDisjunctivePropositions());
        return components;
    }
    /**
     * Gives the negative form of the definition.
     * @return The negative form of the definition.
     * @see org.esco.dynamicgroups.domain.definition.IProposition#toNegativeForm()
     */
    public IProposition toNegativeForm() {
        return new Disjunction(first.toNegativeForm(), second.toNegativeForm());
    }

    /**
     * Getter for first.
     * @return first.
     */
    public IProposition getFirst() {
        return first;
    }

    /**
     * Getter for second.
     * @return second.
     */
    public IProposition getSecond() {
        return second;
    }


}
