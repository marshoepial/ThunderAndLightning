package com.CCraze.ThunderAndLightning;

import com.CCraze.ThunderAndLightning.weather.TempestWeather;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void changeFogColor(EntityViewRenderEvent.FogColors event){
        if (TempestWeather.tempestActive){
            event.setRed((float)TempestWeather.getFogRed());
            event.setBlue((float) TempestWeather.getFogBlue());
            event.setGreen((float) TempestWeather.getFogGreen());
        }
    }
}
