/**
 * 
 */
package org.esco.dynamicgroups.dao.grouper;

import edu.internet2.middleware.subject.Source;
import edu.internet2.middleware.subject.SourceUnavailableException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectType;
import edu.internet2.middleware.subject.provider.SourceManager;
import edu.internet2.middleware.subject.provider.SubjectTypeEnum;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Deleted subject implementation. This is required as Grouper tries to find
 * a subject before to delete it.
 * @author GIP RECIA - A. Deman
 * 29 janv. 2009
 *
 */
public class ESCODeletedSubjectImpl implements Subject, Serializable {
   
    /** Serial version UID.*/
    private static final long serialVersionUID = 6438801573249217982L;

    /** The LOGGER to use. */
    private static final Logger LOGGER = Logger.getLogger(ESCODeletedSubjectImpl.class);
    
    /** Subjects source id. */
    private static String sourceId;
    
    /** Id if a subject. */
    private  String id;
    
    /**
     * Builds an instance of ESCODeletedSubjectImpl.
     * @param id Identifier of the subject.
     */
    ESCODeletedSubjectImpl(final String id) {
        this.id = id;
    }

    /**
     * Gives the value of a single-valued attribute.
     * @param attributeName The name of the attribute.
     * @return Null as the subject is deleted.
     * @see edu.internet2.middleware.subject.Subject#getAttributeValue(java.lang.String)
     */
    public String getAttributeValue(final String attributeName) {
        return null;
    }

    /**
     * Gives the values of a multi-valued attribute.
     * @param attributeName The name of the attribute.
     * @return Null as the subject is deleted.
     * @see edu.internet2.middleware.subject.Subject#getAttributeValues(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public Set getAttributeValues(final String attributeName) {
        return null;
    }

    /**
     * Unused method for the deleted subjects.
     * @return null.
     * @see edu.internet2.middleware.subject.Subject#getAttributes()
     */
    @SuppressWarnings("unchecked")
    public Map getAttributes() {
        return null;
    }

    /**
     * Gives the description of the subject.
     * @return The description of the subject.
     * @see edu.internet2.middleware.subject.Subject#getDescription()
     */
    public String getDescription() {
        return id;
    }

    /**
     * Gives the id t=of the subject.
     * @return The id of the subject.
     * @see edu.internet2.middleware.subject.Subject#getId()
     */
    public String getId() {
       return id;
    }

    /**
     * Gives the name of the subject.
     * @return The name of the suject.
     * @see edu.internet2.middleware.subject.Subject#getName()
     */
    public String getName() {
        return id;
    }

    /**
     * Gives the Subject source.
     * @return The Subject source.
     * @see edu.internet2.middleware.subject.Subject#getSource()
     */
    public Source getSource() {
        try {
            return SourceManager.getInstance().getSource(sourceId);
        } catch (SourceUnavailableException e) {
            LOGGER.error(e, e);
        } catch (Exception e) {
            LOGGER.error(e, e);
        }
        return null;
    }

    /**
     * Gives the Subject type.
     * @return The Subject type.
     * @see edu.internet2.middleware.subject.Subject#getType()
     */
    public SubjectType getType() {
        return SubjectTypeEnum.PERSON;
    }

    /**
     * Setter for sourceId.
     * @param newSourceId the new value for sourceId.
     */
    public static void setSourceId(final String newSourceId) {
        sourceId = newSourceId;
    }

	@Override
	public String getAttributeValueOrCommaSeparated(String attributeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttributeValueSingleValued(String attributeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceId() {
		return sourceId;
	}

	@Override
	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

    
    
}
