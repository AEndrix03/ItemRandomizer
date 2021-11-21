package it.aendrix.itemrandomizer.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class utils {

    public static Enchantment getEnchant(String name) {
        for (Enchantment enchant : Enchantment.values())
            if (name.equalsIgnoreCase(enchant.toString()))
                return enchant;
        return null;
    }

    public static double locationDistance(Location loc1, Location loc2) {
        return Math.sqrt(Math.pow(loc1.getX()-loc2.getX(),2) + Math.pow(loc1.getZ()-loc2.getZ(),2));
    }

    public static int booleanToSign(boolean b) {
        if (b)
            return 1;
        else
            return -1;
    }

    public static void sendMsg(Player p, String s) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

    public static void sendMsg(CommandSender p, String s) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

    public static Location randomLocation(List<Location> l) {
        return l.get((int) (Math.random() * l.size()-1));
    }

    public static Location copyLocation(Location loc) {
        return new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
    }

    public static void broadcast(String s) {
        for (Player p : Bukkit.getOnlinePlayers())
            sendMsg(p,s);
    }

    public static ItemStack randomItem(HashMap<String, ItemStack> l) {
        return l.values().toArray(new ItemStack[l.size()])[(int) (Math.random() * l.size()-1)];
    }

}
