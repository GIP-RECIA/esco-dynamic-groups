/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;

import java.io.Serializable;

import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Used to decode a String that represents an IProposition.
 * @author GIP RECIA - A. Deman
 * 16 janv. 2009
 *
 */
public class PropositionCodec implements InitializingBean, Serializable {
    
    /** Serial Version UID.*/
    private static final long serialVersionUID = -67872885435776773L;

    /** Singleton. */
    private static PropositionCodec instance;
    
    /** Space constant. */
    private static final String SPACE = " ";
    
    /** Comma constant. */
    private static final char COMMA = ',';
    
    /** And constant. */
    private static final String AND = "AND";
    
    /** Or constant. */
    private static final String OR = "OR";
    
    /** Not constant. */
    private static final String NOT = "NOT";
    
    /** Open bracket constant. */
    private static final char OPEN_BRACKET = '(';
    
    /** Close bracket constant. */
    private static final char CLOSE_BRACKET = ')';
    
    /** The equal constant. */
    private static final String EQUAL = "=";
    
    /** The different constant. */
    private static final String NOT_EQUAL = "!=";
    
    /** I18n entry for the bracket errors. */
    private static final String BRACKET_ERROR = "proposition.decoding.error.bracket";

    /** I18n entry for an empty string atom. */
    private static final String EMPTY_STRING_ATOM_ERROR = "proposition.decoding.error.empty.atom";

    /** I18n entry for an empty string atom. */
    private static final String EMPTY_STRING_ERROR = "proposition.decoding.error.empty.string";
    
    /** I18n entry for the = not found. */
    private static final String EQUAL_NOT_FOUND = "proposition.decoding.error.equalnotfound";
    
    /** I18n entry for the = not found. */
    private static final String INVALID_PROP = "proposition.decoding.error.invalidproposition";
   
    /** The atom validator to use. */
    private IAtomicPropositionValidator atomValidator;
    
    /** I18N manager. */
    private transient I18NManager i18n;
   
    /**
     * Builds an instance of PropositionCodec.
     */
    protected PropositionCodec() {
        instance = this;
    }
    

    /**
     * Checks the bean injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(atomValidator, 
                "The property atomValidator in the class " + getClass().getName()
                + " can't be null.");
        Assert.notNull(i18n, 
                "The property i18n in the class " + getClass().getName()
                + " can't be null.");
    }
    
    /**
     * Gives the available instance.
     * @return The singleton.
     */
    public static PropositionCodec instance() {
        return instance;
    }
    
    /**
     * Extracts the content of a coded string (i.e. between ( and  ))
     * @param source The source string.
     * @return The string that can be decoded in a Ipropositionn if the
     * coded string is valid, the empty String otherwise.
     */
    private String extractContent(final String source) {
        final int start = source.indexOf(OPEN_BRACKET);
        final int stop = source.lastIndexOf(CLOSE_BRACKET);
        if (start >= 0 &&  stop > 0 && start < stop) {
            return source.substring(start + 1, stop).trim();
        } 
        return "";
    }
    
