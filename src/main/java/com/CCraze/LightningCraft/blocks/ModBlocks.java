package com.CCraze.LightningCraft.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {
    @ObjectHolder("lightningcraft:creativelightningattractor")
    public static creativeLightningAttractor CREATIVELIGHTNINGATTRACTOR;

    @ObjectHolder("lightningcraft:lightningattractortile")
    public static TileEntityType<lightningAttractorTile> LIGHTNINGATTRACTOR_TILE;
}
