package io.github.AzmiiD.aProfile.listeners;

import io.github.AzmiiD.aProfile.AProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuClickListener implements Listener {
    private final AProfile plugin;

    public MenuClickListener(AProfile plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        String title = event.getView().getTitle();
        if (!title.contains("Profile")) return; // cek lebih fleksibel

        // Prevent all clicks in the stats menu
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        String title = event.getView().getTitle();
        if (!title.contains("Profile")) return;

        // Prevent dragging items in the stats menu
        event.setCancelled(true);
    }
}
