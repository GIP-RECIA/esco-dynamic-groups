/**
 * 
 */
package org.esco.dynamicgroups.util;

import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Util class used to load properties.
 * @author GIP RECIA - A. Deman
 * 29 avr. 08
 *
 */
public class PropertyParser implements Serializable { 
    
    /** Serial version UID. */
    private static final long serialVersionUID = 7763505305410596203L;

    /** Separator for the attributes. */
    private static final String ATTRIBUTES_SEP = " ";

    /** Separator in the property keys. */
    private static final String PROP_KEY_SEP = ".";

    /** Constant String. */
    private static final String IN_FILE = " in file: ";

    /** Singleton. */
    private static final PropertyParser INSTANCE = new PropertyParser();

    /**
     * Constructor for PropertyParser.
     */
    protected PropertyParser() {
        /* Use the singleton. */
    }
    
    /**
     * Gives the singleton.
     * @return The singleton.
     */
    public static PropertyParser instance() {
        return INSTANCE;
    }
    
    /**
     * Retrieves the string value from a properties INSTANCE for a given key.
     * @param logger The logger to use.
     * @param propertiesSourceName The name of the source used to load the properties.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String value if available in the properties, null otherwise.
     */
    public String parseStringFromProperty(final Logger logger,
            final String propertiesSourceName,
            final Properties properties, 
            final String key) {
        final String strValue = properties.getProperty(key);
        if (strValue == null) {
            logger.error("Unable to find a value for " + key + IN_FILE + propertiesSourceName);
            return null;
        }
        return strValue.trim();
    }
    
    /**
     * Retrieves the string value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @param defaultValue The default value.
     * @return The String value if available in the properties, the default value otherwise.
     */
    public String parseStringFromPropertySafe(final Properties properties, 
            final String key, final String defaultValue) {
        final String strValue = properties.getProperty(key);
        if (strValue == null) {
            return defaultValue;
        }
        return strValue.trim();
    }
    
    /**
     * Retrieves the string array value from a properties INSTANCE for a given key.
     * @param logger The logger to use.
     * @param propertiesSourceName The name of the source used to load the properties.
     * @param properties The properties INSTANCE.
     * @param key The considered key.
     * @return The String array value if available in the properties, null otherwise.
     */
    public String[] parseStringArrayFromProperty(final Logger logger,
            final String propertiesSourceName,
            final Properties properties, 
            final String key) {
        final String strValue = properties.getProperty(key);
        if (strValue == null) {
            logger.error("Unable to find a value for " + key + IN_FILE + propertiesSourceName);
            return null;
        }
        final String[] arrayValue = strValue.split(ATTRIBUTES_SEP);
        for (int i = 0; i < arrayValue.length; i++) {
            arrayValue[i] = arrayValue[i].trim();
        }
        return arrayValue;
    }
    
    /**
     * Retrieves the integer value from a properties INSTANCE for a given key.
     * @param logger The logger to use.
     * @param propertiesSourceName The name of the source used to load the properties.
     * @param properties The properties INSTANCE.
     * @param key The considered key.
     * @return The Integer value if available in the properties, null otherwise.
     */
    public Integer parseIntegerFromProperty(final Logger logger,
            final String propertiesSourceName,
            final Properties properties, 
            final String key) {
        final String strValue = properties.getProperty(key);
        if (strValue == null) {
            logger.error("Unable to find a value for " + key + IN_FILE + propertiesSourceName);
            return null;
        }
        try {
            final int intValue = Integer.parseInt(strValue.trim());
            return intValue;
        } catch (NumberFormatException e) {
            logger.error("Invalid value for " + key + ": " + strValue + IN_FILE + propertiesSourceName);
            logger.error(e, e);
            return null;
        }
    }
    
    /**
     * Retrieves the double value from a properties INSTANCE for a given key.
     * @param logger The logger to use.
     * @param propertiesSourceName The name of the source used to load the properties.
     * @param properties The properties INSTANCE.
     * @param key The considered key.
     * @return The double value if available in the properties, null otherwise.
     */
    public Double parseDoubleFromProperty(final Logger logger,
            final String propertiesSourceName,
            final Properties properties, 
            final String key) {
        final String strValue = properties.getProperty(key);
        if (strValue == null) {
            logger.error("Unable to find a value for " + key + IN_FILE + propertiesSourceName);
            return null;
        }
        try {
            final double doubleValue = Double.parseDouble(strValue.trim());
            return doubleValue;
        } catch (NumberFormatException e) {
            logger.error("Invalid value for " + key + ": " + strValue + IN_FILE + propertiesSourceName);
            logger.error(e, e);
            return null;
        }
    }
    
    /**
     * Retrieves the prefix of a key.
     * @param key The key. For instance if the key is esco-dynamicGroups.defaultSourceId 
     * then esco-dynamicGroups will be returned.
     * @return The prefix of the key.
     */
    public String retrievePrefix(final String key) {
        if (key.contains(PROP_KEY_SEP)) {
            return key.substring(0, key.indexOf(PROP_KEY_SEP)).trim();
        }
        return key.trim();
    }
    
    
}
