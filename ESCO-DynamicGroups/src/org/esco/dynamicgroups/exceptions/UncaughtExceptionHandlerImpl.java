package org.esco.dynamicgroups.exceptions;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.IRepositoryListener;
import org.esco.dynamicgroups.util.IMailer;

/**
 * Exception handler for the Repository listener.
 * @author GIP RECIA - A. Deman
 * 9 févr. 2009
 *
 */
public class UncaughtExceptionHandlerImpl implements Thread.UncaughtExceptionHandler, Serializable {

    /** Serial veriosn uid.*/
    private static final long serialVersionUID = 1776039245072654844L;

    /** The logger to use. */
    private static final Logger LOGGER = Logger.getLogger(UncaughtExceptionHandlerImpl.class);

    /** The listener associated to this listener. */
    private IRepositoryListener listener;
    
    /** The mailer to use to send the notifications of exceptions. */
    private IMailer mailer;

    /**
     * Builds an instance of ExceptionHandler.
     * @param listener The listener associated to this handler.
     * @param mailer The mailer used to notify the exceptions.
     */
    public UncaughtExceptionHandlerImpl(final IRepositoryListener listener, final IMailer mailer) {
        this.listener = listener;
        this.mailer = mailer;
    }

    /**
     * Handles an uncaught exception.
     * @param thread The thread where the exception has to be caught.
     * @param exception The exception to handle.
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    public void uncaughtException(final Thread thread, final Throwable exception) {
        LOGGER.fatal("!!! Uncaught exception in the thread. The handler is trying to stop the listener." 
                + " See below for the traces. The listener will be stoped !!!");
        LOGGER.fatal(exception, exception);
        mailer.sendExeceptionNotification(exception);
        listener.stop();
    }
}
