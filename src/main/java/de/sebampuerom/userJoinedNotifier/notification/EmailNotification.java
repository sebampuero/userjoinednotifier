package de.sebampuerom.userJoinedNotifier.notification;

import de.sebampuerom.userJoinedNotifier.UserJoinedNotifier;
import de.sebampuerom.userJoinedNotifier.config.Config;
import de.sebampuerom.userJoinedNotifier.exceptions.EnvironmentVariableNotDefinedException;
import de.sebampuerom.userJoinedNotifier.formatting.Time;
import jakarta.mail.Authenticator;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.*;
import org.bukkit.Bukkit;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class EmailNotification extends Notification{

    private final String username;
    private final String password;
    private final String smtpAddress;
    private final String from;
    private final List<String> recipients;
    private final int port;
    private String subject;
    private String content;
    private NotificationFields fields;
    private final Logger logger = Bukkit.getPluginManager().getPlugin(UserJoinedNotifier.NAME).getLogger();

    public EmailNotification(Config config, NotificationFields fields) throws EnvironmentVariableNotDefinedException {
        String emailPrefix = "notification.email.";
        this.username = config.getConfig().getString(emailPrefix + "username");
        if(config.getConfig().getBoolean(emailPrefix + "loadFromEnv.enabled")){
            String passwordEnvVarKey = config.getConfig().getString(emailPrefix + "loadFromEnv.key");
            String envVar = System.getenv(passwordEnvVarKey);
            if(envVar == null || envVar.isEmpty()) throw new EnvironmentVariableNotDefinedException(passwordEnvVarKey + " does not exist in the system!");
            this.password = envVar;
        }else {
            this.password = config.getConfig().getString(emailPrefix + "password");
        }
        this.smtpAddress = config.getConfig().getString(emailPrefix + "serverAddress");
        this.port = config.getConfig().getInt(emailPrefix + "port");
        this.from = config.getConfig().getString(emailPrefix + "from");
        this.recipients = config.getConfig().getStringList(emailPrefix + "recipients");
        this.fields = fields;
        logger.info("Successfully configured email client " + this);
        parseSubjectAndContent(config);
    }

    private void parseSubjectAndContent(Config config){
        HashMap<String, String> placeholders = new HashMap<>();
        String timeFormat = config.getConfig().getString("time.datetime-format");
        placeholders.put("datetime", Time.formatTime(fields.getDateTime(), timeFormat));
        placeholders.put("username", fields.getUsername());
        placeholders.put("servername", fields.getServerName());
        placeholders.put("coords", fields.getCoordinates());
        subject = config.parseMessage("email.subject", placeholders);
        content = config.parseMessage("email.content", placeholders);
    }

    @Override
    public void send() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", this.smtpAddress);
        props.put("mail.smtp.port", this.port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress[] addresses = new InternetAddress[recipients.size()];
            for (int i = 0; i < recipients.size(); i++) {
                addresses[i] = new InternetAddress(recipients.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            logger.warning("Could not send email! " + e.getMessage());
        }
    }

    @Override
    public void run() {
        send();
    }

    @Override
    public String toString() {
        return "EmailNotification{" +
                "username='" + username + '\'' +
                ", password='" + (password != null ? "******" : null) + '\'' +
                ", smtpAddress='" + smtpAddress + '\'' +
                ", from='" + from + '\'' +
                ", recipients=" + recipients +
                ", port=" + port +
                '}';
    }
}
