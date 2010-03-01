/**
 * 
 */
package org.esco.dynamicgroups.domain.beans;

/**
 * Interface for the I18N managers.
 * @author GIP RECIA - A. Deman
 * 5 oct. 2009
 *
 */
public interface II18NManager {
    
    /**
     * Gives the message that corresponds to an i18n key.
     * @param key The i18n key.
     * @return The message.
     */
    String getI18nMessage(final String key);
    
    /**
     * Gives the message that corresponds to an i18n key.
     * @param key The i18n key.
     * @param args The arguments to include in the message.
     * @return The message.
     */
    String getI18nMessage(final String key, final String...args);

}
