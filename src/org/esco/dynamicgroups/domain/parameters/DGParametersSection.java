/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;


import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.util.PropertyParser;



/**
 * Base class for a group of user parameters.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public abstract class DGParametersSection implements Serializable {

    /** Prefix for the properties. */
    protected static final String PROPERTIES_PREFIX = "esco.dynamic.groups.";
    
    /** The default value for the configuration file. */
    private static final String UNDEF_CONF_FILE = "<Undefined file>";

    /** Serial version UID.*/
    private static final long serialVersionUID = -1638308508107621926L;

    /** The configuration file associated to the properties (used in the error messages). */
    private String configurationFile = UNDEF_CONF_FILE;

    /**
     * Loads the values from a properties instance.
     * @param params The properties that contains the values to load.
     */
    public abstract void loadFromProperties(final Properties params);

    /** 
     * Gives the logger to use for the class.
     * @return The logger.
     */
    protected abstract Logger getLogger();

    /**
     * Retrieves the Boolean value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The Boolean value if available in the properties, null otherwise.
     */
    protected Boolean parseBooleanFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseBooleanFromProperty(getLogger(), configurationFile, properties, key);
    }

    /**
     * Retrieves the integer value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The Integer value if available in the properties, null otherwise.
     */
    protected Integer parseIntegerFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseIntegerFromProperty(getLogger(), configurationFile, properties, key);
    }

    /**
     * Retrieves a positive integer value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @param defaultValue The default value to use if the property is not found.
     * @return The positive  Integer value if available and valid in the properties, 0 otherwise.
     */
    protected Integer parsePositiveIntegerSafeFromProperty(final Properties properties, 
            final String key, final int defaultValue) {
        Integer value =  PropertyParser.instance().parsePositiveIntegerFromPropertySafe(getLogger(), 
                configurationFile, properties, key);

        if (value == null) {
            getLogger().warn("Unable to retrieve a valid value in the file: " + configurationFile
                    + " for the property: " + key
                    + " - Using the default value: " + defaultValue + ".");
            return defaultValue;
        }

        return value;
    }


    /**
     * Retrieves a positive integer value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @param defaultValue The default value to use if the property is not found.
     * @return The positive  Integer value if available and valid in the properties, 0 otherwise.
     */
    protected Integer parseStrictPositiveIntegerSafeFromProperty(final Properties properties, 
            final String key, final int defaultValue) {
        Integer value =  PropertyParser.instance().parseStrictPositiveIntegerFromPropertySafe(getLogger(), 
                configurationFile, properties, key);

        if (value == null) {
            getLogger().warn("Unable to retrieve a valid value in the file: " + configurationFile
                    + " for the property: " + key
                    + " - Using the default value: " + defaultValue + ".");
            return defaultValue;
        }

        return value;
    }

    /**
     * Retrieves the string value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String value if available in the properties, null otherwise.
     */
    protected String parseStringFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseStringFromProperty(getLogger(), configurationFile, properties, key);
    }
    /**
     * Retrieves the string value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @param defaultValue The default value to use if the key is not in the Properties instance.
     * @return The String value if available in the properties, null otherwise.
     */
    protected String parseStringSafeFromProperty(final Properties properties, 
            final String key, 
            final String defaultValue) {
        return PropertyParser.instance().parseStringFromPropertySafe(properties, key, defaultValue);
    }

    /**
     * Retrieves the string array value from a properties instance for a given key.
     * @param properties The properties instance.
     * @param key The considered key.
     * @return The String array value if available in the properties, null otherwise.
     */
    protected String[] parseStringArrayFromProperty(final Properties properties, final String key) {
        return PropertyParser.instance().parseStringArrayFromProperty(getLogger(), configurationFile, properties, key);
    }

    /**
     * Getter for configurationFile.
     * @return configurationFile.
     */
    public String getConfigurationFile() {
        return configurationFile;
    }

    /**
     * Setter for configurationFile.
     * @param configurationFile the new value for configurationFile.
     */
    public void setConfigurationFile(final String configurationFile) {
        this.configurationFile = configurationFile;
    }
}
