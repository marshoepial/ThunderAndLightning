package com.CCraze.ThunderAndLightning;

import com.CCraze.ThunderAndLightning.behavior.BlockSetter;
import com.CCraze.ThunderAndLightning.behavior.LightningRecipeParser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
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
    public static void worldLoad(WorldEvent.Load event){
        World world = event.getWorld().getWorld();
        if (!world.isRemote()){
            //Gets server instance of the world. Only the server instance has the globalEntities list.
            ServerWorld worldServer = DimensionManager.getWorld(event.getWorld().getWorld().getServer(), DimensionType.OVERWORLD, false, false);

            //adds code to add function of globalEntities list to move lightningbolts according to setblock method.
            ObfuscationReflectionHelper.setPrivateValue(ServerWorld.class, worldServer, new ArrayList<Entity>(){
                @Override
                public boolean add(Entity e){
                    if (e instanceof LightningBoltEntity) {
                        BlockPos pos = BlockSetter.setBlock(e);
                        BlockPos initialStrikePos = e.getPosition();
                        new LightningBoltEntity(e.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), false);
                        e.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        e.world.playSound(null, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + e.world.rand.nextFloat() * 0.2F);
                        e.world.playSound(null, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + e.world.rand.nextFloat() * 0.2F);
                        BlockSetter.cleanFire(e.getEntityWorld(), initialStrikePos, pos);
                        //System.out.println("Lightning strike at "+e.getPosition());
                    } else super.add(e);
                    return true;
                }
            }, "field_217497_w"); //globalEntities obfuscated name
        }
    }
}
