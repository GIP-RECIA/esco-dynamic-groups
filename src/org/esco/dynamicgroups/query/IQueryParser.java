/**
 * 
 */
package org.esco.dynamicgroups.query;

import java.io.Serializable;

/**
 * Base interface for the query parsers.
 * @author GIP RECIA - A. Deman
 * 29 avr. 08
 *
 */
public interface IQueryParser extends Serializable {

    
    /**
     * Retrieves the involved attributes from a query.
     * @param query The query from which the attributes are retrieved.
     * @return  The array of the involved attribute names.
     */
    String[] retrieveInvolvedAttributes(final String query);
    
}
