package com.CCraze.ThunderAndLightning;

import com.CCraze.ThunderAndLightning.blocks.ModBlocks;
import com.CCraze.ThunderAndLightning.entity.ModEntities;
import com.CCraze.ThunderAndLightning.particles.ParticleRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value=Dist.CLIENT, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ClientModBusHandler {
    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event){
        System.out.println("Registering particle factory");
        ParticleRegistry.registerParticles();
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        RenderTypeLookup.setRenderLayer(ModBlocks.SKYSEEDER, RenderType.getCutout());
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BLUEBOLT, LightningBoltRenderer::new);
    }
}
