package de.sebampuerom.userJoinedNotifier.listener;

import de.sebampuerom.userJoinedNotifier.config.Config;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private Config config;

    public PlayerJoinListener(Config config) {
        this.config = config;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String username = player.getName();
        Location location = player.getLocation();
        String serverName = config.getConfig().getString("serverName");
        String coordinates = "X:" + location.getBlockX() + " Z:" + location.getBlockZ();
        // start async task that sends notification
        // for enabledNotification in notifications:
        // create runnable (a notification should be runnable (?)
        // schedule runnable tasks
    }
}
