package com.CCraze.ThunderAndLightning.weather;

import com.CCraze.ThunderAndLightning.entity.BlueLightningBolt;
import com.CCraze.ThunderAndLightning.networking.BlueBoltEntityPacket;
import com.CCraze.ThunderAndLightning.networking.TAndLPacketHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;

public class TempestWeather {
    private static double fogRed;
    private static double fogBlue;
    private static double fogGreen;

    public static boolean tempestActive;

    private static final Random rand = new Random();

    public static void beginTempest (World world){
        if (!tempestActive){
            tempestActive = true;
            fogRed = world.getFogColor(1).x;
            fogGreen = Math.max(0, world.getFogColor(1).y - 0.2);
            fogBlue = world.getFogColor(1).z;
        }
    }
    public static void ticked (World world, Chunk chunk){
        if (tempestActive && world.dimension.canDoLightning(chunk) && rand.nextInt(1000) == 0){
            int x  = rand.nextInt(15)+chunk.getPos().getXStart();
            int z = rand.nextInt(15)+chunk.getPos().getZStart();
            BlockPos pos = new BlockPos(x, chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x, z), z);
            addBlueLightningBolt(pos, world);
        }
    }
    public static void addBlueLightningBolt (BlockPos pos, World world){
        BlueLightningBolt blueBolt = new BlueLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false);
        world.addEntity(blueBolt);
        TAndLPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new BlueBoltEntityPacket(blueBolt.getEntityId(), blueBolt.posX, blueBolt.posY, blueBolt.posZ));
    }
    public static void endTempest (){
        if (tempestActive) tempestActive = false;
    }

    public static double getFogRed() {
        return fogRed;
    }

    public static double getFogBlue() {
        return fogBlue;
    }

    public static double getFogGreen() {
        return fogGreen;
    }
}
