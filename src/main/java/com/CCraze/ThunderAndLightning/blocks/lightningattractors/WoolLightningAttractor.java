package com.CCraze.ThunderAndLightning.blocks.lightningattractors;

import com.CCraze.ThunderAndLightning.blocks.LightningAttractorBlock;
import com.CCraze.ThunderAndLightning.blocks.LightningAttractorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class WoolLightningAttractor extends LightningAttractorBlock {
    public WoolLightningAttractor() {
        super(Block.Properties.create(Material.WOOL).sound(SoundType.CLOTH).hardnessAndResistance(1.0f),"Wool", "woollightningattractor");
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
