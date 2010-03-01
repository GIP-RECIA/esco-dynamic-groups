package org.esco.dynamicgroups.domain.definition.antlr.error.handling;

/**
 * Interface used to handle the errors.
 * @author GIP RECIA - A. Deman
 * 20 oct. 2009
 *
 */
public interface IErrorHandler {
    /**
     * Handles the error message.
     * @param line The line in error.
     * @param charPos the position of the invalid char.
     * @param invalidChar The invalid char.
     */
    void handleError(final int line, final int charPos, final char invalidChar);
}
