package com.CCraze.LightningCraft.behavior;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class AttractorEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT>{

    public final int dissipateTime = 4;

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

    public void setEnergy (int inEnergy) {
        System.out.println("Setting energy value at "+inEnergy);
        this.energy = inEnergy;
        System.out.println("Energy set at "+this.energy);
    }
    public void dissipateEnergy(){
        int energyToRelease = capacity/(dissipateTime*20);
        energy = energy - energyToRelease;
    }
    public void removeEnergy(int energyToRemove){
        energy = energy - energyToRemove;
    }


}