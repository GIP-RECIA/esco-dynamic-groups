/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * XHTML formater.
 * @author GIP RECIA - A. Deman
 * 5 mai 2009
 *
 */
public class XHTMLReportFormatter implements IReportFormatter {

    /** Serial version UID. */
    private static final long serialVersionUID = 9189132848934989683L;
    
    /** The date format to use. */
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    /** The opening tag used to format the labels. */
    private String labelOpeningTag = "<b>";

    /** The closing tag used to format the labels. */
    private String labelClosingTag = "</b>";
    
    /** The opening tag used to format the values. */
    private String valueOpeningTag = "";
    
    /** The closing tag used to format the values. */
    private String valueClosingTag = "";
    
    /** Opening tag for the title of level 1.*/
    private String titleL1OpeningTag = "<h1>";

    /** Closing tag for the title of level 1.*/
    private String titleL1ClosingTag = "</h1>";
    
    /** Opening tag for the title of level 2.*/
    private String titleL2OpeningTag = "<h2>";

    /** Closing tag for the title of level 2.*/
    private String titleL2ClosingTag = "</h2>";
    
    /** Opening tag for the title of level 3.*/
    private String titleL3OpeningTag = "<h3>";

    /** Closing tag for the title of level 3.*/
    private String titleL3ClosingTag = "</h3>";
    
    /** The separation tag. */
    private String separationTag = "<hr/>";
    
    /** The opening tag used to highligh text. */
    private String highlightOpeningTag = "<b>";

    /** The closing tag used to highligh text. */
    private String highlightClosingTag = "</b>";
    
    /** The opening tag for the lists. */
    private String listOpeningTag = "<ul>";

    /** The closing tag for the lists. */
    private String listClosingTag = "</ul>";
    
    /** Header. */
    private String header = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
        + "\n \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title/></head><body>";
    
    /** Footer. */
    private String footer = "</body></html>";
    
    /** New line. */
    private String newLine = "<br/>";
    
    
    /**
     * Builds an instance of XHTMLReportFormatter.
     */
    public XHTMLReportFormatter() {
        super();
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
     * Format an entry of the form label: value.
     * @param label The label of the entry.
     * @param value The value of the entry.
     * @return The formatted entry.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatEntry(String, String)
     */
    public String formatEntry(final String label, final String value) {
        return labelOpeningTag + label + labelClosingTag  + valueOpeningTag + value + valueClosingTag;
    }

   

    /**
     * Format a title of level 1.
     * @param titleL1 The title.
     * @return The formatted title.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatTitleLevel1(String)
     */
    public String formatTitleLevel1(final String titleL1) {
        return titleL1OpeningTag + titleL1 + titleL1ClosingTag;
    }

    /**
     * Format a title of level 2.
     * @param titleL2 The title.
     * @return The formatted title.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatTitleLevel2(String)
     */
    public String formatTitleLevel2(final String titleL2) {
        return titleL2OpeningTag + titleL2 + titleL2ClosingTag;
    }

    /**
     * Format a title of level 3.
     * @param titleL3 The title.
     * @return The formatted title.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatTitleLevel3(String)
     */
    public String formatTitleLevel3(final String titleL3) {
        return titleL3OpeningTag + titleL3 + titleL3ClosingTag;
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
        return highlightOpeningTag + text + highlightClosingTag;
    }

    /**
     * Gives a separation, for instance between two sections.
     * @return The separation.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getSeparation()
     */
    public String getSeparation() {
        return separationTag;
    }

    /**
     * Gives the footer of the report.
     * @return The footer of the report.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getFooter()
     */
    public String getFooter() {
        return footer;
    }

    /**
     * Gives the hearder of the report.
     * @return The header of the report.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getHearder()
     */
    public String getHearder() {
        return header;
    }

    /**
     * Gives the string for a new line.
     * @return the string for a new line.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getNewLine()
     */
    public String getNewLine() {
        return newLine;
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
            padd += "&nbsp;";
        }
        return padd + text;
    }

    /**
     * Format a list.
     * @param list The list to format.
     * @return The formatted list.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatList(java.lang.Iterable)
     */
    public String formatList(final Iterable<String> list) {
        String formattedList = listOpeningTag;
        for (String listEntry : list ) {
            formattedList += "<li>" + listEntry + "</li>";
        }
        formattedList += listClosingTag;
        return formattedList;
    }
    /**
     * Getter for labelOpeningTag.
     * @return labelOpeningTag.
     */
    public String getLabelOpeningTag() {
        return labelOpeningTag;
    }

    /**
     * Setter for labelOpeningTag.
     * @param labelOpeningTag the new value for labelOpeningTag.
     */
    public void setLabelOpeningTag(final String labelOpeningTag) {
        this.labelOpeningTag = labelOpeningTag;
    }

    /**
     * Getter for labelClosingTag.
     * @return labelClosingTag.
     */
    public String getLabelClosingTag() {
        return labelClosingTag;
    }

    /**
     * Setter for labelClosingTag.
     * @param labelClosingTag the new value for labelClosingTag.
     */
    public void setLabelClosingTag(final String labelClosingTag) {
        this.labelClosingTag = labelClosingTag;
    }

    /**
     * Getter for valueOpeningTag.
     * @return valueOpeningTag.
     */
    public String getValueOpeningTag() {
        return valueOpeningTag;
    }

