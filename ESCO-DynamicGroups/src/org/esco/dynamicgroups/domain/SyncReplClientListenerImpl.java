/**
 * 
 */
package org.esco.dynamicgroups.domain;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.syncrepl.client.ESCOSyncReplClient;
import org.esco.dynamicgroups.exceptions.IUncaughtExceptionHandlerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/** 
 * Listener based on a LDAP replicat.
 * @author GIP RECIA - A. Deman
 * 23 janv. 2009
 *
 */
public class SyncReplClientListenerImpl implements IRepositoryListener, InitializingBean, Serializable {
    /** The logger. */
    protected static final Logger LOGGER = Logger.getLogger(SyncReplClientListenerImpl.class);

    /** Limit while waiting for the SyncRepl client to stop. */
    private static final int NB_MAX_WAIT = 15;
    
    /** Serial version UID.*/
    private static final long serialVersionUID = -5712448170582262878L;

    /** The ldap replication client. */
    private ESCOSyncReplClient syncReplClient;
    
    /** The handler for the exception. */
    private IUncaughtExceptionHandlerFactory exceptionHandlerFactory;
    
    /** The thread for the listener. */
    private Thread thread;
    
    

    /**
     * Builds an instance of SyncReplClientListenerImpl.
     */
    public SyncReplClientListenerImpl() {
        super();
    }

    /**
     * Checks the properties after the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.syncReplClient, 
                "The property syncReplClient in the class " + this.getClass().getName() 
                + " can't be null.");
        
        Assert.notNull(this.exceptionHandlerFactory,
                "The property exceptionHandlerFactory in the class " + this.getClass().getName() 
                + " can't be null.");
    }

    /**
     * Launches the listener for the LDAP changes.
     * @see org.esco.dynamicgroups.domain.IRepositoryListener#listen()
     */
    public void listen() {

        LOGGER.debug("Starting the listener.");
        
        thread = new Thread() {

            /**
             * Run method.
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                try {
                    getSyncReplClient().launch();
                } catch (IOException e) {
                    LOGGER.error(e, e);
                }
            }
        };
        thread.setUncaughtExceptionHandler(exceptionHandlerFactory.createExceptionHandler());
        
        thread.start();
        
        LOGGER.info("Listener started.");

    }
    
    /**
     * Stop listening.
     * @see org.esco.dynamicgroups.domain.IRepositoryListener#stop()
     */
    public void stop() {
        
        if (thread != null) {
            LOGGER.debug("Stopping the listener.");
            getSyncReplClient().requestToStop();
            final int secondInMillis = 1000;
            int count = 0;
        
            while (getSyncReplClient().isRunning() && count++ < NB_MAX_WAIT) {
                LOGGER.debug(" Waitting for the listener to be stopped " + count + "/" + NB_MAX_WAIT + ".");
                try {
                    Thread.sleep(secondInMillis);
                } catch (InterruptedException e) {
                    // Nothing to do.
                }
            }
            LOGGER.info("Listener stopped");
            thread = null;
        }
    }
    
    /**
     * Checks that the listener is active.
     * @return True if the listener is active.
     * @see org.esco.dynamicgroups.domain.IRepositoryListener#isListening()
     */
    public boolean isListening() {
        return getSyncReplClient().isRunning();
    }

    /**
     * Getter for syncReplClient.
     * @return syncReplClient.
     */
    public ESCOSyncReplClient getSyncReplClient() {
        return syncReplClient;
    }

    /**
     * Setter for syncReplClient.
     * @param syncReplClient the new value for syncReplClient.
     */
    public void setSyncReplClient(final ESCOSyncReplClient syncReplClient) {
        this.syncReplClient = syncReplClient;
    }

    /**
     * Getter for exceptionHandlerFactory.
     * @return exceptionHandlerFactory.
     */
    public IUncaughtExceptionHandlerFactory getExceptionHandlerFactory() {
        return exceptionHandlerFactory;
    }

    /**
     * Setter for exceptionHandlerFactory.
     * @param exceptionHandlerFactory the new value for exceptionHandlerFactory.
     */
    public void setExceptionHandlerFactory(final IUncaughtExceptionHandlerFactory exceptionHandlerFactory) {
        this.exceptionHandlerFactory = exceptionHandlerFactory;
    }

}
