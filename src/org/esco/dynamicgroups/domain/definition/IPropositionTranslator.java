/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

/**
 * Interface for the implementation of the proposition translator.
 * For instance an implementation could translate a proposition into an LDAP filter,
 * and another one into an SSQL query.
 * @author GIP RECIA - A. Deman
 * 5 oct. 2009
 *
 */
public interface IPropositionTranslator {
    
    /**
     * Translate a proposition to a query adapted to the backend.
     * @param proposition The proposition to translate.
     * @return The translation that corresponds to the backend,
     * for instance a QL query or an LDAPFilter.
     */
    String translate(final IProposition proposition);

}
