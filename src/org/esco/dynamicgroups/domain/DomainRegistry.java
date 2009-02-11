/**
 * 
 */
package org.esco.dynamicgroups.domain;

import java.io.Serializable;

/**
 * Used to retrieve the domain service outside of a spring context.
 * @author GIP RECIA - A. Deman
 * 10 févr. 2009
 *
 */
public class DomainRegistry implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -7272809773686150716L;
    
    /** Singleton. */
    private static final DomainRegistry INSTANCE = new DomainRegistry();
    
    /** The domain service. */
    private IDomainService domainService;
    
    /**
     * Builds an instance of DomainRegistry.
     */
    protected DomainRegistry() {
        super();
    }
    
    /**
     * Gives the singleton.
     * @return The available instance of DomainRegistry.
     */
    public static DomainRegistry instance() {
        return INSTANCE;
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
