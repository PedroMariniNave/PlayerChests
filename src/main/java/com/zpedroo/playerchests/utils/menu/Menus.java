package com.zpedroo.playerchests.utils.menu;

import com.zpedroo.playerchests.listeners.PlayerChatListener;
import com.zpedroo.playerchests.managers.DataManager;
import com.zpedroo.playerchests.objects.Chest;
import com.zpedroo.playerchests.objects.PlayerData;
import com.zpedroo.playerchests.utils.FileUtils;
import com.zpedroo.playerchests.utils.builder.InventoryBuilder;
import com.zpedroo.playerchests.utils.builder.InventoryUtils;
import com.zpedroo.playerchests.utils.builder.ItemBuilder;
import com.zpedroo.playerchests.utils.config.Messages;
import com.zpedroo.playerchests.utils.config.Settings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    private final ItemStack nextPageItem;
    private final ItemStack previousPageItem;

    public Menus() {
        instance = this;
        this.nextPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Next-Page").build();
        this.previousPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Previous-Page").build();
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        int nextPageSlot = FileUtils.get().getInt(file, "Inventory.next-page-slot");
        int previousPageSlot = FileUtils.get().getInt(file, "Inventory.previous-page-slot");

        InventoryBuilder inventory = new InventoryBuilder(title, size, previousPageItem, previousPageSlot, nextPageItem, nextPageSlot);

        PlayerData data = DataManager.getInstance().load(player);

        int id = 0;
        int slotPosition = -1;
        String[] slots = FileUtils.get().getString(file, "Inventory.item-slots").replace(" ", "").split(",");
        for (Chest chest : data.getChests()) {
            if (++slotPosition >= slots.length) slotPosition = 0;

            SimpleDateFormat dateFormatter = new SimpleDateFormat(Settings.DATE_FORMAT);

            ItemBuilder builder = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Item", new String[]{
                    "{id}",
                    "{items_amount}",
                    "{modification}"
            }, new String[]{
                    String.valueOf(++id),
                    String.valueOf(chest.getItemsAmount()),
                    chest.getLastModificationInMillis() <= 0 ? Messages.NEVER : dateFormatter.format(chest.getLastModificationInMillis())
            });

            if (chest.getIcon() != null) builder.setType(chest.getIcon());
            if (chest.getName() != null) builder.setName(chest.getName());
            if (chest.isGlowIcon()) builder.setGlow();

            int slot = Integer.parseInt(slots[slotPosition]);

            ItemStack item = builder.build();

            inventory.addItem(item, slot, () -> {
                openChestMenu(player, chest);
            }, ActionType.LEFT_CLICK);

            inventory.addAction(slot, () -> {
                openEditChestMenu(player, chest);
            }, ActionType.RIGHT_CLICK);
        }

        inventory.open(player);
    }

    public void openOtherChestsMenu(Player player, Player target) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        int nextPageSlot = FileUtils.get().getInt(file, "Inventory.next-page-slot");
        int previousPageSlot = FileUtils.get().getInt(file, "Inventory.previous-page-slot");

        InventoryBuilder inventory = new InventoryBuilder(title, size, previousPageItem, previousPageSlot, nextPageItem, nextPageSlot);

        PlayerData data = DataManager.getInstance().load(target);

        int id = 0;
        int slotPosition = -1;
        String[] slots = FileUtils.get().getString(file, "Inventory.item-slots").replace(" ", "").split(",");
        for (Chest chest : data.getChests()) {
            if (++slotPosition >= slots.length) slotPosition = 0;

            SimpleDateFormat dateFormatter = new SimpleDateFormat(Settings.DATE_FORMAT);

            ItemBuilder builder = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Item", new String[]{
                    "{id}",
                    "{items_amount}",
                    "{modification}"
            }, new String[]{
                    String.valueOf(++id),
                    String.valueOf(chest.getItemsAmount()),
                    chest.getLastModificationInMillis() <= 0 ? Messages.NEVER : dateFormatter.format(chest.getLastModificationInMillis())
            });

            if (chest.getIcon() != null) builder.setType(chest.getIcon());
            if (chest.getName() != null) builder.setName(chest.getName());
            if (chest.isGlowIcon()) builder.setGlow();

            int slot = Integer.parseInt(slots[slotPosition]);

            ItemStack item = builder.build();

            inventory.addItem(item, slot, () -> {
                openChestMenu(player, chest);
            }, ActionType.LEFT_CLICK);

            inventory.addAction(slot, () -> {
                openEditChestMenu(player, chest);
            }, ActionType.RIGHT_CLICK);
        }

        inventory.open(player);
    }

    public void openEditChestMenu(Player player, Chest chest) {
        FileUtils.Files file = FileUtils.Files.EDIT_CHEST;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            ItemBuilder builder = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str, new String[]{
                    "{glowing}"
            }, new String[]{
                    chest.isGlowIcon() ? Messages.GLOWING : Messages.NOT_GLOWING
            });
            String action = FileUtils.get().getString(file, "Inventory.items." + str + ".action");
            int slot = FileUtils.get().getInt(file, "Inventory.items." + str + ".slot");

            if (StringUtils.equals(action.toUpperCase(), "EDIT_ICON")) builder.setType(chest.getIcon());
            if ((StringUtils.equals(action.toUpperCase(), "GLOW") ||
                    StringUtils.equals(action.toUpperCase(), "EDIT_ICON")) && chest.isGlowIcon()) builder.setGlow();

            ItemStack item = builder.build();

            inventory.addItem(item, slot, () -> {
                switch (action.toUpperCase()) {
                    case "EDIT_NAME":
                        player.closeInventory();

                        for (int i = 0; i < 25; ++i) {
                            player.sendMessage("");
                        }

                        for (String msg : Messages.EDIT_NAME) {
                            player.sendMessage(msg);
                        }

                        PlayerChatListener.getEditingChestName().put(player, chest);
                        break;

                    case "EDIT_ICON":
                        openIconsMenu(player, chest);
                        break;

                    case "GLOW":
                        chest.setGlowIcon(!chest.isGlowIcon());
                        openEditChestMenu(player, chest);
                        break;

                    case "BACK":
                        openMainMenu(player);
                        break;
                }
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    public void openIconsMenu(Player player, Chest chest) {
        FileUtils.Files file = FileUtils.Files.ICONS;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        int nextPageSlot = FileUtils.get().getInt(file, "Inventory.next-page-slot");
        int previousPageSlot = FileUtils.get().getInt(file, "Inventory.previous-page-slot");

        InventoryBuilder inventory = new InventoryBuilder(title, size, previousPageItem, previousPageSlot, nextPageItem, nextPageSlot);

        int slotPosition = -1;
        String[] slots = FileUtils.get().getString(file, "Inventory.item-slots").replace(" ", "").split(",");

        PlayerData data = DataManager.getInstance().load(player);

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            if (++slotPosition >= slots.length) slotPosition = 0;

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str).build();
            int slot = Integer.parseInt(slots[slotPosition]);

            inventory.addItem(item, slot, () -> {
                chest.setIcon(item.getType());
                openMainMenu(player);
                data.setUpdate(true);
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    public void openChestMenu(Player player, Chest chest) {
        Inventory inventory = chest.getInventory();
        chestInventories.put(inventory, chest);

        player.openInventory(inventory);
    }
}