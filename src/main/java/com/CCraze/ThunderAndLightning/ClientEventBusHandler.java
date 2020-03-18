package com.CCraze.ThunderAndLightning;

import com.CCraze.ThunderAndLightning.weather.TempestWeather;
import com.CCraze.ThunderAndLightning.weather.TempestWeatherClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientEventBusHandler {
    @SubscribeEvent
    public static void changeFogColor(EntityViewRenderEvent.FogColors event){
        ClientWorld world = Minecraft.getInstance().world;
        TempestWeatherClient.setFogColorsClear(new double[]{event.getRed(), event.getGreen(), event.getBlue()});
        TempestWeatherClient.handleFogColor(event, world);
    }
}
