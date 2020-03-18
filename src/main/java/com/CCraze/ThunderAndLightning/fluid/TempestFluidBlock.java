package com.CCraze.ThunderAndLightning.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class TempestFluidBlock extends FlowingFluidBlock {
    public TempestFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties p_i48368_1_) {
        super(supplier, p_i48368_1_);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityCollision(state, worldIn, pos, entityIn);

        if (entityIn instanceof BoatEntity){
            Vec3d entityMotion = entityIn.getMotion();
            double multiplier = Math.sqrt(Math.pow(entityMotion.x, 2) + Math.pow(entityMotion.z, 2)) < 0.8 ? 1.01 : 1; //pythag theorem
            entityIn.setMotion(entityMotion.mul(multiplier, 1, multiplier));
        }
    }
}
