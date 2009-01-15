/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import java.util.Map;

import org.esco.dynamicgroups.domain.beans.DynGroup;

/**
 * @author GIP RECIA - A. Deman
 * 15 janv. 2009
 *
 */
public interface IGroupsDAOService {
    /**
     * Updates the memberships of an user.
     * @param userId The id of the user.
     * @param newGroups The new groups.
     */
    void updateMemberships(final String userId, final Map<String, DynGroup> newGroups);
}
