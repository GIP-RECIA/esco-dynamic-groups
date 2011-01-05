// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g 2010-07-07 11:23:43

 package org.esco.dynamicgroups.domain.definition.antlr.generated;


import org.antlr.runtime.*;

/**
 * DynamicGroupDefinitionLexer
 */
public class DynamicGroupDefinitionLexer extends Lexer {
    /** */
    public static final int WS=12;
    /** */
    public static final int ESCAPE=13;
    /** */
    public static final int L_BRACKET=4;
    /** */
    public static final int COMMA=10;
    /** */
    public static final int EQUAL=9;
    /** */
    public static final int OR=8;
    /** */
    public static final int IDENT=14;
    /** */
    public static final int NOT=6;
    /** */
    public static final int AND=7;
    /** */
    public static final int SPACE=11;
    /** */
    public static final int EOF=-1;
    /** */
    public static final int R_BRACKET=5;

    // delegates
    // delegators

    /**
     * Constructor of the object DynamicGroupDefinitionLexer.java
     */
    public DynamicGroupDefinitionLexer() {;} 
    /**
     * Constructor of the object DynamicGroupDefinitionLexer.java
     * @param input
     */
    public DynamicGroupDefinitionLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    /**
     * Constructor of the object DynamicGroupDefinitionLexer.java
     * @param input
     * @param state
     */
    public DynamicGroupDefinitionLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    /**
     * @return wrong
     */
    /* (non-Javadoc)
     * @see org.antlr.runtime.BaseRecognizer#getGrammarFileName()
     */
    @Override
	public String getGrammarFileName() { return "/home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g"; }

    // $ANTLR start "L_BRACKET"
    /**
     * @throws RecognitionException
     */
    public final void mL_BRACKET() throws RecognitionException {
        try {
            int _type = L_BRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:130:12: ( '(' )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:130:14: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	//TODO
        }
    }
    // $ANTLR end "L_BRACKET"

    // $ANTLR start "R_BRACKET"
    /**
     * @throws RecognitionException
     */
    public final void mR_BRACKET() throws RecognitionException {
        try {
            int _type = R_BRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:131:12: ( ')' )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:131:14: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	//TODO
        }
    }
    // $ANTLR end "R_BRACKET"

