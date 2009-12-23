grammar DynamicGroupDefinition;

options {output=text;backtrack=true; memoize=true;}

@header {
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
}

@lexer::header {
 package org.esco.dynamicgroups.domain.definition.antlr.generated;
}

// Adds some methods to handles the error.
@members {

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
}

 // Tokens.
 L_BRACKET 	: '(';
 R_BRACKET 	: ')';
 NOT 		: ('n' | 'N')('o' | 'O')('t' | 'T');
 AND 		: ('a' | 'A')('n' | 'N')('d' | 'D');
 OR 		: ('o' | 'O') ('r' | 'R');
 EQUAL 		: '=';
 COMMA 		: ',';

SPACE 	:	
(' '
 | '\t'
 );

 WS :
 (
 '\r' '\n'
 | '\n'
 ) { skip(); }
 ;


 

NUMBER : ('0'..'9');
CHAR : ~(('0'..'9'| '\\'));
ESCAPE 	: ('\\');

// Start of the expressions. 
fragment evaluateExpression returns [IProposition evaluatedExpression = null] : 
	expression { evaluatedExpression = $expression.proposition; };

// General expression.
fragment expression returns [IProposition proposition = null]: 
	 L_BRACKET (SPACE)* e=expression (SPACE)* R_BRACKET {proposition = $e.proposition;}
	| not_expression {proposition = $not_expression.negation;}
	| and_expression {proposition = $and_expression.conjunction;}
	| or_expression {proposition = $or_expression.disjunction;}
	| atomic_expression {proposition = $atomic_expression.atomicExpression;};

// Not expression.	
fragment  not_expression returns [IProposition negation = null] 
     : NOT (SPACE)* L_BRACKET (SPACE)* expression (SPACE)* R_BRACKET{negation = new Negation($expression.proposition);};

// Or expression.     
fragment or_expression returns [IProposition disjunction = null]
	: OR (SPACE)* L_BRACKET (SPACE)* e1=expression (SPACE)* COMMA (SPACE)* e2=expression (SPACE)* R_BRACKET {disjunction = new Disjunction($e1.proposition, $e2.proposition);};

// And expression.
fragment and_expression returns [IProposition conjunction = null]
 	: AND (SPACE)* L_BRACKET (SPACE)* e1=expression (SPACE)* COMMA (SPACE)* e2=expression (SPACE)* R_BRACKET {conjunction = new Conjunction($e1.proposition, $e2.proposition);};

// Atomic expression.   
fragment atomic_expression returns [IProposition atomicExpression = null] 
       :  attribute (SPACE)* EQUAL (SPACE)* value 
     {atomicExpression = new AtomicProposition($attribute.stringContent.trim(), $value.stringContent.trim(), false);} ; 

// Atribute's name.
fragment  attribute returns [String stringContent = ""] : c1=CHAR {stringContent += $c1.text;} (
	 c2 = CHAR {stringContent += $c2.text;}
	 | n = NUMBER {stringContent += $n.text;}
	 | OR {stringContent += $OR.text;}
	 | AND {stringContent += $AND.text;}
	 | NOT {stringContent += $NOT.text;}
	 | SPACE {stringContent += $SPACE.text;}
	 | escaped {stringContent = $escaped.stringContent;}
	 )*;     

// Attribute's value. 	
fragment  value returns [String stringContent = ""]: (c1=CHAR {stringContent += $c1.text;}
	 | NUMBER {stringContent += $NUMBER.text;}
	 | OR {stringContent += $OR.text;}
	 | AND {stringContent += $AND.text;}
	 | NOT {stringContent += $NOT.text;}
         | EQUAL {stringContent += $EQUAL.text;}
         | SPACE { stringContent += $SPACE.text;}
         | escaped {stringContent = $escaped.stringContent;}
	 )+;
  

// Escaped entity.  
fragment escaped returns [String stringContent] :       ESCAPE (
	  COMMA {stringContent = $COMMA.text; }
	  | L_BRACKET {stringContent = $L_BRACKET.text; }
  	  | R_BRACKET {stringContent = $R_BRACKET.text; }
  	  | esc2=ESCAPE {stringContent = $esc2.text; }
  	  );
   
