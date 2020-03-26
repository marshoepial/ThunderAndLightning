package com.CCraze.ThunderAndLightning.networking;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SoundPacket {
    public ResourceLocation resourceLocation;
    public BlockPos pos;
    public double volume;
    public double pitch;
    public SoundPacket(ResourceLocation resourceLocation, BlockPos pos, double volume, double pitch){
        this.resourceLocation = resourceLocation;
        this.pos = pos;
        this.volume = volume;
        this.pitch = pitch;
    }
    public void encode(PacketBuffer buffer){
        buffer.writeResourceLocation(resourceLocation);
        buffer.writeBlockPos(pos);
        buffer.writeDouble(volume);
        buffer.writeDouble(pitch);
    }
    public static SoundPacket decode(PacketBuffer buffer){
        return new SoundPacket(buffer.readResourceLocation(), buffer.readBlockPos(), buffer.readDouble(), buffer.readDouble());
    }
    public void onReceive(Supplier<NetworkEvent.Context> context){
        ClientHandling.handleISoundPacket(this);
        context.get().setPacketHandled(true);
    }
}
