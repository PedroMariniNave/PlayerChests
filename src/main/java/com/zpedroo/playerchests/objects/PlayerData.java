package com.zpedroo.playerchests.objects;

import com.zpedroo.playerchests.utils.FileUtils;
import com.zpedroo.playerchests.utils.builder.ItemBuilder;
import com.zpedroo.playerchests.utils.serialization.Base64Encoder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.zpedroo.playerchests.utils.config.Settings.*;

public class PlayerData {

    private UUID uuid;
    private List<Chest> chests;
    private boolean update;

    public PlayerData(UUID uuid, List<Chest> chests) {
        this.uuid = uuid;
        this.chests = chests == null ? new ArrayList<>(CHESTS_AMOUNT) : chests;
        this.update = false;
        this.updateChests();
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<Chest> getChests() {
        return chests;
    }

    public boolean isQueueUpdate() {
        return update;
    }

    public Chest getChest(int id) {
        return chests.get(id);
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    private void updateChests() {
        while (chests.size() < CHESTS_AMOUNT) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.MAIN).get(), "Item", new String[]{
                    "{id}"
            }, new String[]{
                    String.valueOf(chests.size() + 1)
            }).build();

            String name = item.hasItemMeta() ? item.getItemMeta().hasDisplayName() ?
                    item.getItemMeta().getDisplayName() : item.getType().toString() : item.getType().toString();
            Material icon = item.getType();
            Inventory inventory = Bukkit.createInventory(null, CHEST_SIZE, name);
            String serializedInventory = Base64Encoder.toBase64(inventory);

            chests.add(new Chest(name, icon, false, serializedInventory, 0));
        }
    }
}