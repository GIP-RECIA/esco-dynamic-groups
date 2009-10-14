/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Bean that can be used to disable the i18n support.
 * @author GIP RECIA - A. Deman
 * 5 oct. 2009
 *
 */
public class DisabledI18NManager implements II18NManager, Serializable {

    
    /** Serial version UID.*/
    private static final long serialVersionUID = 1321597261487055320L;
    
    /** Flag to determine if the keys or empty strings are returned. */ 
    private boolean returnKeys;

    /**
     * Builds an instance of DisabledI18NManager.
     */
    public DisabledI18NManager() {
        this(false);
    }
    
    /**
     * Builds an instance of DisabledI18NManager.
     * @param returnKeys Flag that determines if the keys are returned.
     */
    public DisabledI18NManager(final boolean returnKeys) {
        this.returnKeys = returnKeys;
    }
    
    /**
     * Disbabled message.
     * @param key The i18n key or an empty string (depending on the property returnKeys)
     * @return The key.
     * @see org.esco.dynamicgroups.domain.beans.II18NManager#getI18nMessage(String)
     */
    public String getI18nMessage(final String key) {
        if (returnKeys) {
            return key;
        } 
        return "";
    }

    /**
     * @param key The i18n key.
     * @param args Not used.
     * @return The key or an empty string (depending on the property returnKeys).
     * @see org.esco.dynamicgroups.domain.beans.II18NManager#getI18nMessage(java.lang.String, java.lang.String[])
     */
    public String getI18nMessage(final String key, final String... args) {
        if (returnKeys) {
            return key + " - " + Arrays.toString(args);
        } 
        return "";
    }

}
