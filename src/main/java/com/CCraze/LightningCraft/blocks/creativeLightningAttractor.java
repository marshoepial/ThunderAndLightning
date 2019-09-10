package com.CCraze.LightningCraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isRemote){
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof lightningAttractorTile){
                player.sendMessage(new StringTextComponent("Current Energy: "+((lightningAttractorTile)te).getEnergy()));
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }
}
