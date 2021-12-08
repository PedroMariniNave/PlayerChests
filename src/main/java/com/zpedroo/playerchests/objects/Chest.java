package com.zpedroo.playerchests.objects;

import com.zpedroo.playerchests.utils.serialization.Base64Encoder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.Arrays;

public class Chest implements Serializable {

    private String name;
    private Material icon;
    private boolean glowIcon;
    private String serializedInventory;
    private long lastModificationInMillis;

    public Chest(String name, Material icon, boolean glowIcon, String serializedInventory, long lastModificationInMillis) {
        this.name = name;
        this.icon = icon;
        this.glowIcon = glowIcon;
        this.serializedInventory = serializedInventory;
        this.lastModificationInMillis = lastModificationInMillis;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public boolean isGlowIcon() {
        return glowIcon;
    }

    public Inventory getInventory() {
        Inventory inventory = null;
        try {
            inventory = Base64Encoder.fromBase64(serializedInventory);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (inventory == null) return null;

        Inventory newInventory = Bukkit.createInventory(null, inventory.getSize(), name);
        for (int slot = 0; slot < inventory.getSize(); ++slot) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType().equals(Material.AIR)) continue;

            newInventory.setItem(slot, item);
        }

        return newInventory;
    }

    public long getLastModificationInMillis() {
        return lastModificationInMillis;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public void setGlowIcon(boolean glowIcon) {
        this.glowIcon = glowIcon;
    }

    public void setInventory(Inventory inventory) {
        this.serializedInventory = Base64Encoder.toBase64(inventory);
        this.lastModificationInMillis = System.currentTimeMillis();
    }

    public int getItemsAmount() {
        return (int) Arrays.stream(getInventory().getContents()).filter(item -> item != null && item.getType() != Material.AIR).count();
    }
}