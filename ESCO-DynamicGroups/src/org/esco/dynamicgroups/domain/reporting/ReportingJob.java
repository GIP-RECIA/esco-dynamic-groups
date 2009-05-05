/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.esco.dynamicgroups.util.IMailer;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Quarts Job used to generate and send the reports.
 * @author GIP RECIA - A. Deman
 * 4 mai 2009
 *
 */
public class ReportingJob extends QuartzJobBean {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ReportingJob.class);

    /** The key for the report subject. */
    private static final String REPORT_SUBJ = "report.subject";

    /** Title for the report. */
    private static final String REPORT_TITLE = "report.title";

    /** Execution date label. */
    private static final String REPORT_EXEC_DATE = "report.date";
    
    /** The statistics manager used to generate the report. */
    private IStatisticsManager statisticsManager;

    /** The mailer to use. */
    private IMailer mailer;

    /** The I18n manager. */
    private I18NManager i18n;
    
    /** The report formatter to use. */
    private IReportFormatter reportFormatter;

    /**
     * Builds an instance of ReportingJob.
     */
    public ReportingJob() {
        super();
    }

 
    /**
     * @param ctx
     * @throws JobExecutionException
     * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
     */
    @Override
    protected void executeInternal(final JobExecutionContext ctx)
    throws JobExecutionException {
        boolean validState = true;
        if (statisticsManager == null) {
            LOGGER.error("Invalid state for the instance of " 
                    + getClass().getName() 
                    + " the property statisticsManager is null.");
            validState =  false;
            
        }
        if (mailer == null) {
            LOGGER.error("Invalid state the instance of " 
                    + getClass().getName() 
                    + " the property mailer is null.");
            validState =  false;
            
        }
        if (i18n == null) {
            LOGGER.error("Invalid state for the instance of " 
                    + getClass().getName() 
                    + " the property i18n is null.");
            validState =  false;
            
        }
        
        if (reportFormatter == null) {
            LOGGER.error("Invalid state for the instance of " 
                    + getClass().getName() 
                    + " the property reportFormatter is null.");
            validState =  false;
        }
        if (validState) {
            
            String mailContent = reportFormatter.getHearder();
            mailContent += reportFormatter.formatTitleLevel1(i18n.getI18nMessage(REPORT_TITLE));
            mailContent += reportFormatter.getNewLine();
            mailContent += reportFormatter.getNewLine();
            mailContent += reportFormatter.highlight(i18n.getI18nMessage(REPORT_EXEC_DATE));
            mailContent += reportFormatter.format(Calendar.getInstance().getTime());
            mailContent += reportFormatter.getNewLine();
            mailContent += reportFormatter.getSeparation();
            mailContent += reportFormatter.getNewLine();
            
            
            mailContent += statisticsManager.generateReport();
            

            
            mailContent += reportFormatter.getFooter();
            mailer.sendMail(i18n.getI18nMessage(REPORT_SUBJ), mailContent);
            statisticsManager.reset();
        } else {
            LOGGER.warn("Reporting disabled : invalid state.");
        }
    }

    /**
     * Getter for statisticsManager.
     * @return statistcsManager.
     */
    public IStatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    /**
     * Setter for statistcsMranager.
     * @param statisticsManager the new value for statistcsMranager.
     */
    public void setStatisticsManager(final IStatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
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
     * Getter for i18n.
     * @return i18n.
     */
    public I18NManager getI18n() {
        return i18n;
    }


    /**
     * Setter for i18n.
     * @param i18n the new value for i18n.
     */
    public void setI18n(final I18NManager i18n) {
        this.i18n = i18n;
    }


    /**
     * Getter for reportFormatter.
     * @return reportFormatter.
     */
    public IReportFormatter getReportFormatter() {
        return reportFormatter;
    }


    /**
     * Setter for reportFormatter.
     * @param reportFormatter the new value for reportFormatter.
     */
    public void setReportFormatter(final IReportFormatter reportFormatter) {
        this.reportFormatter = reportFormatter;
    }


}
