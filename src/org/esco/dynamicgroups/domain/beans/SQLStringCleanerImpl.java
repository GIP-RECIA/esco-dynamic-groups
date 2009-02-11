/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;

/**
 * Imlementation of a String cleaner for a SQL context.
 * @author GIP RECIA - A. Deman
 * 9 févr. 2009
 *
 */
public class SQLStringCleanerImpl implements IStringCleaner, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -5538097487956464749L;
    
    /** The character to remove. */
    private static final String QUOTE = "\'";
    
    /** The empty string. */
    private static final String EMPTY_STR = ""; 

    
    /**
     * Builds an instance of SQLStringCleanerImpl.
     */
    public SQLStringCleanerImpl() {
        super();
    }
    
    /**
     * Cleans the String for the SQL repository.
     * @param str The string to clean.
     * @return The cleaned String.
     * @see org.esco.dynamicgroups.domain.beans.IStringCleaner#clean(java.lang.String)
     */
    public String clean(final String str) {
        return str.replaceAll(QUOTE, EMPTY_STR);
    }

}
