package com.CCraze.ThunderAndLightning.blocks.boiler;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import static com.CCraze.ThunderAndLightning.blocks.ModBlocks.BOILER_TILE;

public class BoilerTileEntity extends TileEntity implements ITickableTileEntity {

    public static FluidTank outFluid;

    public BoilerTileEntity() {
        super(BOILER_TILE);
        outFluid = new FluidTank(3000);
    }

    @Override
    public void tick() {

    }
}
