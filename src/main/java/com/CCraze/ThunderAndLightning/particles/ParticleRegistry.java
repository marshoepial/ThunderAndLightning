package com.CCraze.ThunderAndLightning.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ParticleRegistry {
    public static void registerParticles(){
        //Minecraft.getInstance().particles.registerFactory(ParticleTypeRegistry.SKYSEEDPARTICLE, SkySeederParticle.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ModParticles.SKYSEEDPARTICLE, SkySeederParticle.Factory::new);
    }
    public static void makeSkySeedParticle(World world, double x, double y, double z, double yaw, double pitch){
       // world.addParticle(ParticleTypeRegistry.SKYSEEDPARTICLE, x, y, z, 0.3*Math.sin(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch)),
       //         0.3*Math.sin(Math.toRadians(-pitch)), 0.3*Math.cos(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch)));
        world.addParticle(ModParticles.SKYSEEDPARTICLE, x, y, z, 0.3*Math.sin(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch)),
                0.3*Math.sin(Math.toRadians(-pitch)), 0.3*Math.cos(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch)));
    }
}
