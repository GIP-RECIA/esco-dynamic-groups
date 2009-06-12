/**
 * 
 */
package org.esco.dynamicgroups.util;

import org.springframework.core.io.Resource;

/**
 * @author GIP RECIA - A. Deman
 * 8 juin 2009
 *
 */
public interface IResourceProvider {

    /**
     * Gives the resource associated to a given path under the servlet
     * context.
     * @param path The path of the resource.
     * @return The resource.
     */
    Resource getResource(final String path);
    
}
