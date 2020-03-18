package com.CCraze.ThunderAndLightning.blocks.skyseeder;

import com.CCraze.ThunderAndLightning.blocks.ModBlocks;
import com.CCraze.ThunderAndLightning.particles.ParticleRegistry;
import com.CCraze.ThunderAndLightning.weather.TempestWeather;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class SkySeederTile extends TileEntity implements ITickableTileEntity {
    public double pitch = 0;
    public double yaw = 0;
    public double fanRPT = 0;
    public boolean fanDown = false;

    public double realYaw = 0;
    public double realPitch = 0;
    public double realFanRPT = 0;
    public double realFanRot = 0;

    public double spoolTicks = 10;
    public double maxFanSpeed = 50;

    public SkySeederTile() {
        super(ModBlocks.SKYSEEDER_TILE);
    }

    @Override
    public void tick() {
        if (yaw > realYaw){
            realYaw = Math.min(realYaw + 5, yaw);
        } else if (yaw < realYaw){
            realYaw = Math.max(realYaw - 5, yaw);
        }

        if (pitch > realPitch){
            realPitch = Math.min(realPitch + 5, pitch);
        } else if (pitch < realPitch){
            realPitch = Math.max(realPitch - 5, pitch);
        }

        realFanRot += realFanRPT ;
        if (realFanRot >= 360) realFanRot =- 360;

        if (fanRPT > realFanRPT){
            double newRPT = realFanRPT + (1/spoolTicks);
            realFanRPT = Math.min(fanRPT, newRPT);
        } else if (fanRPT < realFanRPT) {
            double newRPT = realFanRPT - (1 / spoolTicks);
            realFanRPT = Math.max(newRPT, fanRPT);
        }

        world.notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 2);
        markDirty();

        /*if (!world.isRemote){
            if (getBlockInRaytrace(world, new Vec3d(pos.getX() + 0.5, pos.getY()+0.63, pos.getZ()+0.5),
                    new Vec2d(realYaw, realPitch), 5, false) == null) System.out.println("no block");
            else System.out.println("block");
        }*/

        if (realFanRPT >= 35){
            if (world.isRemote) {
                int pcount = world.rand.nextInt(10) + 5;
                for (int i = 0; i < pcount; i++) {
                    //ParticleRegistry.makeSkySeedParticle(world, pos.getX()+0.5+Math.sin(Math.toRadians(realYaw))*0.3, pos.getY()+0.63+spawnPoint[1],
                    //        pos.getZ()+0.5+Math.cos(Math.toRadians(realYaw))*0.3+Math.cos(Math.toRadians(realPitch))*0.3, realYaw, realPitch);
                    ParticleRegistry.makeSkySeedParticle(world, pos.getX() + 0.5 + 0.3 * Math.cos(Math.toRadians(realPitch)) * Math.sin(Math.toRadians(realYaw)),
                            pos.getY() + 0.63 + 0.3 * Math.sin(Math.toRadians(-realPitch)),
                            pos.getZ() + 0.5 + 0.3 * Math.cos(Math.toRadians(realPitch)) * Math.cos(Math.toRadians(realYaw)), realYaw, realPitch);
                }
            } else {
                TempestWeather.tick(world, world.getChunkAt(pos), this);
            }
        }
    } public static BlockState getBlockInRaytrace (World world, Vec3d vecPoint, Vec2d vecAngle, double lengthToCheck, boolean checkAtVecPoint){
        //x, y, z to step between checks
        double[] testInterval  = new double[]{Math.cos(Math.toRadians(vecAngle.y))*Math.sin(Math.toRadians(vecAngle.x)),
                Math.sin(Math.toRadians(-vecAngle.y)),
                Math.cos(Math.toRadians(vecAngle.y))*Math.cos(Math.toRadians(vecAngle.x))};
        for (int i = checkAtVecPoint ? 0 : 1; i <= Math.ceil(lengthToCheck); i++){
            BlockState bs = world.getBlockState(new BlockPos(vecPoint.x+testInterval[0]*i, vecPoint.y+testInterval[1]*i, vecPoint.z+testInterval[2]*i));
            if (!bs.isAir()) return bs;
        }
        return null;
    }

    public void activateSeeder(){
        Random random = new Random();
        pitch = (random.nextDouble()-0.5)*20;
        yaw = random.nextDouble()*360;
        fanRPT = maxFanSpeed;
        markDirty();
    }
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putDouble("pitch", pitch);
        compound.putDouble("yaw", yaw);
        compound.putDouble("fanRPT", fanRPT);
        compound.putDouble("realPitch", realPitch);
        compound.putDouble("realYaw", realYaw);
        compound.putDouble("realFanRPT", realFanRPT);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        pitch = compound.getDouble("pitch");
        yaw = compound.getDouble("yaw");
        fanRPT = compound.getDouble("fanRPT");
        realPitch = compound.getDouble("realPitch");
        realYaw = compound.getDouble("realYaw");
        realFanRPT = compound.getDouble("realFanRPT");
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return new SUpdateTileEntityPacket(this.pos, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }
}
