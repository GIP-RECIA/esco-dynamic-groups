/**
 * 
 */
package org.esco.dynamicgroups.exceptions;

import java.io.Serializable;
import java.lang.Thread.UncaughtExceptionHandler;

import org.esco.dynamicgroups.util.IMailer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Implementation of UncaughtExcptionHanlderFactory used to create the base exception handlers for the threads.
 * @author GIP RECIA - A. Deman
 * 18 mai 2009
 *
 */
public class UncaughtExceptionHandlerFactory 
    implements IUncaughtExceptionHandlerFactory, InitializingBean, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 9125801065236845241L;
      
    /** The mailer to use to send the notifications of exceptions. */
    private IMailer mailer;
    
    /**
     * Builds an instance of UncaughtExceptionHandlerFactory.
     */
    public UncaughtExceptionHandlerFactory() {
        super();
    }
    
    
    /**
     * Checks the properties after the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
       
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
        return new org.esco.dynamicgroups.exceptions.UncaughtExceptionHandler(mailer);
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
