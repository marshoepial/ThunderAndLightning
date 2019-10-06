package com.CCraze.LightningCraft.jei;

import com.CCraze.LightningCraft.blocks.ModBlocks;
import com.CCraze.LightningCraft.recipes.LightningAttractorRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class LightningAttractorCategory implements IRecipeCategory<LightningAttractorRecipe> {
    private ResourceLocation guibg = new ResourceLocation("lightningcraft:textures/gui/lightning_attractor.png");
    //private ResourceLocation guibg = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");
    private IDrawable background;
    private IDrawable icon;
    private IDrawableAnimated arrow;
    private IDrawableAnimated bolt;

    public LightningAttractorCategory(IGuiHelper helper){
        super();
        //original coords: 0, 17, 77, 54
        this.background = helper.createDrawable(guibg, 0, 17, 77, 54);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.IRONLIGHTNINGATTRACTOR));
        this.arrow = helper.drawableBuilder(guibg, 54, 0, 22, 16).buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.bolt = helper.drawableBuilder(guibg, 43, 0, 11, 16).buildAnimated(100, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("lightningcraft", "lightningattractor");
    }

    @Override
    public Class<? extends LightningAttractorRecipe> getRecipeClass() {
        return LightningAttractorRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Lightning Attractor";
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(LightningAttractorRecipe lightningAttractorRecipe, IIngredients iIngredients) {
        iIngredients.setInput(VanillaTypes.ITEM, lightningAttractorRecipe.getInputIngredient());
        iIngredients.setOutput(VanillaTypes.ITEM, lightningAttractorRecipe.getOutputIngredient());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, LightningAttractorRecipe lightningAttractorRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup itemStackGroup = iRecipeLayout.getItemStacks();
        List<ItemStack> attractorList = new ArrayList<>();
        attractorList.add(new ItemStack(ModBlocks.CREATIVELIGHTNINGATTRACTOR));
        attractorList.add(new ItemStack(ModBlocks.IRONLIGHTNINGATTRACTOR));
        itemStackGroup.init(0, true, 0, 18);
        itemStackGroup.set(0, lightningAttractorRecipe.getInputIngredient());
        itemStackGroup.init(1, false, 55, 18);
        itemStackGroup.set(1, lightningAttractorRecipe.getOutputIngredient());
        itemStackGroup.init(2, false, 0, 38);
        itemStackGroup.set(2, attractorList);

    }

    @Override
    public void draw(LightningAttractorRecipe recipe, double mouseX, double mouseY) {
        String maxRepeatString = "Max Stack:"+recipe.getMaxRepeat();
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.fontRenderer;
        fr.drawString(maxRepeatString, 16, 2, 0x575757);
        this.arrow.draw(23, 19);
        this.bolt.draw(3, 1);
    }
}
