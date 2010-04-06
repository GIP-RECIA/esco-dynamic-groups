/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Informations about the missing or the invalid members of a group.
 * @author GIP RECIA - A. Deman
 * 2 juin 2009
 */
public class MissingOrInvalidMembersEntry implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 5960202885495003410L;

    /** The missing memebers. */
    private Set<String> missingMembers;

    /** The invalid memebers. */
    private Set<String> invalidMembers;
    
    /**
     * Builds an instance of MissingOrInvalidMembersEntry.
     */
    public MissingOrInvalidMembersEntry() {
        super();
    }
    
    /**
     * Adds an invalid mamber.
     * @param uid The id to the invalid memeber.
     */
    public void addInvalidMember(final String uid) {
        if (invalidMembers == null) {
            invalidMembers = new HashSet<String>();
        }
        invalidMembers.add(uid);
    }
    
    /**
     * Adds an invalid mamber.
     * @param uid The id to the invalid memeber.
     */
    public void addMissingMember(final String uid) {
        if (missingMembers == null) {
            missingMembers = new HashSet<String>();
        }
        missingMembers.add(uid);
    }

    /**
     * Tests if there are missing members.
     * @return true if there are missing members. 
     */
    public boolean hasMissingMembers() {
        return missingMembers != null;
    }

    /**
     * Tests if there are invalid members.
     * @return true if there are invalid members. 
     */
    public boolean hasInvalidMembers() {
        return invalidMembers != null;
    }

    /**
     * Getter for missingMembers.
     * @return missingMembers.
     */
    public Set<String> getMissingMembers() {
        return missingMembers;
    }

    /**
     * Getter for invalidMembers.
     * @return invalidMembers.
     */
    public Set<String> getInvalidMembers() {
        return invalidMembers;
    }
    
}
