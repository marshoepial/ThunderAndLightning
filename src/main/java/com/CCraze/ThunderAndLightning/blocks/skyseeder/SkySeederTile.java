package com.CCraze.ThunderAndLightning.blocks.skyseeder;

import com.CCraze.ThunderAndLightning.blocks.ModBlocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.Random;

public class SkySeederTile extends TileEntity{
    public double pitch = 0;
    public double yaw = 0;
    public double fanRPT = 0;
    public boolean fanDown = false;

    public double prevTicks = 0;

    public double realYaw = 0;
    public double realPitch = 0;
    public double realFanRPT = 0;
    public double realFanRot = 0;

    public double spoolTicks = 10;
    public double maxFanSpeed = 10;

    public SkySeederTile() {
        super(ModBlocks.SKYSEEDER_TILE);
        System.out.println("SkySeeder TE Created");
    }

    public void activateSeeder(){
        Random random = new Random();
        pitch = random.nextDouble()*20;
        yaw = (random.nextDouble()-0.5)*360;
        fanRPT = maxFanSpeed;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putDouble("pitch", pitch);
        compound.putDouble("yaw", yaw);
        compound.putDouble("fanRPT", fanRPT);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        pitch = compound.getDouble("pitch");
        yaw = compound.getDouble("yaw");
        fanRPT = compound.getDouble("fanRPT");
    }
}
