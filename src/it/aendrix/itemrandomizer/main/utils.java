package it.aendrix.itemrandomizer.main;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;

public class utils {

    public static Enchantment getEnchant(String name) {
        for (Enchantment enchant : Enchantment.values())
            if (name.equalsIgnoreCase(enchant.getKey().getKey()))
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

}
