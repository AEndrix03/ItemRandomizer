package it.aendrix.itemrandomizer.files;

import it.aendrix.itemrandomizer.main.Main;
import it.aendrix.itemrandomizer.main.utils;
import it.aendrix.itemrandomizer.obj.BaseBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Configuration {

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
            b.setMaxY(cfg.getDouble("blocks."+name+".maxY"));
            b.setLife(cfg.getInt("blocks."+name+".life"));

            List<String> dest = cfg.getStringList("blocks."+name+".destinationBlocks");
            Location[] destinationBlocks = new Location[dest.size()];

            for (int i = 0; i<destinationBlocks.length; i++) {
                String[] loc = dest.get(i).split(";"); //WORLD;X;Y;Z
                destinationBlocks[i] = new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1])
                        , Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
            }

            b.setDestinationBlocks(destinationBlocks);

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

            //Enchantments
            if (cfg.getString("items."+name+".enchantments")!=null) {
                List<String> enchants = cfg.getStringList("items."+name+".enchantments"); // Enchant;Level
                for (String s : enchants) {
                    String[] enchant = s.split(";");
                    item.addEnchantment(utils.getEnchant(enchant[0]), Integer.parseInt(enchant[1]));
                }
            }

            //ItemFlags
            if (cfg.getBoolean("items."+name+".hide_attributes")) //Hide Attributes
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            if (cfg.getBoolean("items."+name+".hide_placed_on")) //Show/Hide place/break block
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);

            if (cfg.getBoolean("items."+name+".hide_destroys")) //Hide durability
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);

            if (cfg.getBoolean("items."+name+".hide_enchantments")) //Hide enchantments
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            if (cfg.getBoolean("items."+name+".hide_unbreakable")) //Hide unbreakable
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

            if (cfg.getBoolean("items."+name+".hide_potioneffects")) //Hide potion effects
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

            //Unbreakable
            meta.setUnbreakable(cfg.getBoolean("items."+name+".unbreakable"));

            item.setItemMeta(meta);
            items.put(name.toUpperCase(), item);
        }
    }

}
