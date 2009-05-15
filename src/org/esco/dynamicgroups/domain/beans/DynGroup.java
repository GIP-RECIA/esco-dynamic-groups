/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.IProposition;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;

/**
 * Representation of a dynamic group.
 * @author GIP RECIA - A. Deman
 * 12 janv. 2009
 */
public class DynGroup implements Serializable {
    
   
    /** Constant for the conjunctive components of a group. */ 
    public static final char CONJ_COMP_INDIRECTION = '@';
    
    /** Constant used for the name of the conjunctive component groups. */
    public static final String OPEN_CURLY_BRACKET = "{";

    /** Constant used for the name of the conjunctive component groups. */
    public static final String CLOSE_CURLY_BRACKET = "}";
    
    /** Serial Version UID.*/
    private static final long serialVersionUID = -6454699802772880028L;
    
    /** Dynamic group identifier. */
    private long groupId;

    /** The name associated to the group. */
    private String groupName;
    
    /** The definition associated to the group. */
    private String groupDefinition;
    
    /** Number of attributes in the definition. */
    private int attributesNb;
    
    /** The id of the group that contains this group as a cunjunctive component. */
    private Long indirectedGroupId;
   
    /**
     * Builds an instance of DynGroup.
     */
    public DynGroup() {
        super();
    }
    
    /**
     * Builds an instance of DynGroup.
     * @param groupName The name of the group.
     */
    public DynGroup(final String groupName) {
        this(groupName, "");
    }
    
    /**
     * Builds an instance of DynGroup.
     * @param definition The definition of the group to buikd.
     */
    public DynGroup(final DynamicGroupDefinition definition) {
        this(definition.getGroupName(), PropositionCodec.instance().code(definition.getProposition()));
    }
   
    /**
     * Builds an instance of DynGroup as the conjunctive component of another group.
     * @param conjonctiveComponentNumber The number of the consjunctive component.
     * @param groupName The name of the group.
     * @param groupDefinition The definition of the group.
     * @param indirectedGroupId The id of the group that contains this group
     * as a conjunctive component.
     */
    protected DynGroup(final int conjonctiveComponentNumber,
            final String groupName, final String groupDefinition, final Long indirectedGroupId) {
       this(CONJ_COMP_INDIRECTION + OPEN_CURLY_BRACKET + conjonctiveComponentNumber 
               + CLOSE_CURLY_BRACKET + groupName, groupDefinition);
       this.indirectedGroupId = indirectedGroupId;
    }
    
    /**
     * Builds an instance of DynGroup.
     * @param groupName The name of the group.
     * @param groupDefinition The definition of the group.
     */
    public DynGroup(final String groupName, final String groupDefinition) {
       this.groupName = groupName;
       setGroupDefinition(groupDefinition);
    }
    
    /**
     * Tests if the group is a conjunctive component of a group.
     * For instance, if a group called myGroup is defined with (A and B) or (D and C)
     * it will have two components @{1}myGroup defined with (A and B), and @{2}myGroup defined with
     * (D and C).
     * @return True if the group is a conjunctive component of another group.
     *  
     */
    public boolean isConjunctiveComponentIndirection() {
       return getIndirectedGroupId() != null;
    }
    
    /**
     * Gives the groups associated to each conjunctive component of the definition.
     * @return The groups that correspond to the conjunctions in the group definition.
     */
    public Set<DynGroup> getConjunctiveComponents() {
        
        final Set<DynGroup> conjunctiveComponents = new HashSet<DynGroup>();
        
        if (!isConjunctiveComponentIndirection()) {
            final IProposition proposition = PropositionCodec.instance().decodeToDisjunctiveNormalForm(groupDefinition);
        
            if (proposition != null) {
                final List<IProposition> conjunctions = proposition.getConjunctivePropositions();
                if (conjunctions.size() > 1) {
                    int componentIndex = 0;
                    for (IProposition conjunction : conjunctions) {
                        conjunctiveComponents.add(new DynGroup(componentIndex++, 
                                getGroupName(), 
                                PropositionCodec.instance().code(conjunction), 
                                getGroupId()));
                    }
                }
            }
        }
        return conjunctiveComponents;
    }
    
    /**
     * Getter for groupId.
     * @return groupId.
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * Setter for groupId.
     * @param groupId the new value for groupId.
     */
    public void setGroupId(final long groupId) {
        this.groupId = groupId;
    }

    /**
     * Getter for groupDefinition.
     * @return groupDefinition.
     */
    public String getGroupDefinition() {
        return groupDefinition;
    }

    /**
     * Setter for groupDefinition.
     * @param groupDefinition the new value for groupDefinition.
     */
    public void setGroupDefinition(final String groupDefinition) {
        this.groupDefinition = groupDefinition;
        
        final IProposition prop = PropositionCodec.instance().decode(groupDefinition);
        if (prop != null) {
            attributesNb = prop.toDisjunctiveNormalForm().getAtomicPropositions().size();
        }
    }

    /**
     * Getter for attributesNb.
     * @return attributesNb.
     */
    public int getAttributesNb() {
        return attributesNb;
    }

    /**
     * Setter for attributesNb.
     * @param attributesNb the new value for attributesNb.
     */
    public void setAttributesNb(final int attributesNb) {
        this.attributesNb = attributesNb;
    }

    /**
     * Getter for groupName.
     * @return groupName.
     */
    public String getGroupName() {
        return groupName;
    }
    
    /**
     * If this group is a conjunctive component of another one, gives the name of
     * the full group, otherwise gives the name of this group. 
     * @return The name of the group associated to this conjunctive component group.
     */
    public String getIndirectedGroupName() {
        if (isConjunctiveComponentIndirection()) {
            final int pos = groupName.indexOf(CLOSE_CURLY_BRACKET);
            return groupName.substring(pos + 1);
        }
        return groupName;
    }

    /**
     * Setter for groupName.
     * @param groupName the new value for groupName.
     */
    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }

    /**
     * Gives the hash code for this instance.
     * @return The hash code.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return groupDefinition.hashCode() + groupName.hashCode();
    }

    /**
     * Tests the equality of this instance with another object.
     * @param obj The object to test.
     * @return True if this instance and the parameter are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DynGroup)) {
            return false;
        }
        final DynGroup other = (DynGroup) obj;
        return other.getGroupDefinition().equals(groupDefinition) && other.getGroupName().equals(groupName);
    }
    
    /**
     * Gives the String representation of this instance.
     * @return The string that represents this instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DynGroup#{" + groupId + ", " + groupName + ", " 
            + groupDefinition + ", " + attributesNb + "}";
    }

    /**
     * Getter for indirectedGroupId.
     * @return indirectedGroupId.
     */
    public Long getIndirectedGroupId() {
        return indirectedGroupId;
    }

    /**
     * Setter for indirectedGroupId.
     * @param indirectedGroupId the new value for indirectedGroupId.
     */
    public void setIndirectedGroupId(final Long indirectedGroupId) {
        this.indirectedGroupId = indirectedGroupId;
    }
}
