/**
 * 
 */
package org.esco.dynamicgroups;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * @author GIP RECIA - A. Deman
 * 18 avr. 08
 *
 */
public class ESCOEntryDTO implements IEntryDTO {

    /** Serial version UID. */
    private static final long serialVersionUID = 3029897707579918936L;

    /** ID of the entry. */
    private String id;

    /** Attributes of the entry. */
    private Map<String, String[]> attributes;

    /** String representation of the DTO. */
    private String stringRepresentation;

    /** Used to retrieve quickly the names of the attributes by index. */
    private String[] names;

    /** Used to retrieve quickly the values of the attributes by index. */
    private String[][] values;

    /**
     * Constructor for ESCOEntryDTO.
     * @param id The id of the entry.
     * @param attributes The map of the attributes of the entry.
     */
    public ESCOEntryDTO(final String id, final Map<String, String[]> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * Gives the hash code for thisDTO.
     * @return The hash code for this DTO.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Tests if an object is equal to this DTO.
     * @param obj The object to test.
     * @return True if the object is a DTO with the same 
     * id.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ESCOEntryDTO)) {
            return false;
        }
        return getId().equals(((ESCOEntryDTO) obj).getId());
    }

    /**
     * Gives the string representation of the DTO.
     * @return The string representation of the DTO.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (stringRepresentation == null) {
            stringRepresentation = getClass().getSimpleName() + "#{"
            + "Id: " + getId()
            + "; Attributes: [";
            for (int i = 0; i < countAttributes(); i++) {
                if (i > 0) {
                    stringRepresentation += ", ";
                }
                stringRepresentation += getAttibuteName(i) + " = ";
                final String[] attrValue = getAttributeValue(i);

                if (attrValue.length == 1) {
                    stringRepresentation += attrValue[0];
                } else {
                    for (int j = 0; j < attrValue.length; j++) {
                        if (j == 0) {
                            stringRepresentation += "[";
                        } else {
                            stringRepresentation += ", ";
                        }
                        stringRepresentation += attrValue[j];
                    }
                    stringRepresentation += "]";
                }
            }
            stringRepresentation += "]}";
        }
        return stringRepresentation;
    }

    /**
     * Gives the number of attributes.
     * @return The number of attributes.
     * @see org.esco.dynamicgroups.IEntryDTO#countAttributes()
     */
    public int countAttributes() {
        return attributes.size();
    }

    /**
     * Gives the name of the attribute at the position index.
     * @param index The position of the attribute.
     * @return The name of the attribute.
     * @see org.esco.dynamicgroups.IEntryDTO#getAttibuteName(int)
     */
    public String getAttibuteName(final int index) {
        initializeNamesAndValues();
        return names[index];
    }

    /**
     * Gives the list of the names of the attributes.
     * @return The list of the values of the attributes.
     * @see org.esco.dynamicgroups.IEntryDTO#getAttributeNames()
     */
    public Set<String> getAttributeNames() {
        return attributes.keySet();
    }

    /**
     * Gets the value of an attribute.
     * @param name The key of the attribute to retrieve.
     * @return The value of the attribute.
     * @see org.esco.dynamicgroups.IEntryDTO#getAttributeValue(java.lang.String)
     */
    public String[] getAttributeValue(final String name) {
        return attributes.get(name);
    }

    /**
     * Gives the value of the attribute at the position index.
     * @param index The position of the attribute.
     * @return The value of the attribute.
     * @see org.esco.dynamicgroups.IEntryDTO#getAttributeValue(int)
     */
    public String[] getAttributeValue(final int index) {
        initializeNamesAndValues();
        return values[index];
    }

    /**
     * Gives the list of the values of the attributes.
     * @return The list of the values of the attributes.
     * @see org.esco.dynamicgroups.IEntryDTO#getAttributeValues()
     */
    public Collection<String[]> getAttributeValues() {
        return attributes.values();
    }

    /**
     * Gives the Id of the subject.
     * @return The id of the subject.
     * @see org.esco.dynamicgroups.IEntryDTO#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * The if the DTO contains a given attribute.
     * @param attributeName The name of the attribute to look for.
     * @return True if the DTO contains the attribute, false otherwise.
     * @see org.esco.dynamicgroups.IEntryDTO#hasAttribute(java.lang.String)
     */
    public boolean hasAttribute(final String attributeName) {
        return attributes.containsKey(attributeName);
    }

    /**
     * Initialization of the names and values,
     * used to retrieve the names and the values by index.
     */
    protected void initializeNamesAndValues() {

        // Names and values are null or not null together.
        if (names == null) {
            names = getAttributeNames().toArray(new String[countAttributes()]);
            values = new String[countAttributes()][];
            for (int i = 0; i < countAttributes(); i++) {
                values[i] = getAttributeValue(names[i]);  
            }
        }
    }


}
