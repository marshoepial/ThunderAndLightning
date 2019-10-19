package com.CCraze.ThunderAndLightning.blocks.lightningattractors;

import com.CCraze.ThunderAndLightning.blocks.LightningAttractorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class IronLightningAttractor extends LightningAttractorBlock {
    public IronLightningAttractor(){
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3.0f),"Iron","ironlightningattractor");
    }
}
