/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;

import java.util.Calendar;

import org.esco.dynamicgroups.domain.beans.I18NManager;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ReportingParametersSection;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.esco.dynamicgroups.util.IMailer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Reference implementation of Reporting manager.
 * @author GIP RECIA - A. Deman
 * 4 mai 2009
 *
 */
public class ReportingManager implements IReportingManager, InitializingBean {

    /** Serial version UID.*/
    private static final long serialVersionUID = -6061720047368543133L;

    /** The key for the report subject. */
    private static final String REPORT_SUBJ = "report.subject";

    /** Title for the report. */
    private static final String REPORT_TITLE = "report.title";

    /** The key for the report subject for the checking process 
     * of the dynamic groups members. */
    private static final String REPORT_SUBJ_MB_CHECK = "report.subject.members.check";

    /** Title for the report of the checking process of the dynamic groups
     * members. */
    private static final String REPORT_TITLE_MB_CHECK = "report.title.members.check";

    /** Execution date label. */
    private static final String REPORT_EXEC_DATE = "report.date";

    /** The statistics manager used to generate the report. */
    private IStatisticsManager statisticsManager;

    /** The mailer to use. */
    private IMailer mailer;

    /** The I18n manager. */
    private transient I18NManager i18n;

    /** The user paramters provider. */
    private ParametersProvider parametersProvider;

    /** The reporting parameters. */
    private ReportingParametersSection reportingParameters;

    /** The report formatter to use. */
    private IReportFormatter reportFormatter;

    /**
     * Builds an instance of ReportingJob.
     */
    public ReportingManager() {
        super();
    }


    /**
     * Checks the bean injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(this.statisticsManager, "The property statisticsManager in the class "
                + getClass() + " can't be null.");

        Assert.notNull(this.reportFormatter, "The property reportFormatter in the class "
                + getClass() + " can't be null.");

        Assert.notNull(this.i18n, "The property i18n in the class "
                + getClass() + " can't be null.");

        Assert.notNull(this.mailer, "The property mailer in the class "
                + getClass() + " can't be null.");

        Assert.notNull(this.parametersProvider, "The property parametersProvider in the class "
                + getClass() + " can't be null.");

        reportingParameters = (ReportingParametersSection) parametersProvider.getReportingParametersSection();

    }

    /**
     * Generates and send the report.
     * @see org.esco.dynamicgroups.domain.reporting.IReportingManager#doReporting()
     */
    public void doReporting() {
        if (reportingParameters.getEnabled()) {

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
        } 

    }

    /**
     * Generates the report for the check of the groups members.
     * @see org.esco.dynamicgroups.domain.reporting.IReportingManager#doReportingForGroupsMembersCheck()
     */
    public void doReportingForGroupsMembersCheck() {
        if (reportingParameters.getEnabled()) {
            String mailContent = reportFormatter.getHearder();
            mailContent += reportFormatter.formatTitleLevel1(i18n.getI18nMessage(REPORT_TITLE_MB_CHECK));
            mailContent += reportFormatter.getNewLine();
            mailContent += reportFormatter.getNewLine();
            mailContent += reportFormatter.highlight(i18n.getI18nMessage(REPORT_EXEC_DATE));
            mailContent += reportFormatter.format(Calendar.getInstance().getTime());
            mailContent += reportFormatter.getNewLine();
            mailContent += reportFormatter.getSeparation();
            mailContent += reportFormatter.getNewLine();

            mailContent += statisticsManager.generateGroupsMembersCheckReport();

            mailContent += reportFormatter.getFooter();
            mailer.sendMail(i18n.getI18nMessage(REPORT_SUBJ_MB_CHECK), mailContent);
            statisticsManager.reset();
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


    /**
     * Getter for parametersProvider.
     * @return parametersProvider.
     */
    public ParametersProvider getParametersProvider() {
        return parametersProvider;
    }


    /**
     * Setter for parametersProvider.
     * @param parametersProvider the new value for parametersProvider.
     */
    public void setParametersProvider(final ParametersProvider parametersProvider) {
        this.parametersProvider = parametersProvider;
    }
}
