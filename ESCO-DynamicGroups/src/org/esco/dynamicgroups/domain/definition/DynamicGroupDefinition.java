/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Definition of a dynamic group.
 * @author GIP RECIA - A. Deman
 * 19 janv. 2009
 *
 */
public class DynamicGroupDefinition implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -6054188705662382538L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DynamicGroupDefinition.class);
    
    /** The name of the group. */
    private String groupName;
    
    /** The logic proposition associated to
     * the dynamic group. */
    private IProposition proposition;
    
    
    /**
     * Builds an instance of DynamicGroupDefinition.
     * @param groupName The name of the group.
     * @param proposition The String to decode that represents the proposition
     * associated to the definition.
     */
    DynamicGroupDefinition(final String groupName, final String proposition) {
       this(groupName, PropositionCodec.instance().decode(proposition));
    }
    
    /**
     * Builds an instance of DynamicGroupDefinition.
     * @param groupName The name of the group.
     * @param proposition The proposition associated to the group definition.
     */
    DynamicGroupDefinition(final String groupName, final IProposition proposition) {
        this.groupName = groupName;
        this.proposition = proposition;
        if (this.proposition != null) {
            this.proposition = proposition.toDisjunctiveNormalForm();
        }
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creates a dynamic group definition: " + this);
        }
        
        if (proposition == null) {
            LOGGER.warn("Error: the logic proposition is null for the group " + groupName);
        }
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
        return "DynamicGroupDefinition#{" + groupName + ", " + proposition + "}";
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
    public List<IProposition> getConjunctiveProposition() {
        return proposition.getConjunctivePropositions();
    }

    /**
     * Getter for groupName.
     * @return groupName.
     */
    public String getGroupName() {
        return groupName;
    }
}
