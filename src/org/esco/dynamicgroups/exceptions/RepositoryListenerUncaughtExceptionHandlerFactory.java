/**
 * 
 */
package org.esco.dynamicgroups.exceptions;

import java.lang.Thread.UncaughtExceptionHandler;
import org.esco.dynamicgroups.domain.IRepositoryListener;
import org.springframework.util.Assert;

/**
 * Implementation of UncaughtExcptionHanlderFactory used to create the handlers associated to the
 * repository listeners threads.
 * @author GIP RECIA - A. Deman
 * 18 mai 2009
 *
 */
public class RepositoryListenerUncaughtExceptionHandlerFactory 
   extends UncaughtExceptionHandlerFactory {

    /** Serial version UID.*/
    private static final long serialVersionUID = -2295603882937677315L;
    
    /** The listener associated to this listener. */
    private IRepositoryListener listener;
    
    
    /**
     * Builds an instance of RepositoryListenerUncaughtExceptionHandlerFactory.
     */
    public RepositoryListenerUncaughtExceptionHandlerFactory() {
        super();
    }
    
    
    /**
     * Checks the properties after the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        
        Assert.notNull(this.listener, 
                "The property listener in the class " + this.getClass().getName() 
                + " can't be null.");
      
    }
    
    /**
     * Creates an instance of handler.
     * @return The handler.
     * @see org.esco.dynamicgroups.exceptions.IUncaughtExceptionHandlerFactory#createExceptionHandler()
     */
    @Override
    public UncaughtExceptionHandler createExceptionHandler() {
        return new RepositoryListenerUncaughtExceptionHandler(getMailer(), listener);
    }

    /**
     * Getter for listener.
     * @return listener.
     */
    public IRepositoryListener getListener() {
        return listener;
    }

    /**
     * Setter for listener.
     * @param listener the new value for listener.
     */
    public void setListener(final IRepositoryListener listener) {
        this.listener = listener;
    }
}
