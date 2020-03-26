package com.CCraze.ThunderAndLightning;

import com.CCraze.ThunderAndLightning.blocks.ModBlocks;
import com.CCraze.ThunderAndLightning.blocks.skyseeder.SkySeederTile;
import com.CCraze.ThunderAndLightning.entity.ModEntities;
import com.CCraze.ThunderAndLightning.particles.ParticleRegistry;
import com.CCraze.ThunderAndLightning.sounds.SoundRegistry;
import com.CCraze.ThunderAndLightning.sounds.LoopingTESound;
import com.CCraze.ThunderAndLightning.sounds.SoundEvents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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

        SoundRegistry.registerISound(() -> new LoopingTESound(SoundEvents.SKYSEEDERRUNNING, SoundCategory.BLOCKS, ModBlocks.SKYSEEDER_TILE, tileEntity -> {
            if (tileEntity instanceof SkySeederTile) return tileEntity.isRemoved() || ((SkySeederTile) tileEntity).realFanRPT > ((SkySeederTile) tileEntity).fanRPT;
            return true;
        }), new ResourceLocation("thunderandlightning:skyseederrunning"));
    }
}
