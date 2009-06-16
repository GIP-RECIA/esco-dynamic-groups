/**
 * 
 */
package org.esco.dynamicgroups.util;

import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This decorator can be used to limit the amount of logs, 
 * for instance to log a message every 100 calls.
 * 
 * @author GIP RECIA - A. Deman
 * 29 mai 2009
 *
 */
public class ModuloDecoratorAppender extends AppenderSkeleton implements AppenderAttachable {

    /** DEfault value for the modulo. */
    private static final int DEF_MODULO = 100;


    /** The modulo to use. */
    private int modulo = DEF_MODULO;

    /** The underlying decorated appender. */
    private AppenderAttachableImpl decoratedAppenders;

    /** Counter for the calls. */
    private int calls;



    /**
     * Builds an instance of ModuloDecoratorAppender.
     */
    public ModuloDecoratorAppender() {
        decoratedAppenders = new AppenderAttachableImpl();
    }



    /**
     * @see org.apache.log4j.Appender#close()
     */
    public void close() {
        synchronized (decoratedAppenders) {
            @SuppressWarnings("unchecked")
            final Enumeration appendersIt = decoratedAppenders.getAllAppenders();

            if (appendersIt != null) {
                while (appendersIt.hasMoreElements()) {
                    final Object appenderObj = appendersIt.nextElement();
                    if (appenderObj instanceof Appender) {
                        ((Appender) appenderObj).close();
                    }
                }
            }
        }
    }

    /**
     * Actually appends only every modulo calls.
     * @param event The logging event to append.
     * @see org.apache.log4j.Appender#doAppend(org.apache.log4j.spi.LoggingEvent)
     */
    @Override
    public void doAppend(final LoggingEvent event) {
        boolean actuallyAppend;

        synchronized (this) {
            actuallyAppend = (calls++ % modulo) == 0;
        }

        
        if (actuallyAppend) {

            @SuppressWarnings("unchecked")
            final Enumeration appendersIt = decoratedAppenders.getAllAppenders();

            if (appendersIt != null) {
                while (appendersIt.hasMoreElements()) {
                    final Object appenderObj = appendersIt.nextElement();
                    if (appenderObj instanceof Appender) {
                        ((Appender) appenderObj).doAppend(event);
                    }
                }
            }
        }
    }

    /**
     * Getter for modulo.
     * @return modulo.
     */
    public int getModulo() {
        return modulo;
    }

    /**
     * Setter for modulo.
     * @param modulo the new value for modulo.
     */
    public void setModulo(final int modulo) {
        this.modulo = modulo;
    }

    /**
     * Performs actual logging.
     * @param loggingEvent The event to log.
     * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
     */
    @Override
    protected void append(final LoggingEvent loggingEvent) {
        doAppend(loggingEvent);
    }

    /**
     * Add an appender.
     * @param appender The new appender.
     * @see org.apache.log4j.spi.AppenderAttachable#addAppender(org.apache.log4j.Appender)
     */
    public void addAppender(final Appender appender) {
        synchronized (decoratedAppenders) {
            decoratedAppenders.addAppender(appender);
        }
    }

    /**
     * Gives all the decorated appenders.     
     * @return The decorated appenders.
     * @see org.apache.log4j.spi.AppenderAttachable#getAllAppenders()
     */
    @SuppressWarnings("unchecked")
    public Enumeration getAllAppenders() {
        synchronized (decoratedAppenders) {
            return decoratedAppenders.getAllAppenders();
        }
    }

    /**
     * Gives an appender.
     * @param appenderName The name of the appender to give.
     * @return The appender.
     * @see org.apache.log4j.spi.AppenderAttachable#getAppender(java.lang.String)
     */
    public Appender getAppender(final String appenderName) {
        synchronized (decoratedAppenders) {
            return decoratedAppenders.getAppender(appenderName);
        }
    }


    /**
     * Tests if an appender is attached.
     * @param appender The appender.
     * @return True if the appender is attached.
     * @see org.apache.log4j.spi.AppenderAttachable#isAttached(org.apache.log4j.Appender)
     */
    public boolean isAttached(final Appender appender) {
        synchronized (decoratedAppenders) {
            return decoratedAppenders.isAttached(appender);
        }
    }

    /**
     * Removes and closes all attached appenders.
     * 
     * @see org.apache.log4j.spi.AppenderAttachable#removeAllAppenders()
     */
    public void removeAllAppenders() {
        synchronized (decoratedAppenders) {
            decoratedAppenders.removeAllAppenders();
        }
    }

    /**
     * Removes an appender.
     * @param appender The appender to remove.
     * @see org.apache.log4j.spi.AppenderAttachable#removeAppender(org.apache.log4j.Appender)
     */
    public void removeAppender(final Appender appender) {
        synchronized (decoratedAppenders) {
            decoratedAppenders.removeAppender(appender);
        }
    }

    /**
     * Removes an appender.
     * @param appenderName The name of the appender to remove.
     * @see org.apache.log4j.spi.AppenderAttachable#removeAppender(java.lang.String)
     */
    public void removeAppender(final String appenderName) {
        synchronized (decoratedAppenders) {
            decoratedAppenders.removeAppender(appenderName);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean requiresLayout() {
      return false;
    }



}
