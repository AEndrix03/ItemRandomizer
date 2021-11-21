package it.aendrix.itemrandomizer.obj;

import it.aendrix.itemrandomizer.main.Main;
import it.aendrix.itemrandomizer.main.utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Trail implements Listener {

    private Particle particle;
    private float speed;
    private int life;
    private double rangeRotation;
    private Location start;
    private Location destination;
    private Location position;
    private ItemStack item;

    public Trail() {

    }

    public Trail(Particle particle) {
        this.particle = particle;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void start() {

        new BukkitRunnable() {

            int r = 0, rmax = (int) ((utils.locationDistance(start,destination)/2)/speed);
            final double Yxs = Math.abs(start.getY()-destination.getY())/5;
            double Ydeg = Yxs/rmax, X, Z, Ainc, T = Math.abs(Math.atan((start.getZ()-destination.getZ())/(start.getX()-destination.getX()))), A = T;
            Location center = new Location(position.getWorld(), position.getX(), position.getY(), position.getZ());
            boolean state1 = true, state2 = false, state3 = false, state4 = false, state5 = false;
            final boolean positiveX = start.getX()<destination.getX(), positiveZ = start.getZ()<destination.getZ();
            int sec = 0;
            Item dropped;

            @Override
            public void run() {

                if (state1) {

                    X = (r*speed) * Math.cos(T);
                    Z = (r*speed) * Math.sin(T);

                    if (r>rmax) {
                        state1 = false;
                        state2 = true;
                        r = 0;
                        rmax = (int) ((Math.PI*rangeRotation)/speed);
                        Ainc = 180/rmax;
                        A = Math.toDegrees(Math.abs(Math.atan((start.getZ()-destination.getZ())/(start.getX()-destination.getX()))));
                        center = new Location(position.getWorld(), position.getX()+(rangeRotation*Math.sin(T)*utils.booleanToSign(positiveX)),
                                position.getY(), position.getZ() -(rangeRotation*Math.cos(T)*utils.booleanToSign(positiveZ)));
                        Ydeg = Yxs/rmax;
                    }
                } else if (state2) {

                    X = -rangeRotation * Math.sin(Math.toRadians(A));
                    Z = rangeRotation * Math.cos(Math.toRadians(A));

                    A -= Ainc;

                    if (r>rmax) {
                        state2 = false;
                        state3 = true;
                        r = 0;
                        rmax = (int) ((utils.locationDistance(start,destination)/1.5)/speed);
                        center = new Location(position.getWorld(), position.getX(), position.getY(), position.getZ());
                        Ydeg = Yxs/rmax;
                    }
                } else if (state3) {
                    X = (-r*speed) * Math.cos(T);
                    Z = (-r*speed) * Math.sin(T);

                    if (r>rmax) {
                        state3 = false;
                        state4 = true;
                        r = 0;
                        rmax = (int) ((Math.PI*rangeRotation)/speed);
                        Ainc = 180/rmax;
                        center = new Location(position.getWorld(), position.getX()-(rangeRotation*Math.sin(T)*utils.booleanToSign(positiveX)),
                                position.getY(), position.getZ() +(rangeRotation*Math.cos(T)*utils.booleanToSign(positiveZ)));
                        Ydeg = Yxs/rmax;
                        A = Math.toDegrees(T);
                    }
                } else if (state4) {
                    X = rangeRotation * Math.sin(Math.toRadians(A));
                    Z = -rangeRotation * Math.cos(Math.toRadians(A));

                    A -= Ainc;

                    if (r>rmax) {
                        state4 = false;
                        state5 = true;
                        r = 0;
                        rmax = (int) ((utils.locationDistance(position,destination))/speed);
                        center = new Location(position.getWorld(), position.getX(), position.getY(), position.getZ());
                        T = Math.abs(Math.atan((center.getZ()-destination.getZ())/(center.getX()-destination.getX())));
                        Ydeg = Yxs/rmax;
                    }
                } else if (state5) {
                    X = (r*speed) * Math.cos(T);
                    Z = (r*speed) * Math.sin(T);

                    if (position.getY()<=destination.getY()) {
                        state5 = false;

                        if (item == null) {
                            cancel();
                            return;
                        }

                        int id = Main.getActualTicket()+1;
                        Main.setActualTicket(id);

                        ItemMeta meta = item.getItemMeta();
                        ArrayList<String> lore = new ArrayList<>();
                        if (meta!=null && meta.hasLore())
                            for (String s : meta.getLore())
                                lore.add(ChatColor.translateAlternateColorCodes('&',s));
                        lore.add(id+"");

                        meta.setLore(lore);
                        item.setItemMeta(meta);

                        Main.getItemsSpawned().put(item.getType().toString()+";"+id,this);

                        dropped = destination.getWorld().dropItem(new Location(destination.getWorld(), destination.getBlockX(), destination.getY()+0.5, destination.getBlockZ()),item);
                        dropped.setGlowing(true);

                        center = dropped.getLocation();
                        A=0;
                        Ydeg = 0.01;
                        position.setY(position.getY()+2.5);
                    }
                } else {
                    center = dropped.getLocation();
                    A+=3;
                    X = 1.5 * Math.cos(Math.toRadians(A));
                    Z = 1.5 * Math.sin(Math.toRadians(A));

                    if (position.getY()<=destination.getBlockY()+0.5) {
                        position.setY(destination.getY() + 2.5);
                        A=0;
                    }
                }

                if (sec>life) {
                    Main.getItemsSpawned().remove(item.getType().toString()+";"+
                            item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1));
                    item = null;
                    cancel();
                }

                position.setX((center.getX())+(utils.booleanToSign(positiveX)*X));
                position.setZ((center.getZ())+(utils.booleanToSign(positiveZ)*Z));
                position.setY(position.getY()-Ydeg);

                position.getWorld().spawnParticle(particle, position.getX(), position.getY(), position.getZ(), 0);

                r++;

            }

        }.runTaskTimer(Main.getInstance(), 1L, 1L);
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getPosition() {
        return position;
    }

    public void setPosition(Location position) {
        this.position = position;
    }

    public double getRangeRotation() {
        return rangeRotation;
    }

    public void setRangeRotation(double rangeRotation) {
        this.rangeRotation = rangeRotation;
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        ItemMeta meta = item.getItemMeta();
        Player p = e.getPlayer();

        if (meta == null || !meta.hasLore()) return;

        int value = 0;
        try {
            value = Integer.parseInt(meta.getLore().get(meta.getLore().size()-1));
        } catch (NumberFormatException exc) {
            return;
        }

        String id = item.getType().toString()+";"+value;
        BukkitRunnable trail = Main.getItemsSpawned().get(id);

        if (trail==null) return;


        p.getInventory().remove(item);
        e.setCancelled(true);
        e.getItem().remove();

        trail.cancel();

        Main.getData().add(p.getName());

        utils.broadcast("&f"+p.getName()+" &6&lHa trovato un biglietto!");
    }

    public void setLife(int life) {
        this.life = life;
    }
}
