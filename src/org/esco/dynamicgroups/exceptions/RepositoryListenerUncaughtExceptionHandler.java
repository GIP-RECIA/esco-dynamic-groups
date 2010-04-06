package org.esco.dynamicgroups.exceptions;


import org.esco.dynamicgroups.domain.IRepositoryListener;
import org.esco.dynamicgroups.util.IMailer;

/**
 * Exception handler for the Repository listener.
 * @author GIP RECIA - A. Deman
 * 9 févr. 2009
 *
 */
public class RepositoryListenerUncaughtExceptionHandler extends UncaughtExceptionHandler {

    /** Serial veriosn uid.*/
    private static final long serialVersionUID = 1776039245072654844L;

    /** The listener associated to this listener. */
    private IRepositoryListener listener;
    

    /**
     * Builds an instance of ExceptionHandler.
     * @param listener The listener associated to this handler.
     * @param mailer The mailer used to notify the exceptions.
     */
    public RepositoryListenerUncaughtExceptionHandler(final IMailer mailer, final IRepositoryListener listener) {
        super(mailer);
        this.listener = listener;
    }

    /**
     * Handles an uncaught exception.
     * @param thread The thread where the exception has to be caught.
     * @param exception The exception to handle.
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable exception) {
        super.uncaughtException(thread, exception);
        getLogger().fatal("Trying to stop the listener.");
        listener.stop();
    }
}
