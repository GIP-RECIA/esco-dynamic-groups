
package org.esco.dynamicgroups.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

/**
 * Util class to send mails.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public class Mailer implements InitializingBean, IMailer {

    /** SMTP host property. */
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";

    /** Property used to specify the mailer. */
    private static final String X_MAILER = "X-Mailer";

    /** Constant for the smtp protocol. */
    private static final String SMTP_PROTOCOL = "smtp";

    /** Constant for the  header du mail. */
    private static final String MAILER_FIELD = "JavaMail";

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(Mailer.class);

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
    private boolean disabled;

    /**
     * Builds an instance of Mailer.
     */
    public Mailer() {
        super();
    }


    /**
     * Checks the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

        final String propertyString = "The property ";
        final String inClassString = " in the class ";
        final String mustNotBeNullString = " must not be null.";

        if (smtpHost == null) {
            throw new IllegalStateException(propertyString + smtpHost + inClassString 
                    + getClass().getSimpleName() + mustNotBeNullString);
        }
        if (fromField == null) {
            throw new IllegalStateException(propertyString + fromField + inClassString 
                    + getClass().getSimpleName() + mustNotBeNullString);
        }
        if (toField == null) {
            throw new IllegalStateException(propertyString + toField + inClassString 
                    + getClass().getSimpleName() + mustNotBeNullString);
        }
        if (smtpUser != null) {

            LOGGER.debug("Authenticated SMTP.");

            if (smtpPassword == null) {
                throw new IllegalStateException("The property  smtpPassword " 
                        + "must not be null when the property "
                        +  "smtpUser is defined.");
            }
        }
    }

    /**
     * Notification of an exception.
     * @param exception The exception.
     * @see org.esco.dynamicgroups.util.IMailer#sendExeceptionNotification(java.lang.Exception)
     */
    public void sendExeceptionNotification(final Exception exception) {


        final StringBuilder sb = new StringBuilder();
        try {
            final InetAddress address = InetAddress.getLocalHost();
            sb.append("\tHostname : ");
            sb.append(address.getHostName());
            sb.append("\n\tAddress IP : ");
            sb.append(address.getHostAddress());
            sb.append("\n\n\t---");

        } catch (UnknownHostException uhe) {
            LOGGER.error(uhe, uhe);
        }

        sb.append("\n\n\tException - class: ");
        sb.append(exception.getClass().getName());
        sb.append("\n\n\tException - Message: ");
        sb.append(exception.getMessage());
        sb.append("\n\n\tException - Stack:\n");

        for (StackTraceElement ste : exception.getStackTrace()) {
            sb.append("\t\t\t");
            sb.append(ste);
            sb.append("\n");
        }

        sb.append("\n\t---\n\n\tSystem properties:\n");
        for (Object key : System.getProperties().keySet()) {
            sb.append("\t\t\t");
            sb.append(key);
            sb.append(": ");
            sb.append(System.getProperty((String) key));
            sb.append("\n");
        }



        sendMail(exception.getClass().getSimpleName(), sb.toString());
    }

    /**
     * Sends a mail.
     * @param subjectField The subject of the mail.
     * @param messageContent The content of the message.
     * @see org.esco.dynamicgroups.util.IMailer#sendMail(java.lang.String, java.lang.String)
     */
    public void sendMail(final String subjectField, final String messageContent) {

        if (isDisabled()) {
            LOGGER.info("Mails are disabled.");
        } else {
            try {
                Properties properties = System.getProperties();
                properties.put(MAIL_SMTP_HOST, smtpHost);
                Session session = Session.getDefaultInstance(properties, null);

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromField));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toField, false));
                message.setSubject(getSubjectPrefix() + subjectField);
                message.setText(messageContent);
                message.setHeader(X_MAILER, MAILER_FIELD);
                message.setSentDate(new Date());

                if (smtpUser != null) {
                    Transport transport = session.getTransport(SMTP_PROTOCOL);
                    transport.connect(smtpHost, 
                            smtpUser, 
                            smtpPassword);
                    Transport.send(message);
                } else {
                    Transport.send(message);
                }
                LOGGER.debug("Mail " + subjectField + " sent.");
            } catch (final AddressException e) {
                LOGGER.error(e, e);
            } catch (final MessagingException e) {
                LOGGER.error(e, e);
            }
        }
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
     * Getter for disabled.
     * @return disabled.
     */
    public boolean isDisabled() {
        return disabled;
    }


    /**
     * Setter for disabled.
     * @param disabled the new value for disabled.
     */
    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

}
