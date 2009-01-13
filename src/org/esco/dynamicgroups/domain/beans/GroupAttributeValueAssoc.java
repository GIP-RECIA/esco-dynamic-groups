/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;

/**
 * Used to store the groups associated to a given value of an attribute.
 * @author GIP RECIA - A. Deman
 * 12 janv. 2009
 */
public class GroupAttributeValueAssoc implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -1438807043953744322L;
    
    /** Id of the association. */
    private long attributeValueId;
    
    /** Attribute involved in the association. */
    private long attributeId;
    
    /** Value of the attribute. */
    private String attributeValue;
    
    /** Group involved in the association. */
    private long groupId;

    /**
     * Builds an instance of GroupAttributeValueAssoc.
     */
    public GroupAttributeValueAssoc() {
        super();
    }
    
    /**
     * Getter for attributeValueId.
     * @return attributeValueId.
     */
    public long getAttributeValueId() {
        return attributeValueId;
    }

    /**
     * Setter for attributeValueId.
     * @param attributeValueId the new value for attributeValueId.
     */
    public void setAttributeValueId(long attributeValueId) {
        this.attributeValueId = attributeValueId;
    }

    /**
     * Getter for attributeValue.
     * @return attributeValue.
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * Setter for attributeValue.
     * @param attributeValue the new value for attributeValue.
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
     * Getter for attributeId.
     * @return attributeId.
     */
    public long getAttributeId() {
        return attributeId;
    }

    /**
     * Setter for attributeId.
     * @param attributeId the new value for attributeId.
     */
    public void setAttributeId(long attributeId) {
        this.attributeId = attributeId;
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
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
