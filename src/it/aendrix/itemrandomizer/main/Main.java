package it.aendrix.itemrandomizer.main;

import it.aendrix.itemrandomizer.commands.Commands;
import it.aendrix.itemrandomizer.files.Configuration;
import it.aendrix.itemrandomizer.obj.BaseBlock;
import it.aendrix.itemrandomizer.obj.Trail;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Configuration config;
    private static HashMap<String, BaseBlock> blocks;
    private static HashMap<String, ItemStack> items;
    private static HashMap<String, BukkitRunnable> itemsSpawned;
    private static List<String> data;
    private static int actualTicket;

    @Override
    public void onEnable() {
        instance = this;

        createDir();

        config = new Configuration();
        try {
            config.createConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.loadBlocks();
        config.loadItems();
        config.loadData();

        this.getCommand("itemrandomizer").setExecutor(new Commands());
        this.getServer().getPluginManager().registerEvents(new Trail(),this);
    }

    @Override
    public void onDisable() {
        config.saveBlocks();
        config.saveItems();
        config.saveData();
    }

    private void createDir() {
        new File("plugins/ItemRandomizer").mkdir();
    }

    public static Main getInstance() {
        return instance;
    }

    public static HashMap<String, BaseBlock> getBlocks() {
        return blocks;
    }

    public static void setBlocks(HashMap<String, BaseBlock> blocks) {
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

    public static HashMap<String, BukkitRunnable> getItemsSpawned() {
        return itemsSpawned;
    }

    public static void setItemsSpawned(HashMap<String, BukkitRunnable> itemsSpawned) {
        Main.itemsSpawned = itemsSpawned;
    }

    public static int getActualTicket() {
        return actualTicket;
    }

    public static void setActualTicket(int actualTicket) {
        Main.actualTicket = actualTicket;
    }

    public static List<String> getData() {
        return data;
    }

    public static void setData(List<String> data) {
        Main.data = data;
    }
}
