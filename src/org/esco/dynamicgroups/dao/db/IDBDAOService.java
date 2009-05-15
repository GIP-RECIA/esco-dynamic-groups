package org.esco.dynamicgroups.dao.db;


import edu.internet2.middleware.grouper.Group;

import java.util.Set;

import org.esco.dynamicgroups.domain.beans.AttributeValue;
import org.esco.dynamicgroups.domain.beans.DynAttribute;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.GroupAttributeValueAssoc;
import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;


/**
 * Interface for the dao service.
 * @author GIP RECIA - A. Deman
 * 13 janv. 2009
 *
 */
public interface IDBDAOService {

    /**
     * Retrieves the attribute associated to a given name.
     * @param name The name of the attribute.
     * @return The attribute if found, null otherwise.
     */
    DynAttribute getDynAttributeByName(final String name);

    /**
     * Retrieves the attribute associated to a given name.
     * @param name The name of the attribute.
     * @return The attribute if found, null otherwise.
     */
    DynGroup getDynGroupByName(final String name);

    /**
     * Stores a DynAttribute instance.
     * @param dynAttribute The instance to store.
     */
    void storeDynAttribute(final DynAttribute dynAttribute);

    /**
     * Stores a GroupAttributeValueAssoc instance.
     * @param grpAttAssoc The instance to store.
     */
    void storeGroupAttributeValueAssoc(final GroupAttributeValueAssoc grpAttAssoc);

    /**
     * Stores a DynGroup instance.
     * @param dynGroup The instance to store.
     */
    void storeDynGroup(final DynGroup dynGroup);
    
    /**
     * Deletes a group.
     * @param groupName The name of the group to delete.
     */
    void deleteDynGroup(final String groupName);
    
    /**
     * Deletes a DynGroup instance.
     * @param dynGroup The instance to delete.
     */
    void deleteDynGroup(final DynGroup dynGroup);
    
    /**
     * Modify a DynGroup instance.
     * @param dynGroup The instance to modify.
     */
    void modifyDynGroup(final DynGroup dynGroup);
    
    /**
     * Stores a dynanic group if not present in the DB 
     * or modify the entry if the group is already stored.
     * @param definition The dynamic group to store of modify.
     */
    void storeOrModifyDynGroup(final DynamicGroupDefinition definition);
    
    /**
     * Retrieves the groups associated to a given attribute.
     * @param attributeName The considered attribute.
     * @return The set of groups which use the attribute in their definition.
     */
    Set<DynGroup> getGroupsForAttribute(final String attributeName);

    /**
     * Retrieves the groups associated to a given value of a specified attribute.
     * @param attributeName The considered attribute.
     * @param attributeValues The values of the attribute.
     * @return The set of groups which use the values of the attribute in their definition 
     * (possibly with negation).
     */
    Set<DynGroup> getGroupsForAttributeValues(final String attributeName,
            final String[] attributeValues);
    
    /**
     * Retrieves the values of a given attribute for a group.
     * @param attributeName The name of the attribute.
     * @param groupName The name of the group.
     * @return The value of the attribute in the group definition if the attribute
     * is present.
     */
    Set<String> getAttributeValuesForGroup(final String attributeName, final String groupName);
    
    
    /**
     * Gives the values, for a given attribute, associated to a set of groups.
     * @param attributeName The name of the attribute.
     * @param groups The considered groups.
     * @return The set of values for the considered attribute.
     */
    Set<String> getAttributeValuesForGroups(final String attributeName, final Set<Group> groups);
    
    /**
     * Retrieves the list of attributes values involved in a group definition.
     * @param groupName The of the considered group.
     * @return The set of attribute values.
     */
    Set<AttributeValue> getAttributeValuesForGroup(final String groupName);
    
    /**
     * Resolves the indirections, i.e. : the groups that correspond to a conjunctive component
     * are replaced by the original group.<br/>
     * For instance, let G = A or B. The group G will be decoposed in G1=A and G2=B and the groups G1 and G2 
     * will be resolved with G. 
     * @param dynGroups The candidat groups to resolve.
     * @return The list of groups where the indirection are resolved.
     */
    Set<DynGroup> resolvConjunctiveComponentIndirections(final Set<DynGroup> dynGroups);

}