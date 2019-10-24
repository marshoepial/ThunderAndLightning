package com.CCraze.ThunderAndLightning.blocks.lightningattractors;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class DiamondLightningAttractor extends LightningAttractorBlock {
    public DiamondLightningAttractor(){
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3.5f),"Diamond","diamondlightningattractor");
    }
}
