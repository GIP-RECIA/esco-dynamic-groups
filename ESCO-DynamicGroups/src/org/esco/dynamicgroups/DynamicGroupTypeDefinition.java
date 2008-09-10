package org.esco.dynamicgroups;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.query.IQueryParser;

/**
 * Definition of a dynamic group type.
 * @author GIP RECIA - A. Deman
 * 29 avr. 08
 *
 */
public class DynamicGroupTypeDefinition implements Serializable {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 6153845564152165159L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DynamicGroupTypeDefinition.class);
    
    /** Token to delimit a string to replace. */
    private static final String REPLACEMENT_TOKEN = "%";
    
    /** Token to replace in the search definition. */
    private static final String QUERY_TOKEN = REPLACEMENT_TOKEN + "QUERY" + REPLACEMENT_TOKEN;

    /** The type of groups in grouper. */
    private String groupType;
    
    /** The id of the subject source to use. */
    private String sourceId;
    
    /** The id of the subject search to use. */
    private String searchId;
    
    /** The string that represents the query. */
    private String query;
    
    /** The parser to use for the query.*/
    private IQueryParser queryParser;
    
    /** Attributes involved in the dynamic group definition. */
    private String[] involvedAttributes;
    
    
    
    /**
     * Constructor for DynamicGroupTypeDefinition.
     * @param groupType The type of group in grouper.
     * @param sourceId The id of the source to use.
     * @param searchId The id of the search to use.
     * @param query The query associated to the group definition.
     * @param queryParser Parser for the query.
     */
    public DynamicGroupTypeDefinition(final String groupType,
            final String sourceId, 
            final String searchId, 
            final String query,
            final IQueryParser queryParser) {
        this.groupType = groupType;
        this.sourceId = sourceId;
        this.searchId = searchId;
        this.query = query;
        this.queryParser = queryParser;
        this.involvedAttributes = queryParser.retrieveInvolvedAttributes(query); 
    }
    
    /**
     * Gives the hash code of the group definition.
     * @return The hash code.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getGrouperType().hashCode();
    }
    
    /**
     * Tests if this object is equal to another one.
     * @param object The object to test.
     * @return True if the two objects are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || !(object instanceof DynamicGroupTypeDefinition)) {
            return false;
        }
        final DynamicGroupTypeDefinition dgDef = (DynamicGroupTypeDefinition) object;
        return dgDef.getGrouperType().equals(getGrouperType()); 
    }

    /**
     * Gives the String representation of this definition.
     * @return The string representation of the definition.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append("#{");
        sb.append("Type: ");
        sb.append(getGrouperType());
        sb.append(", sourceId: ");
        sb.append(getSourceId());
        sb.append(", searchId: ");
        sb.append(getSearchId());
        sb.append(", query:");
        sb.append(getQuery());
        sb.append(", queryParser:");
        sb.append(getQueryParser());
        sb.append(", involved attributes:");
        sb.append(Arrays.toString(involvedAttributes));
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * Getter for groupType.
     * @return the groupType
     */
    public String getGrouperType() {
        return groupType;
    }

    /**
     * Setter for groupType.
     * @param groupType the groupType to set
     */
    protected void setGrouperType(final String groupType) {
        this.groupType = groupType;
    }

    /**
     * Getter for sourceId.
     * @return the sourceId
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * Setter for sourceId.
     * @param sourceId the sourceId to set
     */
    protected void setSourceId(final String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * Getter for searchId.
     * @return the searchId
     */
    public String getSearchId() {
        return searchId;
    }

    /**
     * Setter for searchId.
     * @param searchId the searchId to set
     */
    protected void setSearchId(final String searchId) {
        this.searchId = searchId;
    }

    /**
     * Getter for query.
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Setter for query.
     * @param query the query to set
     */
    protected void setQuery(final String query) {
        this.query = query;
    }
    
    /**
     * Gives the number of attributes that are involved 
     * in the dynamic group definition.
     * @return The number of involved attributes.
     */
    public int countInvolvedAttributes() {
        return involvedAttributes.length;
    }
    
    /**
     * Gives an attribute at a given position.
     * @param index The index of the attribute.
     * @return The attribute.
     */
    public String getInvolvedAttribute(final int index) {
        return involvedAttributes[index];
    }

    /**
     * Getter for queryParser.
     * @return the queryParser
     */
    public IQueryParser getQueryParser() {
        return queryParser;
    }

    /**
     * Setter for queryParser.
     * @param queryParser the queryParser to set
     */
    protected void setQueryParser(final IQueryParser queryParser) {
        this.queryParser = queryParser;
    }
    

}
