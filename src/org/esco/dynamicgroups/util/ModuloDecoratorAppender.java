/**
 * 
 */
package org.esco.dynamicgroups.util;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * This decorator can be used to limit the amount of logs, 
 * for instance to log a message every 100 calls.
 * 
 * @author GIP RECIA - A. Deman
 * 29 mai 2009
 *
 */
public class ModuloDecoratorAppender extends AppenderSkeleton {
    
    /** DEfault value for the modulo. */
    private static final int DEF_MODULO = 100;
    
    
    /** The modulo to use. */
    private int modulo = DEF_MODULO;
    
    /** The underlying decorated appender. */
    private Appender decoratedAppender;
    
    /** Counter for the calls. */
    private int calls;
    
    
    
    /**
     * Builds an instance of ModuloDecoratorAppender.
     */
    public ModuloDecoratorAppender() {
        super();
    }
    
    /**
     *  @param filter
     * @see org.apache.log4j.Appender#addFilter(org.apache.log4j.spi.Filter)
     */
    @Override
    public void addFilter(final Filter filter) {
        decoratedAppender.addFilter(filter);
    }

    /**
     * @see org.apache.log4j.Appender#clearFilters()
     */
    @Override
    public void clearFilters() {
        decoratedAppender.clearFilters();
    }

    /**
     * @see org.apache.log4j.Appender#close()
     */
    @Override
    public void close() {
       decoratedAppender.close();
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
            decoratedAppender.doAppend(event);
        }

    }

    /**
     * @return ErrorHandler
     * @see org.apache.log4j.Appender#getErrorHandler()
     */
    @Override
    public ErrorHandler getErrorHandler() {
       return decoratedAppender.getErrorHandler();
    }

    /**
     * @return Filter
     * @see org.apache.log4j.Appender#getFilter()
     */
    @Override
    public Filter getFilter() {
       return decoratedAppender.getFilter();
    }

    /**
     * @return Layout
     * @see org.apache.log4j.Appender#getLayout()
     */
    @Override
    public Layout getLayout() {
        return decoratedAppender.getLayout();
    }

    /**
     * This Appender does not require a layout.
     * @return boolean.
     * @see org.apache.log4j.Appender#requiresLayout()
     */
    @Override
    public boolean requiresLayout() {
      return false;
    }

    /**
     * @param arg0
     * @see org.apache.log4j.Appender#setErrorHandler(org.apache.log4j.spi.ErrorHandler)
     */
    @Override
    public void setErrorHandler(final ErrorHandler arg0) {
        decoratedAppender.setErrorHandler(arg0);
    }

    /**
     * @param arg0
     * @see org.apache.log4j.Appender#setLayout(org.apache.log4j.Layout)
     */
    @Override
    public void setLayout(final Layout arg0) {
        decoratedAppender.setLayout(arg0);
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
     * Getter for decoratedAppender.
     * @return decoratedAppender.
     */
    public Appender getDecoratedAppender() {
        return decoratedAppender;
    }

    /**
     * Setter for decoratedAppender.
     * @param decoratedAppender the new value for decoratedAppender.
     */
    public void setDecoratedAppender(final String decoratedAppenderName) {
       
        this.decoratedAppender = decoratedAppender;
    }
    
    
    /**
     * @param loggingEvent.
     * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
     */
    @Override
    protected void append(final LoggingEvent loggingEvent) {
        doAppend(loggingEvent);
        
    }

   

}
