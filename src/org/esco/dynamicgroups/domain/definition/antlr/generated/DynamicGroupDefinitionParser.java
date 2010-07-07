// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g 2010-07-07 11:23:43

 package org.esco.dynamicgroups.domain.definition.antlr.generated;
 import org.esco.dynamicgroups.domain.definition.AtomicProposition;
 import org.esco.dynamicgroups.domain.definition.IProposition;
 import org.esco.dynamicgroups.domain.definition.Negation;
 import org.esco.dynamicgroups.domain.definition.Conjunction;
 import org.esco.dynamicgroups.domain.definition.Disjunction;
 import org.esco.dynamicgroups.domain.definition.antlr.error.handling.ErrorHandlerImpl;
 import org.esco.dynamicgroups.domain.definition.antlr.error.handling.IErrorHandler;
 import org.esco.dynamicgroups.domain.definition.antlr.util.ITokensEscaper;
 import org.esco.dynamicgroups.domain.definition.antlr.util.TokensEscaperImpl;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class DynamicGroupDefinitionParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "L_BRACKET", "R_BRACKET", "NOT", "AND", "OR", "EQUAL", "COMMA", "SPACE", "WS", "ESCAPE", "IDENT"
    };
    public static final int WS=12;
    public static final int ESCAPE=13;
    public static final int L_BRACKET=4;
    public static final int COMMA=10;
    public static final int EQUAL=9;
    public static final int OR=8;
    public static final int IDENT=14;
    public static final int NOT=6;
    public static final int AND=7;
    public static final int SPACE=11;
    public static final int EOF=-1;
    public static final int R_BRACKET=5;

    // delegates
    // delegators


        public DynamicGroupDefinitionParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public DynamicGroupDefinitionParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
            this.state.ruleMemo = new HashMap[16+1];
             
             
        }
        

    public String[] getTokenNames() { return DynamicGroupDefinitionParser.tokenNames; }
    public String getGrammarFileName() { return "/home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g"; }



    	/** Constant for the string or. */
    	public static final String OR_STRING = "OR";
    	
    	/** Constant for the string and. */	
    	public static final String AND_STRING = "AND";
    	
    	/** Constant for the string not. */	
    	public static final String NOT_STRING = "NOT";
    	
    	/** Constant string for the comma . */	
    	public static final String COMMA_STRING = ",";
    	
    	/** Constant string for the left bracket . */	
    	public static final String L_BRACKET_STRING = "(";
    	
    	/** Constant string for the right bracket . */	
    	public static final String R_BRACKET_STRING = ")";
    	
    	/** Constant string for the equal. */	
    	public static final String EQUAL_STRING = "=";
    	
    	/** Constant string for the escape. */	
    	public static final String ESCAPE_STRING = "\\\\";
    	
        /** Error handler to use. */
        private IErrorHandler errorHandler;
        
        /** The expression to parse. */
        private String expression;
        
        /** Util instance used to escape some tokens. */
        private static ITokensEscaper tokensEscaper = new TokensEscaperImpl();
        
        /**
         * Setter for the error handler.
         * @param errorHandler The error handler to set.
         */
        public void setErrorHandler(IErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
        }
        
        /**
         * Setter for the class member tokensEscaper.
         * @param newTokensEscaper The new tokens escaper to use for the class.
         */
        public static void setTokensEscaper(final ITokensEscaper newTokensEscaper) {
        	tokensEscaper = newTokensEscaper;
        }
        
        /**
         * Builds an instance of parser for a given string expression.
         * @param expression The string expression to parse.
         */
        public DynamicGroupDefinitionParser(final String expression) {
        	super(new CommonTokenStream(new DynamicGroupDefinitionLexer(new ANTLRStringStream(expression))));
        	this.expression = expression;
        }
        
        /**
         * Parse an expression.
         * @param expression The expression to parse.
         * @return The proposition corresponding to the expression.
         */
        public static IProposition parseExpression(final String expression) throws RecognitionException {
        	final DynamicGroupDefinitionParser parser = new DynamicGroupDefinitionParser(expression);
        	parser.setErrorHandler(new ErrorHandlerImpl());
        	return parser.evaluateExpression();
        }
        
        /**
         * Emits the error message.
         * @param line the line of the error.
         * @param charPos the position of the invalid char.
         * @param invalidChar The invalid char.
         * @param header The header of the error.
         * @param message The message associated to the error.
         */
        public void emitErrorMessage(final int line, final int charPos, final char invalidChar, 
        	final String header, final String message) {
        	
        	if (errorHandler == null) {
        		super.emitErrorMessage(header + " " + message);
        	} else {
    	        errorHandler.handleError(line, charPos, invalidChar);
    	}
        }
        
        /**
         * Displays a recognition error.
         * @param tokenNames The name of the available tokens.
         * @param e The recognition exception.
         */
         @Override
        public void displayRecognitionError(final String[] tokenNames,
                                            final RecognitionException e) {
            final int line = e.line;
            final int charPos = e.charPositionInLine;
            final char invalidChar = (char) e.c;
            final String header = getErrorHeader(e);
            final String message = getErrorMessage(e, tokenNames);
            emitErrorMessage(line, charPos, invalidChar, header, message);
        }



    // $ANTLR start "evaluateExpression"
    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:156:10: fragment evaluateExpression returns [IProposition evaluatedExpression = null] : expression ;
    public final IProposition evaluateExpression() throws RecognitionException {
        IProposition evaluatedExpression =  null;
        int evaluateExpression_StartIndex = input.index();
        IProposition expression1 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return evaluatedExpression; }
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:156:79: ( expression )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:157:2: expression
            {
            pushFollow(FOLLOW_expression_in_evaluateExpression271);
            expression1=expression();

            state._fsp--;
            if (state.failed) return evaluatedExpression;
            if ( state.backtracking==0 ) {
               evaluatedExpression = expression1; 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 1, evaluateExpression_StartIndex); }
        }
        return evaluatedExpression;
    }
    // $ANTLR end "evaluateExpression"


    // $ANTLR start "expression"
    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:160:10: fragment expression returns [IProposition proposition = null] : ( L_BRACKET e= expression R_BRACKET | not_expression | and_expression | or_expression | atomic_expression );
    public final IProposition expression() throws RecognitionException {
        IProposition proposition =  null;
        int expression_StartIndex = input.index();
        IProposition e = null;

        IProposition not_expression2 = null;

        IProposition and_expression3 = null;

        IProposition or_expression4 = null;

        IProposition atomic_expression5 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return proposition; }
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:160:62: ( L_BRACKET e= expression R_BRACKET | not_expression | and_expression | or_expression | atomic_expression )
            int alt1=5;
            switch ( input.LA(1) ) {
            case L_BRACKET:
                {
                alt1=1;
                }
                break;
            case NOT:
                {
                alt1=2;
                }
                break;
            case AND:
                {
                alt1=3;
                }
                break;
            case OR:
                {
                alt1=4;
                }
                break;
            case IDENT:
                {
                alt1=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return proposition;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:161:3: L_BRACKET e= expression R_BRACKET
                    {
                    match(input,L_BRACKET,FOLLOW_L_BRACKET_in_expression290); if (state.failed) return proposition;
                    pushFollow(FOLLOW_expression_in_expression295);
                    e=expression();

                    state._fsp--;
                    if (state.failed) return proposition;
                    match(input,R_BRACKET,FOLLOW_R_BRACKET_in_expression298); if (state.failed) return proposition;
                    if ( state.backtracking==0 ) {
                      proposition = e;
                    }

                    }
                    break;
                case 2 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:162:4: not_expression
                    {
                    pushFollow(FOLLOW_not_expression_in_expression305);
                    not_expression2=not_expression();

                    state._fsp--;
                    if (state.failed) return proposition;
                    if ( state.backtracking==0 ) {
                      proposition = not_expression2;
                    }

                    }
                    break;
                case 3 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:163:4: and_expression
                    {
                    pushFollow(FOLLOW_and_expression_in_expression312);
                    and_expression3=and_expression();

                    state._fsp--;
                    if (state.failed) return proposition;
                    if ( state.backtracking==0 ) {
                      proposition = and_expression3;
                    }

                    }
                    break;
                case 4 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:164:4: or_expression
                    {
                    pushFollow(FOLLOW_or_expression_in_expression319);
                    or_expression4=or_expression();

                    state._fsp--;
                    if (state.failed) return proposition;
                    if ( state.backtracking==0 ) {
                      proposition = or_expression4;
                    }

                    }
                    break;
                case 5 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:165:4: atomic_expression
                    {
                    pushFollow(FOLLOW_atomic_expression_in_expression326);
                    atomic_expression5=atomic_expression();

                    state._fsp--;
                    if (state.failed) return proposition;
                    if ( state.backtracking==0 ) {
                      proposition = atomic_expression5;
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 2, expression_StartIndex); }
        }
        return proposition;
    }
    // $ANTLR end "expression"


    // $ANTLR start "not_expression"
    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:168:11: fragment not_expression returns [IProposition negation = null] : NOT L_BRACKET expression R_BRACKET ;
    public final IProposition not_expression() throws RecognitionException {
        IProposition negation =  null;
        int not_expression_StartIndex = input.index();
        IProposition expression6 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return negation; }
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:169:6: ( NOT L_BRACKET expression R_BRACKET )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:169:8: NOT L_BRACKET expression R_BRACKET
            {
            match(input,NOT,FOLLOW_NOT_in_not_expression350); if (state.failed) return negation;
            match(input,L_BRACKET,FOLLOW_L_BRACKET_in_not_expression353); if (state.failed) return negation;
            pushFollow(FOLLOW_expression_in_not_expression356);
            expression6=expression();

            state._fsp--;
            if (state.failed) return negation;
            match(input,R_BRACKET,FOLLOW_R_BRACKET_in_not_expression359); if (state.failed) return negation;
            if ( state.backtracking==0 ) {
              negation = new Negation(expression6);
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 3, not_expression_StartIndex); }
        }
        return negation;
    }
    // $ANTLR end "not_expression"


    // $ANTLR start "or_expression"
    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:172:10: fragment or_expression returns [IProposition disjunction = null] : OR L_BRACKET e1= expression COMMA e2= expression R_BRACKET ;
    public final IProposition or_expression() throws RecognitionException {
        IProposition disjunction =  null;
        int or_expression_StartIndex = input.index();
        IProposition e1 = null;

        IProposition e2 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return disjunction; }
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:173:2: ( OR L_BRACKET e1= expression COMMA e2= expression R_BRACKET )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:173:4: OR L_BRACKET e1= expression COMMA e2= expression R_BRACKET
            {
            match(input,OR,FOLLOW_OR_in_or_expression376); if (state.failed) return disjunction;
            match(input,L_BRACKET,FOLLOW_L_BRACKET_in_or_expression379); if (state.failed) return disjunction;
            pushFollow(FOLLOW_expression_in_or_expression384);
            e1=expression();

            state._fsp--;
            if (state.failed) return disjunction;
            match(input,COMMA,FOLLOW_COMMA_in_or_expression387); if (state.failed) return disjunction;
            pushFollow(FOLLOW_expression_in_or_expression392);
            e2=expression();

            state._fsp--;
            if (state.failed) return disjunction;
            match(input,R_BRACKET,FOLLOW_R_BRACKET_in_or_expression395); if (state.failed) return disjunction;
            if ( state.backtracking==0 ) {
              disjunction = new Disjunction(e1, e2);
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 4, or_expression_StartIndex); }
        }
        return disjunction;
    }
    // $ANTLR end "or_expression"


    // $ANTLR start "and_expression"
    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:176:10: fragment and_expression returns [IProposition conjunction = null] : AND L_BRACKET e1= expression COMMA e2= expression R_BRACKET ;
    public final IProposition and_expression() throws RecognitionException {
        IProposition conjunction =  null;
        int and_expression_StartIndex = input.index();
        IProposition e1 = null;

        IProposition e2 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return conjunction; }
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:177:3: ( AND L_BRACKET e1= expression COMMA e2= expression R_BRACKET )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:177:5: AND L_BRACKET e1= expression COMMA e2= expression R_BRACKET
            {
            match(input,AND,FOLLOW_AND_in_and_expression414); if (state.failed) return conjunction;
            match(input,L_BRACKET,FOLLOW_L_BRACKET_in_and_expression417); if (state.failed) return conjunction;
            pushFollow(FOLLOW_expression_in_and_expression422);
            e1=expression();

            state._fsp--;
            if (state.failed) return conjunction;
            match(input,COMMA,FOLLOW_COMMA_in_and_expression425); if (state.failed) return conjunction;
            pushFollow(FOLLOW_expression_in_and_expression430);
            e2=expression();

            state._fsp--;
            if (state.failed) return conjunction;
            match(input,R_BRACKET,FOLLOW_R_BRACKET_in_and_expression433); if (state.failed) return conjunction;
            if ( state.backtracking==0 ) {
              conjunction = new Conjunction(e1, e2);
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 5, and_expression_StartIndex); }
        }
        return conjunction;
    }
    // $ANTLR end "and_expression"


    // $ANTLR start "atomic_expression"
    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:180:10: fragment atomic_expression returns [IProposition atomicExpression = null] : attribute EQUAL value ;
    public final IProposition atomic_expression() throws RecognitionException {
        IProposition atomicExpression =  null;
        int atomic_expression_StartIndex = input.index();
        String attribute7 = null;

        String value8 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return atomicExpression; }
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:181:8: ( attribute EQUAL value )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:181:11: attribute EQUAL value
            {
            pushFollow(FOLLOW_attribute_in_atomic_expression459);
            attribute7=attribute();

            state._fsp--;
            if (state.failed) return atomicExpression;
            match(input,EQUAL,FOLLOW_EQUAL_in_atomic_expression462); if (state.failed) return atomicExpression;
            pushFollow(FOLLOW_value_in_atomic_expression465);
            value8=value();

            state._fsp--;
            if (state.failed) return atomicExpression;
            if ( state.backtracking==0 ) {
              atomicExpression = new AtomicProposition(attribute7.trim(), value8.trim(), false);
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 6, atomic_expression_StartIndex); }
        }
        return atomicExpression;
    }
    // $ANTLR end "atomic_expression"


    // $ANTLR start "attribute"
    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:185:11: fragment attribute returns [String stringContent = \"\"] : m1= IDENT ;
    public final String attribute() throws RecognitionException {
        String stringContent =  "";
        int attribute_StartIndex = input.index();
        Token m1=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return stringContent; }
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:186:2: (m1= IDENT )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:186:4: m1= IDENT
            {
            m1=(Token)match(input,IDENT,FOLLOW_IDENT_in_attribute495); if (state.failed) return stringContent;
            if ( state.backtracking==0 ) {
              stringContent += (m1!=null?m1.getText():null);
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 7, attribute_StartIndex); }
        }
        return stringContent;
    }
    // $ANTLR end "attribute"


    // $ANTLR start "value"
    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:190:11: fragment value returns [String stringContent = \"\"] : m2= IDENT ;
    public final String value() throws RecognitionException {
        String stringContent =  "";
        int value_StartIndex = input.index();
        Token m2=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return stringContent; }
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:191:2: (m2= IDENT )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:191:4: m2= IDENT
            {
            m2=(Token)match(input,IDENT,FOLLOW_IDENT_in_value522); if (state.failed) return stringContent;
            if ( state.backtracking==0 ) {
              stringContent += (m2!=null?m2.getText():null);
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 8, value_StartIndex); }
        }
        return stringContent;
    }
    // $ANTLR end "value"


    // $ANTLR start "escaped"
    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:195:10: fragment escaped returns [String stringContent] : ESCAPE ( COMMA | L_BRACKET | R_BRACKET | esc2= ESCAPE ) ;
    public final String escaped() throws RecognitionException {
        String stringContent = null;
        int escaped_StartIndex = input.index();
        Token esc2=null;
        Token COMMA9=null;
        Token L_BRACKET10=null;
        Token R_BRACKET11=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return stringContent; }
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:195:49: ( ESCAPE ( COMMA | L_BRACKET | R_BRACKET | esc2= ESCAPE ) )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:195:57: ESCAPE ( COMMA | L_BRACKET | R_BRACKET | esc2= ESCAPE )
            {
            match(input,ESCAPE,FOLLOW_ESCAPE_in_escaped548); if (state.failed) return stringContent;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:195:64: ( COMMA | L_BRACKET | R_BRACKET | esc2= ESCAPE )
            int alt2=4;
            switch ( input.LA(1) ) {
            case COMMA:
                {
                alt2=1;
                }
                break;
            case L_BRACKET:
                {
                alt2=2;
                }
                break;
            case R_BRACKET:
                {
                alt2=3;
                }
                break;
            case ESCAPE:
                {
                alt2=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return stringContent;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:196:4: COMMA
                    {
                    COMMA9=(Token)match(input,COMMA,FOLLOW_COMMA_in_escaped555); if (state.failed) return stringContent;
                    if ( state.backtracking==0 ) {
                      stringContent = (COMMA9!=null?COMMA9.getText():null); 
                    }

                    }
                    break;
                case 2 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:197:6: L_BRACKET
                    {
                    L_BRACKET10=(Token)match(input,L_BRACKET,FOLLOW_L_BRACKET_in_escaped564); if (state.failed) return stringContent;
                    if ( state.backtracking==0 ) {
                      stringContent = (L_BRACKET10!=null?L_BRACKET10.getText():null); 
                    }

                    }
                    break;
                case 3 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:198:8: R_BRACKET
                    {
                    R_BRACKET11=(Token)match(input,R_BRACKET,FOLLOW_R_BRACKET_in_escaped575); if (state.failed) return stringContent;
                    if ( state.backtracking==0 ) {
                      stringContent = (R_BRACKET11!=null?R_BRACKET11.getText():null); 
                    }

                    }
                    break;
                case 4 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:199:8: esc2= ESCAPE
                    {
                    esc2=(Token)match(input,ESCAPE,FOLLOW_ESCAPE_in_escaped588); if (state.failed) return stringContent;
                    if ( state.backtracking==0 ) {
                      stringContent = (esc2!=null?esc2.getText():null); 
                    }

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 9, escaped_StartIndex); }
        }
        return stringContent;
    }
    // $ANTLR end "escaped"

    // Delegated rules


 

    public static final BitSet FOLLOW_expression_in_evaluateExpression271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_L_BRACKET_in_expression290 = new BitSet(new long[]{0x00000000000041D0L});
    public static final BitSet FOLLOW_expression_in_expression295 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_R_BRACKET_in_expression298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_not_expression_in_expression305 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_and_expression_in_expression312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_or_expression_in_expression319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomic_expression_in_expression326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_not_expression350 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_L_BRACKET_in_not_expression353 = new BitSet(new long[]{0x00000000000041D0L});
    public static final BitSet FOLLOW_expression_in_not_expression356 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_R_BRACKET_in_not_expression359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_or_expression376 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_L_BRACKET_in_or_expression379 = new BitSet(new long[]{0x00000000000041D0L});
    public static final BitSet FOLLOW_expression_in_or_expression384 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_COMMA_in_or_expression387 = new BitSet(new long[]{0x00000000000041D0L});
    public static final BitSet FOLLOW_expression_in_or_expression392 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_R_BRACKET_in_or_expression395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_and_expression414 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_L_BRACKET_in_and_expression417 = new BitSet(new long[]{0x00000000000041D0L});
    public static final BitSet FOLLOW_expression_in_and_expression422 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_COMMA_in_and_expression425 = new BitSet(new long[]{0x00000000000041D0L});
    public static final BitSet FOLLOW_expression_in_and_expression430 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_R_BRACKET_in_and_expression433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attribute_in_atomic_expression459 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_EQUAL_in_atomic_expression462 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_value_in_atomic_expression465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_attribute495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_value522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ESCAPE_in_escaped548 = new BitSet(new long[]{0x0000000000002430L});
    public static final BitSet FOLLOW_COMMA_in_escaped555 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_L_BRACKET_in_escaped564 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_R_BRACKET_in_escaped575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ESCAPE_in_escaped588 = new BitSet(new long[]{0x0000000000000002L});

}