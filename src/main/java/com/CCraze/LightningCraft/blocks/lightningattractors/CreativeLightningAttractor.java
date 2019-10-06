package com.CCraze.LightningCraft.blocks.lightningattractors;

import com.CCraze.LightningCraft.blocks.LightningAttractorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class CreativeLightningAttractor extends LightningAttractorBlock {
    public CreativeLightningAttractor() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f), 1,
                99999, 40000,true, "creativelightningattractor");
    }
}
