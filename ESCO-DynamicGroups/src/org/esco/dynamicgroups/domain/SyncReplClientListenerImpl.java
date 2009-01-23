/**
 * 
 */
package org.esco.dynamicgroups.domain;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.syncrepl.client.ESCOSyncReplClient;
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

    /** Serial version UID.*/
    private static final long serialVersionUID = -5712448170582262878L;

    /** The ldap replication client. */
    private ESCOSyncReplClient syncReplClient;

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
    }

    /**
     * Launches the listener for the LDAP changes.
     * @see org.esco.dynamicgroups.domain.IRepositoryListener#listen()
     */
    public void listen() {

        final Thread thread = new Thread() {

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
        thread.start();


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

}
