/**
 * 
 */
package org.esco.dynamicgroups.domain.statistics;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *  Container for the membership's statistics.
 * @author GIP RECIA - A. Deman
 * 24 févr. 2009
 */
public class MembershipStatsEntry implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -7256703407099055223L;

    /** Total number of added users. */
    private int totalAddedCount;
    
    /** Total number of removed users. */
    private int totalRemovedCount;
    
    /** Id of the removed users. */
    private final Set<String> removed = new HashSet<String>();
    
    /** id of the added users. */
    private final Set<String> added = new HashSet<String>();

    /**
     * Builds an instance of MembershipStatsEntry.
     */
    public MembershipStatsEntry() {
        super();
    }
    
    /**
     * Records an added member.
     * @param memberId The id of the added member.
     */
    public void recordAddedMember(final String memberId) {
        added.add(memberId);
    }
    
    /**
     * Records a removed member.
     * @param memberId The id of the removed member.
     */
    public void recordRemovedMember(final String memberId) {
        removed.add(memberId);
    }
    
    /**
     * Gives the string representation of the entry.
     * @return The string representation of the entry.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
       final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
       sb.append("#{");
       sb.append("totalAddedCount: ");
       sb.append(totalAddedCount);
       sb.append(", ");
       sb.append("totalRemovedCount: ");
       sb.append(totalRemovedCount);
       sb.append(", added members: ");
       sb.append(added);
       sb.append(", removed members: ");
       sb.append(removed);
       sb.append("}");
       return sb.toString();
    }
    
    /**
     * Gives the hash code for this entry.
     * @return The hash code.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return totalAddedCount + added.hashCode() 
            + totalRemovedCount + removed.hashCode();
    }
    
    /**
     * Test the equality with another object.
     * @param obj The object ot test.
     * @return True if the tested object is equal to this instance.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (!(obj instanceof MembershipStatsEntry)) {
            return false;
        }
        final MembershipStatsEntry other = (MembershipStatsEntry) obj;
        if (other.getTotalAddedCount() != getTotalAddedCount() 
                || other.getTotalRemovedCount() != getTotalRemovedCount()) {
            return false;
        }
        
        if (!other.getAdded().equals(getAdded())) {
            return false;
        }
        
        return other.getRemoved().equals(getRemoved());
    }

    /**
     * Getter for totalAddedCount.
     * @return totalAddedCount.
     */
    public int getTotalAddedCount() {
        return totalAddedCount;
    }

    /**
     * Gives the number of distinct members removed from a group.
     * @return The number of distinct users removed from a group.
     */
    public int getDistinctRemovedCount() {
        return removed.size();
    }
    
    /**
     * Gives the number of distinct members added to a group.
     * @return The number of distinct users added to a group.
     */
    public int getDistinctAddedCount() {
        return added.size();
    }

    /**
     * Getter for totalRemovedCount.
     * @return totalRemovedCount.
     */
    public int getTotalRemovedCount() {
        return totalRemovedCount;
    }

    /**
     * Getter for removed.
     * @return removed.
     */
    public Set<String> getRemoved() {
        return removed;
    }

    /**
     * Getter for added.
     * @return added.
     */
    public Set<String> getAdded() {
        return added;
    }
}
