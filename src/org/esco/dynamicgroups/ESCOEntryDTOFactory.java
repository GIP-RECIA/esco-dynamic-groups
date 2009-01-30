/**
 * 
 */
package org.esco.dynamicgroups;


import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPEntry;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Factory to build Entry DTO.
 * @author GIP RECIA - A. Deman
 * 18 avr. 08
 *
 */
public class ESCOEntryDTOFactory implements Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = -5063551721940325754L;
    
    /** Comma separator. */
    private static final String COMMA = ",";
    
    /** Equal constant. */
    private static final String EQUAL = "=";
    
    /** LDAP id attribute. */
    private String idAttribute;
    
    /**
     * Constructor for ESCOEntryDTOFactory.
     * @param idAttribute The LDAP id attribute.
     */
    public ESCOEntryDTOFactory(final String idAttribute) {
       this.idAttribute = idAttribute;
    }
    
    /**
     * Fetches the id from an ldap entry.
     * Tries first to retrieve the specified id attribute and if not found tries
     * to extract it from the dn.
     * @param ldapEntry The ldap entry from which the id has to be retrieved.
     * @return The id if found, null otherwise.
     */
    private String fetchID(final LDAPEntry ldapEntry) {
        final LDAPAttribute idAttr = ldapEntry.getAttribute(idAttribute);
        if (idAttr != null) {
            return idAttr.getStringValue();
        }
        final String dn = ldapEntry.getDN();
        if (dn != null) {
            final int attrPos = dn.indexOf(idAttribute);
            if (attrPos < 0) {
                return null;
            }
            
            final int equalPos = dn.indexOf(EQUAL, attrPos);
            if (equalPos < 0) {
               return null;
            }
            final int startPos = equalPos + 1;
            final int commaPos = dn.indexOf(COMMA, startPos);
                if (commaPos > startPos) {
                    final String id = dn.substring(startPos, commaPos);
                    return id.trim();
                }
            }
        
        return null;
    }
   
    
    /**
     * Creation of an entry DTO from LDAP informations.
     * @param ldapEntry The LDAP informations.
     * @return The EntryDTO if the LDAP entry is valid (an id can be retrieved) null otherwise.
     */
    public IEntryDTO createEntryDTO(final LDAPEntry ldapEntry) {
        
        final String entryId = fetchID(ldapEntry);
        if (entryId == null) {
            return null;
        }
        
        final LDAPAttributeSet attributeSet = ldapEntry.getAttributeSet(); 
        final Map<String, String[]> entryAttributes = new HashMap<String, String[]>(attributeSet.size());
        final Iterator< ? > it = attributeSet.iterator();
        
        while (it.hasNext()) {
            
            final LDAPAttribute attribute = (LDAPAttribute) it.next();
            final String name = attribute.getName();
            final String[] values = attribute.getStringValueArray();
            
            // Retrieves the attributes.
            if (!name.equals(idAttribute)) {
                entryAttributes.put(name, values);
            }
        }
        
        return new ESCOEntryDTO(entryId, entryAttributes);
    }
}
