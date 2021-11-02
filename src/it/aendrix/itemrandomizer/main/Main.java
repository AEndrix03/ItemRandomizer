package it.aendrix.itemrandomizer.main;

import it.aendrix.itemrandomizer.files.Configuration;
import it.aendrix.itemrandomizer.obj.BaseBlock;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Configuration config;
    private static HashMap<Location, BaseBlock> blocks;
    private static HashMap<String, ItemStack> items;

    @Override
    public void onEnable() {
        instance = this;

        createDir();

        config = new Configuration();
        config.loadBlocks();
        config.loadItems();

    }

    private void createDir() {
        new File("plugins/ItemRandomizer").mkdir();
    }

    public static Main getInstance() {
        return instance;
    }

    public static HashMap<Location, BaseBlock> getBlocks() {
        return blocks;
    }

    public static void setBlocks(HashMap<Location, BaseBlock> blocks) {
        Main.blocks = blocks;
    }

    public static Configuration getConfiguration() {
        return config;
    }

    public static void setConfiguration(Configuration config) {
        Main.config = config;
    }

    public static HashMap<String, ItemStack> getItems() {
        return items;
    }

    public static void setItems(HashMap<String, ItemStack> items) {
        Main.items = items;
    }
}
