package entities;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mailing {


        public static void sendEmail(String recipient, String subject, String body) {
            // Email sender configuration
            String senderEmail = "malekfeki18@gmail.com"; // email address
            String senderPassword = "ozgm ipxf foxo uplz"; // email password
            String smtpHost = "smtp.gmail.com"; // SMTP server host
            int smtpPort = 587; // SMTP server port (e.g., 587 for TLS)

            // Email properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);

            // Create a session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            try {
                // Create a MIME message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(subject);
                message.setText(body);

                // Send the email
                Transport.send(message);

                System.out.println("Email sent successfully.");
            } catch (MessagingException e) {
                System.err.println("Error sending email: " + e.getMessage());
            }
        }
    public static void sendEmailWithAttachment(String recipientEmail, String subject, Multipart multipart) {
        // Email configuration
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String senderEmail = "malekfeki18@gmail.com";
        String senderPassword = "ozgm ipxf foxo uplz";

        // Get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender email address
            message.setFrom(new InternetAddress(senderEmail));

            // Set the recipient email address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Set the email subject
            message.setSubject(subject);

            // Set the multipart content with attachment
            message.setContent(multipart);

            // Send the email
            Transport.send(message);

            System.out.println("Email with attachment sent successfully to: " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

        public static void main(String[] args) {
            String recipient = "recipient@example.com"; // Recipient's email address
            String subject = "Test Email";
            String body = "This is a test email sent using JavaMail API.";

            // Send the email
            sendEmail(recipient, subject, body);
        }

    }


