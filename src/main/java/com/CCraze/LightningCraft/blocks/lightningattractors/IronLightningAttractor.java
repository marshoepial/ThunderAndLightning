package com.CCraze.LightningCraft.blocks.lightningattractors;

import com.CCraze.LightningCraft.blocks.LightningAttractorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class IronLightningAttractor extends LightningAttractorBlock {
    public IronLightningAttractor(){
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f),0.5,
                200,15000, true,"ironlightningattractor");
    }
}