package com.CCraze.LightningCraft.blocks.lightningattractors;

import com.CCraze.LightningCraft.blocks.LightningAttractorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class WoolLightningAttractor extends LightningAttractorBlock {
    public WoolLightningAttractor() {
        super(Block.Properties.create(Material.WOOL).sound(SoundType.CLOTH).hardnessAndResistance(1.0f),0.5,
                100, 0, false, "woollightningattractor");
    }

    @Override
    public void onStrike(World world, TileEntity te) {
        world.createExplosion(null, DamageSource.LIGHTNING_BOLT, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(),
                2.0F, true, Explosion.Mode.DESTROY);
        super.onStrike(world, te);
    }
}
