package com.zpedroo.playerchests.listeners;

import com.zpedroo.playerchests.PlayerChests;
import com.zpedroo.playerchests.managers.DataManager;
import com.zpedroo.playerchests.objects.Chest;
import com.zpedroo.playerchests.objects.PlayerData;
import com.zpedroo.playerchests.utils.menu.Menus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerChatListener implements Listener {

    private static final Map<Player, Chest> editingChestName = new HashMap<>(4);

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!editingChestName.containsKey(event.getPlayer())) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        Chest chest = editingChestName.remove(player);
        String message = event.getMessage();
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', message);

        chest.setName(formattedMessage.length() > 32 ? formattedMessage.substring(0, 32) : formattedMessage);

        PlayerChests.get().getServer().getScheduler().runTaskLater(PlayerChests.get(), () -> Menus.getInstance().openMainMenu(player), 0L);

        PlayerData data = DataManager.getInstance().load(player);
        data.setUpdate(true);
    }

    public static Map<Player, Chest> getEditingChestName() {
        return editingChestName;
    }
}
