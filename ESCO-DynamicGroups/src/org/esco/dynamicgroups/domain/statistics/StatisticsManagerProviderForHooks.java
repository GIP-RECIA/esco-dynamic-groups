/**
 * 
 */
package org.esco.dynamicgroups.domain.statistics;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Singleton used to provide the Statistics manager for the hooks.
 * This is usefull as the hooks cannot be managed via spring.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 */
public class StatisticsManagerProviderForHooks implements InitializingBean {

    /**
     * The available instance of statistics manger.
     */
    private static IStatisticsManager statisticsManager;
    
    /**
     * Builds an instance of StatisticsManagerProviderForHooks.
     */
    public StatisticsManagerProviderForHooks() {
        super();
    }

    /**
     * Getter for statisticsManager.
     * @return statisticsManager.
     */
    public static IStatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    /**
     * Setter for statisticsManager.
     * @param statisticsManager the new value for statisticsManager.
     */
    public static void setStatisticsManager(final IStatisticsManager statisticsManager) {
        StatisticsManagerProviderForHooks.statisticsManager = statisticsManager;
    }

    /**
     * Checks the bean injection. 
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(statisticsManager, 
                "The property statisticsManager in the class " + this.getClass().getName() 
                + " can't be null.");
    }
    
    
}
