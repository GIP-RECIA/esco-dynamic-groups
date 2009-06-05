/**
 * 
 */
package org.esco.dynamicgroups.domain;

import java.util.Locale;

import org.esco.dynamicgroups.dao.ldap.LDAPMembersFromDefinitionDAO;
import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.esco.dynamicgroups.domain.definition.DecodedPropositionResult;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;
import org.esco.dynamicgroups.domain.parameters.CommonsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author GIP RECIA - A. Deman 
 * 19 mars 2009
 *
 */
public class DefinitionToLDAPFilterTesterBatch {
    /** The LDAP initializer used to test the definition. */
    private LDAPMembersFromDefinitionDAO ldapMembers = new LDAPMembersFromDefinitionDAO();
    
    

    /**
     * Builds an instance of DefinitionToLDAPFilterTesterBatch.
     */
    private DefinitionToLDAPFilterTesterBatch() {
        super();
    }

    /**
     * Checks a logical proposition.
     * @param proposition The proposition to check.
     */
    private void check(final String proposition) {
       
        System.out.println("Test the string: " + proposition);
        final DecodedPropositionResult result = PropositionCodec.instance().decode(proposition);

        if (!result.isValid()) {
            System.out.println("The definition is not valid. Invalid string: " + result.getInvalidEncodedString());
        } else {
            final DynamicGroupDefinition def = new DynamicGroupDefinition("TEST GROUP", result.getProposition());
            System.out.println("The definition is valid: " + def.getProposition());
            final String filter = ldapMembers.translateToLdapFilter(def);
            System.out.println("The associated filter is: " + filter);
        }

    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        ThreadLocal<ApplicationContext> appCtx = new ThreadLocal<ApplicationContext>();
        appCtx.set(new FileSystemXmlApplicationContext("classpath:applicationContext-light.xml"));
        final BeanFactory beanFactory = appCtx.get();
        
        final I18NManager i18N = (I18NManager) beanFactory.getBean("i18n");
        final MessageSource ms = (MessageSource) beanFactory.getBean("i18nService");
        final ParametersProvider parametersProvider = (ParametersProvider) beanFactory.getBean("parametersProvider");
        final CommonsParametersSection commonsParameters = 
            (CommonsParametersSection) parametersProvider.getCommonsParametersSection();
        System.out.println("=>" + ms.getMessage("report.subjects", null, new Locale("en")));
        System.out.println("=>" + ms.getMessage("report.subjects", null, commonsParameters.getLocale()));
        System.out.println("=>" + ms.getMessage("report.subjects", null, Locale.FRENCH));
        System.out.println("=>" + ms.getMessage("report.subjects", null, Locale.US));
        System.out.println("=>" + ms.getMessage("report.subjects", null, new Locale("en", "UK")));
        System.out.println("=>" + ms.getMessage("report.subjects", null, Locale.GERMAN));
//        System.out.println("=>" + i18N.getI18nMessage("report.subject"));
//        System.out.println("=>" + i18N.getI18nMessage("report.subject"));
//        System.out.println("=>" + i18N.getI18nMessage("report.subject"));
        
        
        
//        final DefinitionToLDAPFilterTesterBatch tester = new DefinitionToLDAPFilterTesterBatch();
//        for (String defString : args) {
//            tester.check(defString);
//        }
    }
}
