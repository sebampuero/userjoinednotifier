package de.sebampuerom.userJoinedNotifier.listener;

import de.sebampuerom.userJoinedNotifier.config.Config;
import de.sebampuerom.userJoinedNotifier.notification.Notification;
import de.sebampuerom.userJoinedNotifier.notification.NotificationFactory;
import de.sebampuerom.userJoinedNotifier.notification.NotificationFields;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerJoinListener implements Listener {

    private Config config;
    private ExecutorService executorService;

    public PlayerJoinListener(Config config) {
        this.config = config;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        String username = player.getName();
        String serverName = config.getConfig().getString("serverName");
        String coordinates = "X:" + location.getBlockX() + " Z:" + location.getBlockZ();
        NotificationFactory notificationFactory = new NotificationFactory(config, new NotificationFields(username, serverName, coordinates));
        List<Notification> notifications = notificationFactory.createNotifications();
        executorService = Executors.newCachedThreadPool();
        for(Notification notification : notifications) {
            executorService.submit(notification);
        }
    }
}