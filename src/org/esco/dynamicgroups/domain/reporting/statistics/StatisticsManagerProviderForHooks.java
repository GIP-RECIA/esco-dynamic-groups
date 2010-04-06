/**
 * 
 */
package org.esco.dynamicgroups.domain.reporting.statistics;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Singleton used to provide the Statistics manager for the hooks.
 * This is usefull as the hooks cannot be managed via spring.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 */
public class StatisticsManagerProviderForHooks implements InitializingBean {

    /** Singleton. */
    private static StatisticsManagerProviderForHooks instance;
    
    /**
     * The available instance of statistics manger.
     */
    private IStatisticsManager statisticsManager;
    
    /**
     * Builds an instance of StatisticsManagerProviderForHooks.
     */
    public StatisticsManagerProviderForHooks() {
        super();
        instance = this;
    }

    /**
     * Gives the available instance.
     * @return The singleton.
     */
    public static StatisticsManagerProviderForHooks instance() {
        return instance;
    }
    
    /**
     * Getter for statisticsManager.
     * @return statisticsManager.
     */
    public IStatisticsManager getStatisticsManager() {
        return this.statisticsManager;
    }

    /**
     * Setter for statisticsManager.
     * @param statisticsManager the new value for statisticsManager.
     */
    public void setStatisticsManager(final IStatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
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
