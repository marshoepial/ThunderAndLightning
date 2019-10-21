package com.CCraze.ThunderAndLightning.setup;

import com.CCraze.ThunderAndLightning.entity.BlueLightningBolt;
import com.CCraze.ThunderAndLightning.entity.BlueLightningBoltRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy{

    @Override
    public void init() {
        System.out.println("Client initalizing");
        RenderingRegistry.registerEntityRenderingHandler(BlueLightningBolt.class, BlueLightningBoltRenderer::new);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getPlayerEntity(){return Minecraft.getInstance().player;}

}
