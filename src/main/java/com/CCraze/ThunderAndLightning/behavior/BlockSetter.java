package com.CCraze.ThunderAndLightning.behavior;

import com.CCraze.ThunderAndLightning.blocks.lightningattractors.LightningAttractorTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockSetter {
    public static BlockPos setBlock(Entity e){
        int xpos = e.getPosition().getX();
        int zpos = e.getPosition().getZ();
        World w = e.getEntityWorld();

        //System.out.println("Iterating over "+w.loadedTileEntityList.size()+" TileEntities");

        List<LightningAttractorTile> tileList = new ArrayList<>();

        for (int i = 0; i < w.loadedTileEntityList.size(); i++){ //iterate over all loaded tileentities
            if (w.loadedTileEntityList.get(i) instanceof LightningAttractorTile){
                LightningAttractorTile currentAttractor = (LightningAttractorTile) w.loadedTileEntityList.get(i);
                if (currentAttractor.isValid()) {
                    if (distanceCalc(xpos, zpos, currentAttractor.getPos().getX(), currentAttractor.getPos().getZ()) <
                            currentAttractor.maxDist) {
                        tileList.add(currentAttractor);
                    }
                }
            }
        }
        if (!tileList.isEmpty()){
            Random rand = new Random();
            LightningAttractorTile randTile = tileList.get(rand.nextInt(tileList.size()));
            randTile.thunderStruck((LightningBoltEntity) e);
            return new BlockPos(randTile.getPos().getX(), randTile.getYWithOffset(), randTile.getPos().getZ());
        }
        return e.getPosition();
    } public static void cleanFire (World world, BlockPos initialStrike, BlockPos attractorStrike){
        BlockState fireState = Blocks.FIRE.getDefaultState();
        BlockState airState = Blocks.AIR.getDefaultState();
        if (world.getBlockState(attractorStrike) == fireState) world.setBlockState(attractorStrike, airState);
        BlockPos tempBlockPos;
        for (int i = -2; i < 3; i++){
            for (int j = -2; j < 3; j++){
                for (int k = -2; k < 3; k++){
                    //System.out.println("Checking if "+initialStrike.getX()+i+", "+initialStrike.getY()+j + ", "+ initialStrike.getZ()+k+" contains fire");
                    tempBlockPos = new BlockPos(initialStrike.getX()+i, initialStrike.getY()+j, initialStrike.getZ()+k);
                    if (world.getBlockState(tempBlockPos).equals(fireState)){
                        //System.out.println("Fire detected; replacing");
                        world.setBlockState(tempBlockPos, airState);
                        //System.out.println("Block replaced at "+tempBlockPos+", it is now "+world.getBlockState(tempBlockPos));
                    }
                }
            }
        }
    }
    private static double distanceCalc(int x1, int z1, int x2, int z2){
        return Math.sqrt(((x1-x2)*(x1-x2))+((z1-z2)*(z1-z2)));
    }
}
