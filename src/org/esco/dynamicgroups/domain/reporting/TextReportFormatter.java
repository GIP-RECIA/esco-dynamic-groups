/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Text report formatter.
 * @author GIP RECIA - A. Deman
 * 5 mai 2009
 *
 */
public class TextReportFormatter implements IReportFormatter {

    /** Serial version UID.*/
    private static final long serialVersionUID = -2682707529960017082L;
    
    /** The date format to use. */
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    /** The format for the entries.*/
//    private String entryFormat = "   %1$-38s  %2$s";
    private String entryFormat = "   %1$s%2$s";
    
    /** The list items. */
    private String listItem = "      -";
    
    /** Separation. */
    private String separation = "---"; 
    
    /**
     * Builds an instance of TextReportFormatter.
     */
    public TextReportFormatter() {
        super();
    }

    /**
     * Format an entry of the form label: value.
     * @param label The label of the entry.
     * @param value The value of the entry.
     * @return The formatted entry.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatEntry(String, String)
     */
    public String formatEntry(final String label, final String value) {
        return String.format(entryFormat, label, value);
    }

    /**
     * Format a title of level 1.
     * @param titleL1 The title.
     * @return The formatted title.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatTitleLevel1(String)
     */
    public String formatTitleLevel1(final String titleL1) {
       return titleL1;
    }

    /**
     * Format a title of level 2.
     * @param titleL2 The title.
     * @return The formatted title.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatTitleLevel2(String)
     */
    public String formatTitleLevel2(final String titleL2) {
        return titleL2;
    }

    /**
     * Format a title of level 3.
     * @param titleL3 The title.
     * @return The formatted title.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatTitleLevel3(String)
     */
    public String formatTitleLevel3(final String titleL3) {
        return titleL3;
    }
    /**
     * Highlight a text.
     * @param text The text to highlight.
     * @return The formatted text.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#highlight(java.lang.String)
     */
    public String highlight(final String text) {
        return text;
    }
    
    /**
     * Format a date.
     * @param date The date to format.
     * @return The formatted date.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#format(java.util.Date)
     */
    public String format(final Date date) {
        return dateFormat.format(date);
    }
    
    /**
     * Format a text.
     * @param text The test to format.
     * @return The formatted text.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#format(java.lang.String)
     */
    public String format(final String text) {
        return text;
    }

    /**
     * Gives the footer of the report.
     * @return The footer of the report.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getFooter()
     */
    public String getFooter() {
        return "";
    }

    /**
     * Gives the hearder of the report.
     * @return The header of the report.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getHearder()
     */
    public String getHearder() {
        return "";
    }

    /**
     * Gives the string for a new line.
     * @return the string for a new line.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getNewLine()
     */
    public String getNewLine() {
      return "\n";
    }

    /**
     * Gives a separation, for instance between two sections.
     * @return The separation.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getSeparation()
     */
    public String getSeparation() {
       return separation;
    }

    /**
     * Getter for entryFormat.
     * @return entryFormat.
     */
    public String getEntryFormat() {
        return entryFormat;
    }

    /**
     * Setter for entryFormat.
     * @param entryFormat the new value for entryFormat.
     */
    public void setEntryFormat(final String entryFormat) {
        this.entryFormat = entryFormat;
    }

    /**
     * Setter for separation.
     * @param separation the new value for separation.
     */
    public void setSeparation(final String separation) {
        this.separation = separation;
    }

    /**
     * Getter for dateFormat.
     * @return dateFormat.
     */
    public DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * Setter for dateFormat.
     * @param dateFormat the new value for dateFormat.
     */
    public void setDateFormat(final DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
    
    /**
     * Setter for dateFormat.
     * @param dateFormat the new value for dateFormat.
     */
    public void setDateFormat(final String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
    }

    /**
     * Format a list.
     * @param list The list to format.
     * @return The formatted list.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatList(java.lang.Iterable)
     */
    public String formatList(final Iterable<String> list) {
        String formattedList = getNewLine();
        for (String listEntry : list ) {
            formattedList += getListItem() + listEntry + getNewLine();
        }
        return formattedList;
    }
    /**
     * Adds spaces before a text.
     * @param text The text to padd.
     * @param paddSize The number of spaces to add.
     * @return The formatted text.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#padd(java.lang.String, int)
     */
    public String padd(final String text, final int paddSize) {
        String padd = "";
        for (int i = 0; i < paddSize; i++) {
            padd += " ";
        }
        return padd + text;
    }

    /**
     * Getter for listItem.
     * @return listItem.
     */
    public String getListItem() {
        return listItem;
    }

    /**
     * Setter for listItem.
     * @param listItem the new value for listItem.
     */
    public void setListItem(final String listItem) {
        this.listItem = listItem;
    }

}
