
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
import org.esco.dynamicgroups.domain.beans.ESCODynamicGroupsParameters;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Util class to send mails.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public class Mailer implements InitializingBean, IMailer {

    /** Content type for the messages. */
    private static final String TEXT_HTML_CONTENT_TYPE = "text/html";

    /** SMTP host property. */
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    
    /** Separator for the field charset in the content type.*/
    private static final char CHARSET_SEP = ';';
    
    /** Charset name. */
    private static final String CHARSET_FIELD = "charset=";
    
    /** Property used to specify the mailer. */
    private static final String X_MAILER = "X-Mailer";

    /** Constant for the smtp protocol. */
    private static final String SMTP_PROTOCOL = "smtp";

    /** Constant for the  header du mail. */
    private static final String MAILER_FIELD = "JavaMail";

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(Mailer.class);
    
    /** The user parameters. */
    private ESCODynamicGroupsParameters parameters;

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
    
    /** The charset to use for the mails. */
    private String charset;
    
    /** Flag to determine if the messages should be send in xhtml.*/
    private boolean xhtml;
    
    

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
        Assert.notNull(this.parameters, 
                "The property parameters in the class " + this.getClass().getName() 
                + " can't be null.");
        setSmtpHost(parameters.getSmtpHost());
        setFromField(parameters.getFromField());
        setToField(parameters.getToField());
        setSubjectPrefix(parameters.getSubjectPrefix());
        setCharset(parameters.getMailCharset());
        setXhtml(parameters.getXHTMLReport());
        if (parameters.isAuthenticatedSMTPHost()) {
            LOGGER.debug("Authenticated SMTP.");
            setSmtpUser(parameters.getSmtpUser());
            setSmtpPassword(parameters.getSmtpPassword());
        }
    }

    /**
     * Notification of an exception.
     * @param exception The exception.
     * @see org.esco.dynamicgroups.util.IMailer#sendExeceptionNotification(Throwable)
     */
    public void sendExeceptionNotification(final Throwable exception) {


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



        sendMailInternal(exception.getClass().getSimpleName(), sb.toString(), false);
    }

    /**
     * Sends a mail.
     * @param subjectField The subject of the mail.
     * @param messageContent The content of the message.
     * @see org.esco.dynamicgroups.util.IMailer#sendMail(java.lang.String, java.lang.String)
     */
    public void sendMail(final String subjectField, final String messageContent) {
        sendMailInternal(subjectField, messageContent, xhtml);
    }
    
    /**
     * Sends a mail.
     * @param subjectField The subject of the mail.
     * @param messageContent The content of the message.
     * @param xhtmlFlag flag to determine if the message contains whtml tags.
     * @see org.esco.dynamicgroups.util.IMailer#sendMail(java.lang.String, java.lang.String)
     */
    public void sendMailInternal(final String subjectField, final String messageContent, final boolean xhtmlFlag) {
        
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
                if (xhtmlFlag) {
                    message.setContent(messageContent, TEXT_HTML_CONTENT_TYPE + CHARSET_SEP + getCharset());
                } else {
                    message.setText(messageContent);
                }
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


    /**
     * Getter for parameters.
     * @return parameters.
     */
    public ESCODynamicGroupsParameters getParameters() {
        return parameters;
    }


    /**
     * Setter for parameters.
     * @param parameters the new value for parameters.
     */
    public void setParameters(final ESCODynamicGroupsParameters parameters) {
        this.parameters = parameters;
    }
    /**
     * Getter for charset.
     * @return charset.
     */
    public String getCharset() {
        return charset;
    }


    /**
     * Setter for charset.
     * @param charset the new value for charset.
     */
    public void setCharset(final String charset) {
        this.charset = charset.trim();
        if (!this.charset.toLowerCase().startsWith(CHARSET_FIELD)) {
            this.charset = CHARSET_FIELD + charset;
        }
    }


    /**
     * Getter for xhtml.
     * @return xhtml.
     */
    public boolean isXhtml() {
        return xhtml;
    }


    /**
     * Setter for xhtml.
     * @param xhtml the new value for xhtml.
     */
    public void setXhtml(final boolean xhtml) {
        this.xhtml = xhtml;
    }
    
//    public static void main(final String args[]) {
//        Mailer mailer = new Mailer();
//        mailer.setSmtpHost("smtp.giprecia.net");
//        mailer.setFromField("noreply@recia.fr");
//        mailer.setToField("arnaud.deman@recia.fr");
//        mailer.setSubjectPrefix("[test mail]");
//        mailer.setCharset("utf8");
//        final String content = "\"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\""
//            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
//            + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title/>"
//            + "</head>"
//            + "<body><h1>Groupes dynamiques - Statisitiques </h1><br/><br/>"
//            + "Rapport Généré le : 05/05/2009::01:32:00<br/><br/><br/><b>Nombre de définitions modifiées : </b>0<br/>"
//            + "<b>Notifications protocole SyncRepl : </b>0 - 0 - 0 - 0<br/></body></html>"
//            + "<H1>test</H1></body></html>";
//        mailer.sendMail(" xxx ", content);
//        
//        
//    }


   

}
