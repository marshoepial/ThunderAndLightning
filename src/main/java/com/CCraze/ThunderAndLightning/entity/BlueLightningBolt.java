package com.CCraze.ThunderAndLightning.entity;

import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.world.World;

public class BlueLightningBolt extends LightningBoltEntity{
    public BlueLightningBolt(World p_i46780_1_, double p_i46780_2_, double p_i46780_4_, double p_i46780_6_, boolean p_i46780_8_) {
        super(p_i46780_1_, p_i46780_2_, p_i46780_4_, p_i46780_6_, p_i46780_8_);
        System.out.println("I'm blue, dabadee dabadaye");
    }

    //This is very boring
    //but because of the way that renderers are registered
    //this class must stay
}
