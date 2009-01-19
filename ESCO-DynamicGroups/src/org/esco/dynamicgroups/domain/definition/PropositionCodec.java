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
    private static final String AND = "And";
    
    /** Or constant. */
    private static final String OR = "Or";
    
    /** Not constant. */
    private static final String NOT = "Not";
    
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
        String content = "";
        if (start >= 0 &&  stop > 0 && start < stop) {
            content = source.substring(start + 1, stop).trim();
        }
        return content;
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
     * @return The proposition that corresponds to the disjunctionn if the
     * coded string is valid, null otherwise.
     */
    private IProposition decodeDisjunction(final String coded) {
        final String content = extractContent(coded);
        int pos = getSplitPosition(content);
        if (pos == 0) {
            return null;
        }
        final String firstPart = content.substring(0, pos).trim();
        final String secondPart = content.substring(pos + 1).trim();
        final IProposition first = decode(firstPart);
        if (first == null) {
            return null;
        }
        final IProposition second = decode(secondPart);
        if (second == null) {
            return null;
        }
        return new Disjunction(first, second);
    }
    
    /**
     * Decodes a conjunction.
     * @param coded The coded string.
     * @return The proposition that corresponds to the conjunctionn if the
     * coded string is valid, null otherwise.
     */    
    private IProposition decodeConjunction(final String coded) {
        final String content = extractContent(coded);
        int pos = getSplitPosition(content);
        if (pos == 0) {
            return null;
        }
        final String firstPart = content.substring(0, pos).trim();
        final String secondPart = content.substring(pos + 1).trim();
        final IProposition first = decode(firstPart);
        if (first == null) {
            return null;
        }
        final IProposition second = decode(secondPart);
        if (second == null) {
            return null;
        }
        return new Conjunction(first, second);
    }
    
    /**
     * Decodes a negation.
     * @param coded The coded string.
     * @return The proposition that corresponds to the negation if the
     * coded string is valid, null otherwise.
     */
    private IProposition decodeNegation(final String coded) {
        final String content = extractContent(coded);
        final IProposition proposition = decode(content);
        if (proposition == null) {
            return null;
        }
        return new Negation(proposition);
    }
    
    /**
     * Decodes a base proposition.
     * @param coded The string to decode.
     * @return The proposition if the coded string is valid, null otherwise.
     */
    private IProposition decodeBaseProposition(final String coded) {
        
        int pos = coded.indexOf(NOT_EQUAL);
        int sepLength = NOT_EQUAL.length();
        boolean negative = true;
        if (pos < 1  || pos == coded.length() - 2) {
           pos = coded.indexOf(EQUAL);
           if (pos < 1  || pos == coded.length() - 2) {
               return null;
           }
           negative = false;
           sepLength = EQUAL.length();
        }
        final String attribute = coded.substring(0, pos).trim();
        final String value = coded.substring(pos + sepLength).trim();
        return new AtomicProposition(attribute, value, negative);
    }
    
    /**
     * Decodes a String that represents a proposition.
     * @param coded The coded string.
     * @return The proposition if the coded string is valid,
     * null otherwise.
     */
    public IProposition decode(final String coded) {
        final String trimed = coded.trim();
        
        if (trimed.startsWith(OR)) {
            return decodeDisjunction(trimed);
        }
        
        if (trimed.startsWith(AND)) {
            return decodeConjunction(trimed);
        }
        
        if (trimed.startsWith(NOT)) {
            return decodeNegation(trimed);
        }
        
        return decodeBaseProposition(trimed);
    }
    
    /**
     * Encodes a proposition into a String.
     * @param proposition The proposition to encode.
     * @return The String that represents the proposition.
     */
    public String encode(final IProposition proposition) {
        if (proposition instanceof AtomicProposition) {
            final AtomicProposition prop = (AtomicProposition) proposition;
            if (prop.isNegative()) {
                return prop.getAttribute() + NOT_EQUAL + prop.getValue();
            }
            return prop.getAttribute() + EQUAL + prop.getValue();
        }
        if (proposition instanceof Disjunction) {
            final Disjunction prop = (Disjunction) proposition;
            final String first = encode(prop.getFirst());
            final String second = encode(prop.getSecond());
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
            final String first = encode(prop.getFirst());
            final String second = encode(prop.getSecond());
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
            final String negprop = encode(prop.getProposition());
            final StringBuilder sb = new StringBuilder(NOT);
            sb.append(OPEN_BRACKET);
            sb.append(negprop);
            sb.append(CLOSE_BRACKET);
            return sb.toString();
        }
        return "";
    }
    
}
