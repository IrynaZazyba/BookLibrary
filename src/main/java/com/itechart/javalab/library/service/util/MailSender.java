package com.itechart.javalab.library.service.util;

import lombok.extern.log4j.Log4j2;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Log4j2
public class MailSender {

    private String username;
    private String password;
    private Properties props;

    private static final MailSender instance = new MailSender();

    public static MailSender getInstance() {
        return instance;
    }

    public void initializeSalSender(Properties props) {
        this.props = props;
        this.username = props.getProperty("mail.username");
        this.password = props.getProperty("mail.password");
    }

    private MailSender() {
    }

    public void send(String subject, String text, String toEmail) {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("MessagingException in attempt to send email", e);
        }
    }

}
