/**
 * 
 */
package org.esco.dynamicgroups.domain;

import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;

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
        final DynamicGroupDefinition def = new DynamicGroupDefinition("TEST GROUP", proposition);
        if (def.isValid()) {
            System.out.println("The definition seems to be valid: " + def.getProposition());
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
