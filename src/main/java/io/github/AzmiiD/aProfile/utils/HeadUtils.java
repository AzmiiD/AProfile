package io.github.AzmiiD.aProfile.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class HeadUtils {

    public static ItemStack getCustomHead(String base64Texture) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        if (meta == null) return head;

        try {
            // Use reflection to create GameProfile and Property
            Class<?> gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            Class<?> propertyClass = Class.forName("com.mojang.authlib.properties.Property");

            // Create GameProfile instance
            Constructor<?> gameProfileConstructor = gameProfileClass.getConstructor(UUID.class, String.class);
            Object gameProfile = gameProfileConstructor.newInstance(UUID.randomUUID(), null);

            // Create Property instance
            Constructor<?> propertyConstructor = propertyClass.getConstructor(String.class, String.class);
            Object property = propertyConstructor.newInstance("textures", base64Texture);

            // Get properties field and add texture property
            Method getPropertiesMethod = gameProfile.getClass().getMethod("getProperties");
            Object properties = getPropertiesMethod.invoke(gameProfile);
            Method putMethod = properties.getClass().getMethod("put", Object.class, Object.class);
            putMethod.invoke(properties, "textures", property);

            // Set the profile field in SkullMeta
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);

            head.setItemMeta(meta);

        } catch (Exception e) {
            // If custom texture fails, silently return regular head
            // This is expected on some server versions
        }

        return head;
    }

    public static ItemStack getPlayerHead(String playerName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        if (meta != null) {
            meta.setOwner(playerName);
            head.setItemMeta(meta);
        }

        return head;
    }
}