    // $ANTLR start "NOT"
    /**
     * @throws RecognitionException
     */
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:132:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:132:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	//TODO
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "AND"
    /**
     * @throws RecognitionException
     */
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:133:7: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:133:9: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        		//TODO
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "OR"
    /**
     * @throws RecognitionException
     */
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:134:6: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:134:8: ( 'o' | 'O' ) ( 'r' | 'R' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	//TODO
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "EQUAL"
    /**
     * @throws RecognitionException
     */
    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:135:9: ( '=' )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:135:11: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        		//TODO
        }
    }
    /**
     * @throws RecognitionException
     */
    // $ANTLR end "EQUAL"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:136:9: ( ',' )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:136:11: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
    		//TODO
        }
    }
    /**
     * @throws RecognitionException
     */
    // $ANTLR end "COMMA"

    // $ANTLR start "SPACE"
    public final void mSPACE() throws RecognitionException {
        try {
            int _type = SPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:138:8: ( ( ' ' | '\\t' ) )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:139:1: ( ' ' | '\\t' )
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            skip(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
    		//TODO
        }
    }
    // $ANTLR end "SPACE"

    /**
     * @throws RecognitionException
     */
    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:144:4: ( ( '\\r' '\\n' | '\\n' ) )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:145:2: ( '\\r' '\\n' | '\\n' )
            {
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:145:2: ( '\\r' '\\n' | '\\n' )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='\r') ) {
                alt1=1;
            }
            else if ( (LA1_0=='\n') ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:146:2: '\\r' '\\n'
                    {
                    match('\r'); 
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:147:4: '\\n'
                    {
                    match('\n'); 

                    }
                    break;

            }

             skip(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
    		//TODO
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "ESCAPE"
    /**
     * @throws RecognitionException
     */
    public final void mESCAPE() throws RecognitionException {
        try {
            int _type = ESCAPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:151:9: ( ( '\\\\' ) )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:151:11: ( '\\\\' )
            {
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:151:11: ( '\\\\' )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:151:12: '\\\\'
            {
            match('\\'); 

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
    		//TODO
        }
    }
    // $ANTLR end "ESCAPE"

    // $ANTLR start "IDENT"
    /**
     * @throws RecognitionException
     */
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:153:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '*' | '$' )+ )
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:153:9: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '*' | '$' )+
            {
            // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:153:9: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '*' | '$' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='$'||LA2_0=='*'||(LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:
            	    {
            	    if ( input.LA(1)=='$'||input.LA(1)=='*'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
    		//TODO
        }
    }
    // $ANTLR end "IDENT"
    /**
     * @throws RecognitionException
     */
    @Override
	public void mTokens() throws RecognitionException {
        // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:8: ( L_BRACKET | R_BRACKET | NOT | AND | OR | EQUAL | COMMA | SPACE | WS | ESCAPE | IDENT )
        int alt3=11;
        alt3 = dfa3.predict(input);
        switch (alt3) {
            case 1 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:10: L_BRACKET
                {
                mL_BRACKET(); 

                }
                break;
            case 2 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:20: R_BRACKET
                {
                mR_BRACKET(); 

                }
                break;
            case 3 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:30: NOT
                {
                mNOT(); 

                }
                break;
            case 4 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:34: AND
                {
                mAND(); 

                }
                break;
            case 5 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:38: OR
                {
                mOR(); 

                }
                break;
            case 6 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:41: EQUAL
                {
                mEQUAL(); 

                }
                break;
            case 7 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:47: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 8 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:53: SPACE
                {
                mSPACE(); 

                }
                break;
            case 9 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:59: WS
                {
                mWS(); 

                }
                break;
            case 10 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:62: ESCAPE
                {
                mESCAPE(); 

                }
                break;
            case 11 :
                // /home/thomas/workspace/ESCO-DynamicGroups-trunk/resources/DynamicGroupDefinition.g:1:69: IDENT
                {
                mIDENT(); 

                }
                break;

        }

    }


    /**    */
    protected DFA3 dfa3 = new DFA3(this);
    /**    */
    static final String DFA3_eotS =
        "\3\uffff\3\13\6\uffff\2\13\1\21\1\22\1\23\3\uffff";
    /**    */
    static final String DFA3_eofS =
        "\24\uffff";
    /**    */
    static final String DFA3_minS =
        "\1\11\2\uffff\1\117\1\116\1\122\6\uffff\1\124\1\104\3\44\3\uffff";
    /**    */
    static final String DFA3_maxS =
        "\1\172\2\uffff\1\157\1\156\1\162\6\uffff\1\164\1\144\3\172\3\uffff";
    /**    */
    static final String DFA3_acceptS =
        "\1\uffff\1\1\1\2\3\uffff\1\6\1\7\1\10\1\11\1\12\1\13\5\uffff\1\5"+
        "\1\3\1\4";
    /**    */
    static final String DFA3_specialS =
        "\24\uffff}>";
    /**    */
    static final String[] DFA3_transitionS = {
            "\1\10\1\11\2\uffff\1\11\22\uffff\1\10\3\uffff\1\13\3\uffff\1"+
            "\1\1\2\1\13\1\uffff\1\7\3\uffff\12\13\3\uffff\1\6\3\uffff\1"+
            "\4\14\13\1\3\1\5\13\13\1\uffff\1\12\2\uffff\1\13\1\uffff\1\4"+
            "\14\13\1\3\1\5\13\13",
            "",
            "",
            "\1\14\37\uffff\1\14",
            "\1\15\37\uffff\1\15",
            "\1\16\37\uffff\1\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\17\37\uffff\1\17",
            "\1\20\37\uffff\1\20",
            "\1\13\5\uffff\1\13\5\uffff\12\13\7\uffff\32\13\4\uffff\1\13"+
            "\1\uffff\32\13",
            "\1\13\5\uffff\1\13\5\uffff\12\13\7\uffff\32\13\4\uffff\1\13"+
            "\1\uffff\32\13",
            "\1\13\5\uffff\1\13\5\uffff\12\13\7\uffff\32\13\4\uffff\1\13"+
            "\1\uffff\32\13",
            "",
            "",
            ""
    };
    /**    */
    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    /**    */
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    /**    */
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    /**    */
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    /**    */
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    /**    */
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    /**    */
    static final short[][] DFA3_transition;
    /**    */
    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }
    /**    */
    class DFA3 extends DFA {
    	
        /**
         * Constructor of the object DynamicGroupDefinitionLexer.java
         * @param recognizer
         */
        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        /**
         * @return String
         * @see org.antlr.runtime.DFA#getDescription()
         */
        @Override
		public String getDescription() {
            return "1:1: Tokens : ( L_BRACKET | R_BRACKET | NOT | AND | OR | EQUAL | COMMA | SPACE | WS | ESCAPE | IDENT );";
        }
    }
 

}