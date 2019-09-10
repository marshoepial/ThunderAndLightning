package com.CCraze.LightningCraft.behavior;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class AttractorEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT>{

    public AttractorEnergyStorage(int capacity, int transfer) {
        super(capacity, transfer);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("energy", getEnergyStored());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setEnergy(nbt.getInt("energy"));
    }

    public void setEnergy (int energy) {this.energy = energy;}


}
