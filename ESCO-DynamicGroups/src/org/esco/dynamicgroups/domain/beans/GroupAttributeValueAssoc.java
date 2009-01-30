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
           
    /** Value of the attribute. */
    private String attributeValue;
    
    /** The attribute in the association. */ 
    private DynAttribute attribute;
    
    /** Negative flag. */
    private boolean negative;
    
    /** The group in the association. */
    private DynGroup group;
   
    /**
     * Builds an instance of GroupAttributeValueAssoc.
     */
    public GroupAttributeValueAssoc() {
        super();
    }
    
    /**
     * Builds an instance of GroupAttributeValueAssoc.
     * @param attribute The dynamic attribute.
     * @param attributeValue The value of the attribute.
     * @param negative Negative flag.
     * @param group The group.
     */
    public GroupAttributeValueAssoc(final DynAttribute attribute,
            final String attributeValue,
            final boolean negative,
            final DynGroup group) {
        this.attribute = attribute;
        this.group = group;
        this.attributeValue = attributeValue;
        this.negative = negative;
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
    public void setAttributeValueId(final long attributeValueId) {
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
    public void setAttributeValue(final String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
     * Getter for attribute.
     * @return attribute.
     */
    public DynAttribute getAttribute() {
        return attribute;
    }

    /**
     * Setter for attribute.
     * @param attribute the new value for attribute.
     */
    public void setAttribute(final DynAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * Getter for group.
     * @return group.
     */
    public DynGroup getGroup() {
        return group;
    }

    /**
     * Setter for group.
     * @param group the new value for group.
     */
    public void setGroup(final DynGroup group) {
        this.group = group;
    }

    /**
     * Getter for negative.
     * @return negative.
     */
    public boolean isNegative() {
        return negative;
    }

    /**
     * Setter for negative.
     * @param negative the new value for negative.
     */
    public void setNegative(final boolean negative) {
        this.negative = negative;
    }

    
}
