/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.io.Serializable;

import java.util.List;


/**
 * DAO for the LDAP. Used for the initialization of the dynamic groups.
 * @author GIP RECIA - A. Deman
 * 19 janv. 2009
 *
 */
public class LDAPPropositionTranslator implements IPropositionTranslator, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 3555266918663719714L;

    /** And constant fot the LDAP filters. */
    private static final String AND = "&";

    /** OR constant fot the LDAP filters. */
    private static final String OR = "|";

    /** Not constant fot the LDAP filters. */
    private static final String NOT = "!";

    /** Equal constant. */
    private static final String EQUAL = "=";

    /** Open bracket constant. */
    private static final char OPEN_BRACKET = '(';

    /** Close bracket constant. */
    private static final char CLOSE_BRACKET = ')';
    
    /**
     * Builds an instance of LDAPPropositionTranslator.
     */
    public LDAPPropositionTranslator() {
       super();
    }

    /**
     * Translates a conjunctive proposition into an ldap filter.
     * @param proposition The conjunctive proposition.
     * @return The ldap filter string.
     */
    protected String translateConjPropToLDAPFilter(final IProposition proposition) {

        List<AtomicProposition> atoms = proposition.getAtomicPropositions();
        final StringBuilder sb = new StringBuilder();
        if (atoms.size() > 0) {
            if (atoms.size() == 1) {
                final AtomicProposition atom = atoms.get(0);
                sb.append(OPEN_BRACKET);
                sb.append(atom.getAttribute());
                sb.append(EQUAL);
                sb.append(atom.getValue());
                sb.append(CLOSE_BRACKET);

                if (atom.isNegative()) {
                    sb.insert(0, NOT);
                    sb.insert(0, OPEN_BRACKET);
                    sb.append(CLOSE_BRACKET);
                }

            } else {
                sb.append(OPEN_BRACKET);
                sb.append(AND);

                for (IProposition atom : atoms) {
                    final AtomicProposition prop = (AtomicProposition) atom;
                    StringBuilder sb2 =  new StringBuilder();
                    sb2.append(OPEN_BRACKET);
                    sb2.append(prop.getAttribute());
                    sb2.append(EQUAL);
                    sb2.append(prop.getValue());
                    sb2.append(CLOSE_BRACKET);
                    if (prop.isNegative()) {
                        sb2.insert(0, NOT);
                        sb2.insert(0, OPEN_BRACKET);
                        sb2.append(CLOSE_BRACKET);
                    }
                    sb.append(sb2);  
                }
                sb.append(CLOSE_BRACKET);
            }
        }
        return sb.toString();
    }

    /**
     * Translates a logical proposition into an LDAP filter.
     * @param proposition The proposition to translate.
     * @return The ldap filter.
     * @see org.esco.dynamicgroups.domain.definition.IPropositionTranslator#translate(IProposition)
     */
    public String translate(final IProposition proposition) {
        final StringBuilder sb = new StringBuilder();
        List<IProposition> conjProps = proposition.getConjunctivePropositions();
        if (conjProps.size() > 0) {
            if (conjProps.size() == 1) {
                sb.append(translateConjPropToLDAPFilter(conjProps.get(0)));
            } else {
                sb.append(OPEN_BRACKET);
                sb.append(OR);

                for (IProposition conjProp : conjProps) {
                    sb.append(translateConjPropToLDAPFilter(conjProp));
                }
                sb.append(CLOSE_BRACKET);
            }
        }
        return sb.toString();
    }
}