    /**
     * Setter for valueOpeningTag.
     * @param valueOpeningTag the new value for valueOpeningTag.
     */
    public void setValueOpeningTag(final String valueOpeningTag) {
        this.valueOpeningTag = valueOpeningTag;
    }

    /**
     * Getter for valueClosingTag.
     * @return valueClosingTag.
     */
    public String getValueClosingTag() {
        return valueClosingTag;
    }

    /**
     * Setter for valueClosingTag.
     * @param valueClosingTag the new value for valueClosingTag.
     */
    public void setValueClosingTag(final String valueClosingTag) {
        this.valueClosingTag = valueClosingTag;
    }

    /**
     * Getter for titleL1OpeningTag.
     * @return titleL1OpeningTag.
     */
    public String getTitleL1OpeningTag() {
        return titleL1OpeningTag;
    }

    /**
     * Setter for titleL1OpeningTag.
     * @param titleL1OpeningTag the new value for titleL1OpeningTag.
     */
    public void setTitleL1OpeningTag(final String titleL1OpeningTag) {
        this.titleL1OpeningTag = titleL1OpeningTag;
    }

    /**
     * Getter for titleL1ClosingTag.
     * @return titleL1ClosingTag.
     */
    public String getTitleL1ClosingTag() {
        return titleL1ClosingTag;
    }

    /**
     * Setter for titleL1ClosingTag.
     * @param titleL1ClosingTag the new value for titleL1ClosingTag.
     */
    public void setTitleL1ClosingTag(final String titleL1ClosingTag) {
        this.titleL1ClosingTag = titleL1ClosingTag;
    }

    /**
     * Getter for titleL2OpeningTag.
     * @return titleL2OpeningTag.
     */
    public String getTitleL2OpeningTag() {
        return titleL2OpeningTag;
    }

    /**
     * Setter for titleL2OpeningTag.
     * @param titleL2OpeningTag the new value for titleL2OpeningTag.
     */
    public void setTitleL2OpeningTag(final String titleL2OpeningTag) {
        this.titleL2OpeningTag = titleL2OpeningTag;
    }

    /**
     * Getter for titleL2ClosingTag.
     * @return titleL2ClosingTag.
     */
    public String getTitleL2ClosingTag() {
        return titleL2ClosingTag;
    }

    /**
     * Setter for titleL2ClosingTag.
     * @param titleL2ClosingTag the new value for titleL2ClosingTag.
     */
    public void setTitleL2ClosingTag(final String titleL2ClosingTag) {
        this.titleL2ClosingTag = titleL2ClosingTag;
    }

    /**
     * Getter for titleL3OpeningTag.
     * @return titleL3OpeningTag.
     */
    public String getTitleL3OpeningTag() {
        return titleL3OpeningTag;
    }

    /**
     * Setter for titleL3OpeningTag.
     * @param titleL3OpeningTag the new value for titleL3OpeningTag.
     */
    public void setTitleL3OpeningTag(final String titleL3OpeningTag) {
        this.titleL3OpeningTag = titleL3OpeningTag;
    }

    /**
     * Getter for titleL3ClosingTag.
     * @return titleL3ClosingTag.
     */
    public String getTitleL3ClosingTag() {
        return titleL3ClosingTag;
    }

    /**
     * Setter for titleL3ClosingTag.
     * @param titleL3ClosingTag the new value for titleL3ClosingTag.
     */
    public void setTitleL3ClosingTag(final String titleL3ClosingTag) {
        this.titleL3ClosingTag = titleL3ClosingTag;
    }

    /**
     * Getter for separationTag.
     * @return separationTag.
     */
    public String getSeparationTag() {
        return separationTag;
    }

    /**
     * Setter for separationTag.
     * @param separationTag the new value for separationTag.
     */
    public void setSeparationTag(final String separationTag) {
        this.separationTag = separationTag;
    }

    /**
     * Getter for header.
     * @return header.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Setter for header.
     * @param header the new value for header.
     */
    public void setHeader(final String header) {
        this.header = header;
    }

    /**
     * Setter for footer.
     * @param footer the new value for footer.
     */
    public void setFooter(final String footer) {
        this.footer = footer;
    }

    /**
     * Setter for newLine.
     * @param newLine the new value for newLine.
     */
    public void setNewLine(final String newLine) {
        this.newLine = newLine;
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
     * Getter for highlightOpeningTag.
     * @return highlightOpeningTag.
     */
    public String getHighlightOpeningTag() {
        return highlightOpeningTag;
    }

    /**
     * Setter for highlightOpeningTag.
     * @param highlightOpeningTag the new value for highlightOpeningTag.
     */
    public void setHighlightOpeningTag(final String highlightOpeningTag) {
        this.highlightOpeningTag = highlightOpeningTag;
    }

    /**
     * Getter for listOpeningTag.
     * @return listOpeningTag.
     */
    public String getListOpeningTag() {
        return listOpeningTag;
    }

    /**
     * Setter for listOpeningTag.
     * @param listOpeningTag the new value for listOpeningTag.
     */
    public void setListOpeningTag(final String listOpeningTag) {
        this.listOpeningTag = listOpeningTag;
    }

    /**
     * Getter for listClosingTag.
     * @return listClosingTag.
     */
    public String getListClosingTag() {
        return listClosingTag;
    }

    /**
     * Setter for listClosingTag.
     * @param listClosingTag the new value for listClosingTag.
     */
    public void setListClosingTag(final String listClosingTag) {
        this.listClosingTag = listClosingTag;
    }
    


}
