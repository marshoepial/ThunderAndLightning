package com.CCraze.ThunderAndLightning.sounds;

import net.minecraft.util.math.BlockPos;

public interface ITAndLSound {
    void onClientReceived(BlockPos pos, double volume, double pitch);
}
