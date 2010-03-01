/**
 * 
 */
package org.esco.dynamicgroups.domain.definition.antlr.util;


import java.io.Serializable;

import org.esco.dynamicgroups.domain.definition.antlr.generated.DynamicGroupDefinitionParser;

/**
 * Reference implementation of ITokensEscaper. 
 * @author GIP RECIA - A. Deman
 * 23 oct. 2009
 *
 */
public class TokensEscaperImpl implements ITokensEscaper, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -2359931260578974690L;
    
    /** Escape constant. */
    private static final String ESCAPE = DynamicGroupDefinitionParser.ESCAPE_STRING; 
        
    /** Escape escape constant. */
    private static final String ESCAPED_ESCAPE = ESCAPE + ESCAPE; 
       
    /** Comma constant. */
    private static final String COMMA =  DynamicGroupDefinitionParser.COMMA_STRING;
    
    /** Escaped comma constant. */
    private static final String ESCAPED_COMMA =  ESCAPE + COMMA;
    
    /** Left bracket. */ 
    private static final String L_BRACKET = ESCAPE + DynamicGroupDefinitionParser.L_BRACKET;
    
    /** Escaped left bracket. */ 
    private static final String ESCAPED_L_BRACKET = ESCAPE + L_BRACKET;
    
    /** Right bracket. */ 
    private static final String R_BRACKET =  ESCAPE + DynamicGroupDefinitionParser.R_BRACKET_STRING;
    
    /** Escaped right bracket. */ 
    private static final String ESCAPED_R_BRACKET =  ESCAPE + R_BRACKET;
    
    /**
     * Builds an instance of TokensEscaperImpl.
     */
    public TokensEscaperImpl() {
        super();
    }

    /**
     * @param stringToEscape
     * @return The string with some tokens escaped.
     * @see org.esco.dynamicgroups.domain.definition.antlr.util.ITokensEscaper#escapeTokens(java.lang.String)
     */
    public String escapeTokens(final String stringToEscape) {
        String escaped = stringToEscape.replaceAll(ESCAPE, ESCAPED_ESCAPE);
        escaped = escaped.replaceAll(COMMA, ESCAPED_COMMA);
        escaped = escaped.replaceAll(L_BRACKET, ESCAPED_L_BRACKET);
        escaped = escaped.replaceAll(R_BRACKET, ESCAPED_R_BRACKET);
        return escaped;
    }
}
