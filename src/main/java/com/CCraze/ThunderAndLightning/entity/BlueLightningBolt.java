package com.CCraze.ThunderAndLightning.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.world.World;

public class BlueLightningBolt extends LightningBoltEntity{
    public BlueLightningBolt(World world, double p_i46780_2_, double p_i46780_4_, double p_i46780_6_, boolean p_i46780_8_) {
        super(world, p_i46780_2_, p_i46780_4_, p_i46780_6_, p_i46780_8_);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.BLUEBOLT;
    }
}
