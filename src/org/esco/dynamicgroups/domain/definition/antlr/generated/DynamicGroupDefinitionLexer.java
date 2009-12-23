// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g 2009-12-22 11:22:57

 package org.esco.dynamicgroups.domain.definition.antlr.generated;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class DynamicGroupDefinitionLexer extends Lexer {
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

    public DynamicGroupDefinitionLexer() {;} 
    public DynamicGroupDefinitionLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public DynamicGroupDefinitionLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g"; }

    // $ANTLR start "L_BRACKET"
    public final void mL_BRACKET() throws RecognitionException {
        try {
            int _type = L_BRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:130:12: ( '(' )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:130:14: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "L_BRACKET"

    // $ANTLR start "R_BRACKET"
    public final void mR_BRACKET() throws RecognitionException {
        try {
            int _type = R_BRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:131:12: ( ')' )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:131:14: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "R_BRACKET"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:132:7: ( ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' ) )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:132:9: ( 'n' | 'N' ) ( 'o' | 'O' ) ( 't' | 'T' )
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
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:133:7: ( ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' ) )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:133:9: ( 'a' | 'A' ) ( 'n' | 'N' ) ( 'd' | 'D' )
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
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:134:6: ( ( 'o' | 'O' ) ( 'r' | 'R' ) )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:134:8: ( 'o' | 'O' ) ( 'r' | 'R' )
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
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "EQUAL"
    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:135:9: ( '=' )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:135:11: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUAL"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:136:9: ( ',' )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:136:11: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "SPACE"
    public final void mSPACE() throws RecognitionException {
        try {
            int _type = SPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:138:8: ( ( ' ' | '\\t' ) )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:139:1: ( ' ' | '\\t' )
            {
            if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
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
        }
    }
    // $ANTLR end "SPACE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:143:4: ( ( '\\r' '\\n' | '\\n' ) )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:144:2: ( '\\r' '\\n' | '\\n' )
            {
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:144:2: ( '\\r' '\\n' | '\\n' )
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
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:145:2: '\\r' '\\n'
                    {
                    match('\r'); 
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:146:4: '\\n'
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
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:153:8: ( ( '0' .. '9' ) )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:153:10: ( '0' .. '9' )
            {
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:153:10: ( '0' .. '9' )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:153:11: '0' .. '9'
            {
            matchRange('0','9'); 

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "CHAR"
    public final void mCHAR() throws RecognitionException {
        try {
            int _type = CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:154:6: (~ ( ( '0' .. '9' | '\\\\' ) ) )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:154:8: ~ ( ( '0' .. '9' | '\\\\' ) )
            {
            if ( (input.LA(1)>='\u0000' && input.LA(1)<='/')||(input.LA(1)>=':' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
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
        }
    }
    // $ANTLR end "CHAR"

    // $ANTLR start "ESCAPE"
    public final void mESCAPE() throws RecognitionException {
        try {
            int _type = ESCAPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:155:9: ( ( '\\\\' ) )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:155:11: ( '\\\\' )
            {
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:155:11: ( '\\\\' )
            // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:155:12: '\\\\'
            {
            match('\\'); 

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ESCAPE"

    public void mTokens() throws RecognitionException {
        // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:8: ( L_BRACKET | R_BRACKET | NOT | AND | OR | EQUAL | COMMA | SPACE | WS | NUMBER | CHAR | ESCAPE )
        int alt2=12;
        alt2 = dfa2.predict(input);
        switch (alt2) {
            case 1 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:10: L_BRACKET
                {
                mL_BRACKET(); 

                }
                break;
            case 2 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:20: R_BRACKET
                {
                mR_BRACKET(); 

                }
                break;
            case 3 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:30: NOT
                {
                mNOT(); 

                }
                break;
            case 4 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:34: AND
                {
                mAND(); 

                }
                break;
            case 5 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:38: OR
                {
                mOR(); 

                }
                break;
            case 6 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:41: EQUAL
                {
                mEQUAL(); 

                }
                break;
            case 7 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:47: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 8 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:53: SPACE
                {
                mSPACE(); 

                }
                break;
            case 9 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:59: WS
                {
                mWS(); 

                }
                break;
            case 10 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:62: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 11 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:69: CHAR
                {
                mCHAR(); 

                }
                break;
            case 12 :
                // /home/arnaud/.Travaux/RECIA/ESCO-DynamicGroups/resources/DynamicGroupDefinition.g:1:74: ESCAPE
                {
                mESCAPE(); 

                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
        "\3\uffff\3\14\3\uffff\1\14\15\uffff";
    static final String DFA2_eofS =
        "\27\uffff";
    static final String DFA2_minS =
        "\1\0\2\uffff\1\117\1\116\1\122\3\uffff\1\12\15\uffff";
    static final String DFA2_maxS =
        "\1\uffff\2\uffff\1\157\1\156\1\162\3\uffff\1\12\15\uffff";
    static final String DFA2_acceptS =
        "\1\uffff\1\1\1\2\3\uffff\1\6\1\7\1\10\1\uffff\1\11\1\12\1\13\1\14"+
        "\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11";
    static final String DFA2_specialS =
        "\1\0\26\uffff}>";
    static final String[] DFA2_transitionS = {
            "\11\14\1\10\1\12\2\14\1\11\22\14\1\10\7\14\1\1\1\2\2\14\1\7"+
            "\3\14\12\13\3\14\1\6\3\14\1\4\14\14\1\3\1\5\14\14\1\15\4\14"+
            "\1\4\14\14\1\3\1\5\uff90\14",
            "",
            "",
            "\1\20\37\uffff\1\20",
            "\1\21\37\uffff\1\21",
            "\1\22\37\uffff\1\22",
            "",
            "",
            "",
            "\1\26",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( L_BRACKET | R_BRACKET | NOT | AND | OR | EQUAL | COMMA | SPACE | WS | NUMBER | CHAR | ESCAPE );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA2_0 = input.LA(1);

                        s = -1;
                        if ( (LA2_0=='(') ) {s = 1;}

                        else if ( (LA2_0==')') ) {s = 2;}

                        else if ( (LA2_0=='N'||LA2_0=='n') ) {s = 3;}

                        else if ( (LA2_0=='A'||LA2_0=='a') ) {s = 4;}

                        else if ( (LA2_0=='O'||LA2_0=='o') ) {s = 5;}

                        else if ( (LA2_0=='=') ) {s = 6;}

                        else if ( (LA2_0==',') ) {s = 7;}

                        else if ( (LA2_0=='\t'||LA2_0==' ') ) {s = 8;}

                        else if ( (LA2_0=='\r') ) {s = 9;}

                        else if ( (LA2_0=='\n') ) {s = 10;}

                        else if ( ((LA2_0>='0' && LA2_0<='9')) ) {s = 11;}

                        else if ( ((LA2_0>='\u0000' && LA2_0<='\b')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='\u001F')||(LA2_0>='!' && LA2_0<='\'')||(LA2_0>='*' && LA2_0<='+')||(LA2_0>='-' && LA2_0<='/')||(LA2_0>=':' && LA2_0<='<')||(LA2_0>='>' && LA2_0<='@')||(LA2_0>='B' && LA2_0<='M')||(LA2_0>='P' && LA2_0<='[')||(LA2_0>=']' && LA2_0<='`')||(LA2_0>='b' && LA2_0<='m')||(LA2_0>='p' && LA2_0<='\uFFFF')) ) {s = 12;}

                        else if ( (LA2_0=='\\') ) {s = 13;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 2, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}