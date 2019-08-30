package com.CCraze.LightningCraft.attractors;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class AttractWorld implements IAttractWorld {

    private List<BlockPos> attractorList = new ArrayList<BlockPos>();

    @Override
    public void add(BlockPos pos) {
        this.attractorList.add(pos);
    }

    @Override
    public void remove(BlockPos pos) {
        this.attractorList.remove(pos);
    }

    @Override
    public BlockPos getIndex(int index) {
        return this.attractorList.get(index);
    }

    @Override
    public int getSize() {
        return this.attractorList.size();
    }

    @Override
    public List<BlockPos> getList() {
        return this.attractorList;
    }
}
