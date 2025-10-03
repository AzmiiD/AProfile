package io.github.AzmiiD.aProfile.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.AzmiiD.aProfile.AProfile;
import io.github.AzmiiD.aProfile.utils.MessageUtils;

public class StatsCommand implements CommandExecutor {
    private final AProfile plugin;

    public StatsCommand(AProfile plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&cOnly players can use this command!"));
            return true;
        }

        // Handle target player (if specified)
        Player target = player;
        if (args.length > 0) {
            if (!player.hasPermission("betterprofile.admin")) {
                player.sendMessage(MessageUtils.colorize("&cYou don't have permission to view other players!"));
                return true;
            }

            target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(MessageUtils.colorize("&cPlayer not found!"));
                return true;
            }
        }

        plugin.getMenuManager().openStatsMenu(player, target);
        return true;
    }
}