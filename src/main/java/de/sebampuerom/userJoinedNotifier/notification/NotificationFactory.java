package de.sebampuerom.userJoinedNotifier.notification;

import de.sebampuerom.userJoinedNotifier.UserJoinedNotifier;
import de.sebampuerom.userJoinedNotifier.config.Config;
import de.sebampuerom.userJoinedNotifier.exceptions.EnvironmentVariableNotDefinedException;
import de.sebampuerom.userJoinedNotifier.exceptions.InvalidNotificationType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class NotificationFactory {

    private final Config config;
    private final Logger logger = Bukkit.getPluginManager().getPlugin(UserJoinedNotifier.NAME).getLogger();
    private NotificationFields fields;

    public NotificationFactory(Config config, NotificationFields fields) {
        this.config = config;
        this.fields = fields;
    }

    public List<Notification> createNotifications() {
        List<Notification> notifications = new ArrayList<>();

        ConfigurationSection configurationSection = config.getConfig().getConfigurationSection("notification");
        for(String notificationType :  configurationSection.getKeys(false)){
            ConfigurationSection typeSection = configurationSection.getConfigurationSection(notificationType);
            boolean enabled = Boolean.parseBoolean(typeSection.getString("enabled"));
            if(enabled){
                try{
                    notifications.add(createNotification(notificationType));
                }catch (InvalidNotificationType e){
                    logger.fine("Could not create notification " + e.getMessage());
                }catch (EnvironmentVariableNotDefinedException e){
                    logger.fine("Env var was not defined! Notification " + notificationType + " wont work!");
                    logger.fine(e.getMessage());
                }
            }
        }
        return notifications;
    }

    private Notification createNotification(String type) throws InvalidNotificationType, EnvironmentVariableNotDefinedException {
        if (type.equalsIgnoreCase("email")) {
            return new EmailNotification(config, fields);
        }
        throw new InvalidNotificationType(type + " has not been implemented yet!");
    }
}
