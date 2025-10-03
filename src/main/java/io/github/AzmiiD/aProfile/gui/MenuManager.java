package io.github.AzmiiD.aProfile.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import io.github.AzmiiD.aProfile.AProfile;
import io.github.AzmiiD.aProfile.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuManager {
    private final AProfile plugin;

    public MenuManager(AProfile plugin) {
        this.plugin = plugin;
    }

    public void openStatsMenu(Player player) {
        openStatsMenu(player, player);
    }

    public void openStatsMenu(Player viewer, Player target) {
        String title = MessageUtils.colorize("&6&l" + target.getName() + "'s Profile");
        int size = 27; // Fixed 3-row GUI

        Inventory inventory = Bukkit.createInventory(null, size, title);

        // Fill borders with gray glass
        fillBorders(inventory);

        // Add menu items in clean layout
        inventory.setItem(4, createPlayerInfoItem(target));
        inventory.setItem(11, createKillsDeathsItem(target));
        inventory.setItem(12, createBlocksItem(target));
        inventory.setItem(13, createPlaytimeItem(target));
        inventory.setItem(14, createLocationItem(target));
        inventory.setItem(15, createHealthItem(target));
        inventory.setItem(22, createMoneyItem(target));

        viewer.openInventory(inventory);
    }

    private void fillBorders(Inventory inventory) {
        ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = border.getItemMeta();
        meta.setDisplayName(" ");
        border.setItemMeta(meta);

        // Top and bottom rows
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, border);
            inventory.setItem(i + 18, border);
        }
        // Sides
        inventory.setItem(9, border);
        inventory.setItem(17, border);
    }

    private ItemStack createPlayerInfoItem(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setOwningPlayer(player);
        meta.setDisplayName(MessageUtils.colorize("&e&l" + player.getName()));

        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.colorize("&7Player Information"));
        lore.add("");
        lore.add(MessageUtils.colorize("&e▸ Name: &f" + player.getName()));

        int ping = 0;
        try {
            ping = player.getPing();
        } catch (Exception ignored) {}

        lore.add(MessageUtils.colorize("&e▸ Ping: &f" + ping + "ms"));
        lore.add(MessageUtils.colorize("&e▸ World: &f" + player.getWorld().getName()));
        lore.add(MessageUtils.colorize("&e▸ Gamemode: &f" + player.getGameMode().toString()));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createKillsDeathsItem(Player player) {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(MessageUtils.colorize("&c&lCombat Stats"));

        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.colorize("&7Your combat statistics"));
        lore.add("");

        int playerKills = player.getStatistic(Statistic.PLAYER_KILLS);
        int mobKills = player.getStatistic(Statistic.MOB_KILLS);
        int deaths = player.getStatistic(Statistic.DEATHS);
        double kdr = deaths > 0 ? (double) playerKills / deaths : playerKills;

        lore.add(MessageUtils.colorize("&c▸ Player Kills: &f" + playerKills));
        lore.add(MessageUtils.colorize("&c▸ Mob Kills: &f" + mobKills));
        lore.add(MessageUtils.colorize("&c▸ Deaths: &f" + deaths));
        lore.add(MessageUtils.colorize("&c▸ K/D Ratio: &f" + String.format("%.2f", kdr)));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createBlocksItem(Player player) {
        ItemStack item = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(MessageUtils.colorize("&a&lBlock Stats"));

        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.colorize("&7Your building statistics"));
        lore.add("");

        // Get blocks mined and placed stats
        int blocksMined = 0;
        int blocksPlaced = 0;

        try {
            // Sum up all block break statistics
            for (Material material : Material.values()) {
                if (material.isBlock()) {
                    try {
                        blocksMined += player.getStatistic(Statistic.MINE_BLOCK, material);
                    } catch (Exception ignored) {}
                }
            }

            // Sum up all block place statistics
            for (Material material : Material.values()) {
                if (material.isBlock()) {
                    try {
                        blocksPlaced += player.getStatistic(Statistic.USE_ITEM, material);
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception e) {
            // Fallback values if statistics fail
        }

        lore.add(MessageUtils.colorize("&a▸ Blocks Mined: &f" + formatNumber(blocksMined)));
        lore.add(MessageUtils.colorize("&a▸ Blocks Placed: &f" + formatNumber(blocksPlaced)));
        lore.add(MessageUtils.colorize("&a▸ Total Actions: &f" + formatNumber(blocksMined + blocksPlaced)));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createPlaytimeItem(Player player) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(MessageUtils.colorize("&b&lPlaytime"));

        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.colorize("&7Time you've spent playing"));
        lore.add("");

        int playTimeTicks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        int totalHours = playTimeTicks / 72000;
        int days = totalHours / 24;
        int hours = totalHours % 24;

        lore.add(MessageUtils.colorize("&b▸ Total: &f" + totalHours + " hours"));
        lore.add(MessageUtils.colorize("&b▸ Days: &f" + days + "d " + hours + "h"));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createLocationItem(Player player) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(MessageUtils.colorize("&d&lLocation"));

        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.colorize("&7Your current position"));
        lore.add("");
        lore.add(MessageUtils.colorize("&d▸ X: &f" + player.getLocation().getBlockX()));
        lore.add(MessageUtils.colorize("&d▸ Y: &f" + player.getLocation().getBlockY()));
        lore.add(MessageUtils.colorize("&d▸ Z: &f" + player.getLocation().getBlockZ()));

        // Safely get biome name with multiple fallback methods
        String biome = "Unknown";
        try {
            // Method 1: Try NamespacedKey approach (Paper 1.21+)
            biome = player.getLocation().getBlock().getBiome().getKey().getKey();
            biome = biome.replace("_", " ");
            if (biome.length() > 0) {
                biome = Character.toUpperCase(biome.charAt(0)) + biome.substring(1);
            }
        } catch (NoSuchMethodError e1) {
            // Fallback untuk server lama
            try {
                biome = player.getLocation().getBlock().getBiome().name();
                biome = biome.replace("_", " ");
                if (biome.length() > 0) {
                    biome = Character.toUpperCase(biome.charAt(0)) + biome.substring(1);
                }
            } catch (Exception e2) {
                biome = "Unknown";
            }
        } catch (IncompatibleClassChangeError e1) {
            // Method 2: Try toString as fallback for older versions
            try {
                biome = player.getLocation().getBlock().getBiome().name();
                biome = biome.replace("_", " ");
                if (biome.length() > 0) {
                    biome = Character.toUpperCase(biome.charAt(0)) + biome.substring(1);
                }
            } catch (Exception e2) {
                // Final fallback - keep "Unknown"
                biome = "Unknown";
            }
        } catch (Exception e) {
            // Any other error - keep "Unknown"
            biome = "Unknown";
        }

        lore.add(MessageUtils.colorize("&d▸ Biome: &f" + biome));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createHealthItem(Player player) {
        ItemStack item = new ItemStack(Material.RED_DYE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(MessageUtils.colorize("&4&lHealth"));

        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.colorize("&7Your current health stats"));
        lore.add("");

        double health = Math.round(player.getHealth() * 10.0) / 10.0;
        double maxHealth = Math.round(player.getMaxHealth() * 10.0) / 10.0;
        int foodLevel = player.getFoodLevel();

        lore.add(MessageUtils.colorize("&4▸ Health: &f" + health + "/" + maxHealth + " ❤"));
        lore.add(MessageUtils.colorize("&4▸ Food: &f" + foodLevel + "/20 🍖"));
        lore.add(MessageUtils.colorize("&4▸ Level: &f" + player.getLevel()));
        lore.add(MessageUtils.colorize("&4▸ XP: &f" + Math.round(player.getExp() * 100) + "%"));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack createMoneyItem(Player player) {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(MessageUtils.colorize("&6&lEconomy"));

        List<String> lore = new ArrayList<>();
        lore.add(MessageUtils.colorize("&7Your money balance"));
        lore.add("");

        String balance = "N/A";
        if (plugin.getEconomy() != null) {
            try {
                double money = plugin.getEconomy().getBalance(player);
                balance = "$" + formatNumber((int) money);
            } catch (Exception e) {
                balance = "Error";
            }
        }

        lore.add(MessageUtils.colorize("&6▸ Balance: &f" + balance));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private String formatNumber(int number) {
        if (number >= 1000000) {
            return String.format("%.1fM", number / 1000000.0);
        } else if (number >= 1000) {
            return String.format("%.1fK", number / 1000.0);
        }
        return String.valueOf(number);
    }

}