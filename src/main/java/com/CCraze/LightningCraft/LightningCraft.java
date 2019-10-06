package com.CCraze.LightningCraft;

import com.CCraze.LightningCraft.blocks.*;
import com.CCraze.LightningCraft.blocks.lightningattractors.CreativeLightningAttractor;
import com.CCraze.LightningCraft.blocks.lightningattractors.DiamondLightningAttractor;
import com.CCraze.LightningCraft.blocks.lightningattractors.IronLightningAttractor;
import com.CCraze.LightningCraft.blocks.lightningattractors.WoolLightningAttractor;
import com.CCraze.LightningCraft.items.ElectrumCoil;
import com.CCraze.LightningCraft.items.LightningAttractorBlockItem;
import com.CCraze.LightningCraft.items.TempestuousBlend;
import com.CCraze.LightningCraft.setup.ClientProxy;
import com.CCraze.LightningCraft.setup.IProxy;
import com.CCraze.LightningCraft.setup.ModVals;
import com.CCraze.LightningCraft.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lightningcraft")
public class LightningCraft {

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "lightningcraft";

    public LightningCraft() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

    }

    private void setup(final FMLCommonSetupEvent event) {
    }


    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            event.getRegistry().register(new CreativeLightningAttractor());
            event.getRegistry().register(new IronLightningAttractor());
            event.getRegistry().register(new DiamondLightningAttractor());
            event.getRegistry().register(new WoolLightningAttractor());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new LightningAttractorBlockItem(ModBlocks.CREATIVELIGHTNINGATTRACTOR, new Item.Properties().group(ModVals.modGroup))
                    .setRegistryName("creativelightningattractor"));
            event.getRegistry().register(new LightningAttractorBlockItem(ModBlocks.IRONLIGHTNINGATTRACTOR, new Item.Properties().group(ModVals.modGroup))
                    .setRegistryName("ironlightningattractor"));
            event.getRegistry().register(new LightningAttractorBlockItem(ModBlocks.DIAMONDLIGHTNINGATTRACTOR, new Item.Properties().group(ModVals.modGroup))
                    .setRegistryName("diamondlightningattractor"));
            event.getRegistry().register(new LightningAttractorBlockItem(ModBlocks.WOOLLIGHTNINGATTRACTOR, new Item.Properties().group(ModVals.modGroup))
                    .setRegistryName("woollightningattractor"));
            event.getRegistry().register(new TempestuousBlend());
            event.getRegistry().register(new ElectrumCoil());
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(TileEntityType.Builder.create(LightningAttractorTile::new,
                    ModBlocks.CREATIVELIGHTNINGATTRACTOR, ModBlocks.IRONLIGHTNINGATTRACTOR, ModBlocks.DIAMONDLIGHTNINGATTRACTOR,
                    ModBlocks.WOOLLIGHTNINGATTRACTOR).build(null)
                    .setRegistryName("lightningattractortile"));
        }



    }
}

