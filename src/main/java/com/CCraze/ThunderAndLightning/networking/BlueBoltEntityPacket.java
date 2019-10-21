package com.CCraze.ThunderAndLightning.networking;

import com.CCraze.ThunderAndLightning.entity.BlueLightningBolt;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BlueBoltEntityPacket{
    private int entityId;
    private double x;
    private double y;
    private double z;

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
        BlueLightningBolt bolt = new BlueLightningBolt(Minecraft.getInstance().world, x, y, z, true);
        bolt.func_213312_b(x, y, z);
        bolt.rotationYaw = 0.0F;
        bolt.rotationPitch = 0.0F;
        bolt.setEntityId(entityId);
        Minecraft.getInstance().world.addLightning(bolt);
        context.get().setPacketHandled(true);
    }
}
