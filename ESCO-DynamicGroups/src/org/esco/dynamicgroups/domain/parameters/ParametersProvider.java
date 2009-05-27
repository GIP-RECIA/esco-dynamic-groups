/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;


import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;



/**
 * Contains the user parameters.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public class ParametersProvider extends DGParametersSection {

       
    /** Default cahrset value. */
    private static final String DEFAULT_CHARSET = "utf-8";

    /** Serial version UID. */
    private static final long serialVersionUID = -186884409502387377L;

    /** Prefix for the properties. */
    private static final String PROPERTIES_PREFIX = "esco.dynamic.groups.";

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ParametersProvider.class);

    /** Singleton instance. */
    private static ParametersProvider instance;
    
    /** Section for the parameters for the person backend. */
    private DGParametersSection personsParametersSection;
    
    /** Section for the mail parameters. */
    private DGParametersSection mailParametersSection;

    /** Properties instance used to initialize this instance. */
    private Properties parametersProperties;

    /** The id of the subject source. */
    private String grouperSubjectsSourceId;

    /** The type in grouper associated to the dynamic groups. */
    private String grouperType;

    /** Flag to determine if the dynamic grouper type should be created
     * if it does not exist. */
    private boolean createGrouperType;

    /** Flag to determine if the dynamic groups should be reseted. */
    private boolean resetOnStartup;

    /** The field used in grouper to define the members. */
    private String grouperDefinitionField;

    /** The Grouper user used to open the Grouper sessions. */
    private String grouperUser;

    /** Flag used to detemine if a deleted user should be removed from
     * all the groups or only from the dynamic ones.*/
    private boolean removeFromAllGroups;

    /** The locale to use. */
    private Locale locale;

    /** Flag to determine the format of the report. */
    private boolean xHTMLReport;
    
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
    
    /** SMTP server.*/
    private String smtpHost;

    /** From field of the mail. */
    private String fromField;

    /** To field of the mail. */
    private String toField;

    /** Login for the smtp server. */
    private String smtpUser;

    /** Password for the smtp server. */
    private String smtpPassword;

    /** Prefix to use for the subjects.*/
    private String subjectPrefix = "";

    /** Flag to disable the mails. */
    private boolean mailDisabled;
    
    /** The mails charset. */
    private String mailCharset;

    /**
     * Constructor for ParametersProvider.
     * @param configurationFile The configuration file to use.
     * @param personParametersSection The parameters section associated to the persons backend.
     * @param mailParametersSection the parameters for the mailer.
     */
    private ParametersProvider(final String configurationFile, 
            final DGParametersSection personParametersSection,
            final DGParametersSection mailParametersSection) {
        setConfigurationFile(configurationFile);
        this.personsParametersSection = personParametersSection;
        this.personsParametersSection.setConfigurationFile(configurationFile);
        
        this.mailParametersSection = mailParametersSection;
        this.mailParametersSection.setConfigurationFile(configurationFile);
                
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loading ParametersProvider from file " + configurationFile);
        }

        initialize();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loaded values: " + this);
        }
        setInstance(this);
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
     * Gives the singleton.
     * @return The singleton.
     */
    public static ParametersProvider instance() {
        return instance;
    }

    /**
     * Initialization.
     */
    private void initialize() {
        try {
            final ClassLoader cl = getClass().getClassLoader();
            final InputStream is  = cl.getResourceAsStream(getConfigurationFile());
            final Properties params = new Properties();
            if (is == null) {
                LOGGER.fatal("Unable to load (from classpath) " + getConfigurationFile());
            }


            params.load(is);
            loadFromProperties(params);
            setParametersProperties(params);

        } catch (InvalidPropertiesFormatException e) {
            LOGGER.error(e, e);
        } catch (IOException e) {
            LOGGER.error(e, e);
        }
    }

    /**
     * Loads all the parameter sections.
     * @param params The properties that contains the values to load.
     */
    @Override
    public void loadFromProperties(final Properties params) {
        
        // Loads the parameters for the persons backend.
        personsParametersSection.loadFromProperties(params);
        
        // Loads the mailer paramters.
        mailParametersSection.loadFromProperties(params);
        
        // keys used to retrieve the values in the properties instance. 
        final String grouperTypeKey = PROPERTIES_PREFIX + "grouper.type";
        final String grouperSubjSourceKey = PROPERTIES_PREFIX + "grouper.subjects.source";
        final String createGrouperTypeKey = PROPERTIES_PREFIX + "grouper.create.type";
        final String resetOnStartupKey = PROPERTIES_PREFIX + "grouper.reset.on.startup";
        final String grouperDefKey = PROPERTIES_PREFIX + "grouper.definiton.field";
        final String grouperUserKey = PROPERTIES_PREFIX + "grouper.user";
        final String removeFromAllGroupsKey = PROPERTIES_PREFIX + "grouper.remove.from.all.groups";
        final String localeKey = PROPERTIES_PREFIX + "locale";

        final String xHTMLReportKey = PROPERTIES_PREFIX + "report.xhtml.format";
        final String reportCronExprKey = PROPERTIES_PREFIX + "report.cron.expression";
        
        final String countDefModKey = PROPERTIES_PREFIX + "stats.handle.definition.modifications";
        final String countSyncReplKey = PROPERTIES_PREFIX + "stats.handle.syncrepl.notifications";
        final String countGroupKey = PROPERTIES_PREFIX + "stats.handle.groups";
        final String countUndefGroupKey = PROPERTIES_PREFIX + "stats.handle.groups.undefined";
        final String countGroupActivityKey = PROPERTIES_PREFIX + "stats.handle.groups.activity";
        
        final String mailDisabledKey = PROPERTIES_PREFIX + "mail.disabled";
        final String mailSMTPKey = PROPERTIES_PREFIX + "mail.smtp";
        final String mailSmtpUserKey = PROPERTIES_PREFIX + "mail.smtp.user"; 
        final String mailSmtpPasswdKey = PROPERTIES_PREFIX + "mail.smtp.password";
        final String mailSubjPrefixKey = PROPERTIES_PREFIX + "mail.subject.prefix";
        final String mailToKey = PROPERTIES_PREFIX + "mail.to";
        final String mailFromKey = PROPERTIES_PREFIX + "mail.from";
        final String mailCharsetKey = PROPERTIES_PREFIX + "mail.charset";

        // Retrieves the values.
        setGrouperSubjectsSourceId(parseStringFromProperty(params, grouperSubjSourceKey));
        setGrouperType(parseStringFromProperty(params, grouperTypeKey));
        setCreateGrouperType(parseBooleanFromProperty(params, createGrouperTypeKey)); 
        setResetOnStartup(parseBooleanFromProperty(params, resetOnStartupKey)); 
        setGrouperDefinitionField(parseStringFromProperty(params, grouperDefKey));
        setGrouperUser(parseStringFromProperty(params, grouperUserKey));
        setRemoveFromAllGroups(parseBooleanFromProperty(params, removeFromAllGroupsKey)); 
        setLocale(new Locale(parseStringFromProperty(params, localeKey)));

        setXHTMLReport(parseBooleanFromProperty(params, xHTMLReportKey));
        setReportCronExpression(parseStringFromProperty(params, reportCronExprKey));
        setCountDefinitionModifications(parseBooleanFromProperty(params, countDefModKey));
        setCountSyncReplNotifications(parseBooleanFromProperty(params, countSyncReplKey));
        setCountGroupCreationDeletion(parseBooleanFromProperty(params, countGroupKey));
        setCountUndefiedGroups(parseBooleanFromProperty(params, countUndefGroupKey));
        setCountGroupsActivity(parseBooleanFromProperty(params, countGroupActivityKey));
        
        setMailDisabled(parseBooleanFromProperty(params, mailDisabledKey));
        if (!isMailDisabled()) {
            setSmtpHost(parseStringFromProperty(params, mailSMTPKey));
            setSubjectPrefix(parseStringSafeFromProperty(params, mailSubjPrefixKey, ""));
            setToField(parseStringFromProperty(params, mailToKey));
            setFromField(parseStringFromProperty(params, mailFromKey));
            setSmtpUser(parseStringSafeFromProperty(params, mailSmtpUserKey, ""));
            setMailCharset(parseStringSafeFromProperty(params, mailCharsetKey, DEFAULT_CHARSET));
            if (isAuthenticatedSMTPHost()) {
                setSmtpPassword(parseStringFromProperty(params, mailSmtpPasswdKey));
            }
        }
    }

   
    /**
     * Gives the string representation of the instance.
     * @return The String that denotes the values of the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append("#{");
        sb.append("Persons section: ");
        sb.append(personsParametersSection);
        
        sb.append("locale");
        sb.append(getLocale());
        sb.append("; mail disabled: ");
        sb.append(isMailDisabled());
        if (!isMailDisabled()) {
            sb.append("; smtp: ");
            sb.append(getSmtpHost());
            if (isAuthenticatedSMTPHost()) {
                sb.append("; smtp user: ");
                sb.append(getSmtpUser());
                sb.append("; smtp password: ");
                sb.append(getSmtpPassword().replaceAll(".", "\\*"));
            }
            sb.append("; mail to: ");
            sb.append(getToField());
            sb.append("; mail from: ");
            sb.append(getFromField());
            sb.append("; subject prefix: ");
            sb.append(getSubjectPrefix());
        }


        sb.append("; xhtml report: ");
        sb.append(getXHTMLReport());
        sb.append("; report cron expression: ");
        sb.append(getReportCronExpression());
        sb.append("; count def mods: ");
        sb.append(getCountDefinitionModifications());

        sb.append("; Grouper Subjects source: ");
        sb.append(getGrouperSubjectsSourceId());

        sb.append("; Grouper Type: ");
        sb.append(getGrouperType());

        sb.append("; Grouper definition field: ");
        sb.append(getGrouperDefinitionField());

        sb.append("; Create grouper type (if not present): ");
        sb.append(getCreateGrouperType());

        sb.append("; Reset dynamic groups on startup: ");
        sb.append(getResetOnStartup());

        sb.append("; Grouper user: ");
        sb.append(getGrouperUser());

        sb.append("; remove from all groups: ");
        sb.append(getRemoveFromAllGroups());

        sb.append("}");
        return sb.toString();
    }

    /**
     * Getter for parametersProperties.
     * @return the parametersProperties
     */
    public Properties getParametersProperties() {
        return parametersProperties;
    }

    /**
     * Setter for parametersProperties.
     * @param parametersProperties the parametersProperties to set
     */
    public void setParametersProperties(final Properties parametersProperties) {
        this.parametersProperties = parametersProperties;
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
     * Getter for resetOnStartup.
     * @return resetOnStartup.
     */
    public boolean getResetOnStartup() {
        return resetOnStartup;
    }

    /**
     * Setter for resetOnStartup.
     * @param resetOnStartup the new value for resetOnStartup.
     */
    public void setResetOnStartup(final boolean resetOnStartup) {
        this.resetOnStartup = resetOnStartup;
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

   
    /**
     * Getter for instance.
     * @return instance.
     */
    protected static synchronized ParametersProvider getInstance() {
        return instance;
    }

    /**
     * Setter for instance.
     * @param instance the new value for instance.
     */
    protected static synchronized void setInstance(final ParametersProvider instance) {
        ParametersProvider.instance = instance;
    }

    /**
     * Getter for locale.
     * @return locale.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Setter for locale.
     * @param locale the new value for locale.
     */
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * Getter for xHTMLReport.
     * @return xHTMLReport.
     */
    public boolean getXHTMLReport() {
        return xHTMLReport;
    }

    /**
     * Setter for xHTMLReport.
     * @param report the new value for xHTMLReport.
     */
    public void setXHTMLReport(final boolean report) {
        xHTMLReport = report;
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
     * Getter for smtpHost.
     * @return smtpHost.
     */
    public String getSmtpHost() {
        return smtpHost;
    }

    /**
     * Setter for smtpHost.
     * @param smtpHost the new value for smtpHost.
     */
    public void setSmtpHost(final String smtpHost) {
        this.smtpHost = smtpHost;
    }

    /**
     * Getter for fromField.
     * @return fromField.
     */
    public String getFromField() {
        return fromField;
    }

    /**
     * Setter for fromField.
     * @param fromField the new value for fromField.
     */
    public void setFromField(final String fromField) {
        this.fromField = fromField;
    }

    /**
     * Getter for toField.
     * @return toField.
     */
    public String getToField() {
        return toField;
    }

    /**
     * Setter for toField.
     * @param toField the new value for toField.
     */
    public void setToField(final String toField) {
        this.toField = toField;
    }

    /**
     * Getter for smtpUser.
     * @return smtpUser.
     */
    public String getSmtpUser() {
        return smtpUser;
    }

    /**
     * Setter for smtpUser.
     * @param smtpUser the new value for smtpUser.
     */
    public void setSmtpUser(final String smtpUser) {
        this.smtpUser = smtpUser;
    }

    /**
     * Getter for smtpPassword.
     * @return smtpPassword.
     */
    public String getSmtpPassword() {
        return smtpPassword;
    }

    /**
     * Setter for smtpPassword.
     * @param smtpPassword the new value for smtpPassword.
     */
    public void setSmtpPassword(final String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    /**
     * Getter for subjectPrefix.
     * @return subjectPrefix.
     */
    public String getSubjectPrefix() {
        return subjectPrefix;
    }

    /**
     * Setter for subjectPrefix.
     * @param subjectPrefix the new value for subjectPrefix.
     */
    public void setSubjectPrefix(final String subjectPrefix) {
        this.subjectPrefix = subjectPrefix;
    }

    /**
     * Getter for mailDisabled.
     * @return mailDisabled.
     */
    public boolean isMailDisabled() {
        return mailDisabled;
    }

    /**
     * Setter for mailDisabled.
     * @param mailDisabled the new value for mailDisabled.
     */
    public void setMailDisabled(final boolean mailDisabled) {
        this.mailDisabled = mailDisabled;
    }

    /**
     * Test if the smtp server needs an authetication.
     * @return True if the smtp is authenticated.
     */
    public boolean isAuthenticatedSMTPHost() {
        return !"".equals(smtpUser);
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
     * Getter for mailCharset.
     * @return mailCharset.
     */
    public String getMailCharset() {
        return mailCharset;
    }

    /**
     * Setter for mailCharset.
     * @param mailCharset the new value for mailCharset.
     */
    public void setMailCharset(final String mailCharset) {
        this.mailCharset = mailCharset;
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
     * Getter for personsParametersSection.
     * @return personsParametersSection.
     */
    public DGParametersSection getPersonsParametersSection() {
        return personsParametersSection;
    }

    /**
     * Setter for personsParametersSection.
     * @param personsParametersSection the new value for personsParametersSection.
     */
    public void setPersonsParametersSection(final DGParametersSection personsParametersSection) {
        this.personsParametersSection = personsParametersSection;
    }

    /**
     * Getter for mailParametersSection.
     * @return mailParametersSection.
     */
    public DGParametersSection getMailParametersSection() {
        return mailParametersSection;
    }

    /**
     * Setter for mailParametersSection.
     * @param mailParametersSection the new value for mailParametersSection.
     */
    public void setMailParametersSection(final DGParametersSection mailParametersSection) {
        this.mailParametersSection = mailParametersSection;
    }
}
