/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;

import java.io.Serializable;
import java.util.Date;

/**
 * Interface for the Reports formatters.
 * @author GIP RECIA - A. Deman
 * 5 mai 2009
 *
 */
public interface IReportFormatter extends Serializable {
    
    /**
     * Gives the hearder of the report.
     * @return The header of the report.
     */
    String getHearder();
    
    /**
     * Gives the footer of the report.
     * @return The footer of the report.
     */
    String getFooter();
    
    /** 
     * Gives the string for a new line.
     * @return the string for a new line.
     */
    String getNewLine();
   
    /**
     * Format a title of level 1.
     * @param titleL1 The title.
     * @return The formatted title.
     */
    String formatTitleLevel1(final String titleL1);

    /**
     * Format a title of level 2.
     * @param titleL2 The title.
     * @return The formatted title.
     */
    String formatTitleLevel2(final String titleL2);
    
    /**
     * Format a title of level 3.
     * @param titleL3 The title.
     * @return The formatted title.
     */
    String formatTitleLevel3(final String titleL3);
    
    /**
     * Highlight a text.
     * @param text The text to highlight.
     * @return The formatted text.
     */
    String highlight(final String text);
    
    /**
     * Format a date.
     * @param date The date to format.
     * @return The formatted date.
     */
    String format(final Date date);
    
    /**
     * Format a text.
     * @param text The test to format.
     * @return The formatted text.
     */
    String format(final String text);
    
    /**
     * Gives a separation, for instance between two sections.
     * @return The separation.
     */
    String getSeparation();
    
    /**
     * Format an entry of the form label: value.
     * @param label The label of the entry.
     * @param value The value of the entry.
     * @return The formatted entry.
     */
    String formatEntry(final String label, final String value);

}
