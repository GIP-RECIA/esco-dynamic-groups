/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;


import java.text.ParseException;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ReportingParametersSection;
import org.springframework.scheduling.quartz.CronTriggerBean;

/**
 * Cron trigger used to generate the reports.
 * @author GIP RECIA - A. Deman
 * 4 mai 2009
 *
 */
public class ReportingTrigger extends CronTriggerBean {

    /** Serial versino UID.*/
    private static final long serialVersionUID = 4733642274228556934L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ReportingTrigger.class);
    
    /** Defualt cron expression. */
    private static final String DEF_CRON_EXPR = "* * * * * ?";
    
    /** Active flag. */
    private boolean active;
    
    /**
     * Builds an instance of ReportingTrigger.
     * @param parametersProvider The instance of parameters that contains the cron expression.
     * @throws ParseException If the cron expression is not valid.
     */
    public ReportingTrigger(final ParametersProvider parametersProvider) throws ParseException { 
        LOGGER.debug("Creating a ReportingTrigger.");
        
        final ReportingParametersSection reportingParameters = 
            (ReportingParametersSection) parametersProvider.getReportingParametersSection();
        active = reportingParameters.getEnabled();
        if (active) {
          setCronExpression(reportingParameters.getReportCronExpression());
        } else {
            setCronExpression(DEF_CRON_EXPR);
        }
        
    }
    
    /**
     * Used to disable the trigger if the reporting is disabled.
     * @return True if the reporting is enabled false otherwise.
     * @see org.quartz.CronTrigger#mayFireAgain()
     */
    @Override
    public boolean mayFireAgain() {
        return active;
    }
    
    
}
