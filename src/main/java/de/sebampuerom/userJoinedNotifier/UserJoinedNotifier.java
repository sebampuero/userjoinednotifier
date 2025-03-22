package de.sebampuerom.userJoinedNotifier;

import de.sebampuerom.userJoinedNotifier.config.Config;
import de.sebampuerom.userJoinedNotifier.listener.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class UserJoinedNotifier extends JavaPlugin {

    public final Logger logger = this.getLogger();
    public final Config config = new Config(this.getDataFolder());
    public static final String NAME = "UserJoinedNotifier";

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        if(!config.configPresent()){
            logger.info("config.yml not present. Please edit config-default.yml to your liking, save as config.yml and restart the server!");
            config.saveConfiguration(this, Config.CONFIG_DEFAULT_FILENAME);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(config), this);
    }

    @Override
    public void onDisable() {
    }
}
