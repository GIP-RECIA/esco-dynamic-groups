/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger;

import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.PersonsParametersSection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Reference implementation of a factory for the SyncRepl messages logger.
 * @author GIP RECIA - A. Deman
 * 3 juin 2009
 *
 */
public class SyncReplMessagesLoggerFactoryImpl implements ISyncReplMessagesLoggerFactory, InitializingBean {

    /** Serial version UID.*/
    private static final long serialVersionUID = 2685440191301993955L;
    
    /** Provider for the user parameters. */
    private ParametersProvider parametersProvider;

    /** The LDAP Parameters. */
    private PersonsParametersSection ldapParameters;

    /**
     * Builds an instance of SyncReplMessagesLoggerFactoryImpl.
     */
    public SyncReplMessagesLoggerFactoryImpl() {
        super();
    }

    /**
     * Creates a SyncRepl messages logger.
     * @return The SyncRepl messages logger.
     * @see org.esco.dynamicgroups.dao.ldap.syncrepl.client.util.logger.ISyncReplMessagesLoggerFactory#createLogger()
     */
    public ISyncReplMessagesLogger createLogger() {
        if (ldapParameters.getSyncReplMessagesLogModulo() == 0) {
            return new DisabledSyncReplMessageLogger();
        } else if (ldapParameters.getSyncReplMessagesLogModulo() == 1) {
            return  new AllMessagesSyncReplMessageLogger();
        } 
        return new ModuloSyncReplMessagesLogger(ldapParameters.getSyncReplMessagesLogModulo());

    }

    /**
     * Checks the bean injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.parametersProvider, "The property parametersProvider in the class " 
                + getClass().getName() + " can't be null.");

        ldapParameters = (PersonsParametersSection) parametersProvider.getPersonsParametersSection();

    }

    /**
     * Getter for parametersProvider.
     * @return parametersProvider.
     */
    public ParametersProvider getParametersProvider() {
        return parametersProvider;
    }

    /**
     * Setter for parametersProvider.
     * @param parametersProvider the new value for parametersProvider.
     */
    public void setParametersProvider(final ParametersProvider parametersProvider) {
        this.parametersProvider = parametersProvider;
    }

}
