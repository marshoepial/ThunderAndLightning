package com.CCraze.LightningCraft.behavior;

import com.CCraze.LightningCraft.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSetter {
    public static BlockPos setBlock(Entity e, World w){
        int xpos = e.getPosition().getX();
        int zpos = e.getPosition().getZ();

        double lowestDist = 1000000000;
        int index = -1;

        for (int i = 0; i < EventHandler.attractorPos.size(); i++){
            double currentDist = distanceCalc(xpos, zpos, EventHandler.attractorPos.get(i).getX(), EventHandler.attractorPos.get(i).getZ());
            if (currentDist < lowestDist){
                lowestDist = currentDist;
                index = i;
            }
        }

        if (index == -1) return e.getPosition();
        else return EventHandler.attractorPos.get(index).up(1);
    }
    public static double distanceCalc (int x1, int z1, int x2, int z2){
        return Math.sqrt(((x1-x2)*(x1-x2))+((z1-z2)*(z1-z2)));
    }
}
