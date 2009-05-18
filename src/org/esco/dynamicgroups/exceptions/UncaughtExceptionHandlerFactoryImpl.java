/**
 * 
 */
package org.esco.dynamicgroups.exceptions;

import java.io.Serializable;
import java.lang.Thread.UncaughtExceptionHandler;

import org.esco.dynamicgroups.domain.IRepositoryListener;
import org.esco.dynamicgroups.util.IMailer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Reference implementation of UncaughtExcptionHanlderFactory.
 * @author GIP RECIA - A. Deman
 * 18 mai 2009
 *
 */
public class UncaughtExceptionHandlerFactoryImpl 
    implements IUncaughtExceptionHandlerFactory, InitializingBean, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 1478142432687769977L;


    /** The listener associated to this listener. */
    private IRepositoryListener listener;
    
    /** The mailer to use to send the notifications of exceptions. */
    private IMailer mailer;
    
    /**
     * Builds an instance of UncaughtExceptionHandlerFactoryImpl.
     */
    public UncaughtExceptionHandlerFactoryImpl() {
        super();
    }
    
    
    /**
     * Checks the properties after the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.listener, 
                "The property listener in the class " + this.getClass().getName() 
                + " can't be null.");
        
        Assert.notNull(this.mailer,
                "The property mailer in the class " + this.getClass().getName() 
                + " can't be null.");
    }
    
    /**
     * Creates an instance of handler.
     * @return The handler.
     * @see org.esco.dynamicgroups.exceptions.IUncaughtExceptionHandlerFactory#createExceptionHandler()
     */
    public UncaughtExceptionHandler createExceptionHandler() {
       return new UncaughtExceptionHandlerImpl(listener, mailer);
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

}
