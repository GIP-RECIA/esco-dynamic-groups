package org.esco.dynamicgroups.domain.definition;

import java.util.List;

import org.esco.dynamicgroups.domain.LDAPDynamicGroupInitializer;

/**
 * @author GIP RECIA - A. Deman
 * 19 janv. 2009
 *
 */
public class TestBatch {
    
    /**
     * Builds an instance of TestBatch.
     */
    private TestBatch() {
        super();
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        IProposition a = new AtomicProposition("a", "_", false);
        IProposition b = new AtomicProposition("b", "_", false);
        IProposition c = new AtomicProposition("c", "_", false);
        IProposition d = new AtomicProposition("d", "_", false);
        IProposition e = new AtomicProposition("e", "_", false);
        IProposition f = new AtomicProposition("f", "_", false);
        IProposition g = new AtomicProposition("g", "_", false);
        IProposition h = new AtomicProposition("h", "_", false);
        
        IProposition f1 = new Conjunction(a, b);
        IProposition f2 = new Conjunction(b, c);
        IProposition f3 = new Disjunction(f1, f2);
        IProposition f4 = new Conjunction(d, e);
        IProposition f5 = new Negation(new Conjunction(g, h));
        IProposition f6 = new Conjunction(f, f5);
        IProposition f7 = new Disjunction(f4, f6);
        IProposition fTerm = new Conjunction(f3, f7);
        
        System.out.println("=>" + fTerm);
        
        System.out.println("==>" + fTerm.toDisjunctiveNormalForm());
        List<IProposition> conjProps = fTerm.toDisjunctiveNormalForm().getConjunctivePropositions();
        for (int i = 0; i < conjProps.size(); i++) {
            System.out.println(i + "==>" + conjProps.get(i));
        }

        
        
        IProposition ldapProp = new Conjunction(new AtomicProposition("objectClass", "ENTAuxEnseignant", false), 
                new AtomicProposition("ENTPersonSexe", "M", false));
        
        LDAPDynamicGroupInitializer initializer = new LDAPDynamicGroupInitializer();
        initializer.initialize(new DynamicGroupDefinition("A dynamic group", ldapProp));
        
        
    }
}
