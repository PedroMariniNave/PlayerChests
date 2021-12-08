package com.zpedroo.playerchests.utils.config;

import com.zpedroo.playerchests.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.List;

public class Settings {

    public static final String COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.command");

    public static final List<String> ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.aliases");

    public static final String PERMISSION = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.permission");

    public static final String PERMISSION_MESSAGE = ChatColor.translateAlternateColorCodes('&',
            FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.permission-message"));

    public static final long SAVE_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.save-interval");

    public static final int CHESTS_AMOUNT = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.chests-amount");

    public static final int CHEST_SIZE = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.chest-size");

    public static final String DATE_FORMAT = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.date-format");
}