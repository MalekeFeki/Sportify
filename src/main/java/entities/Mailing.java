package entities;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mailing {






        public static void sendEmail(String recipient, String subject, String body) {
            // Email sender configuration
            String senderEmail = "malekfeki18@gmail.com"; // Your email address
            String senderPassword = "ozgm ipxf foxo uplz"; // Your email password
            String smtpHost = "smtp.gmail.com"; // Your SMTP server host
            int smtpPort = 587; // Your SMTP server port (e.g., 587 for TLS)

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

        public static void main(String[] args) {
            String recipient = "recipient@example.com"; // Recipient's email address
            String subject = "Test Email";
            String body = "This is a test email sent using JavaMail API.";

            // Send the email
            sendEmail(recipient, subject, body);
        }
    }


