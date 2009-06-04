/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;


import org.apache.log4j.Logger;
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

    /** Serial version UID.*/
    private static final long serialVersionUID = -6061720047368543133L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ReportingJob.class);

    /** The reporting manager. */
    private IReportingManager reportingManager; 

    /**
     * Builds an instance of ReportingJob.
     */
    public ReportingJob() {
        super();
    }
 
    /**
     * Executes the Job.
     * @param ctx The job execution context.
     * @throws JobExecutionException
     * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
     */
    @Override
    protected void executeInternal(final JobExecutionContext ctx)
    throws JobExecutionException {
        boolean validState = true;
        if (reportingManager == null) {
            LOGGER.error("Invalid state for the instance of " 
                    + getClass().getName() 
                    + " the property reportingManager is null.");
            validState =  false;
        }
        
        if (validState) {
           reportingManager.doReporting();
        } else {
            LOGGER.warn("Reporting disabled : invalid state.");
        }
    }

    /**
     * Getter for reportingManager.
     * @return reportingManager.
     */
    public IReportingManager getReportingManager() {
        return reportingManager;
    }

    /**
     * Setter for reportingManager.
     * @param reportingManager the new value for reportingManager.
     */
    public void setReportingManager(final IReportingManager reportingManager) {
        this.reportingManager = reportingManager;
    }


}
