package com.CCraze.LightningCraft.attractors;

import net.minecraft.nbt.CollectionNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class AttractorSave implements Capability.IStorage<IAttractWorld> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IAttractWorld> capability, IAttractWorld instance, Direction side) {
        ListNBT nbtList = new ListNBT();
        nbtList.
        for (int i = 0; i < instance.getSize(); i++){

        }
    }

    @Override
    public void readNBT(Capability<IAttractWorld> capability, IAttractWorld instance, Direction side, INBT nbt) {

    }
}
