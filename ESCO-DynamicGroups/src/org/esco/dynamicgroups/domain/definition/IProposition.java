/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.io.Serializable;
import java.util.List;

/**
 * Logical proposition that defines a dynamic group.
 * @author GIP RECIA - A. Deman
 * 16 janv. 2009
 *
 */
public interface IProposition extends Serializable {
    
    /**
     * Gives the negative form of the definition.
     * @return The negative form of the definition.
     */
    IProposition toNegativeForm();
    
    /**
     * Gives the disjonctive nomrmal form of the definition. 
     * @return The definition in a normal disjunctive form.
     */
    IProposition toDisjunctiveNormalForm();
    
    /**
     * Gives the Conjunctive components of a proposition.
     * @return The Conjunctive components of the proposition. 
     */
    List<IProposition> getConjunctivePropositions();
    
    /**
     * Gives the disjunctive components of a proposition.
     * @return The disjunctive components of the proposition.
     */
    List<IProposition> getDisjunctivePropositions();

    /**
     * Gives the atomic porpositions.
     * @return The atomic propositions.
     */
    List<AtomicProposition> getAtomicPropositions();
    
    
    
}
