/**
 * 
 */
package org.esco.dynamicgroups.util;

import java.io.Serializable;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Resources provider.
 * @author GIP RECIA - A. Deman
 * 8 juin 2009
 *
 */
public class ResourceProviderManager implements IResourceProvider, Serializable {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = -6231608603279692988L;
    
    /** Servlet context resource. */
    private static ServletContextResourcesProvider servletContextResourceUtil;

    /**
     * Builds an instance of ResourceProviderManager.
     */
    public ResourceProviderManager() {
        super();
    }
   
    /**
     * Setter for servletContextResourceUtil.
     * @param newServletContextResourceUtil the new value for servletContextResourceUtil.
     */
    public static void register(final ServletContextResourcesProvider newServletContextResourceUtil) {
        ResourceProviderManager.servletContextResourceUtil = newServletContextResourceUtil;
    }
    
    /**
     * Gives the resource associated to a given path under the servlet
     * context.
     * @param path The path of the resource.
     * @return The resource.
     */
    public Resource getResource(final String path) {
        if (servletContextResourceUtil != null) {
            return servletContextResourceUtil.getResource(path);
        } 
        return new FileSystemResource(path);
    }

}
