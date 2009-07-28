/**
 * 
 */
package org.esco.dynamicgroups.dao.db;


import edu.internet2.middleware.grouper.Group;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.beans.AttributeValue;
import org.esco.dynamicgroups.domain.beans.DynAttribute;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.GroupAttributeValueAssoc;
import org.esco.dynamicgroups.domain.beans.IStringCleaner;
import org.esco.dynamicgroups.domain.definition.AtomicProposition;
import org.esco.dynamicgroups.domain.definition.DecodedPropositionResult;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;
import org.esco.dynamicgroups.domain.definition.IProposition;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

/**
 * Hibernate implementation of the DAO service.
 * @author GIP RECIA - A. Deman
 * 12 janv. 2009
 *
 */
public class HibernateDAOServiceImpl extends AbstractHibernateDAOSupport implements IDBDAOService, Serializable {

    /** Serial versin UID. */
    private static final long serialVersionUID = 4195252879885105837L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(HibernateDAOServiceImpl.class);

    /** Constant for the group. */
    private static final String GROUP = "group";

    /** Constant for the group name. */
    private static final String GROUP_UUID = "groupUUID";
    
    /** Constant for the group id. */
    private static final String GROUP_ID = "groupId";

    /** Constant for the attribute. */
    private static final String ATTRIBUTE = "attribute";

    /** Constant for the attribute name. */
    private static final String ATTRIBUTE_NAME = "attributeName";

    /** The String cleaner. */
    private IStringCleaner stringCleaner;

    /**
     * Builds an instance of HibernateDAOServiceImpl.
     */
    public HibernateDAOServiceImpl() {
        super();
    } 
    
