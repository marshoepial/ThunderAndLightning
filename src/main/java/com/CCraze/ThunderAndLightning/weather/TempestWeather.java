package com.CCraze.ThunderAndLightning.weather;

import com.CCraze.ThunderAndLightning.behavior.BlockSetter;
import com.CCraze.ThunderAndLightning.blocks.skyseeder.SkySeederTile;
import com.CCraze.ThunderAndLightning.entity.BlueLightningBolt;
import com.CCraze.ThunderAndLightning.networking.BlueBoltEntityPacket;
import com.CCraze.ThunderAndLightning.networking.TAndLPacketHandler;
import com.CCraze.ThunderAndLightning.networking.TempestWeatherPacket;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.*;

//TODO: Lower light levels during tempest

public class TempestWeather{
    private static long prevTick;

    private static final Random rand = new Random();
    private static long ticksSinceWorldLoad;

    //DO NOT TRY TO UNBOX NON-PRIMITIVE BOOLEANS

    private static BiMap<Chunk, SkySeederTile> isActiveInChunk = HashBiMap.create();
    private static Map<Chunk, Boolean> isChunkColored = new HashMap<>();
    private static Map<PlayerEntity, Boolean> isInColoredChunk = new HashMap<>();
    private static Map<SkySeederTile, Long> tickNums = new HashMap<>();

    public static void tick (World world, Chunk chunk, SkySeederTile tile){
        //System.out.println("Ticked!");
        checkPlayerLoc(world);
        long ticks = world.getGameTime();
        ticksSinceWorldLoad++;
        if (ticks > prevTick){
            List<SkySeederTile> removeList = new ArrayList<>();
            tickNums.forEach((skySeederTile, heldTicks) -> {
                if (heldTicks < prevTick) {
                    Chunk removeChunk = isActiveInChunk.inverse().remove(skySeederTile);
                    handleTileUpdates(removeChunk, world);
                    removeList.add(skySeederTile);
                }
            });
            for (SkySeederTile removeTile : removeList) tickNums.remove(removeTile);
            prevTick = ticks;
        }
        if (tile != null && chunk != null) {
            tickNums.put(tile, ticks);
            if (!isTempestActive(chunk) && world.dimension.canDoLightning(chunk)) {
                isActiveInChunk.put(chunk, tile);
                tickNums.put(tile, ticks);
                handleTileUpdates(chunk, world);
            }
            if (isTempestActive(chunk) && world.dimension.canDoLightning(chunk) && rand.nextInt(100) == 0) {
                int x = rand.nextInt(15) + chunk.getPos().getXStart();
                int z = rand.nextInt(15) + chunk.getPos().getZStart();
                BlockPos pos = new BlockPos(x, chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x, z), z);
                addBlueLightningBolt(pos, world);
            }
        }
    }
    public static void addBlueLightningBolt (BlockPos pos, World world){
        BlueLightningBolt blueBolt = new BlueLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false);
        BlockSetter.boltAdded(blueBolt);
        TAndLPacketHandler.BLUEBOLTHANDLER.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 128, world.dimension.getType())),
                new BlueBoltEntityPacket(blueBolt.getEntityId(), blueBolt.getPosX(), blueBolt.getPosY(), blueBolt.getPosZ()));
    }
    private static void handleTileUpdates(Chunk chunk, World world){
        if (chunk == null) return; //we crash if we remove the block via command, this makes sure everything's still there
        boolean[][] isColorChanged = new boolean[5][5];
        BlockPos centralPos = new BlockPos(chunk.getPos().getXStart(), 1, chunk.getPos().getZStart());
        for (int i = -2; i <= 2; i++){
            for (int j = -2; j <= 2; j++){
                if (isActiveInChunk.get(world.getChunkAt(new BlockPos(centralPos.getX() + 16*i, 1, centralPos.getZ() + 16*j))) != null){
                    for (int k = Math.max(-2, i-1); k <= Math.min(i+1, 2); k++){
                        for (int l = Math.max(-2, i-1); l <= Math.min(i+1, 2); l++){
                            isColorChanged[k+2][l+2] = true;
                        }
                    }
                }
            }
        }
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                isChunkColored.put(world.getChunkAt(new BlockPos(chunk.getPos().getXStart()+16*i, 1, chunk.getPos().getZStart()+16*j)), isColorChanged[i+2][j+2]);
            }
        }
    } private static void checkPlayerLoc(World world){
        List<? extends PlayerEntity> players = world.getPlayers();
        for (PlayerEntity player : players) {
            if (safelyCheckBool(isChunkColored.get(world.getChunkAt(player.getPosition())), true, false)
                    && safelyCheckBool(isInColoredChunk.get(player), false, true) && player instanceof ServerPlayerEntity){
                TAndLPacketHandler.TEMPESTWEATHERHANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new TempestWeatherPacket(true, ticksSinceWorldLoad > 50));
                isInColoredChunk.put(player, true);
            } else if (safelyCheckBool(isChunkColored.get(world.getChunkAt(player.getPosition())), false, true)
                    && safelyCheckBool(isInColoredChunk.get(player), true, false) && player instanceof ServerPlayerEntity){
                TAndLPacketHandler.TEMPESTWEATHERHANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new TempestWeatherPacket(false, ticksSinceWorldLoad > 50));
                isInColoredChunk.put(player, false);
            }
        }
    }
    public static void tempestServerOnLoad(){
        isActiveInChunk.clear();
        tickNums.clear();
        prevTick = 0;
        ticksSinceWorldLoad = 0;
    }
    public static boolean isTempestActive(Chunk chunk){
        return isActiveInChunk.get(chunk) != null;
    }
    private static boolean safelyCheckBool(Boolean bool, boolean checkVal, boolean includeNull) {
        if (checkVal){
            if (includeNull) return bool == null || bool;
            else return bool != null && bool;
        } else {
            if (includeNull) return bool == null || !bool;
            else return bool != null && !bool;
        }
    }
}
