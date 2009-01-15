/**
 * 
 */
package org.esco.dynamicgroups.dao.db;


import edu.internet2.middleware.grouper.Group;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esco.dynamicgroups.domain.beans.AttributeValue;
import org.esco.dynamicgroups.domain.beans.DynAttribute;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.GroupAttributeValueAssoc;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate implementation of the DAO service.
 * @author GIP RECIA - A. Deman
 * 12 janv. 2009
 *
 */
public class HibernateDAOServiceImpl extends AbstractHibernateDAOSupport implements IDBDAOService {

    /** Constant for the group. */
    private static final String GROUP = "group";
    
    /** Constant for the group name. */
    private static final String GROUP_NAME = "groupName";
    
    /** Constant for the attribute value. */
    private static final String ATTRIBUTE_VALUE = "attributeValue";
    
        /** Constant for the attribute. */
    private static final String ATTRIBUTE = "attribute";
    
    /** Constant for the attribute name. */
    private static final String ATTRIBUTE_NAME = "attributeName";

    /**
     * Builds an instance of HibernateDAOServiceImpl.
     */
    public HibernateDAOServiceImpl() {
        super();
    }  
    
    /**
     * Retrieves the attribute associated to a given name.
     * @param name The name of the attribute.
     * @return The attribute if found, null otherwise.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getDynAttributeByName(java.lang.String)
     */
    public DynAttribute getDynAttributeByName(final String name) {
        return (DynAttribute) retrieveUniqueInstanceByAttribute(DynAttribute.class, ATTRIBUTE_NAME, name);
    }
    
    /**
     * Retrieves the attribute associated to a given name.
     * @param name The name of the attribute.
     * @return The attribute if found, null otherwise.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getDynGroupByName(java.lang.String)
     */
    public DynGroup getDynGroupByName(final String name) {
        return (DynGroup) retrieveUniqueInstanceByAttribute(DynGroup.class, GROUP_NAME, name);
    }
    
    /**
     * Stores a DynAttribute instance.
     * @param dynAttribute The instance to store.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#storeDynAttribute(DynAttribute)
     */
    public void storeDynAttribute(final DynAttribute dynAttribute) {
        store(dynAttribute);
    }
    
    /**
     * Stores a GroupAttributeValueAssoc instance.
     * @param grpAttAssoc The instance to store.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#storeGroupAttributeValueAssoc(GroupAttributeValueAssoc)
     */
    public void storeGroupAttributeValueAssoc(final GroupAttributeValueAssoc grpAttAssoc) {
        store(grpAttAssoc);
    }
    
    /**
     * Stores a DynGroup instance.
     * @param dynGroup The instance to store.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#storeDynGroup(org.esco.dynamicgroups.domain.beans.DynGroup)
     */
    public void storeDynGroup(final DynGroup dynGroup) {
        store(dynGroup);
    }
    
    /**
     * Retrieves the groups associated to a given attribute.
     * @param attributeName The considered attribute.
     * @return The set of groups which use the attribute in their definition.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getGroupsForAttribute(String)
     */
    public Set<DynGroup> getGroupsForAttribute(final String attributeName) {
        
        final Set<DynGroup> result = new HashSet<DynGroup>();
        
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();

        final Criteria criteria = session.createCriteria(GroupAttributeValueAssoc.class);
        criteria.setFetchMode(GROUP, FetchMode.JOIN);
        criteria.setFetchMode(ATTRIBUTE, FetchMode.JOIN);
        criteria.createCriteria(ATTRIBUTE, ATTRIBUTE).add(Restrictions.eq(ATTRIBUTE_NAME, attributeName));
        criteria.setCacheable(true);
        
        // Retieves the associations.
        @SuppressWarnings("unchecked")
        List<GroupAttributeValueAssoc> associations =  criteria.list();

        closeSessionForThread();
 
        // Retrieves the groups.
        for (GroupAttributeValueAssoc association : associations) {
                result.add(association.getGroup());
           
        }
        
        return result;
    }
    
    /**
     * Retrieves the groups associated to a given value of a specified attribute.
     * @param attributeName The considered attribute.
     * @param attributeValue The value of the attribute.
     * @return The set of groups which use the value of the attribute in their definition.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getGroupsForAttributeValue(java.lang.String, java.lang.String)
     */
    public Set<DynGroup> getGroupsForAttributeValue(final String attributeName, 
                        final String attributeValue) {
        
        final Set<DynGroup> result = new HashSet<DynGroup>();
        
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();

        final Criteria criteria = session.createCriteria(GroupAttributeValueAssoc.class);
        criteria.setFetchMode(GROUP, FetchMode.JOIN);
        criteria.setFetchMode(ATTRIBUTE, FetchMode.JOIN);
        criteria.add(Restrictions.eq(ATTRIBUTE_VALUE, attributeValue));
        criteria.createCriteria(ATTRIBUTE, ATTRIBUTE).add(Restrictions.eq(ATTRIBUTE_NAME, attributeName));
        criteria.setCacheable(true);
        
        // Retieves the associations.
        @SuppressWarnings("unchecked")
        List<GroupAttributeValueAssoc> associations =  criteria.list();

        closeSessionForThread();
 
        // Retrieves the groups.
        for (GroupAttributeValueAssoc association : associations) {
                result.add(association.getGroup());
           
        }
        
        return result;
    }
    
