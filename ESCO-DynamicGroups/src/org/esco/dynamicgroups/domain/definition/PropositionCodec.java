/**
 * 
 */
package org.esco.dynamicgroups.domain.definition;


/**
 * Used to decode a String that represents an IProposition.
 * @author GIP RECIA - A. Deman
 * 16 janv. 2009
 *
 */
public class PropositionCodec {
    
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


    /** Singleton. */
    private static final PropositionCodec INSTANCE = new PropositionCodec();
    
    /**
     * Builds an instance of PropositionCodec.
     */
    protected PropositionCodec() {
        super();
    }
    
    /**
     * Gives the available instance.
     * @return The singleton.
     */
    public static PropositionCodec instance() {
        return INSTANCE;
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
     * Count the number of open bracket in astring.
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
     * Extracts the content of a coded string (i.e. between ( and  ))
     * @param source The source string.
     * @return The string that can be decoded in a IProposition if the
     * coded string is valid, the source String otherwise.
     */
    private String extractContentSafe(final String source) {
        if (countOpenBacket(source) != countCloseBacket(source)) {
            return "";
        }
        final int start = source.indexOf(OPEN_BRACKET);
        final int stop = source.lastIndexOf(CLOSE_BRACKET);
        if (start >= 0 &&  stop > 0 && start < stop) {
            return source.substring(start + 1, stop).trim();
        } 
        
        return source;
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
        final String content = extractContent(coded);
        int pos = getSplitPosition(content);
        if (pos == 0) {
            return new DecodedPropositionResult(coded);
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
        final String content = extractContent(coded);
        int pos = getSplitPosition(content);
        if (pos == 0) {
            return null;
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
        final String content = extractContentSafe(coded);
        if ("".equals(content)) {
            return new DecodedPropositionResult(coded);
        }
        int pos = content.indexOf(NOT_EQUAL);
        int sepLength = NOT_EQUAL.length();
        boolean negative = true;
        if (pos < 1  || pos == content.length() - 2) {
           pos = content.indexOf(EQUAL);
           if (pos < 1  || pos == content.length() - 1) {
               return new DecodedPropositionResult(coded);
           }
           negative = false;
           sepLength = EQUAL.length();
        }
        final String attribute = content.substring(0, pos).trim();
        final String value = content.substring(pos + sepLength).trim();
        return new DecodedPropositionResult(new AtomicProposition(attribute, value, negative));
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
            return new DecodedPropositionResult("null");
        }
        
        final String trimed = coded.trim();
        final String trimedUC = trimed.toUpperCase();
        
        if ("".equals(trimed)) {
            return new DecodedPropositionResult("Empty string");
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
    
}
