package com.CCraze.LightningCraft.recipes;

import net.minecraft.item.ItemStack;

public class LightningAttractorRecipe{
    private ItemStack inputIngredient;
    private ItemStack outputIngredient;
    private int maxRepeat;

    public LightningAttractorRecipe(ItemStack in, ItemStack out, int repeat){
        this.inputIngredient = in;
        this.outputIngredient = out;
        this.maxRepeat = repeat;
    }
    public ItemStack getInputIngredient(){
        return inputIngredient;
    } public ItemStack getOutputIngredient(){
        return outputIngredient;
    } public int getMaxRepeat(){
        return maxRepeat;
    }
}
