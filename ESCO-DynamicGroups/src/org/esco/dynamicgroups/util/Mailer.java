
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
import org.esco.dynamicgroups.domain.parameters.CommonsParametersSection;
import org.esco.dynamicgroups.domain.parameters.MailParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
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
    private static final String CHARSET_FIELD_PREFIX = "charset=";
    
    /** Property used to specify the mailer. */
    private static final String X_MAILER = "X-Mailer";

    /** Constant for the smtp protocol. */
    private static final String SMTP_PROTOCOL = "smtp";

    /** Constant for the  header du mail. */
    private static final String MAILER_FIELD = "JavaMail";

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(Mailer.class);
    
    /** The user parameters provider. */
    private ParametersProvider parametersProvider;
    
    /** The user parameters for the mail. */
    private MailParametersSection mailParameters;
    
    /** The common paramters. */
    private CommonsParametersSection commonsParameters;
    
    /** The charset field. */
    private String charsetField;

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
        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " + this.getClass().getName() 
                + " can't be null.");
        
        mailParameters = (MailParametersSection) parametersProvider.getMailParametersSection();
        commonsParameters = (CommonsParametersSection) parametersProvider.getCommonsParametersSection();
        charsetField = mailParameters.getMailCharset().trim();
        if (!charsetField.toLowerCase().startsWith(CHARSET_FIELD_PREFIX)) {
            charsetField = CHARSET_FIELD_PREFIX + charsetField;
        }
//        setSmtpHost(parametersProvider.getSmtpHost());
//        setFromField(parametersProvider.getFromField());
//        setToField(parametersProvider.getToField());
//        setSubjectPrefix(parametersProvider.getSubjectPrefix());
//        setCharset(parametersProvider.getMailCharset());
//        setXhtml(parametersProvider.getXHTMLReport());
//        if (parametersProvider.isAuthenticatedSMTPHost()) {
//            LOGGER.debug("Authenticated SMTP.");
//            setSmtpUser(parametersProvider.getSmtpUser());
//            setSmtpPassword(parametersProvider.getSmtpPassword());
//        }
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
        sendMailInternal(subjectField, messageContent, commonsParameters.getXHTML());
    }
    
    /**
     * Sends a mail.
     * @param subjectField The subject of the mail.
     * @param messageContent The content of the message.
     * @param xhtmlFlag flag to determine if the message contains whtml tags.
     * @see org.esco.dynamicgroups.util.IMailer#sendMail(java.lang.String, java.lang.String)
     */
    public void sendMailInternal(final String subjectField, final String messageContent, final boolean xhtmlFlag) {
        
        if (mailParameters.isMailDisabled()) {
            LOGGER.info("Mails are disabled.");
        } else {
            try {
                Properties properties = System.getProperties();
                properties.put(MAIL_SMTP_HOST, mailParameters.getSmtpHost());
                Session session = Session.getDefaultInstance(properties, null);
                
                Message message = new MimeMessage(session);
                
                message.setFrom(new InternetAddress(mailParameters.getFromField()));
                message.setRecipients(Message.RecipientType.TO, 
                        InternetAddress.parse(mailParameters.getToField(), false));
                message.setSubject(mailParameters.getSubjectPrefix() + subjectField);
                if (xhtmlFlag) {
                    message.setContent(messageContent, 
                            TEXT_HTML_CONTENT_TYPE + CHARSET_SEP + charsetField);
                } else {
                    message.setText(messageContent);
                }
                message.setHeader(X_MAILER, MAILER_FIELD);
                message.setSentDate(new Date());
                
                if (mailParameters.getSmtpUser() != null) {
                    Transport transport = session.getTransport(SMTP_PROTOCOL);
                    transport.connect(mailParameters.getSmtpHost(), 
                            mailParameters.getSmtpUser(), 
                            mailParameters.getSmtpPassword());
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
     * Getter for parametersProvider.
     * @return parametersProvider.
     */
    public ParametersProvider getParametersProvider() {
        return parametersProvider;
    }


    /**
     * Setter for parametersProvider.
     * @param parametersProvider the new value for parametersProvider.
     */
    public void setParametersProvider(final ParametersProvider parametersProvider) {
        this.parametersProvider = parametersProvider;
    }
   

}
