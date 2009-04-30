package org.esco.dynamicgroups.util;

/**
 * Interface for the mailer classes.
 * @author GIP RECIA - A. Deman
 * 30 avr. 2009
 *
 */
public interface IMailer {

    /**
     * Sends the message associated to an exception.
     * @param e The exception.
     */
    void sendExeceptionNotification(final Exception e);

    /**
     * Sends a mail.
     * @param subjectField The subject of the mail.
     * @param messageContent The content of the message.
     */
    void sendMail(final String subjectField, final String messageContent);
}