    /**
     * Retrieves the list of attributes values involved in a group definition.
     * @param groupName The of the considered group.
     * @return The set of attribute values.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getAttributeValuesForGroup(String)
     */
    public Set<AttributeValue> getAttributeValuesForGroup(final String groupName) {
        
        final Set<AttributeValue> result = new HashSet<AttributeValue>();
        
        startTransaction();
        
        final Session session = openOrRetrieveSessionForThread();
        final Criteria criteria = session.createCriteria(GroupAttributeValueAssoc.class);
        criteria.setFetchMode(GROUP, FetchMode.JOIN);
        criteria.setFetchMode(ATTRIBUTE, FetchMode.JOIN);
        criteria.createCriteria(GROUP, GROUP).add(Restrictions.eq(GROUP_NAME, groupName));
        criteria.setCacheable(true);
        
        @SuppressWarnings("unchecked")
        final List<GroupAttributeValueAssoc> associations =  criteria.list();
        closeSessionForThread();

        // Retrieves the attributes.
        for (GroupAttributeValueAssoc association : associations) {
            result.add(new AttributeValue(association.getAttribute().getAttributeName(), 
                    association.getAttributeValue()));
        }
        
        return result;
    }
    
    /**
     * Gives the values, for a given attribute, associated to a set of groups.
     * @param attributeName The name of the attribute.
     * @param groups The considered groups.
     * @return The set of values for the considered attribute.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getAttributeValuesForGroups(java.lang.String, java.util.Set)
     */
    public Set<String> getAttributeValuesForGroups(final String attributeName, final Set<Group> groups) {
        
        final Set<String> result = new HashSet<String>();
        
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        
        // Retrieves the (attribute, value)/group associations.
        final Criteria criteria = session.createCriteria(GroupAttributeValueAssoc.class);
        criteria.setFetchMode(GROUP, FetchMode.JOIN);
        criteria.setFetchMode(ATTRIBUTE, FetchMode.JOIN);
        
        Criterion groupConj = null;
        
        for (Group group : groups) { 
            if (groupConj == null) {
                groupConj = Restrictions.eq(GROUP_NAME, group.getName());
            } else {
                groupConj = Restrictions.or(groupConj, Restrictions.eq(GROUP_NAME, group.getName()));
            }
        }
        
        criteria.createCriteria(GROUP, GROUP).add(groupConj);
        criteria.createCriteria(ATTRIBUTE, ATTRIBUTE).add(Restrictions.eq(ATTRIBUTE_NAME, attributeName));
        criteria.setCacheable(true);
        
        
        @SuppressWarnings("unchecked")
        final List<GroupAttributeValueAssoc> associations =  criteria.list();
        
        // Retrieves the attributes.
        for (GroupAttributeValueAssoc association : associations) {
            result.add(association.getAttributeValue());
        }
        return result;
    }
    
    /**
     * Retrieves the values of a given attribute for a group.
     * @param attributeName The name of the attribute.
     * @param groupName The name of the group.
     * @return The values of the attribute in the group definition if the attribute
     * is present.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getAttributeValuesForGroup(String, String)
     */
    public Set<String> getAttributeValuesForGroup(final String attributeName, final String groupName) {
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        final Set<String> result = new HashSet<String>();
           
        final Criteria criteria = session.createCriteria(GroupAttributeValueAssoc.class);
        criteria.setFetchMode(GROUP, FetchMode.JOIN);
        criteria.setFetchMode(ATTRIBUTE, FetchMode.JOIN);
        criteria.createCriteria(GROUP, GROUP).add(Restrictions.eq(GROUP_NAME, groupName));
        criteria.createCriteria(ATTRIBUTE, ATTRIBUTE).add(Restrictions.eq(ATTRIBUTE_NAME, attributeName));
        criteria.setCacheable(true);
        
        @SuppressWarnings("unchecked")
        final List<GroupAttributeValueAssoc> associations =  criteria.list();
        
        // Retrieves the attributes.
        for (GroupAttributeValueAssoc association : associations) {
            result.add(association.getAttributeValue());
        }
        return result;
    }
}
