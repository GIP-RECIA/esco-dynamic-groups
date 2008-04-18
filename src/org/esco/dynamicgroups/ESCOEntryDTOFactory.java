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
    
    /** LDAP id attribute. */
    private String uidAttribute;
    
    /**
     * Constructor for ESCOEntryDTOFactory.
     * @param uidAttribute The LDAP id attribute.
     */
    public ESCOEntryDTOFactory(final String uidAttribute) {
       this.uidAttribute = uidAttribute;
    }
    
    /**
     * Creation of an entry DTO from LDAP informations.
     * @param ldapEntry The LDAP informations.
     * @return The EntryDTO.
     */
    public IEntryDTO createEntryDTO(final LDAPEntry ldapEntry) {
        
        final LDAPAttributeSet attributeSet = ldapEntry.getAttributeSet(); 
        final Map<String, String[]> entryAttributes = new HashMap<String, String[]>(attributeSet.size());
        final Iterator< ? > it = attributeSet.iterator();
        String entryId = null;
        
        while (it.hasNext()) {
            
            LDAPAttribute attribute = (LDAPAttribute) it.next();
            final String name = attribute.getName();
            final String[] values = attribute.getStringValueArray();
            
            // Retrieves the id and the other attributes.
            if (name.equals(uidAttribute)) {
                entryId = values[0];
            } else {
                entryAttributes.put(name, values);
            }
        }
        return new ESCOEntryDTO(entryId, entryAttributes);
    }
}
