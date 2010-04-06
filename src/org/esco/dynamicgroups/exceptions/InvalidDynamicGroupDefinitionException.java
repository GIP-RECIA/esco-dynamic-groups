/**
 * 
 */
package org.esco.dynamicgroups.exceptions;

/**
 * Esception thrown for an invalid definition.
 * @author GIP RECIA - A. Deman
 * 23 oct. 2009
 *
 */
public class InvalidDynamicGroupDefinitionException extends DynamicGroupsException {

    /** Serial version UID.*/
    private static final long serialVersionUID = 6342996199399934961L;

    /**The position of the invalid character. */
    private final int position;
    
    /** The invalid char. */
    private final char invalidChar;
    
    /** The invalid expression (if known). */
    private final String invalidExpression;
    
    
    /**
     * Builds an instance of InvalidDynamicGroupDefinitionException.
     * @param invalidExpression The invalid expression.
     * @param position The position of the invalid char.
     * @param invalidChar The invalid char.
     */
    public InvalidDynamicGroupDefinitionException(final String invalidExpression, 
            final int position, final char invalidChar) {
        this.invalidExpression = invalidExpression;
       this.position = position;
       this.invalidChar = invalidChar;
    }
    
    /**
     * Builds an instance of InvalidDynamicGroupDefinitionException.
     * @param position The position of the invalid char.
     * @param invalidChar The invalid char.
     */
    public InvalidDynamicGroupDefinitionException(final int position, final char invalidChar) {
        this("", position, invalidChar);
    }
    
    /**
     * Tests if the invalid expession is stored in the exception.
     * @return True if the invalid expression can be retrieved from this
     * exception.
     */
    public boolean invalidExpressionIsKnown() {
        return !("".equals(invalidExpression));
    }

    /**
     * Getter for position.
     * @return position.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Getter for invalidChar.
     * @return invalidChar.
     */
    public char getInvalidChar() {
        return invalidChar;
    }

    /**
     * Getter for invalidExpression.
     * @return invalidExpression.
     */
    public String getInvalidExpression() {
        return invalidExpression;
    }

}
