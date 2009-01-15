/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;

/**
 * DynAttribute.
 * Used to represents the attributes involved in dynmic groups definition.
 * @author GIP RECIA - A. Deman
 * 12 janv. 2009
 *
 */
public class DynAttribute implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -6519451510706496803L;

    /** Idetifier for an attribute. */   
    private long attributeId;
    
    /** The name of the attibute. */
    private String attributeName;
    
    /**
     * Builds an instance of DynAttribute.
     */
    public DynAttribute() {
        super();
    }
    
    /**
     * Builds an instance of DynAttribute.
     * @param attributeId The id of the attribute
     * @param attributeName The name of the attribute.
     */
    public DynAttribute(final long attributeId, final String attributeName) {
        this.attributeId = attributeId;
        this.attributeName = attributeName;
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
    public void setAttributeId(final long attributeId) {
        this.attributeId = attributeId;
    }

    /**
     * Getter for attributeName.
     * @return attributeName.
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Setter for attributeName.
     * @param attributeName the new value for attributeName.
     */
    public void setAttributeName(final String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * Computes the hash code of this instance.
     * @return The hash code of this instance.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return attributeName.hashCode();
    }

    /**
     * Tests if two objects are equal.
     * @param obj The object to compare this instance with.
     * @return True if this instance is equal to the object.
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
        if (!(obj instanceof DynAttribute)) {
            return false;
        }
        return ((DynAttribute) obj).attributeName.equals(this.attributeName);
    }
    
    /**
     * Gives the string representation of this instance.
     * @return The string that represents this instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DynAttribute#{" + attributeId + ", " + attributeName + "}";
    }
    
}
