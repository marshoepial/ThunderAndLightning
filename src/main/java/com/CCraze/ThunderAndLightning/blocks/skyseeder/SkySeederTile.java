package com.CCraze.ThunderAndLightning.blocks.skyseeder;

import com.CCraze.ThunderAndLightning.blocks.ModBlocks;
import com.CCraze.ThunderAndLightning.networking.SoundPacket;
import com.CCraze.ThunderAndLightning.networking.TAndLPacketHandler;
import com.CCraze.ThunderAndLightning.particles.ParticleRegistry;
import com.CCraze.ThunderAndLightning.sounds.SoundEvents;
import com.CCraze.ThunderAndLightning.weather.TempestWeather;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Random;

public class SkySeederTile extends TileEntity implements ITickableTileEntity {
    public double pitch = 0;
    public double yaw = 0;
    public double fanRPT = 0;

    public double realYaw = 0;
    public double realPitch = 0;
    public double realFanRPT = 0;
    public double realFanRot = 0; //client only - will always be 0 on server, and never updated on server.

    public double spoolTicksUp = SoundEvents.SKYSEEDERONLENGTH;
    public double spoolTicksDown = SoundEvents.SKYSEEDEROFFLENGTH;
    public double maxFanSpeed = 50;

    private long ticksAtLastSound;
    private int lastSound = 2; //skySeederOn = 0, skySeederRunning = 1, skySeederOff = 2.

    public SkySeederTile() {
        super(ModBlocks.SKYSEEDER_TILE);
    }

    @Override
    public void tick() {
        boolean valsChanged = false;
        if (yaw > realYaw){
            realYaw = Math.min(realYaw + 5, yaw); valsChanged = true;
        } else if (yaw < realYaw){
            realYaw = Math.max(realYaw - 5, yaw); valsChanged = true;
        }

        if (pitch > realPitch){
            realPitch = Math.min(realPitch + 5, pitch); valsChanged = true;
        } else if (pitch < realPitch){
            realPitch = Math.max(realPitch - 5, pitch); valsChanged = true;
        }

        if (fanRPT > realFanRPT){
            double newRPT = realFanRPT + (1/spoolTicksUp)*fanRPT;
            realFanRPT = Math.min(fanRPT, newRPT); valsChanged = true;
        } else if (fanRPT < realFanRPT) {
            double newRPT = realFanRPT - (1 / spoolTicksDown)*fanRPT;
            realFanRPT = Math.max(newRPT, fanRPT); valsChanged = true;
        }
        if (valsChanged) {
            world.notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 2);
            markDirty();
        }

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
        if (realFanRPT > 0 && lastSound == 2){
            lastSound = 0;
            ticksAtLastSound = world.getGameTime();
            world.playSound(null, pos, SoundEvents.SKYSEEDERON, SoundCategory.BLOCKS, 1.0f, 1.0f);
        } else if (lastSound == 0 && world.getGameTime() - ticksAtLastSound >= SoundEvents.SKYSEEDERONLENGTH && !(fanRPT < realFanRPT)) {
            lastSound = 1;
            if (!world.isRemote) TAndLPacketHandler.SOUNDPACKETHANDLER.send(PacketDistributor.NEAR.with(() ->
                    new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 200, world.getDimension().getType())),
                    new SoundPacket(new ResourceLocation("thunderandlightning:skyseederrunning"), pos, 1.0, 1.0));
        } else if (fanRPT < realFanRPT){
            ticksAtLastSound = world.getGameTime();
            lastSound = 2;
            world.playSound(null, pos, SoundEvents.SKYSEEDEROFF, SoundCategory.BLOCKS, 1.0f, 1.0f);
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
        boolean isValid = false;
        int iters = 0;
        while (!isValid) {
            pitch = (random.nextDouble() - 0.5) * 20;
            yaw = random.nextDouble() * 360;
            if (getBlockInRaytrace(world, new Vec3d(pos.getX() + 0.5 + 0.3 * Math.cos(Math.toRadians(realPitch)) * Math.sin(Math.toRadians(realYaw)),
                            pos.getY() + 0.63 + 0.3 * Math.sin(Math.toRadians(-realPitch)),
                            pos.getZ() + 0.5 + 0.3 * Math.cos(Math.toRadians(realPitch)) * Math.cos(Math.toRadians(realYaw))),
                    new Vec2d(yaw, pitch), 5, false) == null){
                isValid = true;
                fanRPT = maxFanSpeed;
                markDirty();
            }
            else{
                if (iters > 10) {
                    yaw = 0; pitch = 0; break;
                }
                iters++;
            }
        }
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
