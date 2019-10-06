package com.CCraze.LightningCraft.jei;

import com.CCraze.LightningCraft.ForgeEventHandler;
import com.CCraze.LightningCraft.behavior.LightningRecipeParser;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class LightningCraftJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("lightningcraft", "lightningcraft");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new LightningAttractorCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ForgeEventHandler.recipeParser.getRecipeList(), new ResourceLocation("lightningcraft", "lightningattractor"));
    }
}
