package com.CCraze.ThunderAndLightning.blocks.boiler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BoilerBlock extends Block {
    public BoilerBlock() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(3f).harvestTool(ToolType.PICKAXE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

}
