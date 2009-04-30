package org.esco.dynamicgroups.util;

import java.io.Serializable;
import java.util.List;

import org.esco.dynamicgroups.domain.statistics.IStatisticsManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Generates and sends a report with some statistics.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public class ReportManager extends QuartzJobBean implements Serializable {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = -2279980526186417645L;

    /** The mailer to use. */
    private IMailer mailer;
    
    /** The statistics manager. */
    private IStatisticsManager statisticsManager;
    
    /** The mail subject when the report is sent. */
    private String subject;

    /**
     * Builds an instance of ReportManager.
     */
    public ReportManager() {
        super();
    }
    
    /**
     * Retrieve the statistics and send the report.
     * @param arg0
     * @throws JobExecutionException
     * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
     */
    @Override
    protected void executeInternal(final JobExecutionContext arg0)
            throws JobExecutionException {
        List<String> report = statisticsManager.generateReport();
        statisticsManager.reset();
        String content = "";
        for (String reportEntry : report) {
            content += reportEntry;
        }
        mailer.sendMail(subject, content);
    }

    /**
     * Getter for mailer.
     * @return mailer.
     */
    public IMailer getMailer() {
        return mailer;
    }

    /**
     * Setter for mailer.
     * @param mailer the new value for mailer.
     */
    public void setMailer(final IMailer mailer) {
        this.mailer = mailer;
    }

    /**
     * Getter for statisticsManager.
     * @return statisticsManager.
     */
    public IStatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    /**
     * Setter for statisticsManager.
     * @param statisticsManager the new value for statisticsManager.
     */
    public void setStatisticsManager(final IStatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

}
