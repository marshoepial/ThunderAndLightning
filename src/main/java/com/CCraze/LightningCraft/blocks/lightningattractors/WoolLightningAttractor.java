package com.CCraze.LightningCraft.blocks.lightningattractors;

import com.CCraze.LightningCraft.blocks.LightningAttractorBlock;
import com.CCraze.LightningCraft.blocks.LightningAttractorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WoolLightningAttractor extends LightningAttractorBlock {
    public WoolLightningAttractor() {
        super(Block.Properties.create(Material.WOOL).sound(SoundType.CLOTH).hardnessAndResistance(1.0f),"Wool", "woollightningattractor");
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        System.out.println("Creating new TileEntity");
        return new LightningAttractorTile(chanceToStrike*modifier, (int)Math.round(maxDist*modifier),
                (int)Math.round(maxEnergyStorage*modifier), energyCap){
            @Override
            protected ItemEntity genItemEntity(World world, double x, double y, double z, ItemStack item) {
                return new ItemEntity(world, x, y ,z, item){
                    @Override
                    public boolean isImmuneToExplosions() {
                        return true;
                    }

                    @Override
                    protected void dealFireDamage(int p_70081_1_) {
                    }
                };
            }
        };
    }

    @Override
    public void onStrike(World world, TileEntity te) {
        world.createExplosion(null, DamageSource.LIGHTNING_BOLT, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(),
                2.0F, true, Explosion.Mode.DESTROY);
        super.onStrike(world, te);
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
        return false;
    }
}
