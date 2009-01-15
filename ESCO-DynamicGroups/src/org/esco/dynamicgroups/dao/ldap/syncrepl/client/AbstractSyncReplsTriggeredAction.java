/**
 * 
 */
package org.esco.dynamicgroups.dao.ldap.syncrepl.client;

import java.io.Serializable;

import org.esco.dynamicgroups.ESCOEntryDTOFactory;
import org.esco.dynamicgroups.util.ESCODynamicGroupsParameters;

/**
 * @author GIP RECIA - A. Deman
 * 15 janv. 2009
 *
 */
public abstract class AbstractSyncReplsTriggeredAction implements ISyncReplTriggeredAction, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 5748661334983614711L;


    /** The LDAP attribute for the id. */
    private String idAttribute;
    
    /** EntryDTO factory. */
    private ESCOEntryDTOFactory entryDTOFactory;
    
    /**
     * Builds an instance of AbstractSyncReplsTriggeredAction.
     */
    public AbstractSyncReplsTriggeredAction() {
      idAttribute = ESCODynamicGroupsParameters.instance().getLdapIdAttribute();
      entryDTOFactory = new ESCOEntryDTOFactory(idAttribute);
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

}
