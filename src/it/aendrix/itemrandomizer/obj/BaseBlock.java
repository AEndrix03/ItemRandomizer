package it.aendrix.itemrandomizer.obj;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BaseBlock {

    private Block block;
    private double maxY; //The max Y value of the trail
    private int life; //The life of the item spawns with the trail intersection
    private Location[] destinationBlocks; //The list of the block where the trail can intersect

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
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

    public Location[] getDestinationBlocks() {
        return destinationBlocks;
    }

    public void setDestinationBlocks(Location[] destinationBlocks) {
        this.destinationBlocks = destinationBlocks;
    }
}
