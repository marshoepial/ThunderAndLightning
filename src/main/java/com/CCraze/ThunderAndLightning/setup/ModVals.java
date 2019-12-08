package com.CCraze.ThunderAndLightning.setup;

import com.CCraze.ThunderAndLightning.ThunderAndLightning;
import com.CCraze.ThunderAndLightning.blocks.ModBlocks;
import com.CCraze.ThunderAndLightning.config.ThunderLightningConfig;
import com.CCraze.ThunderAndLightning.entity.BlueLightningBolt;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModVals {
    public static ItemGroup modGroup = new ItemGroup("thunderandlightning") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.IRONLIGHTNINGATTRACTOR);
        }
    };
    public static List<ItemStack> attractorList = new ArrayList<>();

    public static final File CONFIGFILE = new File("config/"+ ThunderAndLightning.MODID+"/"+ ThunderAndLightning.MODID+".cfg");
    public static final File RECIPEFILE = new File("config/"+ ThunderAndLightning.MODID+"/recipes.json");
    public static ThunderLightningConfig thunderLightningConfig = ThunderAndLightning.CONFIG;

    public static final ResourceLocation SKYSEEDER_MODEL_BASE = new ResourceLocation("thunderandlightning","block/skyseeder/base");
    public static final ResourceLocation SKYSEEDER_MODEL_FAN = new ResourceLocation("thunderandlightning","block/skyseeder/fan");
    public static final ResourceLocation SKYSEEDER_MODEL_HEAD = new ResourceLocation("thunderandlightning","block/skyseeder/head");

    public ModVals(){
        attractorList.add(new ItemStack(ModBlocks.CREATIVELIGHTNINGATTRACTOR));
        attractorList.add(new ItemStack(ModBlocks.IRONLIGHTNINGATTRACTOR));
        attractorList.add(new ItemStack(ModBlocks.DIAMONDLIGHTNINGATTRACTOR));
        attractorList.add(new ItemStack(ModBlocks.WOOLLIGHTNINGATTRACTOR));
    }
}
