package com.CCraze.ThunderAndLightning.particles;

import com.CCraze.ThunderAndLightning.ThunderAndLightning;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(ThunderAndLightning.MODID)
public class ParticleTypeRegistry {
    public static void registerParticleTypes(){
        Registry.<ParticleType<BasicParticleType>>register(Registry.PARTICLE_TYPE, "thunderandlightning:skyseed", new BasicParticleType(false));
    }
}
