package com.CCraze.ThunderAndLightning.blocks;

import com.CCraze.ThunderAndLightning.blocks.boiler.BoilerTileEntity;
import com.CCraze.ThunderAndLightning.blocks.lightningattractors.*;
import com.CCraze.ThunderAndLightning.blocks.skyseeder.SkySeederBlock;
import com.CCraze.ThunderAndLightning.blocks.skyseeder.SkySeederTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {
    @ObjectHolder("thunderandlightning:creativelightningattractor")
    public static CreativeLightningAttractor CREATIVELIGHTNINGATTRACTOR;
    @ObjectHolder("thunderandlightning:ironlightningattractor")
    public static IronLightningAttractor IRONLIGHTNINGATTRACTOR;
    @ObjectHolder("thunderandlightning:diamondlightningattractor")
    public static DiamondLightningAttractor DIAMONDLIGHTNINGATTRACTOR;
    @ObjectHolder("thunderandlightning:woollightningattractor")
    public static WoolLightningAttractor WOOLLIGHTNINGATTRACTOR;
    @ObjectHolder("thunderandlightning:skyseeder")
    public static SkySeederBlock SKYSEEDER;

    @ObjectHolder("thunderandlightning:lightningattractortile")
    public static TileEntityType<LightningAttractorTile> LIGHTNINGATTRACTOR_TILE;
    @ObjectHolder("thunderandlightning:skyseedertile")
    public static TileEntityType<SkySeederTile> SKYSEEDER_TILE;
    @ObjectHolder("thunderandlightning:boilertile")
    public static TileEntityType<BoilerTileEntity> BOILER_TILE;
}
