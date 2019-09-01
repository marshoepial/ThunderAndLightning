package com.CCraze.LightningCraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class creativeLightningAttractor extends Block {

    public final int chanceToStrike = 1;
    public final int maxDist = 10000000;

    public creativeLightningAttractor(){
        super(Properties.create(Material.IRON)
        .sound(SoundType.METAL)
        .hardnessAndResistance(2.0f));
        setRegistryName("creativelightningattractor");
        
    }

    @Override
    public boolean hasTileEntity(BlockState state){
        return true;
    }
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){
        return new lightningAttractorTile(chanceToStrike, maxDist);
    }

}
