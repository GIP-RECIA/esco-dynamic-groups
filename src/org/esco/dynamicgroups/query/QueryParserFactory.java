/**
 * 
 */
package org.esco.dynamicgroups.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author GIP RECIA - A. Deman
 * 29 avr. 08
 *
 */
public class QueryParserFactory implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -2228771550565511790L;

    /** Logger.*/
    private static final Logger LOGGER = Logger.getLogger(QueryParserFactory.class);
    
    /** The singleton. */
    private static final QueryParserFactory INSTANCE = new QueryParserFactory();
    
    /** Instances of parsers. */
    private Map<String, IQueryParser> queryParsersInstances = new HashMap<String, IQueryParser>(); 
    
    
    /**
     * Constructor for QueryParserFactory.
     */
    protected QueryParserFactory() {
        /* Use the singleton. */
    }
    
    /**
     * Gives the singleton.
     * @return The singleton.
     */
    public static QueryParserFactory instance() {
        return INSTANCE;
    }

    /**
     * Gives an instance of IQueryParser.
     * @param queryParserClass The class name of the query parser to retrieve.
     * @return The instance of IQueryParser.
     */
    public IQueryParser getQueryParser(final String queryParserClass) {
        IQueryParser queryParser = queryParsersInstances.get(queryParserClass);
        if (queryParser == null) {
            try {
                queryParser = (IQueryParser) Class.forName(queryParserClass).newInstance();
                queryParsersInstances.put(queryParserClass, queryParser);
            } catch (ClassNotFoundException e) {
                LOGGER.error(e, e);
            } catch (InstantiationException e) {
                LOGGER.error(e, e);
            } catch (IllegalAccessException e) {
                LOGGER.error(e, e);
            }
        }
        return queryParser;
    }
    
}
