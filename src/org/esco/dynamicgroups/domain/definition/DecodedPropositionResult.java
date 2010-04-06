/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.io.Serializable;


/**
 * Result when a string that should denote a proposition is decoded.
 * @author GIP RECIA - A. Deman
 * 18 mai 2009
 *
 */
public class DecodedPropositionResult implements Serializable {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = -5432170097045091122L;

    /** The decoded proposition, if the result is valid. */
    private IProposition proposition;
    
    /** The source of the error if the result is invalid. */
    private String invalidEncodedString;
    
    /** Error message. */
    private String errorMessage;
    
    /**
     * Builds a valid instance of DecodedPropositionResult.
     * @param proposition The decoded proposition.
     */
    public DecodedPropositionResult(final IProposition proposition) {
        this.proposition = proposition;
    }
    
   
    /**
     * Builds an invalid instance of DecodedPropositionResult.
     * @param invalidEncodedString The source of the error.
     * @param errorMessage The error message.
     */
    public DecodedPropositionResult(final String invalidEncodedString,
            final String errorMessage) {
        this.invalidEncodedString = invalidEncodedString;
        this.errorMessage = errorMessage;
    }
    
    /**
     * Gives the String that represents the instance.
     * @return The string that represent the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String image = getClass().getSimpleName() + "#{";
        if (isValid()) {
            image += "valid: ";
            image += getProposition();
        } else {
            image += "invalid: ";
            image += getInvalidEncodedString();
        }
        image += "}";
        return image;
    }
    
    /**
     * Computes the hash code of the instance.
     * @return The hash value of the instance.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (isValid()) {
            return proposition.hashCode();
        }
        return invalidEncodedString.hashCode();
    }

    /**
     * Test if the instance is equal to another object.
     * @param obj The object to test.
     * @return True if this instance is equal to obj.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DecodedPropositionResult)) {
            return false;
        }
        final DecodedPropositionResult other = (DecodedPropositionResult) obj;
        if (isValid() != other.isValid()) {
            return false;
        }
        if (isValid()) {
            return proposition.equals(other.getProposition());
        }
        return invalidEncodedString.endsWith(other.getInvalidEncodedString());
    }
    
    /**
     * Tests if the result is valid.
     * @return True if the result is valid.
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
     * Getter for invalidEncodedString.
     * @return invalidEncodedString.
     */
    public String getInvalidEncodedString() {
        return invalidEncodedString;
    }
    
    /**
     * Tests if there is an error message.
     * @return The error message.
     */
    public boolean hasErrorMessage() {
        return errorMessage != null;
    }

    /**
     * Getter for errorMessage.
     * @return errorMessage.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
