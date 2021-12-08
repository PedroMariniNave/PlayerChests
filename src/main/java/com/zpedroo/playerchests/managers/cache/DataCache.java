package com.zpedroo.playerchests.managers.cache;

import com.zpedroo.playerchests.objects.PlayerData;
import org.bukkit.entity.Player;
import java.util.*;

public class DataCache {

    private Map<Player, PlayerData> playerData;

    public DataCache() {
        this.playerData = new HashMap<>(32);
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }
}