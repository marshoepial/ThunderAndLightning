package com.CCraze.LightningCraft.behavior;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class LightningRecipeParser implements IFutureReloadListener{
    public List<String> initialThings = new ArrayList<>();
    public List<Object[]> finalThings = new ArrayList<>();

    @Override
    public CompletableFuture<Void> reload(IStage iStage, IResourceManager iResourceManager, IProfiler iProfiler, IProfiler iProfiler1, Executor executor, Executor executor1) {
        return CompletableFuture.runAsync(() -> {
            loadRecipes(iResourceManager); //the method is separate because I want to call it outside of this completableFuture
        }, executor);
    }
    public void loadRecipes(IResourceManager resourceManager){
        if (!initialThings.isEmpty() || !finalThings.isEmpty()){ //reset lists if they aren't empty (a.k.a. they have already been loaded)
            initialThings = new ArrayList<>();
            finalThings = new ArrayList<>();
        }
        ResourceLocation lightningRecipes = new ResourceLocation("lightningcraft",
                "lightning_recipes/recipes.json"); //points to json file
        InputStream streamIn = null;
        try {
            streamIn = resourceManager.getResource(lightningRecipes).getInputStream(); //get an inputstream for the json
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser(); //parse thru the json's inputstream using google's handy methods
        JsonArray jsonArray = (JsonArray)jsonParser.parse(new InputStreamReader(streamIn, StandardCharsets.UTF_8));
        jsonArray.forEach(iterate -> { //iterate over each recipe
            JsonObject currentObj = (JsonObject) iterate;
            String initial = currentObj.get("initial").getAsString(); //initial block/item
            String Final = currentObj.get("final").getAsString(); //final block/item
            int maxCreate = currentObj.get("maxRepeat").getAsInt();
            System.out.println("LightningRecipe generated: "+initial+" -> "+Final);
            initialThings.add(initial); //add them to a list
            finalThings.add(new Object[]{Final, maxCreate});
        });
    }
    public Item swapItem (Item inItem){
        String inString = inItem.getRegistryName().toString();
        int index = initialThings.indexOf(inString);
        System.out.println("Item detected as "+inString+", this is registered as recipe number "+index);
        if (index != -1){
            System.out.println("Returning "+new ResourceLocation((String)finalThings.get(index)[0]).toString());
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation((String)finalThings.get(index)[0]));
        }
        return inItem;
    } public Block swapBlock (Block inBlock){
        String inString = inBlock.getRegistryName().toString();
        int index = initialThings.indexOf(inString);
        System.out.println("Block detected as "+inString+", this is registered as recipe number "+index);
        if (index != -1) return ForgeRegistries.BLOCKS.getValue(new ResourceLocation((String)finalThings.get(index)[0]));
        return inBlock;
    } public int getMaxRepeat (Item inItem){
        String inString = inItem.getRegistryName().toString();
        int index = initialThings.indexOf(inString);
        if (index != -1) return (int)finalThings.get(index)[1];
        return -1;
    } public int getMaxRepeat (Block inBlock){
        String inString = inBlock.getRegistryName().toString();
        int index = initialThings.indexOf(inString);
        if (index != -1) return (int)finalThings.get(index)[1];
        return -1;
    }

}
