package com.CCraze.LightningCraft.behavior;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
    public List<String> finalThings = new ArrayList<>();
    public Item swapItem(Item inItem){
        return null;
    }

    @Override
    public CompletableFuture<Void> reload(IStage iStage, IResourceManager iResourceManager, IProfiler iProfiler, IProfiler iProfiler1, Executor executor, Executor executor1) {
        return CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                if (!initialThings.isEmpty() || !finalThings.isEmpty()){
                    initialThings = new ArrayList<>();
                    finalThings = new ArrayList<>();
                }
                ResourceLocation lightningRecipes = new ResourceLocation("lightningcraft",
                        "lightning_recipes/recipes.json");
                InputStream streamIn = null;
                try {
                    streamIn = iResourceManager.getResource(lightningRecipes).getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonArray = (JsonArray)jsonParser.parse(new InputStreamReader(streamIn, StandardCharsets.UTF_8));
                jsonArray.forEach(iterate -> {
                    JsonObject currentObj = (JsonObject) iterate;
                    String initial = currentObj.get("initial").getAsString();
                    String Final = currentObj.get("final").getAsString();
                    System.out.println("LightningRecipe generated: "+initial+" -> "+Final);
                    initialThings.add(initial);
                    finalThings.add(Final);
                });
            }
        }, executor);
    }
}
