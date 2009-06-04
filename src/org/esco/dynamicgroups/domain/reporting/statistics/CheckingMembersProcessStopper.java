/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import java.util.Timer;
import java.util.TimerTask;

import org.esco.dynamicgroups.domain.IDomainService;

/**
 * Timer used to stop the checking process ot he dynamic groups members.
 * @author GIP RECIA - A. Deman
 * 4 juin 2009
 *
 */
public class CheckingMembersProcessStopper extends Timer {
    /**
     * Timer task used to stop the members checking process after a given 
     * number of minutes.
     * @author GIP RECIA - A. Deman
     * 4 juin 2009
     *
     */
    class CheckingMembersProcessStopperTask extends TimerTask {

        /** The domain service to use. */
        private IDomainService domainService;

        /**
         * 
         * Builds an instance of CheckingMembersProcessStopper.
         * @param domainService the domain service used to stop the
         * members checking process.
         */
        public CheckingMembersProcessStopperTask(final IDomainService domainService) {
            this.domainService = domainService;
        }

        /**
         * Stops the checking process of the dynamic groups members.
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run() {
            domainService.stopMembersCheckingProcess();
        }
    } 
    
    /** To convert the minutes into milliseconds. */
    private static final long MINUTES_TO_MILLIS_FACTOR = 60000;
    
    /**
     * Builds an instance of CheckingMembersProcessStopper.
     * @param domainService The domain service used to stop the process.
     * @param delayMinutes The number of minutes before the process is to be stopped.
     */
    public CheckingMembersProcessStopper(final IDomainService domainService, final int delayMinutes) {
        schedule(new CheckingMembersProcessStopperTask(domainService), delayMinutes * MINUTES_TO_MILLIS_FACTOR);
    }
}