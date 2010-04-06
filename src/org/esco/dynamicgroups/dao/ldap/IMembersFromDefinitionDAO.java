package org.esco.dynamicgroups.dao.ldap;

import java.util.Set;

import org.esco.dynamicgroups.domain.definition.DynamicGroupDefinition;

/**
 * Interface for the service used to retrieve the members associated to a definition
 * of a dynamic group.
 * @author GIP RECIA - A. Deman
 * 27 mai 2009
 *
 */
public interface IMembersFromDefinitionDAO {

    /**
     * Gives the members that are corresponding to the logic definition of a group.
     * @param definition The logic definition of the members of the group.
     * @return The set of the members ids.
     */
    Set<String> getMembers(final DynamicGroupDefinition definition);
    
}
