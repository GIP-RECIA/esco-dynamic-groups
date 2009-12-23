// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g 2009-12-22 11:22:57

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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "L_BRACKET", "R_BRACKET", "NOT", "AND", "OR", "EQUAL", "COMMA", "SPACE", "WS", "NUMBER", "CHAR", "ESCAPE"
    };
    public static final int WS=12;
    public static final int ESCAPE=15;
    public static final int L_BRACKET=4;
    public static final int COMMA=10;
    public static final int EQUAL=9;
    public static final int OR=8;
    public static final int NUMBER=13;
    public static final int CHAR=14;
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
            this.state.ruleMemo = new HashMap[48+1];
             
             
        }
        

    public String[] getTokenNames() { return DynamicGroupDefinitionParser.tokenNames; }
    public String getGrammarFileName() { return "/home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g"; }



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
    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:158:10: fragment evaluateExpression returns [IProposition evaluatedExpression = null] : expression ;
    public final IProposition evaluateExpression() throws RecognitionException {
        IProposition evaluatedExpression =  null;
        int evaluateExpression_StartIndex = input.index();
        IProposition expression1 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return evaluatedExpression; }
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:158:79: ( expression )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:159:2: expression
            {
            pushFollow(FOLLOW_expression_in_evaluateExpression272);
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
    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:162:10: fragment expression returns [IProposition proposition = null] : ( L_BRACKET ( SPACE )* e= expression ( SPACE )* R_BRACKET | not_expression | and_expression | or_expression | atomic_expression );
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
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:162:62: ( L_BRACKET ( SPACE )* e= expression ( SPACE )* R_BRACKET | not_expression | and_expression | or_expression | atomic_expression )
            int alt3=5;
            switch ( input.LA(1) ) {
            case L_BRACKET:
                {
                alt3=1;
                }
                break;
            case NOT:
                {
                alt3=2;
                }
                break;
            case AND:
                {
                alt3=3;
                }
                break;
            case OR:
                {
                alt3=4;
                }
                break;
            case CHAR:
                {
                alt3=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return proposition;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:163:3: L_BRACKET ( SPACE )* e= expression ( SPACE )* R_BRACKET
                    {
                    match(input,L_BRACKET,FOLLOW_L_BRACKET_in_expression291); if (state.failed) return proposition;
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:163:13: ( SPACE )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0==SPACE) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:163:14: SPACE
                    	    {
                    	    match(input,SPACE,FOLLOW_SPACE_in_expression294); if (state.failed) return proposition;

                    	    }
                    	    break;

                    	default :
                    	    break loop1;
                        }
                    } while (true);

                    pushFollow(FOLLOW_expression_in_expression300);
                    e=expression();

                    state._fsp--;
                    if (state.failed) return proposition;
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:163:35: ( SPACE )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0==SPACE) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:163:36: SPACE
                    	    {
                    	    match(input,SPACE,FOLLOW_SPACE_in_expression303); if (state.failed) return proposition;

                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);

                    match(input,R_BRACKET,FOLLOW_R_BRACKET_in_expression307); if (state.failed) return proposition;
                    if ( state.backtracking==0 ) {
                      proposition = e;
                    }

                    }
                    break;
                case 2 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:164:4: not_expression
                    {
                    pushFollow(FOLLOW_not_expression_in_expression314);
                    not_expression2=not_expression();

                    state._fsp--;
                    if (state.failed) return proposition;
                    if ( state.backtracking==0 ) {
                      proposition = not_expression2;
                    }

                    }
                    break;
                case 3 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:165:4: and_expression
                    {
                    pushFollow(FOLLOW_and_expression_in_expression321);
                    and_expression3=and_expression();

                    state._fsp--;
                    if (state.failed) return proposition;
                    if ( state.backtracking==0 ) {
                      proposition = and_expression3;
                    }

                    }
                    break;
                case 4 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:166:4: or_expression
                    {
                    pushFollow(FOLLOW_or_expression_in_expression328);
                    or_expression4=or_expression();

                    state._fsp--;
                    if (state.failed) return proposition;
                    if ( state.backtracking==0 ) {
                      proposition = or_expression4;
                    }

                    }
                    break;
                case 5 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:167:4: atomic_expression
                    {
                    pushFollow(FOLLOW_atomic_expression_in_expression335);
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
    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:170:11: fragment not_expression returns [IProposition negation = null] : NOT ( SPACE )* L_BRACKET ( SPACE )* expression ( SPACE )* R_BRACKET ;
    public final IProposition not_expression() throws RecognitionException {
        IProposition negation =  null;
        int not_expression_StartIndex = input.index();
        IProposition expression6 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return negation; }
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:171:6: ( NOT ( SPACE )* L_BRACKET ( SPACE )* expression ( SPACE )* R_BRACKET )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:171:8: NOT ( SPACE )* L_BRACKET ( SPACE )* expression ( SPACE )* R_BRACKET
            {
            match(input,NOT,FOLLOW_NOT_in_not_expression359); if (state.failed) return negation;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:171:12: ( SPACE )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==SPACE) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:171:13: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_not_expression362); if (state.failed) return negation;

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            match(input,L_BRACKET,FOLLOW_L_BRACKET_in_not_expression366); if (state.failed) return negation;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:171:31: ( SPACE )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==SPACE) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:171:32: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_not_expression369); if (state.failed) return negation;

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            pushFollow(FOLLOW_expression_in_not_expression373);
            expression6=expression();

            state._fsp--;
            if (state.failed) return negation;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:171:51: ( SPACE )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==SPACE) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:171:52: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_not_expression376); if (state.failed) return negation;

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            match(input,R_BRACKET,FOLLOW_R_BRACKET_in_not_expression380); if (state.failed) return negation;
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
    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:174:10: fragment or_expression returns [IProposition disjunction = null] : OR ( SPACE )* L_BRACKET ( SPACE )* e1= expression ( SPACE )* COMMA ( SPACE )* e2= expression ( SPACE )* R_BRACKET ;
    public final IProposition or_expression() throws RecognitionException {
        IProposition disjunction =  null;
        int or_expression_StartIndex = input.index();
        IProposition e1 = null;

        IProposition e2 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return disjunction; }
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:2: ( OR ( SPACE )* L_BRACKET ( SPACE )* e1= expression ( SPACE )* COMMA ( SPACE )* e2= expression ( SPACE )* R_BRACKET )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:4: OR ( SPACE )* L_BRACKET ( SPACE )* e1= expression ( SPACE )* COMMA ( SPACE )* e2= expression ( SPACE )* R_BRACKET
            {
            match(input,OR,FOLLOW_OR_in_or_expression397); if (state.failed) return disjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:7: ( SPACE )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==SPACE) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:8: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_or_expression400); if (state.failed) return disjunction;

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            match(input,L_BRACKET,FOLLOW_L_BRACKET_in_or_expression404); if (state.failed) return disjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:26: ( SPACE )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==SPACE) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:27: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_or_expression407); if (state.failed) return disjunction;

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            pushFollow(FOLLOW_expression_in_or_expression413);
            e1=expression();

            state._fsp--;
            if (state.failed) return disjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:49: ( SPACE )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==SPACE) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:50: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_or_expression416); if (state.failed) return disjunction;

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            match(input,COMMA,FOLLOW_COMMA_in_or_expression420); if (state.failed) return disjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:64: ( SPACE )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==SPACE) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:65: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_or_expression423); if (state.failed) return disjunction;

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            pushFollow(FOLLOW_expression_in_or_expression429);
            e2=expression();

            state._fsp--;
            if (state.failed) return disjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:87: ( SPACE )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==SPACE) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:175:88: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_or_expression432); if (state.failed) return disjunction;

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

            match(input,R_BRACKET,FOLLOW_R_BRACKET_in_or_expression436); if (state.failed) return disjunction;
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
    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:178:10: fragment and_expression returns [IProposition conjunction = null] : AND ( SPACE )* L_BRACKET ( SPACE )* e1= expression ( SPACE )* COMMA ( SPACE )* e2= expression ( SPACE )* R_BRACKET ;
    public final IProposition and_expression() throws RecognitionException {
        IProposition conjunction =  null;
        int and_expression_StartIndex = input.index();
        IProposition e1 = null;

        IProposition e2 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return conjunction; }
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:3: ( AND ( SPACE )* L_BRACKET ( SPACE )* e1= expression ( SPACE )* COMMA ( SPACE )* e2= expression ( SPACE )* R_BRACKET )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:5: AND ( SPACE )* L_BRACKET ( SPACE )* e1= expression ( SPACE )* COMMA ( SPACE )* e2= expression ( SPACE )* R_BRACKET
            {
            match(input,AND,FOLLOW_AND_in_and_expression455); if (state.failed) return conjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:9: ( SPACE )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==SPACE) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:10: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_and_expression458); if (state.failed) return conjunction;

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            match(input,L_BRACKET,FOLLOW_L_BRACKET_in_and_expression462); if (state.failed) return conjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:28: ( SPACE )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==SPACE) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:29: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_and_expression465); if (state.failed) return conjunction;

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

            pushFollow(FOLLOW_expression_in_and_expression471);
            e1=expression();

            state._fsp--;
            if (state.failed) return conjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:51: ( SPACE )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==SPACE) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:52: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_and_expression474); if (state.failed) return conjunction;

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            match(input,COMMA,FOLLOW_COMMA_in_and_expression478); if (state.failed) return conjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:66: ( SPACE )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==SPACE) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:67: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_and_expression481); if (state.failed) return conjunction;

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            pushFollow(FOLLOW_expression_in_and_expression487);
            e2=expression();

            state._fsp--;
            if (state.failed) return conjunction;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:89: ( SPACE )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==SPACE) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:179:90: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_and_expression490); if (state.failed) return conjunction;

            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            match(input,R_BRACKET,FOLLOW_R_BRACKET_in_and_expression494); if (state.failed) return conjunction;
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
    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:182:10: fragment atomic_expression returns [IProposition atomicExpression = null] : attribute ( SPACE )* EQUAL ( SPACE )* value ;
    public final IProposition atomic_expression() throws RecognitionException {
        IProposition atomicExpression =  null;
        int atomic_expression_StartIndex = input.index();
        String attribute7 = null;

        String value8 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return atomicExpression; }
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:183:8: ( attribute ( SPACE )* EQUAL ( SPACE )* value )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:183:11: attribute ( SPACE )* EQUAL ( SPACE )* value
            {
            pushFollow(FOLLOW_attribute_in_atomic_expression520);
            attribute7=attribute();

            state._fsp--;
            if (state.failed) return atomicExpression;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:183:21: ( SPACE )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==SPACE) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:183:22: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_atomic_expression523); if (state.failed) return atomicExpression;

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            match(input,EQUAL,FOLLOW_EQUAL_in_atomic_expression527); if (state.failed) return atomicExpression;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:183:36: ( SPACE )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==SPACE) ) {
                    int LA18_2 = input.LA(2);

                    if ( (synpred21_DynamicGroupDefinition()) ) {
                        alt18=1;
                    }


                }


                switch (alt18) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:183:37: SPACE
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_atomic_expression530); if (state.failed) return atomicExpression;

            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            pushFollow(FOLLOW_value_in_atomic_expression534);
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
    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:187:11: fragment attribute returns [String stringContent = \"\"] : c1= CHAR (c2= CHAR | n= NUMBER | OR | AND | NOT | SPACE | escaped )* ;
    public final String attribute() throws RecognitionException {
        String stringContent =  "";
        int attribute_StartIndex = input.index();
        Token c1=null;
        Token c2=null;
        Token n=null;
        Token OR9=null;
        Token AND10=null;
        Token NOT11=null;
        Token SPACE12=null;
        String escaped13 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return stringContent; }
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:187:57: (c1= CHAR (c2= CHAR | n= NUMBER | OR | AND | NOT | SPACE | escaped )* )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:187:59: c1= CHAR (c2= CHAR | n= NUMBER | OR | AND | NOT | SPACE | escaped )*
            {
            c1=(Token)match(input,CHAR,FOLLOW_CHAR_in_attribute562); if (state.failed) return stringContent;
            if ( state.backtracking==0 ) {
              stringContent += (c1!=null?c1.getText():null);
            }
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:187:96: (c2= CHAR | n= NUMBER | OR | AND | NOT | SPACE | escaped )*
            loop19:
            do {
                int alt19=8;
                alt19 = dfa19.predict(input);
                switch (alt19) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:188:3: c2= CHAR
            	    {
            	    c2=(Token)match(input,CHAR,FOLLOW_CHAR_in_attribute574); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (c2!=null?c2.getText():null);
            	    }

            	    }
            	    break;
            	case 2 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:189:5: n= NUMBER
            	    {
            	    n=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_attribute586); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (n!=null?n.getText():null);
            	    }

            	    }
            	    break;
            	case 3 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:190:5: OR
            	    {
            	    OR9=(Token)match(input,OR,FOLLOW_OR_in_attribute594); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (OR9!=null?OR9.getText():null);
            	    }

            	    }
            	    break;
            	case 4 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:191:5: AND
            	    {
            	    AND10=(Token)match(input,AND,FOLLOW_AND_in_attribute602); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (AND10!=null?AND10.getText():null);
            	    }

            	    }
            	    break;
            	case 5 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:192:5: NOT
            	    {
            	    NOT11=(Token)match(input,NOT,FOLLOW_NOT_in_attribute610); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (NOT11!=null?NOT11.getText():null);
            	    }

            	    }
            	    break;
            	case 6 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:193:5: SPACE
            	    {
            	    SPACE12=(Token)match(input,SPACE,FOLLOW_SPACE_in_attribute618); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (SPACE12!=null?SPACE12.getText():null);
            	    }

            	    }
            	    break;
            	case 7 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:194:5: escaped
            	    {
            	    pushFollow(FOLLOW_escaped_in_attribute626);
            	    escaped13=escaped();

            	    state._fsp--;
            	    if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent = escaped13;
            	    }

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);


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
    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:198:11: fragment value returns [String stringContent = \"\"] : (c1= CHAR | NUMBER | OR | AND | NOT | EQUAL | SPACE | escaped )+ ;
    public final String value() throws RecognitionException {
        String stringContent =  "";
        int value_StartIndex = input.index();
        Token c1=null;
        Token NUMBER14=null;
        Token OR15=null;
        Token AND16=null;
        Token NOT17=null;
        Token EQUAL18=null;
        Token SPACE19=null;
        String escaped20 = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return stringContent; }
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:198:52: ( (c1= CHAR | NUMBER | OR | AND | NOT | EQUAL | SPACE | escaped )+ )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:198:54: (c1= CHAR | NUMBER | OR | AND | NOT | EQUAL | SPACE | escaped )+
            {
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:198:54: (c1= CHAR | NUMBER | OR | AND | NOT | EQUAL | SPACE | escaped )+
            int cnt20=0;
            loop20:
            do {
                int alt20=9;
                alt20 = dfa20.predict(input);
                switch (alt20) {
            	case 1 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:198:55: c1= CHAR
            	    {
            	    c1=(Token)match(input,CHAR,FOLLOW_CHAR_in_value656); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (c1!=null?c1.getText():null);
            	    }

            	    }
            	    break;
            	case 2 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:199:5: NUMBER
            	    {
            	    NUMBER14=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_value664); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (NUMBER14!=null?NUMBER14.getText():null);
            	    }

            	    }
            	    break;
            	case 3 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:200:5: OR
            	    {
            	    OR15=(Token)match(input,OR,FOLLOW_OR_in_value672); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (OR15!=null?OR15.getText():null);
            	    }

            	    }
            	    break;
            	case 4 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:201:5: AND
            	    {
            	    AND16=(Token)match(input,AND,FOLLOW_AND_in_value680); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (AND16!=null?AND16.getText():null);
            	    }

            	    }
            	    break;
            	case 5 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:202:5: NOT
            	    {
            	    NOT17=(Token)match(input,NOT,FOLLOW_NOT_in_value688); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (NOT17!=null?NOT17.getText():null);
            	    }

            	    }
            	    break;
            	case 6 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:203:12: EQUAL
            	    {
            	    EQUAL18=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_value703); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent += (EQUAL18!=null?EQUAL18.getText():null);
            	    }

            	    }
            	    break;
            	case 7 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:204:12: SPACE
            	    {
            	    SPACE19=(Token)match(input,SPACE,FOLLOW_SPACE_in_value718); if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	       stringContent += (SPACE19!=null?SPACE19.getText():null);
            	    }

            	    }
            	    break;
            	case 8 :
            	    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:205:12: escaped
            	    {
            	    pushFollow(FOLLOW_escaped_in_value733);
            	    escaped20=escaped();

            	    state._fsp--;
            	    if (state.failed) return stringContent;
            	    if ( state.backtracking==0 ) {
            	      stringContent = escaped20;
            	    }

            	    }
            	    break;

            	default :
            	    if ( cnt20 >= 1 ) break loop20;
            	    if (state.backtracking>0) {state.failed=true; return stringContent;}
                        EarlyExitException eee =
                            new EarlyExitException(20, input);
                        throw eee;
                }
                cnt20++;
            } while (true);


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
    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:210:10: fragment escaped returns [String stringContent] : ESCAPE ( COMMA | L_BRACKET | R_BRACKET | esc2= ESCAPE ) ;
    public final String escaped() throws RecognitionException {
        String stringContent = null;
        int escaped_StartIndex = input.index();
        Token esc2=null;
        Token COMMA21=null;
        Token L_BRACKET22=null;
        Token R_BRACKET23=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return stringContent; }
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:210:49: ( ESCAPE ( COMMA | L_BRACKET | R_BRACKET | esc2= ESCAPE ) )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:210:57: ESCAPE ( COMMA | L_BRACKET | R_BRACKET | esc2= ESCAPE )
            {
            match(input,ESCAPE,FOLLOW_ESCAPE_in_escaped764); if (state.failed) return stringContent;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:210:64: ( COMMA | L_BRACKET | R_BRACKET | esc2= ESCAPE )
            int alt21=4;
            switch ( input.LA(1) ) {
            case COMMA:
                {
                alt21=1;
                }
                break;
            case L_BRACKET:
                {
                alt21=2;
                }
                break;
            case R_BRACKET:
                {
                alt21=3;
                }
                break;
            case ESCAPE:
                {
                alt21=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return stringContent;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:211:4: COMMA
                    {
                    COMMA21=(Token)match(input,COMMA,FOLLOW_COMMA_in_escaped771); if (state.failed) return stringContent;
                    if ( state.backtracking==0 ) {
                      stringContent = (COMMA21!=null?COMMA21.getText():null); 
                    }

                    }
                    break;
                case 2 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:212:6: L_BRACKET
                    {
                    L_BRACKET22=(Token)match(input,L_BRACKET,FOLLOW_L_BRACKET_in_escaped780); if (state.failed) return stringContent;
                    if ( state.backtracking==0 ) {
                      stringContent = (L_BRACKET22!=null?L_BRACKET22.getText():null); 
                    }

                    }
                    break;
                case 3 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:213:8: R_BRACKET
                    {
                    R_BRACKET23=(Token)match(input,R_BRACKET,FOLLOW_R_BRACKET_in_escaped791); if (state.failed) return stringContent;
                    if ( state.backtracking==0 ) {
                      stringContent = (R_BRACKET23!=null?R_BRACKET23.getText():null); 
                    }

                    }
                    break;
                case 4 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:214:8: esc2= ESCAPE
                    {
                    esc2=(Token)match(input,ESCAPE,FOLLOW_ESCAPE_in_escaped804); if (state.failed) return stringContent;
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

    // $ANTLR start synpred21_DynamicGroupDefinition
    public final void synpred21_DynamicGroupDefinition_fragment() throws RecognitionException {   
        // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:183:37: ( SPACE )
        // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:183:37: SPACE
        {
        match(input,SPACE,FOLLOW_SPACE_in_synpred21_DynamicGroupDefinition530); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred21_DynamicGroupDefinition

    // $ANTLR start synpred27_DynamicGroupDefinition
    public final void synpred27_DynamicGroupDefinition_fragment() throws RecognitionException {   
        // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:193:5: ( SPACE )
        // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:193:5: SPACE
        {
        match(input,SPACE,FOLLOW_SPACE_in_synpred27_DynamicGroupDefinition618); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred27_DynamicGroupDefinition

    // $ANTLR start synpred35_DynamicGroupDefinition
    public final void synpred35_DynamicGroupDefinition_fragment() throws RecognitionException {   
        // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:204:12: ( SPACE )
        // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:204:12: SPACE
        {
        match(input,SPACE,FOLLOW_SPACE_in_synpred35_DynamicGroupDefinition718); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred35_DynamicGroupDefinition

    // Delegated rules

    public final boolean synpred27_DynamicGroupDefinition() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred27_DynamicGroupDefinition_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred35_DynamicGroupDefinition() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred35_DynamicGroupDefinition_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred21_DynamicGroupDefinition() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred21_DynamicGroupDefinition_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA19 dfa19 = new DFA19(this);
    protected DFA20 dfa20 = new DFA20(this);
    static final String DFA19_eotS =
        "\12\uffff";
    static final String DFA19_eofS =
        "\12\uffff";
    static final String DFA19_minS =
        "\1\6\1\0\10\uffff";
    static final String DFA19_maxS =
        "\1\17\1\0\10\uffff";
    static final String DFA19_acceptS =
        "\2\uffff\1\10\1\1\1\2\1\3\1\4\1\5\1\7\1\6";
    static final String DFA19_specialS =
        "\1\uffff\1\0\10\uffff}>";
    static final String[] DFA19_transitionS = {
            "\1\7\1\6\1\5\1\2\1\uffff\1\1\1\uffff\1\4\1\3\1\10",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
    static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
    static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
    static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
    static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
    static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
    static final short[][] DFA19_transition;

    static {
        int numStates = DFA19_transitionS.length;
        DFA19_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
        }
    }

    class DFA19 extends DFA {

        public DFA19(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 19;
            this.eot = DFA19_eot;
            this.eof = DFA19_eof;
            this.min = DFA19_min;
            this.max = DFA19_max;
            this.accept = DFA19_accept;
            this.special = DFA19_special;
            this.transition = DFA19_transition;
        }
        public String getDescription() {
            return "()* loopback of 187:96: (c2= CHAR | n= NUMBER | OR | AND | NOT | SPACE | escaped )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA19_1 = input.LA(1);

                         
                        int index19_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_DynamicGroupDefinition()) ) {s = 9;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index19_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 19, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA20_eotS =
        "\13\uffff";
    static final String DFA20_eofS =
        "\1\1\12\uffff";
    static final String DFA20_minS =
        "\1\5\1\uffff\1\0\10\uffff";
    static final String DFA20_maxS =
        "\1\17\1\uffff\1\0\10\uffff";
    static final String DFA20_acceptS =
        "\1\uffff\1\11\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\10\1\7";
    static final String DFA20_specialS =
        "\2\uffff\1\0\10\uffff}>";
    static final String[] DFA20_transitionS = {
            "\1\1\1\7\1\6\1\5\1\10\1\1\1\2\1\uffff\1\4\1\3\1\11",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "()+ loopback of 198:54: (c1= CHAR | NUMBER | OR | AND | NOT | EQUAL | SPACE | escaped )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA20_2 = input.LA(1);

                         
                        int index20_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred35_DynamicGroupDefinition()) ) {s = 10;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index20_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 20, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_expression_in_evaluateExpression272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_L_BRACKET_in_expression291 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_SPACE_in_expression294 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_expression_in_expression300 = new BitSet(new long[]{0x0000000000000820L});
    public static final BitSet FOLLOW_SPACE_in_expression303 = new BitSet(new long[]{0x0000000000000820L});
    public static final BitSet FOLLOW_R_BRACKET_in_expression307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_not_expression_in_expression314 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_and_expression_in_expression321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_or_expression_in_expression328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomic_expression_in_expression335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_not_expression359 = new BitSet(new long[]{0x0000000000000810L});
    public static final BitSet FOLLOW_SPACE_in_not_expression362 = new BitSet(new long[]{0x0000000000000810L});
    public static final BitSet FOLLOW_L_BRACKET_in_not_expression366 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_SPACE_in_not_expression369 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_expression_in_not_expression373 = new BitSet(new long[]{0x0000000000000820L});
    public static final BitSet FOLLOW_SPACE_in_not_expression376 = new BitSet(new long[]{0x0000000000000820L});
    public static final BitSet FOLLOW_R_BRACKET_in_not_expression380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_or_expression397 = new BitSet(new long[]{0x0000000000000810L});
    public static final BitSet FOLLOW_SPACE_in_or_expression400 = new BitSet(new long[]{0x0000000000000810L});
    public static final BitSet FOLLOW_L_BRACKET_in_or_expression404 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_SPACE_in_or_expression407 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_expression_in_or_expression413 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_SPACE_in_or_expression416 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_COMMA_in_or_expression420 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_SPACE_in_or_expression423 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_expression_in_or_expression429 = new BitSet(new long[]{0x0000000000000820L});
    public static final BitSet FOLLOW_SPACE_in_or_expression432 = new BitSet(new long[]{0x0000000000000820L});
    public static final BitSet FOLLOW_R_BRACKET_in_or_expression436 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_and_expression455 = new BitSet(new long[]{0x0000000000000810L});
    public static final BitSet FOLLOW_SPACE_in_and_expression458 = new BitSet(new long[]{0x0000000000000810L});
    public static final BitSet FOLLOW_L_BRACKET_in_and_expression462 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_SPACE_in_and_expression465 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_expression_in_and_expression471 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_SPACE_in_and_expression474 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_COMMA_in_and_expression478 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_SPACE_in_and_expression481 = new BitSet(new long[]{0x00000000000049D0L});
    public static final BitSet FOLLOW_expression_in_and_expression487 = new BitSet(new long[]{0x0000000000000820L});
    public static final BitSet FOLLOW_SPACE_in_and_expression490 = new BitSet(new long[]{0x0000000000000820L});
    public static final BitSet FOLLOW_R_BRACKET_in_and_expression494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_attribute_in_atomic_expression520 = new BitSet(new long[]{0x0000000000000A00L});
    public static final BitSet FOLLOW_SPACE_in_atomic_expression523 = new BitSet(new long[]{0x0000000000000A00L});
    public static final BitSet FOLLOW_EQUAL_in_atomic_expression527 = new BitSet(new long[]{0x000000000000EBC0L});
    public static final BitSet FOLLOW_SPACE_in_atomic_expression530 = new BitSet(new long[]{0x000000000000EBC0L});
    public static final BitSet FOLLOW_value_in_atomic_expression534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_in_attribute562 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_CHAR_in_attribute574 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_NUMBER_in_attribute586 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_OR_in_attribute594 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_AND_in_attribute602 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_NOT_in_attribute610 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_SPACE_in_attribute618 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_escaped_in_attribute626 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_CHAR_in_value656 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_NUMBER_in_value664 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_OR_in_value672 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_AND_in_value680 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_NOT_in_value688 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_EQUAL_in_value703 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_SPACE_in_value718 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_escaped_in_value733 = new BitSet(new long[]{0x000000000000EBC2L});
    public static final BitSet FOLLOW_ESCAPE_in_escaped764 = new BitSet(new long[]{0x0000000000008430L});
    public static final BitSet FOLLOW_COMMA_in_escaped771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_L_BRACKET_in_escaped780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_R_BRACKET_in_escaped791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ESCAPE_in_escaped804 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SPACE_in_synpred21_DynamicGroupDefinition530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SPACE_in_synpred27_DynamicGroupDefinition618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SPACE_in_synpred35_DynamicGroupDefinition718 = new BitSet(new long[]{0x0000000000000002L});

}