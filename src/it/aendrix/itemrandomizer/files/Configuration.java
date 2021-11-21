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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Configuration {

    private static File f = new File("plugins/ItemRandomizer" + File.separator + "config.yml");
    private  static FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

    public Configuration() {
        Main.setItemsSpawned(new HashMap<>());
    }

    public File getFile() {
        return f;
    }

    public FileConfiguration getConfiguration() {
        return cfg;
    }

    public void createConfiguration() throws IOException {
        Main.setBlocks(new HashMap<>());
        Main.setItems(new HashMap<>());
        Main.setData(new ArrayList<>());

        if (f.exists()) return;

        f.createNewFile();
    }

    public void loadBlocks() {
        if (cfg.getString("blocks") == null)
            return;

        HashMap<String, BaseBlock> blocks = new HashMap<>();

        for (String name : cfg.getConfigurationSection("blocks").getKeys(false)) {
            BaseBlock b  = new BaseBlock();
            b.setBlock(cfg.getLocation("blocks."+name+".location"));
            b.setMaxY(cfg.getDouble("blocks."+name+".maxY"));
            b.setLife(cfg.getInt("blocks."+name+".life"));

            List<String> dest = cfg.getStringList("blocks."+name+".destinationBlocks");
            int destsize = dest.size();
            ArrayList<Location> destinationblocks = new ArrayList<Location>();

            for (int i = 0; i<destsize; i++) {
                String[] loc = dest.get(i).split(";"); //WORLD;X;Y;Z
                destinationblocks.add(new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1])
                        , Double.parseDouble(loc[2]), Double.parseDouble(loc[3])));
            }

            b.setDestinationBlocks(destinationblocks);

            blocks.put(name.toUpperCase(), b);

            Main.setBlocks(blocks);
        }
    }

    public void saveBlocks(){
        HashMap<String, BaseBlock> blocks = Main.getBlocks();

        for (String s : blocks.keySet()) {
            cfg.set("blocks."+s+".location", blocks.get(s).getBlock());
            cfg.set("blocks."+s+".maxY", blocks.get(s).getMaxY());
            cfg.set("blocks."+s+".life", blocks.get(s).getLife());
            ArrayList<String> dbl = new ArrayList<>();
            for (Location l : blocks.get(s).getDestinationBlocks())
                dbl.add(l.getWorld().getName()+";"+l.getX()+";"+l.getY()+";"+l.getZ());
            cfg.set("blocks."+s+".destinationBlocks", dbl);
        }

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        if (cfg.getString("data") == null)
            return;

        List<String> data = cfg.getStringList("data");

        Main.setData(data);
    }

    public void saveData() {
        List<String> data = Main.getData();

        cfg.set("data", data);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadItems() {
        Main.setActualTicket(cfg.getInt("actualTicket"));

        if (cfg.getString("items") == null)
            return;

        HashMap<String, ItemStack> items = new HashMap<>();

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

            Main.setItems(items);
        }
    }

    public void saveItems() {
        cfg.set("actualTicket", Main.getActualTicket());

        HashMap<String, ItemStack> items = Main.getItems();

        for (String s : items.keySet()) {
            cfg.set("items."+s+".material", items.get(s).getType());
            cfg.set("items."+s+".amount", items.get(s).getAmount());
            ItemMeta meta = items.get(s).getItemMeta();
            cfg.set("items."+s+".lore", meta.getLore());
            cfg.set("items."+s+".displayname", meta.getDisplayName());
            ArrayList<String> ench = new ArrayList<>();
            for (Enchantment e : meta.getEnchants().keySet())
                ench.add(e.toString()+";"+meta.getEnchantLevel(e));
            cfg.set("items."+s+".enchantments", ench);
            cfg.set("items."+s+".hide_attributes", meta.getItemFlags().contains(ItemFlag.HIDE_ATTRIBUTES));
            cfg.set("items."+s+".hide_placed_on", meta.getItemFlags().contains(ItemFlag.HIDE_PLACED_ON));
            cfg.set("items."+s+".hide_destroys", meta.getItemFlags().contains(ItemFlag.HIDE_DESTROYS));
            cfg.set("items."+s+".hide_enchantments", meta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS));
            cfg.set("items."+s+".hide_unbreakable", meta.getItemFlags().contains(ItemFlag.HIDE_UNBREAKABLE));
            cfg.set("items."+s+".hide_potioneffects", meta.getItemFlags().contains(ItemFlag.HIDE_POTION_EFFECTS));
            cfg.set("items."+s+".unbreakable", meta.isUnbreakable());
        }

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
