package org.esco.dynamicgroups.domain.definition;

import java.util.List;
import java.util.Set;

import org.esco.dynamicgroups.domain.beans.DynGroup;

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

        final DynGroup group = new DynGroup("a name", fTerm.toString());
        System.out.println("group: " + group);
        Set<DynGroup> conjGroups = group.getConjunctiveComponents();
        for (DynGroup conjGroup : conjGroups) {
            System.out.println(" == >" + conjGroup);
        }
        
        
        
        IProposition ldapProp = new Conjunction(new AtomicProposition("objectClass", "ENTAuxEnseignant", false), 
                new AtomicProposition("ENTPersonSexe", "M", false));
        System.out.println("=>" + ldapProp); 
        // And(objectClass=ENTAuxEnseignant, ENTPersonSexe=M)

        IProposition prop = PropositionCodec.instance()
            .decode("And (Or(objectClass =ENTAuxEnseignant, ENTPersonSexe=M), (ENTPersonSexe=F))");
        System.out.println("=>" + prop);
        System.out.println("==>" + prop.toDisjunctiveNormalForm());
        
        
    }
}
