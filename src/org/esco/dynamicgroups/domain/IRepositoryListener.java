/**
 * 
 */
package org.esco.dynamicgroups.domain;

/**
 * Base interface for the changes listener in the repository.
 * @author GIP RECIA - A. Deman
 * 23 janv. 2009
 *
 */
public interface IRepositoryListener {
    
    /**
     * Launches the listener.
     */
    void listen();
    
    /**
     * Stop listening.
     */
    void stop();
    
    /**
     * Checks that the listener is active.
     * @return True if the listener is active.
     */
    boolean isListening();

}
