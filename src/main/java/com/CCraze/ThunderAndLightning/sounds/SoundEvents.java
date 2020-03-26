package com.CCraze.ThunderAndLightning.sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class SoundEvents {
    public static final SoundEvent SKYSEEDERON = makeSounds("skyseederon");
    public static final SoundEvent SKYSEEDERRUNNING = makeSounds("skyseederrunning");
    public static final SoundEvent SKYSEEDEROFF = makeSounds("skyseederoff");

    public static final double SKYSEEDERONLENGTH = 55.2f;
    public static final double SKYSEEDERRUNNINGLENGTH = 297.4f;
    public static final double SKYSEEDEROFFLENGTH = 168;

    public static void registerSounds(RegistryEvent.Register<SoundEvent> event){
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        registry.register(SKYSEEDERON);
        registry.register(SKYSEEDERRUNNING);
        registry.register(SKYSEEDEROFF);
    }
    private static SoundEvent makeSounds(String location){
        return new SoundEvent(new ResourceLocation("thunderandlightning", location))
                .setRegistryName("thunderandlightning", location);
    }
}