    /**
     * Checks the initialization of the bean.
     * @see org.springframework.dao.support.DaoSupport#initDao()
     */
    @Override
    protected void initDao() {
        Assert.notNull(this.stringCleaner, 
                "The property stringCleaner in the class " 
                + this.getClass().getName() 
                + " can't be null."); 
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
     * Retrieves the attribute associated to a given name. The attribute is inserted first in the table if it
     * does not exist.
     * @param session The hibernate session to use.
     * @param name The name of the attribute.
     * @return The attribute.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getDynAttributeByName(java.lang.String)
     */
    protected DynAttribute retrieveOrCreateDynAttribut(final Session session, final String name) {
        DynAttribute result = (DynAttribute) retrieveUniqueInstanceByAttributeInternal(session, 
                DynAttribute.class, 
                ATTRIBUTE_NAME, 
                name);
        if (result == null) {
            result = new DynAttribute(name);
            storeInternal(session, result);
            session.flush();
        }


        return result;
    }

    /**
     * Retrieves the attribute associated to a given name.
     * @param uuid The name of the attribute.
     * @return The attribute if found, null otherwise.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getDynGroupByUUID(java.lang.String)
     */
    public DynGroup getDynGroupByUUID(final String uuid) {
        return (DynGroup) retrieveUniqueInstanceByAttribute(DynGroup.class, GROUP_UUID, uuid);
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
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        storeGroupAttributeValueAssocInternal(session, grpAttAssoc);
        commit();
        closeSessionForThread();
    }

    /**
     * Stores a GroupAttributeValueAssoc instance.
     * @param session The hibernate session.
     * @param grpAttAssoc The instance to store.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#storeGroupAttributeValueAssoc(GroupAttributeValueAssoc)
     */
    protected void storeGroupAttributeValueAssocInternal(final Session session,
            final GroupAttributeValueAssoc grpAttAssoc) {
        storeInternal(session, grpAttAssoc);
    }

    /**
     * Stores a DynGroup instance.
     * @param dynGroup The instance to store.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#storeDynGroup(org.esco.dynamicgroups.domain.beans.DynGroup)
     */
    public void storeDynGroup(final DynGroup dynGroup) {
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        storeDynGroupInternal(session, dynGroup);
        commit();
        closeSessionForThread();
    }

    /**
     * Stores a DynGroup instance.
     * @param session The session to use.
     * @param dynGroup The instance to store.
     */
    protected void storeDynGroupInternal(final Session session, final DynGroup dynGroup) {
        storeInternal(session, dynGroup);

        // Stores all the cunjunction in the defintion as groups.
        final Set<DynGroup> conjGroups = dynGroup.getConjunctiveComponents();
        for (DynGroup conjGroup : conjGroups) {
            storeDynGroupInternal(session, conjGroup);
        }

        // Stores the attribute/value contribution for this group if it is not
        // composed of several conjunctions.
        if (conjGroups.size() == 0) {
            final DecodedPropositionResult decRes = 
                PropositionCodec.instance().decodeToDisjunctiveNormalForm(dynGroup.getGroupDefinition());
            final IProposition prop = decRes.getProposition();
            if (prop == null) {
                LOGGER.error("Unable to decode (to a normal disjunctive form) the logical proposition in the group: " 
                        + dynGroup + ".");
            } else {
                final List<AtomicProposition> atoms = prop.getAtomicPropositions();
                for (AtomicProposition atom : atoms) {
                    final DynAttribute dynAtt = retrieveOrCreateDynAttribut(session, atom.getAttribute());
                    final GroupAttributeValueAssoc grpAttAssoc = 
                        new GroupAttributeValueAssoc(dynAtt, 
                                stringCleaner.clean(atom.getValue().replaceAll(STD_JOCKER, SQL_JOCKER)), 
                                atom.isNegative(), 
                                dynGroup);
                    
                    storeGroupAttributeValueAssocInternal(session, grpAttAssoc);
                } 
            }
        }
    }
    

    /**
     * Retrieves the groups corresponding to the cunjunctive components o a group.
     * @param session The session.
     * @param dynGroup The group from which the conjunctive components have to be retrieved.
     * @return The list of the groups for the conjunctive components.
     */
    protected List<DynGroup> retrieveConjunctiveComponents(final Session session, 
            final DynGroup dynGroup) {

//        final StringBuilder queryString = new StringBuilder(FROM);
//        queryString.append(DynGroup.class.getName());
//        queryString.append(WHERE);
//        queryString.append(GROUP_UUID);
//        queryString.append(" like '");
//        queryString.append(DynGroup.CONJ_COMP_INDIRECTION);
//        queryString.append(ESCAPE);
//        queryString.append(DynGroup.OPEN_CURLY_BRACKET);
//        queryString.append(SQL_JOCKER);
//        queryString.append(ESCAPE);
//        queryString.append(DynGroup.CLOSE_CURLY_BRACKET);
//        queryString.append(dynGroup.getGroupName());
//        queryString.append("'");
//        Query query = session.createQuery(queryString.toString());
//        query.setReadOnly(true);
//        query.setCacheable(true);
//        @SuppressWarnings("unchecked")
//        List<DynGroup> result =  query.list();
//        
        final StringBuilder queryString = new StringBuilder(FROM);
        queryString.append(DynGroup.class.getName());
        queryString.append(" g ");
        queryString.append(WHERE);
        queryString.append(" g.indirectedGroupId = ");
        queryString.append(dynGroup.getGroupId());
        Query query = session.createQuery(queryString.toString());
        query.setReadOnly(true);
        query.setCacheable(true);
        @SuppressWarnings("unchecked")
        List<DynGroup> result =  query.list();
        
        
        return result;
    }

    /**
     * Deletes a DynGroup instance.
     * @param dynGroup The instance to delete.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#deleteDynGroup(DynGroup)
     */
    public void deleteDynGroup(final DynGroup dynGroup) {
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        deleteDynGroupInternal(session, dynGroup);
        commit();
        closeSessionForThread();
    }
    
    /**
     * Deletes a group.
     * @param uuid The uuid of the group to delete.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#deleteDynGroup(java.lang.String)
     */
    public void deleteDynGroup(final String uuid) {
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        final DynGroup group = 
            (DynGroup) retrieveUniqueInstanceByAttributeInternal(session, DynGroup.class, GROUP_UUID, uuid);
        if (group != null) {
            deleteDynGroupInternal(session, group);
            if (LOGGER.isDebugEnabled()) {
                final StringBuilder sb = new StringBuilder("Deleting the group: " );
                sb.append(uuid);
                sb.append(".");
                LOGGER.debug(sb.toString());
            }
        
        } else {
            LOGGER.error("Unable to retrieve the group for deletion: " + uuid);
        }

        commit();
        closeSessionForThread();

    }

    /**
     * Resolves the indirections, i.e. : the groups that correspond to a conjunctive component
     * are replaced by the original group.<br/>
     * For instance, let G = A or B. The group G will be decoposed in G1=A and G2=B and the groups G1 and G2 
     * will be resolved with G. 
     * @param dynGroups The candidat groups to resolve.
     * @return The list of groups where the indirection are resolved.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#resolvConjunctiveComponentIndirections(java.util.Set)
     */
    public Set<DynGroup> resolvConjunctiveComponentIndirections(final Set<DynGroup> dynGroups) {
        final Set<DynGroup> result = new HashSet<DynGroup>();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Start to retrieve the group associated to the cunjunctive components: "
                    + dynGroups);
        }

        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        for (DynGroup dynGroup : dynGroups) {
            if (dynGroup.isConjunctiveComponentIndirection()) {
                final DynGroup resolvedGroup = (DynGroup) retrieveUniqueInstanceByAttributeInternal(session, 
                        DynGroup.class, GROUP_ID, String.valueOf(dynGroup.getIndirectedGroupId()));
                if (resolvedGroup != null) {
                    result.add(resolvedGroup); 
                } else {
                    LOGGER.error("Unable to retrieve the group associated to the conjunction component: " + dynGroup);
                }
            } else {
                result.add(dynGroup);
            }
        }

