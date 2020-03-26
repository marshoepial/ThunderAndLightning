package com.CCraze.ThunderAndLightning.sounds;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class SoundRegistry {
    private static BiMap<ResourceLocation, Supplier<? extends ITAndLSound>> soundMap = HashBiMap.create();

    public static void registerISound (Supplier<? extends ITAndLSound> sound, ResourceLocation resourceLocation){
        soundMap.put(resourceLocation, sound);
    }
    public static Supplier<? extends ITAndLSound> getSound (ResourceLocation resourceLocation){
        return soundMap.get(resourceLocation);
    }
    public static ResourceLocation getResourceLoc (ISound sound){
        return soundMap.inverse().get(sound);
    }
}