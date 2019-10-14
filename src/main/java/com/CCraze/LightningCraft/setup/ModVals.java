package com.CCraze.LightningCraft.setup;

import com.CCraze.LightningCraft.LightningCraft;
import com.CCraze.LightningCraft.blocks.ModBlocks;
import com.CCraze.LightningCraft.config.LightningCraftConfig;
import com.CCraze.LightningCraft.items.LightningAttractorBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModVals {
    public static ItemGroup modGroup = new ItemGroup("lightningcraft") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.IRONLIGHTNINGATTRACTOR);
        }
    };
    public static List<ItemStack> attractorList = new ArrayList<>();

    public static final File CONFIGFILE = new File("config/"+LightningCraft.MODID+"/"+LightningCraft.MODID+".cfg");
    public static final File RECIPEFILE = new File("config/"+LightningCraft.MODID+"/recipes.json");
    public static boolean recipeFileInUse = false;
    public static LightningCraftConfig lightningCraftConfig = LightningCraft.CONFIG;

    public ModVals(){
        attractorList.add(new ItemStack(ModBlocks.CREATIVELIGHTNINGATTRACTOR));
        attractorList.add(new ItemStack(ModBlocks.IRONLIGHTNINGATTRACTOR));
        attractorList.add(new ItemStack(ModBlocks.DIAMONDLIGHTNINGATTRACTOR));
        attractorList.add(new ItemStack(ModBlocks.WOOLLIGHTNINGATTRACTOR));
    }
}
