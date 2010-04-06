/**
 * 
 */
package org.esco.dynamicgroups.exceptions;

/**
 * Factory for the UncaughExceptionHandlers to use in the threads.
 * @author GIP RECIA - A. Deman
 * 18 mai 2009
 *
 */
public interface IUncaughtExceptionHandlerFactory {

    /**
     * Creates an instance of handler.
     * @return The handler.
     */
    Thread.UncaughtExceptionHandler createExceptionHandler();
}
