package com.zpedroo.playerchests.managers;

import com.zpedroo.playerchests.managers.cache.DataCache;
import com.zpedroo.playerchests.mysql.DBConnection;
import com.zpedroo.playerchests.objects.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private final DataCache dataCache = new DataCache();

    public DataManager() {
        instance = this;
    }

    public PlayerData load(Player player) {
        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null) {
            data = DBConnection.getInstance().getDBManager().loadData(player);
            dataCache.getPlayerData().put(player, data);
        }

        return data;
    }

    public void save(Player player) {
        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null || !data.isQueueUpdate()) return;

        DBConnection.getInstance().getDBManager().saveData(data);
        data.setUpdate(false);
    }

    public void saveAll() {
        new HashSet<>(dataCache.getPlayerData().keySet()).forEach(this::save);
    }

    public DataCache getCache() {
        return dataCache;
    }
}