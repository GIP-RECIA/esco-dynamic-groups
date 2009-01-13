package org.esco.dynamicgroups.dao.db;

import java.util.Set;

import org.esco.dynamicgroups.domain.beans.DynAttribute;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.GroupAttributeValueAssoc;

/**
 * Interface for the dao service.
 * @author GIP RECIA - A. Deman
 * 13 janv. 2009
 *
 */
public interface DAOService {

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
    void storeGroupAttributeValueAssoc(
            final GroupAttributeValueAssoc grpAttAssoc);

    /**
     * Stores a DynGroup instance.
     * @param dynGroup The instance to store.
     */
    void storeDynGroup(final DynGroup dynGroup);

    /**
     * Retrieves the groups associated to a given value of a specified attribute.
     * @param attributeName The considered attribute.
     * @param attributeValue The value of the attribute.
     * @return The set of groups which use the value of the attribute in their definition.
     */
    Set<DynGroup> getGroupsByAttributeValue(final String attributeName,
            final String attributeValue);

}