        commit();
        closeSessionForThread();


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resolution of the cunjunctive components: "
                    + result);
        }

        return result;
    }

    /**
     * Deletes all the (attribute,value) associated to the group.
     * @param session The session to use.
     * @param dynGroup The group for wich the associations should be deleted.
     */
    protected void deleteGroupAssociationsInternal(final Session session, final DynGroup dynGroup) {
        final Set<GroupAttributeValueAssoc> associations = getAttributeValuesAssociationForGroup(session, dynGroup);
        for (GroupAttributeValueAssoc association : associations) {
            deleteInternal(session, association);
        }
    }

    /**
     * Deletes a DynGroup instance.
     * @param session The session to use.
     * @param dynGroup The instance to delete.
     */
    protected void deleteDynGroupInternal(final Session session, final DynGroup dynGroup) {
        deleteGroupAssociationsInternal(session, dynGroup);
        deleteInternal(session, dynGroup);
        final List<DynGroup> conjGroups = retrieveConjunctiveComponents(session, dynGroup);

        for (DynGroup conjGroup : conjGroups) {
            deleteGroupAssociationsInternal(session, conjGroup);
            deleteInternal(session, conjGroup);
        }
    }

    /**
     * Modify a DynGroup instance.
     * @param dynGroup The instance to modify.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#modifyDynGroup(org.esco.dynamicgroups.domain.beans.DynGroup)
     */
    public void modifyDynGroup(final DynGroup dynGroup) {
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        modifyDynGroupInternal(session, dynGroup);
        commit();
        closeSessionForThread();
    }

    /**
     * Modify a DynGroup instance.
     * @param session The session to use.
     * @param dynGroup The instance to modify.
     */
    public void modifyDynGroupInternal(final Session session, final DynGroup dynGroup) {
        deleteDynGroupInternal(session, dynGroup);
        session.flush();
        storeDynGroupInternal(session, dynGroup);
    }

    /**
     * Stores a dynanic group if not present in the DB 
     * or modify the entry if the group is already stored.
     * @param definition The dynamic group to store of modify.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#storeOrModifyDynGroup(DynamicGroupDefinition)
     */
    public void storeOrModifyDynGroup(final DynamicGroupDefinition definition) {
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();


        DynGroup group = (DynGroup) retrieveUniqueInstanceByAttributeInternal(session, 
                DynGroup.class, GROUP_UUID, definition.getGroupUUID());

        if (group == null) {
            if (definition.isValid()) {
                storeDynGroupInternal(session, new DynGroup(definition));
            }
        } else {
            if (definition.isValid()) {
                group.setGroupDefinition(definition.getProposition().toString());
                modifyDynGroupInternal(session, group);
            } else {
                deleteDynGroupInternal(session, group);
            }
        }

        commit();
        closeSessionForThread();
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
     * @param attributeValues The values of the attribute.
     * @return The set of groups which use the values of the attribute in their definition 
     * (possibly with negation).
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getGroupsForAttributeValues(String, String[])
     */
    public Set<DynGroup> getGroupsForAttributeValues(final String attributeName, 
            final String[] attributeValues) {

        if (attributeValues.length == 0) {
            return new HashSet<DynGroup>();
        }
        
        final Set<DynGroup> result = new HashSet<DynGroup>();

        startTransaction();
        
        final Session session = openOrRetrieveSessionForThread();
     
        
        final StringBuilder queryString = new StringBuilder("SELECT gva");
        queryString.append(" ");
        queryString.append(FROM); 
        queryString.append(GroupAttributeValueAssoc.class.getSimpleName());
        queryString.append(" gva ");
        queryString.append(", ");
        queryString.append(DynGroup.class.getSimpleName());
        queryString.append(" dg, ");
        queryString.append(DynAttribute.class.getSimpleName());
        queryString.append(" da ");
        queryString.append(WHERE);
        queryString.append(" gva.attribute.attributeId = da.attributeId");
        queryString.append(" AND da.attributeName = '" );
        queryString.append(attributeName);
        queryString.append("'");
        
        queryString.append(" AND gva.group.groupId = dg.groupId");
      
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(queryString.toString());
        }
        
        // To handle the groups defined on the attribute values and those defined on 
        // their negation.
        StringBuilder noNegValueCritSb = new StringBuilder();
        StringBuilder negValueCritSb = new StringBuilder();
        boolean init = true;
        for (String value : attributeValues) {
            final String escValue = stringCleaner.clean(value);
             
            if (init) {
                init = false;
                noNegValueCritSb = new StringBuilder(" (('");
                noNegValueCritSb.append(escValue);
                noNegValueCritSb.append("' LIKE ");
                noNegValueCritSb.append("gva.attributeValue");
                
                negValueCritSb = new StringBuilder(" (NOT('");
                negValueCritSb.append(escValue);
                negValueCritSb.append("' LIKE ");
                negValueCritSb.append("gva.attributeValue");
            } else {
                
                noNegValueCritSb.append(" OR '");
                noNegValueCritSb.append(escValue);
                noNegValueCritSb.append("' LIKE ");
                noNegValueCritSb.append("gva.attributeValue");
                
                negValueCritSb.append(" OR '");
                negValueCritSb.append(escValue);
                negValueCritSb.append("' LIKE ");
                negValueCritSb.append("gva.attributeValue");
            }
        }
        
        noNegValueCritSb.append(") AND ");
        noNegValueCritSb.append("gva.negative");
        noNegValueCritSb.append("=FALSE)");
        
        negValueCritSb.append(") AND ");
        negValueCritSb.append("gva.negative");
        negValueCritSb.append("=TRUE)");
      
        queryString.append(" AND (");
        queryString.append(noNegValueCritSb.toString());
        queryString.append(" OR ");
        queryString.append(negValueCritSb.toString());
        queryString.append(") ");
        

        // Retieves the associations.
        final Query query = session.createQuery(queryString.toString());
        query.setCacheable(true);
        
        @SuppressWarnings("unchecked")
        final List<GroupAttributeValueAssoc> associations =  query.list();


        // Retrieves the groups.
        for (GroupAttributeValueAssoc association : associations) {
            result.add(association.getGroup());
        }
        closeSessionForThread();
        return result;
    }
    
    /**
     * Retrieves the list of attributes values involved in a group definition.
     * @param groupUUID The uuid of the considered group.
     * @return The set of attribute values.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getAttributeValuesForGroup(String)
     */
    public Set<AttributeValue> getAttributeValuesForGroup(final String groupUUID) {

        final Set<AttributeValue> result = new HashSet<AttributeValue>();

        startTransaction();

        final Session session = openOrRetrieveSessionForThread();
        final Criteria criteria = session.createCriteria(GroupAttributeValueAssoc.class);
        criteria.setFetchMode(GROUP, FetchMode.JOIN);
        criteria.setFetchMode(ATTRIBUTE, FetchMode.JOIN);
        criteria.createCriteria(GROUP, GROUP).add(Restrictions.eq(GROUP_UUID, groupUUID));
        criteria.setCacheable(true);

        @SuppressWarnings("unchecked")
        final List<GroupAttributeValueAssoc> associations =  criteria.list();
        closeSessionForThread();

        // Retrieves the attributes.
        for (GroupAttributeValueAssoc association : associations) {
            result.add(new AttributeValue(association.getAttribute().getAttributeName(), 
                    association.getAttributeValue(), association.isNegative()));
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
                groupConj = Restrictions.eq(GROUP_UUID, group.getUuid());
            } else {
                groupConj = Restrictions.or(groupConj, Restrictions.eq(GROUP_UUID, group.getUuid()));
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
     * @param groupUUID The uuid of the group.
     * @return The values of the attribute in the group definition if the attribute
     * is present.
     * @see org.esco.dynamicgroups.dao.db.IDBDAOService#getAttributeValuesForGroup(String, String)
     */
    public Set<String> getAttributeValuesForGroup(final String attributeName, final String groupUUID) {
        startTransaction();
        final Session session = openOrRetrieveSessionForThread();
        final Set<String> result = new HashSet<String>();

        final Criteria criteria = session.createCriteria(GroupAttributeValueAssoc.class);
        criteria.setFetchMode(GROUP, FetchMode.JOIN);
        criteria.setFetchMode(ATTRIBUTE, FetchMode.JOIN);
        criteria.createCriteria(GROUP, GROUP).add(Restrictions.eq(GROUP_UUID, groupUUID));
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
     * Retrieves the (attribute, values) association for a given group.
     * @param session The session to use.
     * @param dynGroup The dynamic group.
     * @return The set of association instances involving the group.
     */
    protected Set<GroupAttributeValueAssoc> getAttributeValuesAssociationForGroup(final Session session, 
            final DynGroup dynGroup) {
        final Set<GroupAttributeValueAssoc> result = new HashSet<GroupAttributeValueAssoc>();

        final Criteria criteria = session.createCriteria(GroupAttributeValueAssoc.class);
        criteria.setFetchMode(GROUP, FetchMode.JOIN);
        criteria.createCriteria(GROUP, GROUP).add(Restrictions.eq(GROUP_UUID, dynGroup.getGroupUUID()));
        criteria.setCacheable(true);

        @SuppressWarnings("unchecked")
        final List<GroupAttributeValueAssoc> associations =  criteria.list();
        result.addAll(associations);

        return result;
    }

    /**
     * Getter for stringCleaner.
     * @return stringCleaner.
     */
    public IStringCleaner getStringCleaner() {
        return stringCleaner;
    }

    /**
     * Setter for stringCleaner.
     * @param stringCleaner the new value for stringCleaner.
     */
    public void setStringCleaner(final IStringCleaner stringCleaner) {
        this.stringCleaner = stringCleaner;
    }

   
}