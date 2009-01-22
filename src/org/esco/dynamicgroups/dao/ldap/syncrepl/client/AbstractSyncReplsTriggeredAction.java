/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client;

import java.io.Serializable;

import org.esco.dynamicgroups.ESCOEntryDTOFactory;
import org.esco.dynamicgroups.domain.IDomainService;
import org.esco.dynamicgroups.util.ESCODynamicGroupsParameters;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author GIP RECIA - A. Deman
 * 15 janv. 2009
 *
 */
public abstract class AbstractSyncReplsTriggeredAction 
    implements ISyncReplTriggeredAction, Serializable, InitializingBean {

    /** Serial version UID.*/
    private static final long serialVersionUID = 5748661334983614711L;

    /** The domain service to use .*/
    private IDomainService domainService;

    /** The LDAP attribute for the id. */
    private String idAttribute;
    
    /** EntryDTO factory. */
    private ESCOEntryDTOFactory entryDTOFactory;
    
    /**
     * Builds an instance of AbstractSyncReplsTriggeredAction.
     */
    public AbstractSyncReplsTriggeredAction() {
      idAttribute = ESCODynamicGroupsParameters.instance().getLdapUidAttribute();
      entryDTOFactory = new ESCOEntryDTOFactory(idAttribute);
    }
        
    /**
     * Checks the properties after the bean injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.domainService, 
                "The property domainService in the class " + this.getClass().getName() 
                + " can't be null.");
        
    }

    /**
     * Getter for idAttribute.
     * @return idAttribute.
     */
    protected String getIdAttribute() {
        return idAttribute;
    }

   

    /**
     * Getter for entryDTOFactory.
     * @return entryDTOFactory.
     */
    protected ESCOEntryDTOFactory getEntryDTOFactory() {
        return entryDTOFactory;
    }
    
    /**
     * Getter for domainService.
     * @return domainService.
     */
    public IDomainService getDomainService() {
        return domainService;
    }

    /**
     * Setter for domainService.
     * @param domainService the new value for domainService.
     */
    public void setDomainService(final IDomainService domainService) {
        this.domainService = domainService;
    }

}
