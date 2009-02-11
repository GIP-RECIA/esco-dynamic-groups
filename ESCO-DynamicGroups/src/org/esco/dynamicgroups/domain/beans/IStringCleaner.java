
package org.esco.dynamicgroups.domain.beans;

/**
 * Interface for the string cleaners.
 * @author GIP RECIA - A. Deman
 * 9 févr. 2009
 *
 */
public interface IStringCleaner {

    /**
     * Cleans the a string.
     * @param str The string to clean.
     * @return The cleaned string.
     */
    String clean(final String str);
}
