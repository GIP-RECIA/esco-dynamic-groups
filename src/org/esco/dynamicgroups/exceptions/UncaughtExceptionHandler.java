package org.esco.dynamicgroups.exceptions;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.util.IMailer;

/**
 * Base exception handler for the threads.
 * @author GIP RECIA - A. Deman
 * 1 sept. 2009
 *
 */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler, Serializable {

    /** Serial veriosn uid.*/
    private static final long serialVersionUID = 1776039245072654844L;

    /** The logger to use. */
    private Logger logger = Logger.getLogger(getClass());

    /** The mailer to use to send the notifications of exceptions. */
    private IMailer mailer;

    /**
     * Builds an instance of ExceptionHandler.
     * @param mailer The mailer used to notify the exceptions.
     */
    public UncaughtExceptionHandler(final IMailer mailer) {
        this.mailer = mailer;
    }

    /**
     * Handles an uncaught exception.
     * @param thread The thread where the exception has to be caught.
     * @param exception The exception to handle.
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    public void uncaughtException(final Thread thread, final Throwable exception) {
        logger.fatal("!!! Uncaught exception in the thread. See below for the traces.");
        logger.fatal(exception, exception);
        mailer.sendExeceptionNotification(exception);
    }

    /**
     * Getter for mailer.
     * @return mailer.
     */
    public IMailer getMailer() {
        return mailer;
    }

    /**
     * Getter for logger.
     * @return logger.
     */
    public Logger getLogger() {
        return logger;
    }
}
