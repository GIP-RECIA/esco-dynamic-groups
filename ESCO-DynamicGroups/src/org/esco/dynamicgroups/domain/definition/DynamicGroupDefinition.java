/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.io.Serializable;
import java.util.List;


/**
 * Definition of a dynamic group.
 * @author GIP RECIA - A. Deman
 * 19 janv. 2009
 *
 */
public class DynamicGroupDefinition implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -6054188705662382538L;
    
    /** The uuid of the group. */
    private String groupUUID;
    
    /** The logic proposition associated to
     * the dynamic group. */
    private IProposition proposition;
    
    
    /**
     * Builds an instance of DynamicGroupDefinition.
     * @param groupUUID The uuid of the group.
     * @param proposition The String to decode that represents the proposition
     * associated to the definition.
     */
    public DynamicGroupDefinition(final String groupUUID, final String proposition) {
       this(groupUUID, PropositionCodec.instance().decode(proposition).getProposition());
    }
    
    /**
     * Builds an instance of DynamicGroupDefinition.
     * @param groupUUID The uuid of the group.
     * @param proposition The proposition associated to the group definition.
     */
    public DynamicGroupDefinition(final String groupUUID, final IProposition proposition) {
        this.groupUUID = groupUUID;
        this.proposition = proposition;
        if (this.proposition != null) {
            this.proposition = proposition.toDisjunctiveNormalForm();
        }
    }
    
    /**
     * Tests if the definition is valid.
     * @return True the underlying logic proposition is valid.
     */
    public boolean isValid() {
       return proposition != null;
    }

    /**
     * Getter for proposition.
     * @return proposition.
     */
    public IProposition getProposition() {
        return proposition;
    }
    
    /**
     * Gives the string that represents the group definition.
     * @return The string that represents the group definition.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DynamicGroupDefinition#{" + groupUUID + ", " + proposition + "}";
    }
    
    /**
     * Gives the conjunctive propositions associated to this definition.
     * @return The conjunctive propositions of the underlying proposition.
     * For instance the proposition ABC + DCE + G will return
     * {ABC, DCE, G}. This method is mainly usedfull if the proposition 
     * is in a normal form but it will not throw an error if it is not the case.
     * (A+BC) + DCE + G will return {(A+BC), DCE, G }.
     * 
     */
    public List<IProposition> getConjunctivePropositions() {
        return proposition.getConjunctivePropositions();
    }

    /**
     * Getter for groupUUID.
     * @return groupUUID.
     */
    public String getGroupUUID() {
        return groupUUID;
    }
}
