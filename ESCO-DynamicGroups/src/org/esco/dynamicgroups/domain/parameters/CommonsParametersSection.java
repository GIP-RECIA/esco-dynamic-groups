/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;


import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;



/**
 * Common section for the user parameters.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public class CommonsParametersSection extends DGParametersSection {

    /** Serial version UID. */
    private static final long serialVersionUID = -5056579857503354986L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(CommonsParametersSection.class);

    /** The locale to use. */
    private Locale locale;

    /** Flag to determine the format to use for the mail, reports, etc. */
    private boolean xHTML;

    /**
     * Constructor for CommonsParametersSection.
     */
    private CommonsParametersSection() {
        super();
    }

    /**
     * Gives the logger for this class.
     * @return The logger for this class.
     * @see org.esco.dynamicgroups.domain.parameters.DGParametersSection#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }


    /**
     * Loads the common parameters.
     * @param params The properties that contains the values to load.
     */
    @Override
    public void loadFromProperties(final Properties params) {

        // keys used to retrieve the values in the properties instance. 
        final String localeKey = PROPERTIES_PREFIX + "locale";
        final String xHTMLKey = PROPERTIES_PREFIX + "xhtml";
        
        // Retrieves the values.
        setLocale(new Locale(parseStringFromProperty(params, localeKey)));
        setXHTML(parseBooleanFromProperty(params, xHTMLKey));
    }


    /**
     * Gives the string representation of the instance.
     * @return The String that denotes the values of the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        toStringFormatSingleEntry(sb, getClass().getSimpleName() + "#{\n");
        toStringFormatProperty(sb, "Locale: ", getLocale());
        toStringFormatProperty(sb, "xhtml: ", getXHTML());
        toStringFormatSingleEntry(sb, "}");
        return sb.toString();
    }


    /**
     * Getter for locale.
     * @return locale.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Setter for locale.
     * @param locale the new value for locale.
     */
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * Getter for xHTML.
     * @return xHTML.
     */
    public boolean getXHTML() {
        return xHTML;
    }

    /**
     * Setter for xHTML.
     * @param xhtml the new value for xHTML.
     */
    public void setXHTML(final boolean xhtml) {
        xHTML = xhtml;
    }
}
