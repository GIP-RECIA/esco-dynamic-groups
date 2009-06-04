/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;


import java.util.Properties;

import org.apache.log4j.Logger;



/**
 * User parameters for the reporting.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public class ReportingParametersSection extends DGParametersSection {

     /** Serial version UID. */
    private static final long serialVersionUID = -186884409502387377L;

     /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ReportingParametersSection.class);
    
    /** Default value fot the check of the dynamic groups members. */
    private static final int DEF_MB_CHK_DUR = 0;
    
    /** The cron expression used to send the reports. */
    private String reportCronExpression;

    /** Flag to determine if the modifications of definition have to handled in 
     * the statistics. */
    private boolean countDefinitionModifications;

    /** Flag to determine if the SyncRepl notifications have to handled in 
     * the statistics. */
    private boolean countSyncReplNotifications;
    
    /** Flag for statistics about the group creation or deletion. */
    private boolean countGroupCreationDeletion;

    /** Flag to count the undefined groups. */
    private boolean countUndefiedGroups;
    
    /** Flag for the stats on the groups activities. */
    private boolean countGroupsActivity;
    
    /** Flag to determine if the missing or the invalid memebers should be counted. */
    private boolean countInvalidOrMissingMembers;
    
    /** Limit for the duration of the members checking process. */
    private int membersCheckingDuration;
    
    
    /**
     * Constructor for ReportingParametersSection.
     */
    private ReportingParametersSection() {
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
        final String reportCronExprKey = PROPERTIES_PREFIX + "reporting.cron.expression";
        final String countDefModKey = PROPERTIES_PREFIX + "reporting.handle.definition.modifications";
        final String countSyncReplKey = PROPERTIES_PREFIX + "reporting.handle.syncrepl.notifications";
        final String countGroupKey = PROPERTIES_PREFIX + "reporting.handle.groups";
        final String countUndefGroupKey = PROPERTIES_PREFIX + "reporting.handle.groups.undefined";
        final String countGroupActivityKey = PROPERTIES_PREFIX + "reporting.handle.groups.activity";
        final String countInvalidOrMissingMembKey = PROPERTIES_PREFIX + "reporting.handle.members.checking";
        final String mbCheckDurationKey = PROPERTIES_PREFIX + "reporting.handle.members.checking.duration.minutes";
      
        // Retrieves the values.
        setReportCronExpression(parseStringFromProperty(params, reportCronExprKey));
        setCountDefinitionModifications(parseBooleanFromProperty(params, countDefModKey));
        setCountSyncReplNotifications(parseBooleanFromProperty(params, countSyncReplKey));
        setCountGroupCreationDeletion(parseBooleanFromProperty(params, countGroupKey));
        setCountUndefiedGroups(parseBooleanFromProperty(params, countUndefGroupKey));
        setCountGroupsActivity(parseBooleanFromProperty(params, countGroupActivityKey));
        setCountInvalidOrMissingMembers(parseBooleanFromProperty(params, countInvalidOrMissingMembKey));
        setMembersCheckingDuration(parsePositiveIntegerSafeFromProperty(params, mbCheckDurationKey, DEF_MB_CHK_DUR));
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
        
        toStringFormatProperty(sb, "Cron expression: ", getReportCronExpression());
        toStringFormatProperty(sb, "Count SyncRepl notifications: ", getCountSyncReplNotifications());
        toStringFormatProperty(sb, "Count def mods: ", getCountDefinitionModifications());
        toStringFormatProperty(sb, "Count groups creation/deletion: ", getCountGroupCreationDeletion());
        toStringFormatProperty(sb, "Count groups activity: ", getCountGroupsActivity());
        toStringFormatProperty(sb, "Count undefined groups: ", getCountUndefiedGroups());
        toStringFormatProperty(sb, "Count invalid or missing members: ", getCountInvalidOrMissingMembers());
        toStringFormatProperty(sb, "Members checking duration: ", getMembersCheckingDuration());
        

        toStringFormatSingleEntry(sb, "}");
        return sb.toString();
    }

    /**
     * Getter for countDefinitionModifications.
     * @return countDefinitionModifications.
     */
    public boolean getCountDefinitionModifications() {
        return countDefinitionModifications;
    }

    /**
     * Setter for countDefinitionModifications.
     * @param countDefinitionModifications the new value for countDefinitionModifications.
     */
    public void setCountDefinitionModifications(final boolean countDefinitionModifications) {
        this.countDefinitionModifications = countDefinitionModifications;
    }

    /**
     * Getter for countSyncReplNotifications.
     * @return countSyncReplNotifications.
     */
    public boolean getCountSyncReplNotifications() {
        return countSyncReplNotifications;
    }

    /**
     * Setter for countSyncReplNotifications.
     * @param countSyncReplNotifications the new value for countSyncReplNotifications.
     */
    public void setCountSyncReplNotifications(final boolean countSyncReplNotifications) {
        this.countSyncReplNotifications = countSyncReplNotifications;
    }

       /**
     * Getter for reportCronExpression.
     * @return reportCronExpression.
     */
    public String getReportCronExpression() {
        return reportCronExpression;
    }

    /**
     * Setter for reportCronExpression.
     * @param reportCronExpression the new value for reportCronExpression.
     */
    public void setReportCronExpression(final String reportCronExpression) {
        this.reportCronExpression = reportCronExpression;
    }

    /**
     * Getter for countGroupCreationDeletion.
     * @return countGroupCreationDeletion.
     */
    public boolean getCountGroupCreationDeletion() {
        return countGroupCreationDeletion;
    }

    /**
     * Setter for countGroupCreationDeletion.
     * @param countGroupCreationDeletion the new value for countGroupCreationDeletion.
     */
    public void setCountGroupCreationDeletion(final boolean countGroupCreationDeletion) {
        this.countGroupCreationDeletion = countGroupCreationDeletion;
    }

    /**
     * Getter for countUndefiedGroups.
     * @return countUndefiedGroups.
     */
    public boolean getCountUndefiedGroups() {
        return countUndefiedGroups;
    }

    /**
     * Setter for countUndefiedGroups.
     * @param countUndefiedGroups the new value for countUndefiedGroups.
     */
    public void setCountUndefiedGroups(final boolean countUndefiedGroups) {
        this.countUndefiedGroups = countUndefiedGroups;
    }

    /**
     * Getter for countGroupsActivity.
     * @return countGroupsActivity.
     */
    public boolean getCountGroupsActivity() {
        return countGroupsActivity;
    }

    /**
     * Setter for countGroupsActivity.
     * @param countGroupsActivity the new value for countGroupsActivity.
     */
    public void setCountGroupsActivity(final boolean countGroupsActivity) {
        this.countGroupsActivity = countGroupsActivity;
    }

    /**
     * Getter for countInvalidOrMissingMembers.
     * @return countInvalidOrMissingMembers.
     */
    public boolean getCountInvalidOrMissingMembers() {
        return countInvalidOrMissingMembers;
    }

    /**
     * Setter for countInvalidOrMissingMembers.
     * @param countInvalidOrMissingMembers the new value for countInvalidOrMissingMembers.
     */
    public void setCountInvalidOrMissingMembers(final boolean countInvalidOrMissingMembers) {
        this.countInvalidOrMissingMembers = countInvalidOrMissingMembers;
    }

    /**
     * Getter for membersCheckingDuration.
     * @return membersCheckingDuration.
     */
    public int getMembersCheckingDuration() {
        return membersCheckingDuration;
    }

    /**
     * Setter for membersCheckingDuration.
     * @param membersCheckingDuration the new value for membersCheckingDuration.
     */
    public void setMembersCheckingDuration(final int membersCheckingDuration) {
        this.membersCheckingDuration = membersCheckingDuration;
    }
}
