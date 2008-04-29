/**
 * 
 */
package org.esco.dynamicgroups;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.query.IQueryParser;
import org.esco.dynamicgroups.query.QueryParserFactory;
import org.esco.dynamicgroups.util.PropertyParser;

/**
 * Used to map custom grouper groups with some parameters associated 
 * to the source associated to the subject API.
 * @author GIP RECIA - A. Deman
 * 29 avr. 08
 *
 */
public class DynamicGroupsMapping implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DynamicGroupsMapping.class);
    
    /** The xml file to load from the class path. */
    private static final String ESCO_DG_MAPPING_FILE = "esco-dynamicGroupsMapping.xml";
    
    /** The prefix for the default properties. */
    private static final String DEFAULT_PROPERTIES_PREFIX = "esco-dynamicGroups.default.";
    
    /** The singleton instance. */
    private static final DynamicGroupsMapping INSTANCE = new DynamicGroupsMapping(); 
    
    /** The id of the default subject source to use. */
    private String defaultSourceId;
    
    /** The id of the default subject search Id to use.*/
    private String defaultSearchId;
    
    /** The query parser to use. */
    private String defaultQueryParserClass;
    
    /** The group definitions. */
    private Map<String, DynamicGroupTypeDefinition> groupTypeDefinitions;
    
    /**
     * Constructor for DynamicGroupsMapping.
     */
    protected DynamicGroupsMapping() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loading ESCODynamicGroupsMapping from file " + ESCO_DG_MAPPING_FILE);
            initialize();
            LOGGER.debug("Loaded values: " + this);
        }
    }
    
    /**
     * Gives the singleton.
     * @return The available instance.
     */
    public static DynamicGroupsMapping instance() {
        return INSTANCE;
    }
    
    /**
     * Initialization.
     */
    private void initialize() {
        try {
            groupTypeDefinitions = new HashMap<String, DynamicGroupTypeDefinition>();
            final ClassLoader cl = getClass().getClassLoader();
            final InputStream is  = cl.getResourceAsStream(ESCO_DG_MAPPING_FILE);
            final Properties params = new Properties();
            if (is == null) {
                LOGGER.error("Unable to load (from classpath) " + ESCO_DG_MAPPING_FILE);
            }
            params.loadFromXML(is);
            loadFromProperties(params);
        } catch (InvalidPropertiesFormatException e) {
            LOGGER.error(e, e);
        } catch (IOException e) {
            LOGGER.error(e, e);
        }
    }
    
    /**
     * Loads the values from a properties INSTANCE.
     * @param params The properties that contains the values to load.
     */
    @SuppressWarnings("unchecked")
    private void loadFromProperties(final Properties params) {
        
        // keys used to retrieve the values in the properties INSTANCE. 
        final String defaultSourceIdKey = DEFAULT_PROPERTIES_PREFIX + "sourceId";
        final String defaultSearchIdKey = DEFAULT_PROPERTIES_PREFIX + "searchId";
        final String defaultQueryParserKey = DEFAULT_PROPERTIES_PREFIX + "queryParserClass";
        
        // loads the default values
        defaultSourceId = parseStringFromProperty(params, defaultSourceIdKey);
        defaultSearchId = parseStringFromProperty(params, defaultSearchIdKey);
        defaultQueryParserClass = parseStringFromProperty(params, defaultQueryParserKey);

        // Check the class name for the default query parer.
        final IQueryParser defaultParser = QueryParserFactory.instance().getQueryParser(defaultQueryParserClass);
        if (defaultParser == null) {
            LOGGER.error("Error while loading the default Parser class: " + defaultQueryParserClass);
        }
       
        // Loads the group type definitions.
        final Enumeration<Object> keys = params.keys();
        
        while (keys.hasMoreElements()) {
        
            final String key = ((String) keys.nextElement()).trim();
            
            if (!key.startsWith(DEFAULT_PROPERTIES_PREFIX)) {
            
                final String groupType = PropertyParser.instance().retrievePrefix(key);
                
                if (!groupTypeDefinitions.containsKey(groupType)) {
                
                    final String sourceIdKey = groupType + ".sourceId";
                    final String searchIdKey = groupType + ".searchId";
                    final String queryKey    = groupType + ".query";
                    final String queryParserKey = groupType + ".queryParserClass";
                    
                    final String sourceId = PropertyParser.instance().parseStringFromPropertySafe(params, 
                            sourceIdKey, defaultSourceId);
                    final String searchId = PropertyParser.instance().parseStringFromPropertySafe(params, 
                            searchIdKey, defaultSearchId);
                    final String query = parseStringFromProperty(params, queryKey);
                    final String queryParserClass = PropertyParser.instance().parseStringFromPropertySafe(params, 
                            queryParserKey, defaultQueryParserClass);
                    final IQueryParser parser = QueryParserFactory.instance().getQueryParser(queryParserClass);
                    
                    final DynamicGroupTypeDefinition def = new DynamicGroupTypeDefinition(groupType, 
                                sourceId, searchId, query, parser);
                    groupTypeDefinitions.put(groupType, def);
                }
            }
        }
    }
    
    /**
     * Gives the String representation of this instance.
     * @return The String that represents the values of this instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append("#{default source Id: ");
        sb.append(getDefaultSourceId());
        sb.append(", defautt search Id: ");
        sb.append(getDefaultSearchId());
        sb.append(", Definitions: ");
        boolean first = true;
        for (DynamicGroupTypeDefinition def : groupTypeDefinitions.values()) {
            if (!first) {
                sb.append(" ; ");
            } else {
                first = false;
            }
            sb.append(def);
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * Tests if there is a definition for a given type name.
     * @param groupTypeName The name of the type.
     * @return True if there is a definition for the group type.
     */
    public boolean containsDefinitionForGroupType(final String groupTypeName) {
        return groupTypeDefinitions.containsKey(groupTypeName);
    }
    
    /**
     * Gives a group definition for a given group type name.
     * @param groupTypeName The name of the type of group.
     * @return The definition if found null otherwise.
     */
    public DynamicGroupTypeDefinition getDefinitionForGroupType(final String groupTypeName) {
        return groupTypeDefinitions.get(groupTypeName);
    }
   
    /**
     * Retrieves the string value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String value if available in the properties, null otherwise.
     */
    private String parseStringFromProperty(final Properties properties, final String key) {
      return PropertyParser.instance().parseStringFromProperty(LOGGER, ESCO_DG_MAPPING_FILE, properties, key);
    }
    
    /**
     * Getter for defaultSourceId.
     * @return the defaultSourceId
     */
    public String getDefaultSourceId() {
        return defaultSourceId;
    }
    /**
     * Setter for defaultSourceId.
     * @param defaultSourceId the defaultSourceId to set
     */
    protected void setDefaultSourceId(final String defaultSourceId) {
        this.defaultSourceId = defaultSourceId;
    }
    /**
     * Getter for defaultSearchId.
     * @return the defaultSearchId
     */
    public String getDefaultSearchId() {
        return defaultSearchId;
    }
    /**
     * Setter for defaultSearchId.
     * @param defaultSearchId the defaultSearchId to set
     */
    protected void setDefaultSearchId(final String defaultSearchId) {
        this.defaultSearchId = defaultSearchId;
    }
    /**
     * Getter for groupTypeDefinitions.
     * @return the groupTypeDefinitions
     */
    public Map<String, DynamicGroupTypeDefinition> getGroupTypeDefinitions() {
        return groupTypeDefinitions;
    }
    /**
     * Setter for groupTypeDefinitions.
     * @param groupTypeDefinitions the groupTypeDefinitions to set
     */
    protected void setGroupTypeDefinitions(final Map<String, DynamicGroupTypeDefinition> groupTypeDefinitions) {
        this.groupTypeDefinitions = groupTypeDefinitions;
    }
}
