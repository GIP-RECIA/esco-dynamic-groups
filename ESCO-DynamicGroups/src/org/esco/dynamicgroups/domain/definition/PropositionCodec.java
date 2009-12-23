/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;


import java.io.Serializable;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.esco.dynamicgroups.domain.beans.II18NManager;
import org.esco.dynamicgroups.domain.definition.antlr.error.handling.ErrorHandlerImpl;
import org.esco.dynamicgroups.domain.definition.antlr.generated.DynamicGroupDefinitionLexer;
import org.esco.dynamicgroups.domain.definition.antlr.generated.DynamicGroupDefinitionParser;
import org.esco.dynamicgroups.exceptions.InvalidDynamicGroupDefinitionException;

/**
 * Used to code and decode a String that represents an IProposition.
 * @author GIP RECIA - A. Deman
 * 16 janv. 2009
 *
 */
public class PropositionCodec implements Serializable {
    
    /** Serial Version UID.*/
    private static final long serialVersionUID = -67872885435776773L;

    /** Singleton. */
    private static PropositionCodec instance;
    
    /** Space constant. */
    private static final String SPACE = " ";
    
    /** Comma constant. */
    private static final String COMMA = DynamicGroupDefinitionParser.COMMA_STRING;
    
    /** And constant. */
    private static final String AND = DynamicGroupDefinitionParser.AND_STRING;
    
    /** Or constant. */
    private static final String OR = DynamicGroupDefinitionParser.OR_STRING;
    
    /** Not constant. */
    private static final String NOT = DynamicGroupDefinitionParser.NOT_STRING;
    
    /** Open bracket constant. */
    private static final String L_BRACKET = DynamicGroupDefinitionParser.L_BRACKET_STRING;
    
    /** Close bracket constant. */
    private static final String R_BRACKET = DynamicGroupDefinitionParser.R_BRACKET_STRING;
    
    /** The equal constant. */
    private static final String EQUAL = DynamicGroupDefinitionParser.EQUAL_STRING;
    
    /** I18n entry for the = not found. */
    private static final String INVALID_PROP = "proposition.decoding.error.invalidproposition";
   
    /** I18n entry for an empty string atom. */
    private static final String EMPTY_STRING_ERROR = "proposition.decoding.error.empty.string";
      
    /** The atom validator to use. */
    private IAtomicPropositionValidator atomValidator;
    
    /** I18N manager. */
    private transient II18NManager i18n;
    
    /**
     * Builds an instance of PropositionCodec.
     */
    public PropositionCodec() {
        instance = this;
    }
    
    /**
     * Builds an instance of PropositionCodec.
     * @param atomValidator The atom validator to use.
     * @param i18n The I18N manager.
     */
    public PropositionCodec(final IAtomicPropositionValidator atomValidator, 
            final II18NManager i18n) {
        this();
        this.atomValidator = atomValidator;
        this.i18n = i18n;
    }
    
    /**
     * Gives the available instance.
     * @return The singleton.
     */
    public static PropositionCodec instance() {
        return instance;
    }
    
    /**
     * Normalizes an expression.
     * @param logicExpression The expression to normalize.
     * @return The normalized expression if the expression is valid, null otherwise.
     */
    public String nomalize(final String logicExpression) {
        if (logicExpression == null) {
            return null;
        }
        
        final DecodedPropositionResult result = decode(logicExpression);
        if (!result.isValid()) {
            return null;
        }
        return result.getProposition().toString();
    }
    
    /**
     * Decodes a String that represents a proposition.
     * @param coded The coded string.
     * @return The proposition if the coded string is valid,
     * null otherwise.
     */
    public DecodedPropositionResult decodeToDisjunctiveNormalForm(final String coded) {
        final DecodedPropositionResult decodedPropRes = decode(coded);
        if (!decodedPropRes.isValid()) {
            return decodedPropRes;
        }
        return new DecodedPropositionResult(decodedPropRes.getProposition().toDisjunctiveNormalForm());
    }
    
    
    
