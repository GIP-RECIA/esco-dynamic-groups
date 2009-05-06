/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;

import java.util.Date;

import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Report formatter that cvan format in text or xhtml mode.
 * @author GIP RECIA - A. Deman
 * 5 mai 2009
 *
 */
public class ReportFormatter implements IReportFormatter, InitializingBean {

    /** Serial version UID.*/
    private static final long serialVersionUID = -2682707529960017082L;
    
    /** The XHTML formatter. */ 
    private IReportFormatter xhtmlFormatter;
    
    /** Text formatter. */
    private IReportFormatter textFormatter;
    
    /** The user paramters. */
    private ESCODynamicGroupsParameters parameters;
    
    /**
     * Builds an instance of TextReportFormatter.
     */
    public ReportFormatter() {
        super();
    }
    
    /**
     * Selects the formatter to use.
     * @return The formatter corresponding the the user paramters.
     */
    protected IReportFormatter selectFormatter() {
        if (parameters.isXHTMLReport()) {
            return xhtmlFormatter;
        }
        return textFormatter;
    }

    /**
     * Format an entry of the form label: value.
     * @param label The label of the entry.
     * @param value The value of the entry.
     * @return The formatted entry.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatEntry(String, String)
     */
    public String formatEntry(final String label, final String value) {
        return selectFormatter().formatEntry(label, value);
    }

    /**
     * Format a title of level 1.
     * @param titleL1 The title.
     * @return The formatted title.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatTitleLevel1(String)
     */
    public String formatTitleLevel1(final String titleL1) {
        return selectFormatter().formatTitleLevel1(titleL1);
    }

    /**
     * Format a title of level 2.
     * @param titleL2 The title.
     * @return The formatted title.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatTitleLevel2(String)
     */
    public String formatTitleLevel2(final String titleL2) {
        return selectFormatter().formatTitleLevel2(titleL2);
    }

    /**
     * Format a title of level 3.
     * @param titleL3 The title.
     * @return The formatted title.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatTitleLevel3(String)
     */
    public String formatTitleLevel3(final String titleL3) {
        return selectFormatter().formatTitleLevel3(titleL3);
    }
    
    /**
     * Format a date.
     * @param date The date to format.
     * @return The formatted date.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#format(java.util.Date)
     */
    public String format(final Date date) {
        return selectFormatter().format(date);
    }

    /**
     * Format a text.
     * @param text The test to format.
     * @return The formatted text.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#format(java.lang.String)
     */
    public String format(final String text) {
        return selectFormatter().format(text);
    }
    
    /**
     * Highlight a text.
     * @param text The text to highlight.
     * @return The formatted text.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#highlight(java.lang.String)
     */
    public String highlight(final String text) {
        return selectFormatter().highlight(text);
    }
    /**
     * Gives the footer of the report.
     * @return The footer of the report.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getFooter()
     */
    public String getFooter() {
        return selectFormatter().getFooter();
    }

    /**
     * Gives the hearder of the report.
     * @return The header of the report.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getHearder()
     */
    public String getHearder() {
        return selectFormatter().getHearder();
    }

    /**
     * Gives the string for a new line.
     * @return the string for a new line.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getNewLine()
     */
    public String getNewLine() {
        return selectFormatter().getNewLine();
    }

    /**
     * Gives a separation, for instance between two sections.
     * @return The separation.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#getSeparation()
     */
    public String getSeparation() {
        return selectFormatter().getSeparation();
    }

    /**
     * Adds spaces before a text.
     * @param text The text to padd.
     * @param paddSize The number of spaces to add.
     * @return The formatted text.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#padd(java.lang.String, int)
     */
    public String padd(final String text, final int paddSize) {
       return selectFormatter().padd(text, paddSize);
    }
    
    /**
     * Format a list.
     * @param list The list to format.
     * @return The formatted list.
     * @see org.esco.dynamicgroups.domain.reporting.IReportFormatter#formatList(java.lang.Iterable)
     */
    public String formatList(final Iterable<String> list) {
        return selectFormatter().formatList(list);
    }
    
    /**
     * Getter for textFormatter.
     * @return textFormatter.
     */
    public IReportFormatter getTextFormatter() {
        return textFormatter;
    }

    /**
     * Setter for textFormatter.
     * @param textFormatter the new value for textFormatter.
     */
    public void setTextFormatter(final IReportFormatter textFormatter) {
        this.textFormatter = textFormatter;
    }

    /**
     * Getter for parameters.
     * @return parameters.
     */
    public ESCODynamicGroupsParameters getParameters() {
        return parameters;
    }

    /**
     * Setter for parameters.
     * @param parameters the new value for parameters.
     */
    public void setParameters(final ESCODynamicGroupsParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Checks the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(parameters, 
                "The property parameters int the class " 
                + getClass().getSimpleName() + " can't be null.");
        Assert.notNull(xhtmlFormatter, 
                "The property xhtmlFormatter int the class " 
                + getClass().getSimpleName() + " can't be null.");
        Assert.notNull(textFormatter, 
                "The property textFormatter int the class " 
                + getClass().getSimpleName() + " can't be null.");
        
    }

    /**
     * Getter for xhtmlFormatter.
     * @return xhtmlFormatter.
     */
    public IReportFormatter getXhtmlFormatter() {
        return xhtmlFormatter;
    }

    /**
     * Setter for xhtmlFormatter.
     * @param xhtmlFormatter the new value for xhtmlFormatter.
     */
    public void setXhtmlFormatter(final IReportFormatter xhtmlFormatter) {
        this.xhtmlFormatter = xhtmlFormatter;
    }
    
    

}
