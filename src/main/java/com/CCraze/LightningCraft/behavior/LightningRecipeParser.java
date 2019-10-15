package com.CCraze.LightningCraft.behavior;

import com.CCraze.LightningCraft.recipes.LightningAttractorRecipe;
import com.CCraze.LightningCraft.setup.ModVals;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.CCraze.LightningCraft.setup.ModVals.RECIPEFILE;

public class LightningRecipeParser implements IFutureReloadListener{

    private List<LightningAttractorRecipe> recipeList = new ArrayList<>();
    private IResourceManager resourceManager;

    @Override
    public CompletableFuture<Void> reload(IStage iStage, IResourceManager iResourceManager, IProfiler iProfiler, IProfiler iProfiler1, Executor executor, Executor executor1) {
        return CompletableFuture.runAsync(() -> {
            loadRecipes(iResourceManager); //the method is separate because I want to call it outside of this completableFuture
        }, executor);
    }
    public void loadRecipes(IResourceManager ResourceManager){
        resourceManager = ResourceManager;
        if (!recipeList.isEmpty()) { //reset lists if they aren't empty (a.k.a. they have already been loaded)
            recipeList = new ArrayList<>();
        }
        ResourceLocation lightningRecipes = new ResourceLocation("lightningcraft",
                "lightning_recipes/recipes.json"); //points to json file
        InputStream streamInInternal = null;
        InputStream streamInExternal = null;
        try {
            streamInInternal = resourceManager.getResource(lightningRecipes).getInputStream(); //get an inputstream for the json
            streamInExternal = new FileInputStream(ModVals.RECIPEFILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser(); //parse thru the json's inputstream using google's handy methods
        JsonArray jsonArray;
        if (RECIPEFILE.length() != 0) {
            try {
                jsonArray = (JsonArray) jsonParser.parse(new InputStreamReader(streamInExternal, StandardCharsets.UTF_8));
                jsonArray.forEach(iterate -> { //iterate over each recipe
                    JsonObject currentObj = (JsonObject) iterate;
                    ItemStack initial = ForgeRegistries.ITEMS.getValue(new ResourceLocation(currentObj.get("initial").getAsString())).getDefaultInstance(); //initial block/item
                    ItemStack Final = ForgeRegistries.ITEMS.getValue(new ResourceLocation(currentObj.get("final").getAsString())).getDefaultInstance(); //final block/item
                    int maxCreate = currentObj.get("maxRepeat").getAsInt();
                    System.out.println("LightningRecipe generated: " + initial + " -> " + Final + " with max repeat of " + maxCreate);
                    recipeList.add(new LightningAttractorRecipe(initial, Final, maxCreate));
                });
            } catch (Exception e){
                System.out.println("Error loading external recipe file. Are you sure you wrote it correctly?");
                System.out.println(e);
            }
        }
        jsonArray = (JsonArray)jsonParser.parse(new InputStreamReader(streamInInternal, StandardCharsets.UTF_8));
        jsonArray.forEach(iterate -> { //iterate over each recipe
            JsonObject currentObj = (JsonObject) iterate;
            ItemStack initial = ForgeRegistries.ITEMS.getValue(new ResourceLocation(currentObj.get("initial").getAsString())).getDefaultInstance(); //initial block/item
            ItemStack Final = ForgeRegistries.ITEMS.getValue(new ResourceLocation(currentObj.get("final").getAsString())).getDefaultInstance(); //final block/item
            int maxCreate = currentObj.get("maxRepeat").getAsInt();
            System.out.println("LightningRecipe generated: "+initial+" -> "+Final+" with max repeat of "+ maxCreate);
            recipeList.add(new LightningAttractorRecipe(initial, Final, maxCreate));
        });
    }
    public Item swapItem (Item inItem){
        ItemStack inStack = inItem.getDefaultInstance();
        int index = -1;
        for (int i = 0; i < recipeList.size(); i++){
            System.out.println("Comparing "+recipeList.get(i).getInputIngredient().getItem()+" and "+inStack.getItem());
            if (recipeList.get(i).getInputIngredient().getItem().equals(inStack.getItem())){
                index = i;
                break;
            }
        }
        System.out.println("Recipe detected as recipe number "+index);
        if (index != -1){
            System.out.println("Replacing with "+recipeList.get(index).getOutputIngredient().getItem());
            return recipeList.get(index).getOutputIngredient().getItem();
        }
        return inItem;
    } public Block swapBlock (Block inBlock){
        ItemStack inStack = inBlock.asItem().getDefaultInstance();
        ItemStack outStack = inStack;
        for (LightningAttractorRecipe lightningAttractorRecipe : recipeList) {
            if (lightningAttractorRecipe.getInputIngredient().getItem().equals(inStack.getItem())) {
                outStack = lightningAttractorRecipe.getOutputIngredient();
                break;
            }
        }
        if (outStack != inStack && outStack.getItem() instanceof BlockItem) return ((BlockItem) outStack.getItem()).getBlock();
        return inBlock;
    } public int getMaxRepeat (Item inItem){
        ItemStack inStack = inItem.getDefaultInstance();
        int maxRepeat = -1;
        for (LightningAttractorRecipe lightningAttractorRecipe : recipeList) {
            if (lightningAttractorRecipe.getInputIngredient().getItem().equals(inStack.getItem())) {
                maxRepeat = lightningAttractorRecipe.getMaxRepeat();
                break;
            }
        }
        return maxRepeat;
    }

    public List<LightningAttractorRecipe> getRecipeList(){
        if (recipeList.isEmpty()){
            if (resourceManager != null) loadRecipes(resourceManager);
            else throw new NullPointerException("the ResourceManager for the Lightning Attractor Recipe Parser is empty, meaning jei integration is unavailable");
        }
        return recipeList;
    }

}
