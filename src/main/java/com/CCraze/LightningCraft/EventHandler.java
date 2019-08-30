package com.CCraze.LightningCraft;

import com.CCraze.LightningCraft.behavior.BlockSetter;
import com.CCraze.LightningCraft.blocks.blockTest;
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
    public static List<BlockPos> attractorPos = new ArrayList<>();

    @SubscribeEvent
    public static void blockPlace(BlockEvent.EntityPlaceEvent event){
        System.out.println("Entity" + event.getState().getBlock() + " placed at " + event.getPos());
        if (event.getState().getBlock() instanceof blockTest) {
            boolean isTop = true;
            BlockPos pos = event.getPos();
            for (int i = 1; i < 255 - event.getPos().getY(); i++) {
                if (event.getWorld().getBlockState(pos.up(i)).getMaterial() != Material.AIR) {
                    isTop = false;
                    break;
                }
            }
            if (isTop) {
                System.out.println("Block just placed has no blocks above it!");
                attractorPos.add(pos);
            }
        } else {
            for (int i = 0; i < attractorPos.size(); i++){
                if (attractorPos.get(i).getX() == event.getPos().getX() && attractorPos.get(i).getZ() == event.getPos().getZ() && attractorPos.get(i).getY() < event.getPos().getY()){
                    attractorPos.remove(i);
                    System.out.println("Block placed above attractor, removed from list");
                }
            }
        }
    }

    @SubscribeEvent
    public static void blockBreak(BlockEvent.BreakEvent event){
        if (event.getState().getBlock() instanceof blockTest){
            int brokenIndex = attractorPos.indexOf(event.getPos());
            if (brokenIndex != -1){
                attractorPos.remove(brokenIndex);
                System.out.println("Attractor broken, removed from list.");
            }
        }
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
                        BlockPos pos = BlockSetter.setBlock(e, world);
                        e.setPosition((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
                        System.out.println("Lightning strike at "+e.getPosition());
                    }
                    return true;
                }
            }, "field_217428_a"); //globalEntities obfuscated name
        }
    }
}
