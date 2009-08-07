/**
 * 
 */
package org.esco.dynamicgroups.util;

import java.io.Serializable;

import javax.servlet.ServletContext;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;

/**
 * Util class to get the resources under the Servlet context or under a batch context.
 * @author GIP RECIA - A. Deman
 * 29 mai 2009
 *
 */
public class ResourcesProviderImpl implements ServletContextAware, IResourcesProvider, Serializable {
    
    /** Serial version UID.*/
    private static final long serialVersionUID = 5640258458049553452L;
    
    /** The servlet context. */
    private ServletContext servletContext;
    
    /**
     * Builds an instance of ResourcesProviderImpl.
     */
    public ResourcesProviderImpl() {
        super();
    }
    
    /**
     * Setter for the servlet context.
     * @param servletContext The servlet context.
     * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    /**
     * Gives the resource associated to a given path under the servlet
     * context.
     * @param path The path of the resource.
     * @return The resource.
     */
    public Resource getResource(final String path) {
        if (servletContext != null) {
            return new ServletContextResource(servletContext, path);
        }
        return new  FileSystemResource(path);
    }

}
