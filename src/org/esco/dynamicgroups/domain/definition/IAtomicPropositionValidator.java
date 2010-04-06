/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

/**
 * Interface used to validate the atomic propositions, for instance by
 * looking if the attribute belongs to a given list.
 * @author GIP RECIA - A. Deman
 * 19 mai 2009
 *
 */
public interface IAtomicPropositionValidator {

    /**
     * Tests if an atomic proposition is valid.
     * @param atom The atom to test.
     * @return True if the atom is valid.
     */
    boolean isValid(final AtomicProposition atom);
    
    /**
     * Explains why an atom is invalid.
     * @param invalidAtom The invalid atom.
     * @return The explanation.
     */
    String explainInvalidAtom(final AtomicProposition invalidAtom);
}
