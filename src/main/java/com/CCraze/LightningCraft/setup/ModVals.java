package com.CCraze.LightningCraft.setup;

import com.CCraze.LightningCraft.blocks.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModVals {
    public static ItemGroup modGroup = new ItemGroup("lightningcraft") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.IRONLIGHTNINGATTRACTOR);
        }
    };
}
