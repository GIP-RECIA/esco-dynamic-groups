/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;


import java.text.ParseException;

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
    
    /**
     * Builds an instance of ReportingTrigger.
     * @param parametersProvider The instance of parameters that contains the cron expression.
     * @throws ParseException If the cron expression is not valid.
     */
    public ReportingTrigger(final ParametersProvider parametersProvider) throws ParseException { 
        final ReportingParametersSection reportingParameters = 
            (ReportingParametersSection) parametersProvider.getReportingParametersSection();
       setCronExpression(reportingParameters.getReportCronExpression()); 
    }
    
  
}
