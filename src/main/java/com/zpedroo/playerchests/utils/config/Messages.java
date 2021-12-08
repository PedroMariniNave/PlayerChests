package com.zpedroo.playerchests.utils.config;

import com.zpedroo.playerchests.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static final String NEVER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.never"));

    public static final String GLOWING = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.glowing"));

    public static final String NOT_GLOWING = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.not-glowing"));

    public static final List<String> EDIT_NAME = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.edit-name"));

    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private static List<String> getColored(List<String> list) {
        List<String> colored = new ArrayList<>(list.size());
        for (String str : list) {
            colored.add(getColored(str));
        }

        return colored;
    }
}