package com.CCraze.ThunderAndLightning.setup;

import com.CCraze.ThunderAndLightning.blocks.ModBlocks;
import com.CCraze.ThunderAndLightning.blocks.skyseeder.SkySeederTERenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy{

    @Override
    public void init() {
        System.out.println("Client initalizing");
        ClientRegistry.bindTileEntityRenderer(ModBlocks.SKYSEEDER_TILE, SkySeederTERenderer::new);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getPlayerEntity(){return Minecraft.getInstance().player;}

}
