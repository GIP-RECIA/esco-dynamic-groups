/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;


import java.util.Properties;

import org.apache.log4j.Logger;



/**
 * User parameters section for the mail.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public class MailParametersSection extends DGParametersSection {

    /** Serial versin UID.*/
    private static final long serialVersionUID = 7919378955466856037L;
    
    /** Default cahrset value. */
    private static final String DEFAULT_CHARSET = "utf-8";
    
    /** Default value for the flag that disables the mail. */
    private static final Boolean DEFAULT_MAIL_DISABLED = false;  
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(MailParametersSection.class);
    
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
     * Constructor for MailParametersSection.
     */
    private MailParametersSection() {
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
        final String mailDisabledKey = PROPERTIES_PREFIX + "mail.disabled";
        final String mailSMTPKey = PROPERTIES_PREFIX + "mail.smtp";
        final String mailSmtpUserKey = PROPERTIES_PREFIX + "mail.smtp.user"; 
        final String mailSmtpPasswdKey = PROPERTIES_PREFIX + "mail.smtp.password";
        final String mailSubjPrefixKey = PROPERTIES_PREFIX + "mail.subject.prefix";
        final String mailToKey = PROPERTIES_PREFIX + "mail.to";
        final String mailFromKey = PROPERTIES_PREFIX + "mail.from";
        final String mailCharsetKey = PROPERTIES_PREFIX + "mail.charset";

        // Retrieves the values.
       
        setMailDisabled(parseBooleanFromPropertySafe(params, mailDisabledKey, DEFAULT_MAIL_DISABLED));
        if (!isMailDisabled()) {
            setSmtpHost(parseStringFromProperty(params, mailSMTPKey));
            setSubjectPrefix(parseStringFromPropertySafe(params, mailSubjPrefixKey, ""));
            setToField(parseStringFromProperty(params, mailToKey));
            setFromField(parseStringFromProperty(params, mailFromKey));
            setSmtpUser(parseStringFromPropertySafe(params, mailSmtpUserKey, ""));
            setMailCharset(parseStringFromPropertySafe(params, mailCharsetKey, DEFAULT_CHARSET));
           
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
        final StringBuilder sb = new StringBuilder();
        toStringFormatSingleEntry(sb, getClass().getSimpleName() + "#{\n");
        toStringFormatProperty(sb, "Mail disabled: ", isMailDisabled());
        if (!isMailDisabled()) {
            toStringFormatProperty(sb, "SMTP Server: ", getSmtpHost());
            if (isAuthenticatedSMTPHost()) {
                toStringFormatProperty(sb, "User: ", getSmtpUser());
                toStringFormatProperty(sb,  "SMTP Password: ", toStringFormatPassword(getSmtpPassword()));
            }
            toStringFormatProperty(sb, "Mail to: ", getToField());
            toStringFormatProperty(sb, "Mail from: ", getFromField());
            toStringFormatProperty(sb, "Subject prefix: ", getSubjectPrefix());
            
        }
        toStringFormatSingleEntry(sb, "}");
        return sb.toString();
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


}
