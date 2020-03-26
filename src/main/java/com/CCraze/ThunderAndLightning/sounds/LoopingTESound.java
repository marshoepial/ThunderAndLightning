package com.CCraze.ThunderAndLightning.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;


public class LoopingTESound extends LocatableSound implements ITickableSound, ITAndLSound {
    private TileEntityType<?> tileEntityType;
    private TileEntity tileEntity;
    private Predicate<TileEntity> shouldStillLoop; //makes the process generic

    public LoopingTESound(SoundEvent soundIn, SoundCategory categoryIn, TileEntityType<?> tileEntityType, Predicate<TileEntity> shouldStillLoop) {
        super(soundIn, categoryIn);
        this.tileEntityType = tileEntityType;
        this.shouldStillLoop = shouldStillLoop;
        repeat = true;
    }

    public void onClientReceived(BlockPos pos, double volume, double pitch){
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.volume = (float) volume;
        this.pitch = (float) pitch;
        System.out.println(this.getSoundLocation());
        if (Minecraft.getInstance().world.getTileEntity(pos).getType().equals(tileEntityType)) {
            Minecraft.getInstance().getSoundHandler().play(this);
            tileEntity = Minecraft.getInstance().world.getTileEntity(pos);
        }
    }

    @Override
    public boolean isDonePlaying() {
        System.out.println(shouldStillLoop.test(tileEntity));
        return shouldStillLoop.test(tileEntity);
    }

    @Override
    public void tick() {
    }
}