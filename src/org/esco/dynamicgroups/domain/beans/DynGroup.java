/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;

/**
 * Representation of a dynamic group.
 * @author GIP RECIA - A. Deman
 * 12 janv. 2009
 */
public class DynGroup implements Serializable {

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
    
    /**
     * Builds an instance of DynGroup.
     */
    public DynGroup() {
        super();
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
        return groupDefinition.hashCode();
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
        return ((DynGroup) obj).groupDefinition.equals(groupDefinition);
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
}
