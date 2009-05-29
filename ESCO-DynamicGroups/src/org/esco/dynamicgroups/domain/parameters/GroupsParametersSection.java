/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;


import java.util.Properties;
import org.apache.log4j.Logger;



/**
 * User parameters for the groups service.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public class GroupsParametersSection extends DGParametersSection {

       
  
    /** Serial version UID. */
    private static final long serialVersionUID = 9168255829280261203L;
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GroupsParametersSection.class);
   
    /** The id of the subject source. */
    private String grouperSubjectsSourceId;

    /** The type in grouper associated to the dynamic groups. */
    private String grouperType;

    /** Flag to determine if the dynamic grouper type should be created
     * if it does not exist. */
    private boolean createGrouperType;

    /** Flag to determine if the members of the dynamic groups should be checked. */
    private boolean checkMembersOnStartup;

    /** The field used in grouper to define the members. */
    private String grouperDefinitionField;

    /** The Grouper user used to open the Grouper sessions. */
    private String grouperUser;

    /** Flag used to detemine if a deleted user should be removed from
     * all the groups or only from the dynamic ones.*/
    private boolean removeFromAllGroups;
    
    
    /**
     * Constructor for GroupsParametersSection.
     */
    private GroupsParametersSection() {
        super();
    }
    
    /**
     * Gives the logger for this class.
     * @return The logger for this class.
     * @see org.esco.dynamicgroups.domain.parameters.DGParametersSection#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }


    /**
     * Loads all the parameters.
     * @param params The properties that contains the values to load.
     */
    @Override
    public void loadFromProperties(final Properties params) {
        
             
        // keys used to retrieve the values in the properties instance. 
        final String grouperTypeKey = PROPERTIES_PREFIX + "grouper.type";
        final String grouperSubjSourceKey = PROPERTIES_PREFIX + "grouper.subjects.source";
        final String createGrouperTypeKey = PROPERTIES_PREFIX + "grouper.create.type";
        final String checkMbOnStartupKey = PROPERTIES_PREFIX + "grouper.check.members.on.startup";
        final String grouperDefKey = PROPERTIES_PREFIX + "grouper.definiton.field";
        final String grouperUserKey = PROPERTIES_PREFIX + "grouper.user";
        final String removeFromAllGroupsKey = PROPERTIES_PREFIX + "grouper.remove.from.all.groups";
        
      
        // Retrieves the values.
        setGrouperSubjectsSourceId(parseStringFromProperty(params, grouperSubjSourceKey));
        setGrouperType(parseStringFromProperty(params, grouperTypeKey));
        setCreateGrouperType(parseBooleanFromProperty(params, createGrouperTypeKey)); 
        setCheckMembersOnStartup(parseBooleanFromProperty(params, checkMbOnStartupKey)); 
        setGrouperDefinitionField(parseStringFromProperty(params, grouperDefKey));
        setGrouperUser(parseStringFromProperty(params, grouperUserKey));
        setRemoveFromAllGroups(parseBooleanFromProperty(params, removeFromAllGroupsKey)); 
    }

   
    /**
     * Gives the string representation of the instance.
     * @return The String that denotes the values of the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        toStringFormatSingleEntry(sb, getClass().getSimpleName() + "#{\n");
        
        toStringFormatProperty(sb, "Grouper Subjects source: ", getGrouperSubjectsSourceId());
        toStringFormatProperty(sb, "Grouper Type: ", getGrouperType());
        toStringFormatProperty(sb, "Grouper definition field: ", getGrouperDefinitionField());
        toStringFormatProperty(sb, "Create grouper type (if not present): ", getCreateGrouperType());
        toStringFormatProperty(sb, "Reset dynamic groups on startup: ", getCheckMembersOnStartup());
        toStringFormatProperty(sb, "Grouper user: ", getGrouperUser());
        toStringFormatProperty(sb, "remove from all groups: ", getRemoveFromAllGroups());
        toStringFormatSingleEntry(sb, "}");
        return sb.toString();
    }

    

    /**
     * Getter for grouperType.
     * @return grouperType.
     */
    public String getGrouperType() {
        return grouperType;
    }

    /**
     * Setter for grouperType.
     * @param grouperType the new value for grouperType.
     */
    public void setGrouperType(final String grouperType) {
        this.grouperType = grouperType;
    }

    /**
     * Getter for grouperUser.
     * @return grouperUser.
     */
    public String getGrouperUser() {
        return grouperUser;
    }

    /**
     * Setter for grouperUser.
     * @param grouperUser the new value for grouperUser.
     */
    public void setGrouperUser(final String grouperUser) {
        this.grouperUser = grouperUser;
    }

    /**
     * Getter for removeFromAllGroups.
     * @return removeFromAllGroups.
     */
    public boolean getRemoveFromAllGroups() {
        return removeFromAllGroups;
    }

    /**
     * Setter for removeFromAllGroups.
     * @param removeFromAllGroups the new value for removeFromAllGroups.
     */
    public void setRemoveFromAllGroups(final boolean removeFromAllGroups) {
        this.removeFromAllGroups = removeFromAllGroups;
    }

    /**
     * Getter for grouperDefinitionField.
     * @return grouperDefinitionField.
     */
    public String getGrouperDefinitionField() {
        return grouperDefinitionField;
    }

    /**
     * Setter for grouperDefinitionField.
     * @param grouperDefinitionField the new value for grouperDefinitionField.
     */
    public void setGrouperDefinitionField(final String grouperDefinitionField) {
        this.grouperDefinitionField = grouperDefinitionField;
    }

    /**
     * Getter for createGrouperType.
     * @return createGrouperType.
     */
    public boolean getCreateGrouperType() {
        return createGrouperType;
    }

    /**
     * Setter for createGrouperType.
     * @param createGrouperType the new value for createGrouperType.
     */
    public void setCreateGrouperType(final boolean createGrouperType) {
        this.createGrouperType = createGrouperType;
    }

    /**
     * Getter for checkMembersOnStartup.
     * @return checkMembersOnStartup.
     */
    public boolean getCheckMembersOnStartup() {
        return checkMembersOnStartup;
    }

    /**
     * Setter for checkMembersOnStartup.
     * @param checkMembersOnStartup the new value for checkMembersOnStartup.
     */
    public void setCheckMembersOnStartup(final boolean checkMembersOnStartup) {
        this.checkMembersOnStartup = checkMembersOnStartup;
    }

    /**
     * Getter for grouperSubjectsSourceId.
     * @return grouperSubjectsSourceId.
     */
    public String getGrouperSubjectsSourceId() {
        return grouperSubjectsSourceId;
    }

    /**
     * Setter for grouperSubjectsSourceId.
     * @param grouperSubjectsSourceId the new value for grouperSubjectsSourceId.
     */
    public void setGrouperSubjectsSourceId(final String grouperSubjectsSourceId) {
        this.grouperSubjectsSourceId = grouperSubjectsSourceId;
    }
}
