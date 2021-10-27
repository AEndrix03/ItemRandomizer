package it.aendrix.itemrandomizer.files;

import it.aendrix.itemrandomizer.main.Main;
import it.aendrix.itemrandomizer.obj.BaseBlock;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Configurtion {

    private static File f = new File("plugins/ItemRandomizer" + File.separator + "config.yml");
    private  static FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

    public File getFile() {
        return f;
    }

    public FileConfiguration getConfiguration() {
        return cfg;
    }

    private void createConfiguration() {

    }

    public void loadBlocks() {
        if (cfg.getString("blocks") == null)
            return;

        HashMap<Location, BaseBlock> blocks = new HashMap<>();
        Main.setBlocks(blocks);

        for (String name : cfg.getConfigurationSection("blocks").getKeys(false)) {
            BaseBlock b  = new BaseBlock();
            b.setBlock(cfg.getLocation("blocks."+name+".location").getBlock());
            blocks.put(b.getBlock().getLocation(), b);
        }
    }

    public void loadItems() {
        if (cfg.getString("items") == null)
            return;

        HashMap<String, ItemStack> items = new HashMap<>();
        Main.setItems(items);

        for (String name : cfg.getConfigurationSection("items").getKeys(false)) {
            //Material
            if (cfg.getString("items."+name+".material")==null)
                return;

            ItemStack item = new ItemStack(Material.valueOf(cfg.getString("items."+name+".material")));
            //Amount
            if (cfg.getInt("items."+name+".amount")>0)
                item.setAmount(cfg.getInt("items."+name+".amount"));

            ItemMeta meta = item.getItemMeta();

            //Lore
            if (cfg.getString("items."+name+".lore")!=null) {
                List<String> lore = cfg.getStringList("items."+name+".lore");

                for (int i = 0; i<lore.size(); i++)
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));

                meta.setLore(lore);
            }

            //DisplayName
            if (cfg.getString("items."+name+".displayname")!=null)
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', cfg.getString("items."+name+".displayname")));

            if (cfg.getString("items."+name+".enchantments")!=null) {
                ArrayList<String> enchants = cfg.getStringList("items."+name+".enchantments")
            }
        }
    }

}
