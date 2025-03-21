package de.sebampuerom.userJoinedNotifier.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config {

    private YamlConfiguration config;

    public static final String CONFIG_FILENAME = "config.yml";
    public static final String CONFIG_DEFAULT_FILENAME = "config-default.yml";

    public Config(){
        loadConfig();
    }

    private void loadConfig(){
        if(configPresent()){
            config = YamlConfiguration.loadConfiguration(new File(CONFIG_FILENAME));
        }
    }

    public boolean configPresent(){
        return new File(CONFIG_FILENAME).exists();
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public void saveConfiguration(JavaPlugin plugin, String configFileName){
        plugin.saveResource(configFileName, true);
    }

}
