package com.CCraze.ThunderAndLightning.networking;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BlueBoltEntityPacket{
    public int entityId;
    public double x;
    public double y;
    public double z;

    public BlueBoltEntityPacket(int entityId, double x, double y, double z){
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void encode (PacketBuffer buffer){
        buffer.writeVarInt(entityId);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
    }

    public static BlueBoltEntityPacket decode(PacketBuffer buffer){
        return new BlueBoltEntityPacket(buffer.readVarInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public void onReceive(Supplier<NetworkEvent.Context> context){
        ClientHandling.handleBBEntityPacket(this);
        context.get().setPacketHandled(true);
    }
}
