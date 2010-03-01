/**
 * 
 */
package org.esco.dynamicgroups.domain.definition.antlr;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import org.esco.dynamicgroups.domain.definition.antlr.error.handling.ErrorHandlerImpl;
import org.esco.dynamicgroups.domain.definition.antlr.generated.DynamicGroupDefinitionLexer;
import org.esco.dynamicgroups.domain.definition.antlr.generated.DynamicGroupDefinitionParser;
import org.esco.dynamicgroups.domain.definition.antlr.util.TokensEscaperImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.esco.dynamicgroups.domain.beans.DisabledI18NManager;
import org.esco.dynamicgroups.domain.beans.II18NManager;
import org.esco.dynamicgroups.domain.definition.AtomicPropositionValidatorFromList;
import org.esco.dynamicgroups.domain.definition.IAtomicPropositionValidator;
import org.esco.dynamicgroups.domain.definition.IProposition;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;

/**
 * @author GIP RECIA - A. Deman
 * 8 oct. 2009
 *
 */
public class Tester {

    /**
     * @param args
     * @throws TokenStreamException 
     * @throws RecognitionException 
     * @throws IOException 
     * @throws org.antlr.runtime.RecognitionException 
     */
    public static void main(String[] args) throws RecognitionException, TokenStreamException, IOException, org.antlr.runtime.RecognitionException {
        final String[] expressions = { "Orx(AND(NoT (w41 or \\\\11w = 222), v=x),tutu=tralala)", };
        final String[] validAttributes = {"objectClass", "ENTPersonSexe", "ENTEleveEnseignements",
                "ENTPersonVille", "ENTEleveBoursier", "ENTEleveDelegClass",
                "ENTEleveMajeur", "ESCOUAI", "ESCOUAIRattachement", "ENTEleveTransport",
                "ENTEleveClasses", "ENTPersonProfils", "ENTEleveFiliere", "ENTEleveRegime",
                "ENTEleveStatutEleve", "ENTPersonStructRattach", "sn", "ENTAuxEnsClasses", }; 

        final II18NManager i18n = new DisabledI18NManager(true);
        final IAtomicPropositionValidator atomValidator = 
            new AtomicPropositionValidatorFromList(validAttributes, i18n);

        new PropositionCodec(atomValidator, i18n);
        for (String expression : expressions) {
            final DynamicGroupDefinitionLexer lexer = new DynamicGroupDefinitionLexer(new ANTLRStringStream(expression));
            final DynamicGroupDefinitionParser parser = new DynamicGroupDefinitionParser(new CommonTokenStream(lexer));
            
            parser.setErrorHandler(new ErrorHandlerImpl());
            parser.setTokensEscaper(new TokensEscaperImpl());
            final IProposition prop = parser.evaluateExpression();
            System.out.println("=> " + prop);
        }

        //      final DynamicGroupDefinitionLexer lexer = new DynamicGroupDefinitionLexer(is);

        //        final String[] validAttributes = {"objectClass", "ENTPersonSexe", "ENTEleveEnseignements",
        //                "ENTPersonVille", "ENTEleveBoursier", "ENTEleveDelegClass",
        //                "ENTEleveMajeur", "ESCOUAI", "ESCOUAIRattachement", "ENTEleveTransport",
        //                "ENTEleveClasses", "ENTPersonProfils", "ENTEleveFiliere", "ENTEleveRegime",
        //                "ENTEleveStatutEleve", "ENTPersonStructRattach", "sn", "ENTAuxEnsClasses", }; 
        //
        //        final II18NManager i18n = new DisabledI18NManager(true);
        //        final IAtomicPropositionValidator atomValidator = 
        //            new AtomicPropositionValidatorFromList(validAttributes, i18n);
        //
        //        new PropositionCodec(atomValidator, i18n);
        //
        //        final String[] expressions = { "ND(OR(And(Not(ab=a1d), a=c),Not(ab=a1d)),OR(And(Not(ab=a1d), a=c),Not(ab=a1d)))" }; 
        //
        //        //final String[] expressions = {"NoT(abc=ab1cd)"}; 
        //        //        final String[] expressions = {"or(ab=1 ,ab=1)", "or(not(ab=1), ab=1)", "and(not(or(not(ab=1), ab=1)), zzz=11)"}; 
        //        //        final String[] expressions = {"and(not(or(not(ab=1), ab=1)), zzz=11)"}; 
        //
        //        for (String expression : expressions) {
        //            System.out.println("Testing syntax for \"" + expression +"\"");
        //            final InputStream is  = new ByteArrayInputStream(expression.getBytes());
        //            final DynamicGroupDefinitionLexer lexer = new DynamicGroupDefinitionLexer(is);
        //            final DynamicGroupDefinitionParser parser = new DynamicGroupDefinitionParser(lexer);
        //                IProposition p = parser.expression();
        //                if (p != null) {
        //                    p.toString();
        //                    System.out.println("Proposition ==> " + p + "\n\n");
        //                }
        //           
        //
        //        }
        //
    }

}
