package com.CCraze.LightningCraft.blocks;

import com.CCraze.LightningCraft.blocks.lightningattractors.CreativeLightningAttractor;
import com.CCraze.LightningCraft.blocks.lightningattractors.DiamondLightningAttractor;
import com.CCraze.LightningCraft.blocks.lightningattractors.IronLightningAttractor;
import com.CCraze.LightningCraft.blocks.lightningattractors.WoolLightningAttractor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {
    @ObjectHolder("lightningcraft:creativelightningattractor")
    public static CreativeLightningAttractor CREATIVELIGHTNINGATTRACTOR;
    @ObjectHolder("lightningcraft:ironlightningattractor")
    public static IronLightningAttractor IRONLIGHTNINGATTRACTOR;
    @ObjectHolder("lightningcraft:diamondlightningattractor")
    public static DiamondLightningAttractor DIAMONDLIGHTNINGATTRACTOR;
    @ObjectHolder("lightningcraft:woollightningattractor")
    public static WoolLightningAttractor WOOLLIGHTNINGATTRACTOR;

    @ObjectHolder("lightningcraft:lightningattractortile")
    public static TileEntityType<LightningAttractorTile> LIGHTNINGATTRACTOR_TILE;
}
