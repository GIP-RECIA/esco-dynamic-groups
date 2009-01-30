package org.esco.dynamicgroups;

import edu.internet2.middleware.grouper.GroupModifyException;
import edu.internet2.middleware.grouper.InsufficientPrivilegeException;


import org.esco.dynamicgroups.dao.db.IDBDAOService;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 * @author GIP RECIA - A. Deman
 * 12 janv. 2009
 *
 */
public class TestBatch {
    
    /**
     * Builds an instance of TestBatch.
     */
    private TestBatch() {
        super();
    }
    /**
     * 
     * @param args
     * @throws InsufficientPrivilegeException 
     * @throws GroupModifyException 
     */
    public static void main(final String[] args) throws GroupModifyException, InsufficientPrivilegeException {
        
        
        
        ThreadLocal<ApplicationContext> appCtx = new ThreadLocal<ApplicationContext>();
        appCtx.set(new FileSystemXmlApplicationContext("classpath:applicationContext.xml"));
        BeanFactory beanFactory = appCtx.get();
        IDBDAOService hib3Support = (IDBDAOService) beanFactory.getBean("daoService");
        
        DynGroup dynGroup = new DynGroup("dynG1", 
                "And (Or(objectClass =ENTAuxEnseignant, ENTPersonSexe=M), (ENTPersonSexe=F))");
        hib3Support.storeDynGroup(dynGroup);
        DynGroup dynGroup2 = new DynGroup("dynG2", 
        "And (Or(objectClass =ENTAuxEnseignant, ENTPersonSexe=M), (ENTPersonSexe=F))");
        hib3Support.storeDynGroup(dynGroup2);
        System.out.println("==>" + dynGroup);
        System.out.println("==>" + dynGroup2);
        
       DynGroup dynGroup3 = hib3Support.getDynGroupByName("dynG2");
       hib3Support.deleteDynGroup(dynGroup3);
       
       dynGroup.setGroupDefinition("Or(ENTPersonSexe=?, toto=tralala)");
       hib3Support.modifyDynGroup(dynGroup);
        
//        DynAttribute dynAtt = new DynAttribute();
//        dynAtt.setAttributeName("a name");
//        hib3Support.storeDynAttribute(dynAtt);
//   
//        DynAttribute dynAtt2 = hib3Support.getDynAttributeByName("a name");
//        System.out.println("==>" + dynAtt2);
//        
//        DynAttribute dynAtt3 = new DynAttribute();
//        dynAtt3.setAttributeName("a name3");
//        hib3Support.storeDynAttribute(dynAtt3);
//        
//        DynAttribute dynAtt4 = hib3Support.getDynAttributeByName("a name3");
//        System.out.println("==>" + dynAtt4);
//        
//        
//        
//        final int nbIns = 10;
//        final int nbAtt = 5;
//        for (int i = 0; i < nbIns; i++) {
//
//            DynGroup dynGroup = new DynGroup();
//            dynGroup.setGroupName("Un nom de groupe dynamique" + i);
//            dynGroup.setGroupDefinition("Une definition de groupe" + i);
//            dynGroup.setAttributesNb(nbAtt);
//            hib3Support.storeDynGroup(dynGroup);
//            DynGroup dynGroup2 = hib3Support.getDynGroupByName("Un nom de groupe dynamique" + i);
//            System.out.println("==>" + dynGroup2);
//       
//            GroupAttributeValueAssoc grpAttAssoc = new GroupAttributeValueAssoc();
//            grpAttAssoc.setAttribute(dynAtt2);
//            grpAttAssoc.setGroup(dynGroup2);
//            grpAttAssoc.setAttributeValue("Value" + (i % 2));
//            hib3Support.storeGroupAttributeValueAssoc(grpAttAssoc);
//            grpAttAssoc.setAttribute(dynAtt4);
//            hib3Support.storeGroupAttributeValueAssoc(grpAttAssoc);
//        }
//        
//        Set<DynGroup> groups = hib3Support.getGroupsForAttributeValue("a name", "Value0");
//        System.out.println("V0=>" + groups);
//        groups = hib3Support.getGroupsForAttributeValue("a name", "Value1");
//        System.out.println("V1=>" + groups);
//        
//        Set<AttributeValue> attributes = hib3Support.getAttributeValuesForGroup("Un nom de groupe dynamique0");
//        System.out.println("Attributes=>" + attributes);
//        
//        Set<String> attributesV = hib3Support.getAttributeValuesForGroup("a name", "Un nom de groupe dynamique0");
//        System.out.println("Attributes=>" + attributesV);

    }

}
