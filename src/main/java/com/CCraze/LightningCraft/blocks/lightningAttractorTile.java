package com.CCraze.LightningCraft.blocks;

import com.CCraze.LightningCraft.ForgeEventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import java.beans.EventHandler;
import java.util.List;

import static com.CCraze.LightningCraft.blocks.ModBlocks.LIGHTNINGATTRACTOR_TILE;

public class lightningAttractorTile extends TileEntity {
    private int chanceStrike = 0; //chance of being struck by lightning, between 0 and 1
    public int maxDist = 0; //maximum distance the block can be from a source of lightning for it to be struck
    public lightningAttractorTile(){ //this is a necessary constructor, however it will only be used internally by Forge
        super(LIGHTNINGATTRACTOR_TILE);
    }
    public lightningAttractorTile(int chanceOfBeingStruck, int maximumDistance) { //this is the constructor we use, it has the variables we need
        super(LIGHTNINGATTRACTOR_TILE);
        chanceStrike = chanceOfBeingStruck;
        maxDist = maximumDistance;
        this.markDirty(); //sets flag for NBT to be written. This is the only time it should be marked dirty
        System.out.println("Tile registered!");
    }
    public boolean isValid(){
        if (Math.random() > chanceStrike) return false; //makes sure the chance of lightning striking is adhered to
        for (int i = this.getPos().getY()+1; i < 255; i++){ //iterate over the blocks above the attractor to see if the attractor is blocked
            BlockState currentState = this.getWorld().getBlockState(new BlockPos(this.getPos().getX(), i, this.getPos().getY()));
            if (currentState.getMaterial() != Material.AIR) return false; //if block is not AIR, then attractor is blocked and not valid
        }
        System.out.println("Is valid");
        return true;
    }
    public int getYWithOffset(){
        if (world.getBlockState(getPos().up()) == Blocks.AIR.getDefaultState()) return 1+getPos().getY();
        else return 2+getPos().getY();
    }
    public boolean thunderStruck(){
        List<Entity> entityList = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(getPos().up()));
        if(world.getBlockState(getPos().up()) == Blocks.AIR.getDefaultState()){
            System.out.println("No blocks above attractor, iterating over "+entityList.size()+" entities checking for item");
            for (Entity entity : entityList) {
                if (entity instanceof ItemEntity) {
                    System.out.println("Item found, replacing...");
                    Item newItem = ForgeEventHandler.recipeParser.swapItem(((ItemEntity) entity).getItem().getItem());
                    if (!newItem.equals(((ItemEntity) entity).getItem().getItem())){
                        if (((ItemEntity) entity).getItem().getCount() > 1) {
                            ItemEntity secondEntity = new ItemEntity(world, entity.posX, entity.posY, entity.posZ,
                                    ((ItemEntity) entity).getItem().copy());
                            secondEntity.getItem().setCount(((ItemEntity) entity).getItem().getCount()-1);
                            world.addEntity(secondEntity);
                        }
                        ((ItemEntity) entity).setItem(newItem.getDefaultInstance());
                        return true;
                    }
                }
            }
        } else {
            System.out.println("Block above attractor detected, replacing...");
            world.setBlockState(getPos().up(), ForgeEventHandler.recipeParser.swapBlock(world.getBlockState(getPos().up()).getBlock()).getDefaultState());
            return true;
        }
        return false;
    }
    @Override
    public void read(CompoundNBT nbt){ //NBT is used for persistence across chunk load/unloads.
        super.read(nbt); //read NBT required by TileEntity class
        this.maxDist = nbt.getInt("maxDist");
        this.chanceStrike = nbt.getInt("chanceStrike");
    }
    @Override
    public CompoundNBT write(CompoundNBT nbt){
        super.write(nbt);
        nbt.putInt("maxDist", maxDist);
        nbt.putInt("chanceStrike", chanceStrike);
        return nbt;
    }

}