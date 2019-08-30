package com.CCraze.LightningCraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class blockTest extends Block {

    public blockTest(){
        super(Properties.create(Material.IRON)
        .sound(SoundType.METAL)
        .hardnessAndResistance(2.0f));
        setRegistryName("blocktest");
    }


}
