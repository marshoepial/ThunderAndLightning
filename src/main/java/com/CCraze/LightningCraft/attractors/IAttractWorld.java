package com.CCraze.LightningCraft.attractors;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface IAttractWorld {
    void add(BlockPos pos);
    void remove(BlockPos pos);
    BlockPos getIndex(int index);
    int getSize();
    List<BlockPos> getList();
}

