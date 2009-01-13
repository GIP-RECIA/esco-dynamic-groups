package org.esco.dynamicgroups;

import java.util.Set;

import org.esco.dynamicgroups.dao.db.DAOService;
import org.esco.dynamicgroups.domain.beans.DynAttribute;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.GroupAttributeValueAssoc;
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
     * 
     * @param args
     */
    public static void main(final String[] args) {
        ThreadLocal<ApplicationContext> appCtx = new ThreadLocal<ApplicationContext>();
        appCtx.set(new FileSystemXmlApplicationContext("classpath:applicationContext.xml"));
        BeanFactory beanFactory = appCtx.get();
        DAOService hib3Support = (DAOService) beanFactory.getBean("dataBaseManager");
//        Session session = hib3Support.getSessionFactory().openSession();
//        session.beginTransaction();
        DynAttribute dynAtt = new DynAttribute();
        dynAtt.setAttributeName("a name");
        hib3Support.storeDynAttribute(dynAtt);
   
        DynAttribute dynAtt2 = hib3Support.getDynAttributeByName("a name");
        System.out.println("==>" + dynAtt2);
        
        for (int i = 0; i < 10; i++){

            DynGroup dynGroup = new DynGroup();
            dynGroup.setGroupName("Un nom de groupe dynamique" + i);
            dynGroup.setGroupDefinition("Une definition de groupe" + i);
            dynGroup.setAttributesNb(10);
            hib3Support.storeDynGroup(dynGroup);
            DynGroup dynGroup2 = hib3Support.getDynGroupByName("Un nom de groupe dynamique" + i);
            System.out.println("==>" + dynGroup2);
       
            GroupAttributeValueAssoc grpAttAssoc = new GroupAttributeValueAssoc();
            grpAttAssoc.setAttributeId(dynAtt2.getAttributeId());
            grpAttAssoc.setGroupId(dynGroup2.getGroupId());
            grpAttAssoc.setAttributeValue("Value"+(i%2));
            hib3Support.storeGroupAttributeValueAssoc(grpAttAssoc);
        }
        
        Set<DynGroup> groups = hib3Support.getGroupsByAttributeValue("a name", "Value0");
        System.out.println("V0=>" + groups);
        groups = hib3Support.getGroupsByAttributeValue("a name", "Value1");
        System.out.println("V1=>" + groups);
//        hib3Support.closeSessionForThread();
        //session.save(dynAtt);
        
//        DynGroup dynGroup = new DynGroup();
//        dynGroup.setGroupName("Un nom de groupe dynamique");
//        dynGroup.setGroupDefinition("Une definition de groupe");
//        dynGroup.setAttributesNb(10);
//        session.save(dynGroup);
        //session.getTransaction().commit();
        
        //session = hib3Support.getSessionFactory().openSession();
        //session.beginTransaction();
//        GroupAttributeValueAssoc grpAttValuAssoc = new GroupAttributeValueAssoc();
//        
//        grpAttValuAssoc.setAttributeId(dynAtt.getAttributeId());
//        grpAttValuAssoc.setGroupId(dynGroup.getGroupId());
//        grpAttValuAssoc.setAttributeValue("attributeValue");
//        session.save(grpAttValuAssoc);
        
//        session.getTransaction().commit();
        
    }

}
