/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting;


import java.text.ParseException;

import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
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
     * @param parameters The instance of parameters that contains the cron expression.
     * @throws ParseException If the cron expression is not valid.
     */
    public ReportingTrigger(final ESCODynamicGroupsParameters parameters) throws ParseException { 
       setCronExpression(parameters.getReportCronExpression()); 
    }
    
  
}
