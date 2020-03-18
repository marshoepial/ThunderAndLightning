package com.CCraze.ThunderAndLightning.networking;

import com.CCraze.ThunderAndLightning.entity.BlueLightningBolt;
import com.CCraze.ThunderAndLightning.weather.TempestWeatherClient;
import net.minecraft.client.Minecraft;

public class ClientHandling {
    public static void handleBBEntityPacket(BlueBoltEntityPacket bbep){
        BlueLightningBolt bolt = new BlueLightningBolt(Minecraft.getInstance().world, bbep.x, bbep.y, bbep.z, true);
        bolt.setPacketCoordinates(bbep.x, bbep.y, bbep.z);
        bolt.rotationYaw = 0.0F;
        bolt.rotationPitch = 0.0F;
        bolt.setEntityId(bbep.entityId);
        Minecraft.getInstance().world.addLightning(bolt);
    }
    public static void handleTempestWeatherPacket(TempestWeatherPacket twp){
        TempestWeatherClient.setTempestActive(twp.starting, twp.transition);
    }
}
