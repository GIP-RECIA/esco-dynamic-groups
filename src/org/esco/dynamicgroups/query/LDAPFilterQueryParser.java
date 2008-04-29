/**
 * 
 */
package org.esco.dynamicgroups.query;

import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * Parser for queries that are LDAP filter.
 * @author GIP RECIA - A. Deman
 * 29 avr. 08
 *
 */
public class LDAPFilterQueryParser implements IQueryParser {

    /** Serial version UID.*/
    private static final long serialVersionUID = -439836808909342594L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(LDAPFilterQueryParser.class);
    
    /** Separator used to retrieve the attributes in a query. */ 
    private static final String END_TOKEN = "=";
    
    /** Separator used to retrieve the attributes in a query. */ 
    private static final String BEGIN_TOKEN = "(";

    /**
     * Constructor for LDAPFilterQueryParser.
     */
    public LDAPFilterQueryParser() {
        /* */
    }
    
    /**
     * Parses a query to retrieves attributes.
     * @param query The Query to parse.
     * @return The list of attributes.
     * @see org.esco.dynamicgroups.query.IQueryParser#retrieveInvolvedAttributes(java.lang.String)
     */
    public String[] retrieveInvolvedAttributes(final String query) {
        final String[] tokens = query.split(END_TOKEN);
        final String[] attributes = new String[tokens.length - 1];   
        for (int i = 0; i < attributes.length; i++) {
            final String token = tokens[i].trim();
            if (token.contains(BEGIN_TOKEN)) {
                final int index = token.lastIndexOf(BEGIN_TOKEN) + 1;
                attributes[i] = token.substring(index).trim();
            } else if (i == 0) {
                attributes[i] = token;
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Retrieving involved atttributes in " + query + " - Result: " + Arrays.toString(attributes));
        }
        return attributes;
    }
    
    /**
     * Gives the string that represents this instance.
     * @return The class name of the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
