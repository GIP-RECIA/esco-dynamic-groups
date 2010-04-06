/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.util.ArrayList;
import java.util.List;


/**
 * Used to moelize the negation of a proposition.
 * @author GIP RECIA - A. Deman
 * 16 janv. 2009
 *
 */
public class Negation implements IProposition {

    /** Serial version UID.*/
    private static final long serialVersionUID = -4347482333330783667L;
    
    /** The proposition. */
    private IProposition proposition;
    
    /**
     * Builds an instance of Negation.
     * @param proposition The proposition in the score of the negation.
     */
    public Negation(final IProposition proposition) {
        this.proposition = proposition;
    }

    /**
     * Gives the string representation of this instance.
     * @return The string that represents this instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return PropositionCodec.instance().code(this);       
    }

    /**
     * Gives the disjonctive nomrmal form of the proposition. 
     * @return The proposition in a normal disjunctive form.
     * @see org.esco.dynamicgroups.domain.definition.IProposition#toDisjunctiveNormalForm()
     */
    public IProposition toDisjunctiveNormalForm() {
        return proposition.toNegativeForm().toDisjunctiveNormalForm();
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
     * Gives the atomic porpositions.
     * @return The atomic propositions.
     * @see org.esco.dynamicgroups.domain.definition.IProposition#getAtomicPropositions()
     */
    public List<AtomicProposition> getAtomicPropositions() {
        List<AtomicProposition> atomic = new ArrayList<AtomicProposition>();
        atomic.addAll(toNegativeForm().getAtomicPropositions());
        return atomic;
    }
    
    /**
     * Gives the negative form of the proposition.
     * @return The negative form of the proposition.
     */
    public IProposition toNegativeForm() {
        return proposition.toNegativeForm();
    }

    /**
     * Getter for proposition.
     * @return proposition.
     */
    public IProposition getProposition() {
        return proposition;
    }
}