    /**
     * Decodes a String that represents a proposition.
     * @param coded The coded string.
     * @return The proposition if the coded string is valid,
     * null otherwise.
     */
    public DecodedPropositionResult decode(final String coded) {
        if (coded == null) {
            return new DecodedPropositionResult("null", i18n.getI18nMessage(INVALID_PROP, "null"));
        }
        
        final String trimed = coded.trim();
        
        if ("".equals(trimed)) {
            return new DecodedPropositionResult("", i18n.getI18nMessage(INVALID_PROP, 
                    i18n.getI18nMessage(EMPTY_STRING_ERROR)));
        }
        
        final DynamicGroupDefinitionLexer lexer = new DynamicGroupDefinitionLexer(new ANTLRStringStream(coded));
        final DynamicGroupDefinitionParser parser = new DynamicGroupDefinitionParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new ErrorHandlerImpl());
        try {
            final IProposition proposition = parser.evaluateExpression();
            final List<AtomicProposition>atomicPropositions = proposition.getAtomicPropositions();
            
            // Checks each atomic proposition.
            for (AtomicProposition atomicProposition : atomicPropositions) {
                if (!atomValidator.isValid(atomicProposition)) {
                    // One atomic proposition is invalid.
                    return new DecodedPropositionResult(coded, atomValidator.explainInvalidAtom(atomicProposition));
                }
            }
            
            // The proposition is valid.
            return new DecodedPropositionResult(proposition);
            
        } catch (RecognitionException e) {
            return new DecodedPropositionResult(coded, i18n.getI18nMessage(INVALID_PROP) + coded);
        } catch (InvalidDynamicGroupDefinitionException e) {
            return new DecodedPropositionResult(coded, i18n.getI18nMessage(INVALID_PROP) + coded);
        }
    }
    
    /**
     * Codes the disjunctive normal form of a proposition into a String.
     * @param proposition The proposition to code.
     * @return The String that represents the proposition.
     */
    public String codeDisjunctiveNormalForm(final IProposition proposition) {
        if (proposition != null) {
            return code(proposition.toDisjunctiveNormalForm());
        }
        return code(proposition);
    }
    
    /**
     * Codes a proposition into a String.
     * @param proposition The proposition to code.
     * @return The String that represents the proposition.
     */
    public String code(final IProposition proposition) {
        if (proposition instanceof AtomicProposition) {
            final AtomicProposition prop = (AtomicProposition) proposition;
            if (prop.isNegative()) {
                return code(new Negation(new AtomicProposition(prop.getAttribute(), prop.getValue(), false)));
            }
            return prop.getAttribute() + EQUAL + prop.getValue();
        }
        if (proposition instanceof Disjunction) {
            final Disjunction prop = (Disjunction) proposition;
            final String first = code(prop.getFirst());
            final String second = code(prop.getSecond());
            final StringBuilder sb = new StringBuilder(OR);
            sb.append(L_BRACKET);
            sb.append(first);
            sb.append(COMMA);
            sb.append(SPACE);
            sb.append(second);
            sb.append(R_BRACKET);
            return sb.toString();
        }
        if (proposition instanceof Conjunction) {
            final Conjunction prop = (Conjunction) proposition;
            final String first = code(prop.getFirst());
            final String second = code(prop.getSecond());
            final StringBuilder sb = new StringBuilder(AND);
            sb.append(L_BRACKET);
            sb.append(first);
            sb.append(COMMA);
            sb.append(SPACE);
            sb.append(second);
            sb.append(R_BRACKET);
            return sb.toString();
        }
        if (proposition instanceof Negation) {
            final Negation prop = (Negation) proposition;
            final String negprop = code(prop.getProposition());
            final StringBuilder sb = new StringBuilder(NOT);
            sb.append(L_BRACKET);
            sb.append(negprop);
            sb.append(R_BRACKET);
            return sb.toString();
        }
        return "";
    }


    /**
     * Getter for atomValidator.
     * @return atomValidator.
     */
    public IAtomicPropositionValidator getAtomValidator() {
        return atomValidator;
    }


    /**
     * Setter for atomValidator.
     * @param atomValidator the new value for atomValidator.
     */
    public void setAtomValidator(final IAtomicPropositionValidator atomValidator) {
        this.atomValidator = atomValidator;
    }


    /**
     * Getter for i18n.
     * @return i18n.
     */
    public II18NManager getI18n() {
        return i18n;
    }


    /**
     * Setter for i18n.
     * @param i18n the new value for i18n.
     */
    public void setI18n(final II18NManager i18n) {
        this.i18n = i18n;
    }

    public static void main(String[] args) {
    	
    }
    
}
