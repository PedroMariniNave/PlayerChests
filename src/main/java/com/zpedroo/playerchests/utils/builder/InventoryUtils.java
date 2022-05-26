package com.zpedroo.playerchests.utils.builder;

import com.zpedroo.playerchests.PlayerChests;
import com.zpedroo.playerchests.managers.DataManager;
import com.zpedroo.playerchests.objects.Chest;
import com.zpedroo.playerchests.objects.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InventoryUtils {

    private static InventoryUtils instance;
    public static InventoryUtils get() { return instance; }

    private final Map<Player, InventoryBuilder> viewers;
    public final Map<Inventory, Chest> chestInventories;

    public InventoryUtils() {
        instance = this;
        this.viewers = new HashMap<>(32);
        this.chestInventories = new HashMap<>(8);
        PlayerChests.get().getServer().getPluginManager().registerEvents(new ActionListeners(), PlayerChests.get());
    }

    public Map<Player, InventoryBuilder> getViewers() {
        return viewers;
    }

    private class ActionListeners implements Listener {

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            if (!viewers.containsKey(player)) return;

            InventoryBuilder inventory = viewers.get(player);
            if (inventory.getInventory().getViewers().isEmpty()) {
                viewers.remove(player);
                return;
            }

            event.setCancelled(true);

            if (event.getClickedInventory() == null) return;
            if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;

            int slot = event.getSlot();

            ActionType actionType = ActionType.ALL_CLICKS;
            if (inventory.getAction(slot, actionType) == null) {
                switch (event.getClick()) {
                    case LEFT:
                    case SHIFT_LEFT:
                        actionType = ActionType.LEFT_CLICK;
                        break;
                    case RIGHT:
                    case SHIFT_RIGHT:
                        actionType = ActionType.RIGHT_CLICK;
                        break;
                    case MIDDLE:
                        actionType = ActionType.SCROLL;
                        break;
                }
            }

            Action action = inventory.getAction(slot, actionType);
            if (action != null) action.getAction().run();
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onClose(InventoryCloseEvent event) {
            if (!chestInventories.containsKey(event.getInventory())) return;

            Inventory inventory = event.getInventory();
            Chest chest = chestInventories.remove(inventory);

            ItemStack[] oldItems = chest.getInventory().getContents();
            ItemStack[] newItems = inventory.getContents();

            if (Arrays.equals(oldItems, newItems)) return;

            chest.setInventory(inventory);

            PlayerData data = DataManager.getInstance().load((Player) event.getPlayer());
            data.setUpdate(true);
        }
    }

    public static class Action {

        private final Runnable action;
        private final ActionType actionType;

        public Action(Runnable action, ActionType actionType) {
            this.action = action;
            this.actionType = actionType;
        }

        public Runnable getAction() {
            return action;
        }

        public ActionType getActionType() {
            return actionType;
        }
    }

    public enum ActionType {
        LEFT_CLICK,
        RIGHT_CLICK,
        ALL_CLICKS,
        SCROLL
    }
}