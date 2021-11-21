package it.aendrix.itemrandomizer.obj;

import org.bukkit.Location;

import java.util.List;

public class BaseBlock {

    private Location block;
    private double maxY;
    private int life;
    private List<Location> destinationBlocks;

    public Location getBlock() {
        return block;
    }

    public void setBlock(Location block) {
        this.block = block;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public List<Location> getDestinationBlocks() {
        return destinationBlocks;
    }

    public void setDestinationBlocks(List<Location> destinationBlocks) {
        this.destinationBlocks = destinationBlocks;
    }

}
