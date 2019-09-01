package com.CCraze.LightningCraft.behavior;

import com.CCraze.LightningCraft.EventHandler;
import com.CCraze.LightningCraft.blocks.lightningAttractorTile;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockSetter {
    public static BlockPos setBlock(Entity e){
        int xpos = e.getPosition().getX();
        int zpos = e.getPosition().getZ();
        World w = e.getEntityWorld();

        System.out.println("Iterating over "+w.loadedTileEntityList.size()+" TileEntities");

        for (int i = 0; i < w.loadedTileEntityList.size(); i++){ //iterate over all loaded tileentities
            if (w.loadedTileEntityList.get(i) instanceof lightningAttractorTile){
                lightningAttractorTile currentAttractor = (lightningAttractorTile) w.loadedTileEntityList.get(i);
                if (currentAttractor.isValid()) {
                    if (distanceCalc(xpos, zpos, currentAttractor.getPos().getX(), currentAttractor.getPos().getZ()) <
                            currentAttractor.maxDist) {
                        System.out.println("Lightning Strikes!");
                        return new BlockPos(currentAttractor.getPos().getX(), currentAttractor.getPos().getY()+1,
                                currentAttractor.getPos().getZ());
                    }
                }
            }
        }
        return e.getPosition();
    }
    private static double distanceCalc(int x1, int z1, int x2, int z2){
        return Math.sqrt(((x1-x2)*(x1-x2))+((z1-z2)*(z1-z2)));
    }
}
