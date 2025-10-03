package io.github.AzmiiD.aProfile;

import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.AzmiiD.aProfile.commands.StatsCommand;
import io.github.AzmiiD.aProfile.config.ConfigManager;
import io.github.AzmiiD.aProfile.gui.MenuManager;
import io.github.AzmiiD.aProfile.listeners.MenuClickListener;
import io.github.AzmiiD.aProfile.utils.MessageUtils;

public class AProfile extends JavaPlugin {
    private static AProfile instance;
    private ConfigManager configManager;
    private MenuManager menuManager;
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        // Display ASCII art on startup
        displayStartupArt();

        // Check dependencies
        if (!checkDependencies()) {
            getLogger().severe("Required dependencies not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize components
        this.configManager = new ConfigManager(this);
        this.menuManager = new MenuManager(this);

        // Setup Vault economy
        setupEconomy();

        // Register commands and listeners
        registerCommands();
        registerListeners();

        getLogger().info("BetterProfile v" + getDescription().getVersion() + " has been enabled!");
        getLogger().info("Economy support: " + (economy != null ? "✓" : "✗"));
    }

    @Override
    public void onDisable() {
        getLogger().info("BetterProfile has been disabled!");
    }

    private boolean checkDependencies() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("PlaceholderAPI not found! Some features may be limited.");
        }
        return true;
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning("Vault not found! Economy features will be disabled.");
            return;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().warning("No economy plugin found! Economy features will be disabled.");
            return;
        }
        economy = rsp.getProvider();
    }

    private void displayStartupArt() {
        getLogger().info("██████╗ ███████╗████████╗████████╗███████╗██████╗ ██████╗ ██████╗  ██████╗ ███████╗██╗██╗     ███████╗");
        getLogger().info("██╔══██╗██╔════╝╚══██╔══╝╚══██╔══╝██╔════╝██╔══██╗██╔══██╗██╔══██╗██╔═══██╗██╔════╝██║██║     ██╔════╝");
        getLogger().info("██████╔╝█████╗     ██║      ██║   █████╗  ██████╔╝██████╔╝██████╔╝██║   ██║█████╗  ██║██║     █████╗  ");
        getLogger().info("██╔══██╗██╔══╝     ██║      ██║   ██╔══╝  ██╔══██╗██╔═══╝ ██╔══██╗██║   ██║██╔══╝  ██║██║     ██╔══╝  ");
        getLogger().info("██████╔╝███████╗   ██║      ██║   ███████╗██║  ██║██║     ██║  ██║╚██████╔╝██║     ██║███████╗███████╗");
        getLogger().info("╚═════╝ ╚══════╝   ╚═╝      ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝╚══════╝");
        getLogger().info("");
        getLogger().info("                           BetterProfile v" + getDescription().getVersion() + " by 67._._._._._.67");
        getLogger().info("");
    }

    private void registerCommands() {
        StatsCommand statsCommand = new StatsCommand(this);
        getCommand("stats").setExecutor(statsCommand);
        getCommand("profile").setExecutor(statsCommand);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new MenuClickListener(this), this);
    }

    public static AProfile getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean hasPlaceholderAPI() {
        return getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }
}