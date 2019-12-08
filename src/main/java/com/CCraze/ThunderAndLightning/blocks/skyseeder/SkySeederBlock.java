package com.CCraze.ThunderAndLightning.blocks.skyseeder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class SkySeederBlock extends Block {
    public static final IntegerProperty modelProperty = IntegerProperty.create("usemodel", 0, 3);
    public SkySeederBlock() {
        super(Block.Properties.create(Material.ANVIL));
        setDefaultState(getStateContainer().getBaseState().with(modelProperty, 0));
        setRegistryName("skyseeder");
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SkySeederTile();
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT; //lets game know that our block is partially transparent and should be rendered as such
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(modelProperty);
    }


}
