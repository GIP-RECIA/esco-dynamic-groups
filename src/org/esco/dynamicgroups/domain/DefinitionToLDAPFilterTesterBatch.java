/**
 * 
 */
package org.esco.dynamicgroups.domain;


import org.esco.dynamicgroups.dao.ldap.LDAPMembersFromDefinitionDAO;
import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.esco.dynamicgroups.domain.definition.DecodedPropositionResult;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Used to test the definitions for an LDAP backend.
 * @author GIP RECIA - A. Deman 
 * 19 mars 2009
 *
 */
public class DefinitionToLDAPFilterTesterBatch {
    
    /** Spring context  file name. */
    private static final String BATCH_CTX_FILE = "classpath:applicationContext-batch.xml";
    
    /** Name of the bean for the I18N manager. */
    private static final String I18N_BEAN = "i18n";

    /** I18N Key for a valid proposition. */
    private static final String VALID_DEF = "definition.to.ldap.filter.tester.valid.proposition";
    
    /** I18N Key for the filter. */
    private static final String FILTER = "definition.to.ldap.filter.tester.filter"; 
    
    /** I18N Key for the proposition. */
    private static final String PROPOSITION = "definition.to.ldap.filter.tester.proposition"; 
    
    /** I18N Key for a valid result. */
    private static final String VALID_RESULT = "definition.to.ldap.filter.tester.result.valid"; 
    
    /** I18N Key for an invalid result. */
    private static final String INVALID_RESULT = "definition.to.ldap.filter.tester.result.invalid"; 
    
    
    /** The LDAP initializer used to test the definition. */
    private LDAPMembersFromDefinitionDAO ldapMembers = new LDAPMembersFromDefinitionDAO();
    
    
    /** The i18N managager. */
    private I18NManager i18n; 
   
    

    /**
     * Builds an instance of DefinitionToLDAPFilterTesterBatch.
     * @param i18n The i18N manager.
     */
    private DefinitionToLDAPFilterTesterBatch(final I18NManager i18n) {
        this.i18n = i18n;
    }

    /**
     * Checks a logical proposition.
     * @param proposition The proposition to check.
     */
    private void check(final String proposition) {
       
        System.out.println("---------------------------------------------");
        System.out.println(i18n.getI18nMessage(PROPOSITION));
        System.out.println(proposition);
        System.out.println("---------------------------------------------");
        final DecodedPropositionResult result = PropositionCodec.instance().decode(proposition);
        
        if (!result.isValid()) {
            System.out.println(i18n.getI18nMessage(INVALID_RESULT));
            System.out.println(result.getErrorMessage());
        } else {
            System.out.println(i18n.getI18nMessage(VALID_RESULT));
            final DynamicGroupDefinition def = new DynamicGroupDefinition("TEST GROUP", result.getProposition());
            System.out.println(i18n.getI18nMessage(VALID_DEF) + def.getProposition());
            final String filter = ldapMembers.translateToLdapFilter(def);
            System.out.println(i18n.getI18nMessage(FILTER) + filter);
        }
        System.out.println("---------------------------------------------");

    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final ThreadLocal<ApplicationContext> appCtx = new ThreadLocal<ApplicationContext>();
        appCtx.set(new FileSystemXmlApplicationContext(BATCH_CTX_FILE));
        final BeanFactory beanFactory = appCtx.get();
        final I18NManager i18n = (I18NManager) beanFactory.getBean(I18N_BEAN); 
        
        final DefinitionToLDAPFilterTesterBatch tester = new DefinitionToLDAPFilterTesterBatch(i18n);
        for (String defString : args) {
            tester.check(defString);
        }
    }
}