    /**
     * Count the number of open bracket in a string.
     * @param source The considered string.
     * @return The number of open bracket in the string.
     */
    private int countOpenBacket(final String source) {
        int nbOb = 0;
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == OPEN_BRACKET) {
                nbOb++;
            }
        }
        return nbOb;
    }
    
    /**
     * Count the number of close bracket in astring.
     * @param source The considered string.
     * @return The number of close bracket in the string.
     */
    private int countCloseBacket(final String source) {
        int nbCb = 0;
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == CLOSE_BRACKET) {
                nbCb++;
            }
        }
        return nbCb;
    }
    
    /**
     * Gives the position of a the comma to use in a conjunctive or disjunctive proposition.
     * @param source The source string.
     * @return The position of the separator.
     */
    private int getSplitPosition(final String source) {
        int openedBracket = 0;
        int index = 0;
        while (index < source.length()) {
            if (source.charAt(index) == COMMA) {
                if (openedBracket == 0) {
                    return index;
                }
            } else if (source.charAt(index) == OPEN_BRACKET) {
                openedBracket++;
            } else if (source.charAt(index) == CLOSE_BRACKET) {
                openedBracket--;
            }
            index++;

            if (openedBracket < 0) {
                return 0;
            }
        }
       return 0;
    }
    
    /**
     * Decodes a disjunction.
     * @param coded The coded string.
     * @return A Result instance that contains the proposition
     * if the coded string is valid, otherwise the result contains the invalid string.
     */
    private DecodedPropositionResult decodeDisjunction(final String coded) {
        
        if (countOpenBacket(coded) != countCloseBacket(coded)) {
            return new DecodedPropositionResult(coded, 
                    i18n.getI18nMessage(BRACKET_ERROR, coded));
        }
        
        final String content = extractContent(coded);
        int pos = getSplitPosition(content);
        if (pos == 0) {
            return new DecodedPropositionResult(coded, i18n.getI18nMessage(INVALID_PROP) + coded);
        }
        final String firstPart = content.substring(0, pos).trim();
        final String secondPart = content.substring(pos + 1).trim();
        final DecodedPropositionResult first = decode(firstPart);
        if (!first.isValid()) {
            return first;
        }
        final DecodedPropositionResult second = decode(secondPart);
        if (!second.isValid()) {
            return second;
        }
        return new DecodedPropositionResult(new Disjunction(first.getProposition(), second.getProposition()));
    }
    
    /**
     * Decodes a conjunction.
     * @param coded The coded string.
     * @return The proposition that corresponds to the conjunctionn if the
     * coded string is valid, null otherwise.
     */    
    private DecodedPropositionResult decodeConjunction(final String coded) {
        
        if (countOpenBacket(coded) != countCloseBacket(coded)) {
            return new DecodedPropositionResult(coded, 
                    i18n.getI18nMessage(BRACKET_ERROR, coded));
        }
        
        final String content = extractContent(coded);
        int pos = getSplitPosition(content);
        if (pos == 0) {
            return new DecodedPropositionResult(coded, i18n.getI18nMessage(INVALID_PROP) + coded);
        }
        final String firstPart = content.substring(0, pos).trim();
        final String secondPart = content.substring(pos + 1).trim();
        final DecodedPropositionResult first = decode(firstPart);
        if (!first.isValid()) {
            return first;
        }
        final DecodedPropositionResult second = decode(secondPart);
        if (!second.isValid()) {
            return second;
        }
        return new DecodedPropositionResult(new Conjunction(first.getProposition(), second.getProposition()));
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
     * Decodes a negation.
     * @param coded The coded string.
     * @return The proposition that corresponds to the negation if the
     * coded string is valid, null otherwise.
     */
    private DecodedPropositionResult decodeNegation(final String coded) {
        final String content = extractContent(coded);
        final DecodedPropositionResult decodedSubProp = decode(content);
        if (!decodedSubProp.isValid()) {
            return decodedSubProp;
        }
        return new DecodedPropositionResult(new Negation(decodedSubProp.getProposition()));
    }
    
    /**
     * Decodes an atomic proposition.
     * @param coded The string to decode.
     * @return The proposition if the coded string is valid, null otherwise.
     */
    private DecodedPropositionResult decodeAtomicProposition(final String coded) {
        
        if (countOpenBacket(coded) != countCloseBacket(coded)) {
            return new DecodedPropositionResult(coded, 
                    i18n.getI18nMessage(BRACKET_ERROR, coded));
        }
        String content = coded;
        final int start = coded.indexOf(OPEN_BRACKET);
        final int stop = coded.lastIndexOf(CLOSE_BRACKET);
        if (start >= 0 &&  stop > 0 && start < stop) {
            content =  coded.substring(start + 1, stop).trim();
        } 
        
        if ("".equals(content)) {
            return new DecodedPropositionResult(i18n.getI18nMessage(EMPTY_STRING_ERROR), 
                    i18n.getI18nMessage(EMPTY_STRING_ATOM_ERROR));
        }
        int pos = content.indexOf(NOT_EQUAL);
        int sepLength = NOT_EQUAL.length();
        boolean negative = true;
        if (pos < 1  || pos == content.length() - 2) {
           pos = content.indexOf(EQUAL);
           if (pos < 1  || pos == content.length() - 1) {
               return new DecodedPropositionResult(coded, i18n.getI18nMessage(EQUAL_NOT_FOUND, content));
           }
           negative = false;
           sepLength = EQUAL.length();
        }
        final String attribute = content.substring(0, pos).trim();
        final String value = content.substring(pos + sepLength).trim();
        final AtomicProposition atom = new AtomicProposition(attribute, value, negative);
        
        // checks the atom
        if (!atomValidator.isValid(atom)) {
            return new DecodedPropositionResult(content, atomValidator.explainInvalidAtom(atom));
        }
        return new DecodedPropositionResult(atom);
        
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
            return new DecodedPropositionResult("null", i18n.getI18nMessage(INVALID_PROP) + "null");
        }
        
        final String trimed = coded.trim();
        final String trimedUC = trimed.toUpperCase();
        
        if ("".equals(trimed)) {
            return new DecodedPropositionResult("", i18n.getI18nMessage(INVALID_PROP) 
                    + i18n.getI18nMessage(EMPTY_STRING_ERROR));
        }
        
        if (trimedUC.startsWith(OR)) {
            return decodeDisjunction(trimed);
        }
        
        if (trimedUC.startsWith(AND)) {
            return decodeConjunction(trimed);
        }
        
        if (trimedUC.startsWith(NOT)) {
            return decodeNegation(trimed);
        }
        
        return decodeAtomicProposition(trimed);
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
                return prop.getAttribute() + NOT_EQUAL + prop.getValue();
            }
            return prop.getAttribute() + EQUAL + prop.getValue();
        }
        if (proposition instanceof Disjunction) {
            final Disjunction prop = (Disjunction) proposition;
            final String first = code(prop.getFirst());
            final String second = code(prop.getSecond());
            final StringBuilder sb = new StringBuilder(OR);
            sb.append(OPEN_BRACKET);
            sb.append(first);
            sb.append(COMMA);
            sb.append(SPACE);
            sb.append(second);
            sb.append(CLOSE_BRACKET);
            return sb.toString();
        }
        if (proposition instanceof Conjunction) {
            final Conjunction prop = (Conjunction) proposition;
            final String first = code(prop.getFirst());
            final String second = code(prop.getSecond());
            final StringBuilder sb = new StringBuilder(AND);
            sb.append(OPEN_BRACKET);
            sb.append(first);
            sb.append(COMMA);
            sb.append(SPACE);
            sb.append(second);
            sb.append(CLOSE_BRACKET);
            return sb.toString();
        }
        if (proposition instanceof Negation) {
            final Negation prop = (Negation) proposition;
            final String negprop = code(prop.getProposition());
            final StringBuilder sb = new StringBuilder(NOT);
            sb.append(OPEN_BRACKET);
            sb.append(negprop);
            sb.append(CLOSE_BRACKET);
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
    public I18NManager getI18n() {
        return i18n;
    }


    /**
     * Setter for i18n.
     * @param i18n the new value for i18n.
     */
    public void setI18n(final I18NManager i18n) {
        this.i18n = i18n;
    }

    
}
