package org.esco.dynamicgroups.domain;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.util.IMailer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Exception handler.
 * @author GIP RECIA - A. Deman
 * 9 févr. 2009
 *
 */
public class UncaughtExceptionHandlerImpl implements Thread.UncaughtExceptionHandler, InitializingBean, Serializable {

    /** Serial veriosn uid.*/
    private static final long serialVersionUID = 1776039245072654844L;

    /** The logger to use. */
    private static final Logger LOGGER = Logger.getLogger(UncaughtExceptionHandlerImpl.class);

    /** The listener. */
    private IRepositoryListener listener;
    
    /** The mailer. */
    private IMailer mailer;

    /**
     * Builds an instance of ExceptionHandler.
     */
    public UncaughtExceptionHandlerImpl() {
        super();
    }

    /**
     * Handles an uncaught exception.
     * @param thread The thread where the exception has to be caught.
     * @param exception The exception to handle.
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    public void uncaughtException(final Thread thread, final Throwable exception) {
        LOGGER.fatal("Uncaught exception in the thread. The handler is trying to stop the listener." 
                + " See below for the traces.");
        LOGGER.fatal(exception, exception);
        mailer.sendExeceptionNotification(exception);
        listener.stop();
    }

    /**
     * Checks the bean injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.listener, "The property listener in the class " 
                + getClass().getName() + " cannot be null.");

        Assert.notNull(this.mailer, "The property mailer in the class " 
                + getClass().getName() + " cannot be null.");
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
