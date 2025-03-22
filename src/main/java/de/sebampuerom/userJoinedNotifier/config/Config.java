package de.sebampuerom.userJoinedNotifier.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String parseMessage(String path, Map<String, String> placeholders){
        String prefix = "messages.";
        String textToParse = getConfig().getString(prefix + path);
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(textToParse);

        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            result.append(textToParse, lastEnd, matcher.start());
            if (placeholders.containsKey(placeholder)) {
                result.append(placeholders.get(placeholder)).append(" ");
            }
            // Update lastEnd to end of the match
            lastEnd = matcher.end();
        }
        if (lastEnd < textToParse.length()) {
            result.append(textToParse.substring(lastEnd));
        }
        return result.toString().replaceAll("\\s+", " ").trim();
    }

}
