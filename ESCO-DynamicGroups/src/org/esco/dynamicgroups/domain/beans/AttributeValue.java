/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;

/**
 * Value for a dynamic attribute.
 * @author GIP RECIA - A. Deman
 * 14 janv. 2009
 *
 */
public class AttributeValue implements Serializable {
    
    /** Constant for the undefined values of attributes. */
    public static final String UNDEF_VALUE = "Undefined";

    /** Serial version UID.*/
    private static final long serialVersionUID = -7145106009433353003L;
    
    /** Name of the dynamic attribute. */
    private String attributeName;
    
    /** Value of the dynamic attribute. */
    private String attributeValue;
    
    /** Negative flag. */
    private boolean negative;

    /**
     * Builds an instance of AttributeValue.
     */
    public AttributeValue() {
        super();
    }
    
    /**
     * Builds an instance of DynAttribute.
     * @param attributeName The name of the attribute.
     * @param attributeValue The value of the attribute.
     * @param negative The negation flag.
     */
    public AttributeValue(final String attributeName,
            final String attributeValue,
            final boolean negative) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.negative = negative;
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
        if (!(obj instanceof AttributeValue)) {
            return false;
        }
        
        final AttributeValue other = (AttributeValue) obj;
        
        return other.getAttributeName().equals(getAttributeName()) 
            && other.getAttributeValue().equals(other.getAttributeValue());
    }
    
    /**
     * Gives the string representation of this instance.
     * @return The string that represents this instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AttributeValue#{" + getAttributeName() + "=" + getAttributeValue() + "}";
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
     * @param attributeValue the new attributeValue for attributeValue.
     */
    public void setAttributeValue(final String attributeValue) {
        this.attributeValue = attributeValue;
    }    

    /**
     * Computes the hash code of this instance.
     * @return The hash code of this instance.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return attributeValue.hashCode() + attributeValue.hashCode();
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
