package com.CCraze.LightningCraft;

import com.CCraze.LightningCraft.behavior.BlockSetter;
import com.CCraze.LightningCraft.blocks.creativeLightningAttractor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber
public class EventHandler {

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
                        e.setPosition((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
                        System.out.println("Lightning strike at "+e.getPosition());
                    }
                    return true;
                }
            }, "field_217428_a"); //globalEntities obfuscated name
        }
    }
}
