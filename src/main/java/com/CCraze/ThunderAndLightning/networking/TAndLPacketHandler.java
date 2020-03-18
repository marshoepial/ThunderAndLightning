package com.CCraze.ThunderAndLightning.networking;

import com.CCraze.ThunderAndLightning.ThunderAndLightning;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class TAndLPacketHandler{
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel BLUEBOLTHANDLER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ThunderAndLightning.MODID, "bluebolthandler"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static final SimpleChannel TEMPESTWEATHERHANDLER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ThunderAndLightning.MODID, "tempestweatherhandler"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
}

