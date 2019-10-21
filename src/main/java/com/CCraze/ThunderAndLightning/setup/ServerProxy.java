package com.CCraze.ThunderAndLightning.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements IProxy {
    @Override
    public void init() {

    }

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Client Only");
    }

    @Override
    public PlayerEntity getPlayerEntity() {
        return null;
    }
}
