package com.CCraze.ThunderAndLightning;

import com.CCraze.ThunderAndLightning.behavior.BlockSetter;
import com.CCraze.ThunderAndLightning.behavior.LightningRecipeParser;
import com.CCraze.ThunderAndLightning.weather.TempestWeather;
import com.CCraze.ThunderAndLightning.weather.TempestWeatherClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.util.ArrayList;


@Mod.EventBusSubscriber
public class ServerEventHandler {
    public static LightningRecipeParser recipeParser = new LightningRecipeParser();

    @SubscribeEvent
    public static void serverStartingEvent(FMLServerStartingEvent event) {
        IResourceManager resourceManager = event.getServer().getResourceManager(); //gets the ResourceManager
        //System.out.println("serverStartingEvent Achieved!");
        ((IReloadableResourceManager) resourceManager).addReloadListener(recipeParser); //adds reloadlistener (/reload)
        recipeParser.loadRecipes(resourceManager); //loads recipes on world start
    }

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote()) {
            TempestWeather.tempestServerOnLoad();

            //Gets server instance of the world. Only the server instance has the globalEntities list.
            ServerWorld worldServer = (ServerWorld) event.getWorld(); //This is castable because we know we are on the logical server

            //adds code to add function of globalEntities list to modify entities as they are added.
            ObfuscationReflectionHelper.setPrivateValue(ServerWorld.class, worldServer, new ArrayList<Entity>() {
                @Override
                public boolean add(Entity e) {
                    if (e instanceof LightningBoltEntity) {
                        BlockSetter.boltAdded(e);
                        //System.out.println("Lightning strike at "+e.getPosition());
                    } else super.add(e);
                    return true;
                }
            }, "field_217497_w"); //globalEntities obfuscated name
        } else {
            TempestWeatherClient.tempestClientOnLoad();
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event){
        TempestWeather.tick(event.world, null, null);
    }
}
