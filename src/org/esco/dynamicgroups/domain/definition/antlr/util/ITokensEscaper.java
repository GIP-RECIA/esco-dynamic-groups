/**
 * 
 */
package org.esco.dynamicgroups.domain.definition.antlr.util;

/**
 * Util class used to escape some of the tokens.
 * @author GIP RECIA - A. Deman
 * 23 oct. 2009
 *
 */
public interface ITokensEscaper {

    /**
     * Escapes some tokens in a given string.
     * @param stringToEscape The string that may contain some tokens to escape.
     * @return The escaped string.
     */
    String escapeTokens(final String stringToEscape);
}
