/**
 * 
 */
package org.esco.dynamicgroups.dao.db;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esco.dynamicgroups.domain.beans.DynAttribute;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.GroupAttributeValueAssoc;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * DAO service.
 * @author GIP RECIA - A. Deman
 * 12 janv. 2009
 *
 */
public class HibernateDAOService extends AbstractHibernateDAOSupport implements DAOService {

  
    
    /**
     * Builds an instance of HibernateDAOService.
     */
    public HibernateDAOService() {
        super();
    }
    
  
    
    /**
     * Retrieves the attribute associated to a given name.
     * @param name The name of the attribute.
     * @return The attribute if found, null otherwise.
     * @see org.esco.dynamicgroups.dao.db.DAOService#getDynAttributeByName(java.lang.String)
     */
    public DynAttribute getDynAttributeByName(final String name) {
        return (DynAttribute) retrieveUniqueInstanceByAttribute(DynAttribute.class, "attributeName", name);
    }
    
    /**
     * Retrieves the attribute associated to a given name.
     * @param name The name of the attribute.
     * @return The attribute if found, null otherwise.
     * @see org.esco.dynamicgroups.dao.db.DAOService#getDynGroupByName(java.lang.String)
     */
    public DynGroup getDynGroupByName(final String name) {
        return (DynGroup) retrieveUniqueInstanceByAttribute(DynGroup.class, "groupName", name);
    }
    
    /**
     * Stores a DynAttribute instance.
     * @param dynAttribute The instance to store.
     * @see org.esco.dynamicgroups.dao.db.DAOService#storeDynAttribute(org.esco.dynamicgroups.domain.beans.DynAttribute)
     */
    public void storeDynAttribute(final DynAttribute dynAttribute) {
        store(dynAttribute);
    }
    
    /**
     * Stores a GroupAttributeValueAssoc instance.
     * @param grpAttAssoc The instance to store.
     * @see org.esco.dynamicgroups.dao.db.DAOService#storeGroupAttributeValueAssoc(org.esco.dynamicgroups.domain.beans.GroupAttributeValueAssoc)
     */
    public void storeGroupAttributeValueAssoc(final GroupAttributeValueAssoc grpAttAssoc) {
        store(grpAttAssoc);
    }
    
    /**
     * Stores a DynGroup instance.
     * @param dynGroup The instance to store.
     * @see org.esco.dynamicgroups.dao.db.DAOService#storeDynGroup(org.esco.dynamicgroups.domain.beans.DynGroup)
     */
    public void storeDynGroup(final DynGroup dynGroup) {
        store(dynGroup);
    }
    
    /**
     * Retrieves the groups associated to a given value of a specified attribute.
     * @param attributeName The considered attribute.
     * @param attributeValue The value of the attribute.
     * @return The set of groups which use the value of the attribute in their definition.
     * @see org.esco.dynamicgroups.dao.db.DAOService#getGroupsByAttributeValue(java.lang.String, java.lang.String)
     */
    public Set<DynGroup> getGroupsByAttributeValue(final String attributeName, 
                        final String attributeValue) {
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        Set<DynGroup> result = new HashSet<DynGroup>();
           
        // Retrieve the attribute.
        final DynAttribute dynAtt =  (DynAttribute) retrieveUniqueInstanceByAttributeInternal(session, 
                DynAttribute.class, "attributeName", attributeName);
        
        if (dynAtt == null) {
            return result;
        }
        
        // Retrieves the association (attribute, value)/groups.
        StringBuilder queryString = new StringBuilder("from ");
        queryString.append(GroupAttributeValueAssoc.class.getSimpleName());
        queryString.append(" where ");
        queryString.append("attributeId");
        queryString.append("='");
        queryString.append(dynAtt.getAttributeId());
        
        queryString.append("' and ");
        queryString.append("attributeValue");
        queryString.append("='");
        queryString.append(attributeValue);
        queryString.append("'");
        Query query = session.createQuery(queryString.toString());
        query.setCacheable(true);
        
        @SuppressWarnings("unchecked")
        List<GroupAttributeValueAssoc> associations =  query.list();
        
        // Retrieves the groups.
        for (GroupAttributeValueAssoc association : associations) {
            final DynGroup dynGroup =  (DynGroup) retrieveUniqueInstanceByAttributeInternal(session, 
                    DynGroup.class, "groupId", String.valueOf(association.getGroupId()));
            result.add(dynGroup);
        }
        
        closeSessionForThread();
        return result;
        
    }
}
