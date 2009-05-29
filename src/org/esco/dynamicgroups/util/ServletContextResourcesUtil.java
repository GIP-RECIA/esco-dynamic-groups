/**
 * 
 */
package org.esco.dynamicgroups.util;

import javax.servlet.ServletContext;

import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;

/**
 * Util class to get the resources under the Servlet context.
 * @author GIP RECIA - A. Deman
 * 29 mai 2009
 *
 */
public class ServletContextResourcesUtil implements ServletContextAware {
    
    /** The servlet context. */
    private ServletContext servletContext;
    

    /**
     * Builds an instance of ServletContextResourcesUtil.
     */
    public ServletContextResourcesUtil() {
        super();
    }
    
    /**
     * Setter for the servlet context.
     * @param servletContext The servlet context.
     * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
    @Override
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
        return new ServletContextResource(servletContext, path);
    }

}
