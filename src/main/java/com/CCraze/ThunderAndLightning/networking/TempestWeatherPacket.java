package com.CCraze.ThunderAndLightning.networking;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TempestWeatherPacket {
    public boolean starting;
    public boolean transition;

    public TempestWeatherPacket(boolean starting, boolean transition){
        //System.out.println("TempestWeatherPacket created");
        this.starting = starting;
        this.transition = transition;
    }

    public void encode (PacketBuffer buffer){
        buffer.writeBoolean(starting);
        buffer.writeBoolean(transition);
    }

    public static TempestWeatherPacket decode(PacketBuffer buffer){
        return new TempestWeatherPacket(buffer.readBoolean(), buffer.readBoolean());
    }

    public void onReceive(Supplier<NetworkEvent.Context> context){
        //System.out.println("TempestWeatherPacket received");
        ClientHandling.handleTempestWeatherPacket(this);
        context.get().setPacketHandled(true);
    }
}
