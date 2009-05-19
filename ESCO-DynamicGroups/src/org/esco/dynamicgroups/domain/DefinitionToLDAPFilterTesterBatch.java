/**
 * 
 */
package org.esco.dynamicgroups.domain;

import org.esco.dynamicgroups.domain.definition.DecodedPropositionResult;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;

/**
 * @author GIP RECIA - A. Deman 
 * 19 mars 2009
 *
 */
public class DefinitionToLDAPFilterTesterBatch {
    /** The LDAP initializer used to test the definition. */
    private LDAPDynamicGroupInitializer initializer = new LDAPDynamicGroupInitializer(true);

    /**
     * Builds an instance of DefinitionToLDAPFilterTesterBatch.
     */
    private DefinitionToLDAPFilterTesterBatch() {
        super();
    }

    /**
     * Checks a logical proposition.
     * @param proposition The proposition to check.
     */
    private void check(final String proposition) {
        System.out.println("Test the string: " + proposition);
        final DecodedPropositionResult result = PropositionCodec.instance().decode(proposition);

        if (!result.isValid()) {
            System.out.println("The definition is not valid. Invalid string: " + result.getInvalidEncodedString());
        } else {
            final DynamicGroupDefinition def = new DynamicGroupDefinition("TEST GROUP", result.getProposition());
            System.out.println("The definition is valid: " + def.getProposition());
            final String filter = initializer.translateToLdapFilter(def);
            System.out.println("The associated filter is: " + filter);
        }

    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final DefinitionToLDAPFilterTesterBatch tester = new DefinitionToLDAPFilterTesterBatch();
        for (String defString : args) {
            tester.check(defString);
        }
    }
}
