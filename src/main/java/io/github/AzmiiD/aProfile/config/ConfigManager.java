package io.github.AzmiiD.aProfile.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.AzmiiD.aProfile.AProfile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final AProfile plugin;
    private FileConfiguration config;
    private FileConfiguration messagesConfig;
    private File messagesFile;

    public ConfigManager(AProfile plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    private void loadConfigs() {
        // Save default config
        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        // Create messages config
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void reloadConfigs() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getMessage(String key) {
        return messagesConfig.getString("messages." + key, "&cMessage not found: " + key);
    }

    public String getMenuTitle() {
        return config.getString("menu.title", "&8&lѕᴛᴀᴛѕ ᴍᴇɴᴜ");
    }

    public int getMenuSize() {
        return config.getInt("menu.size", 45);
    }

    public boolean isPermissionRequired() {
        return config.getBoolean("menu.require-permission", false);
    }

    public String getRequiredPermission() {
        return config.getString("menu.required-permission", "betterprofile.use");
    }
}