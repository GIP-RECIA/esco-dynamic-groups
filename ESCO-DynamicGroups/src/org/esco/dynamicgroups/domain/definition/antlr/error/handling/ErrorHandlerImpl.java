/**
 * 
 */
package org.esco.dynamicgroups.domain.definition.antlr.error.handling;

import java.io.Serializable;

import org.esco.dynamicgroups.exceptions.InvalidDynamicGroupDefinitionException;

/**
 * Implementation of the error handler.
 * @author GIP RECIA - A. Deman
 * 20 oct. 2009
 *
 */
public class ErrorHandlerImpl implements IErrorHandler, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -6838431962443438533L;

    /**
     * Builds an instance of ErrorHandlerImpl.
     */
    public ErrorHandlerImpl() {
        super();
    }

    /**
     * Handles an error.
     * @param line The line in error.
     * @param charPos the position of the invalid char.
     * @param invalidChar The invalid char.
     * @see org.esco.dynamicgroups.domain.definition.antlr.error.handling.IErrorHandler#handleError(int, int, char)
     */
    public void handleError(final int line, final int charPos, final char invalidChar) {
        throw new InvalidDynamicGroupDefinitionException(charPos, invalidChar);
    }
    

}
