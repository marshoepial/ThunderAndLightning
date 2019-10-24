package com.CCraze.ThunderAndLightning;

import com.CCraze.ThunderAndLightning.blocks.*;
import com.CCraze.ThunderAndLightning.blocks.lightningattractors.*;
import com.CCraze.ThunderAndLightning.config.ThunderLightningConfig;
import com.CCraze.ThunderAndLightning.entity.BlueLightningBolt;
import com.CCraze.ThunderAndLightning.items.ElectrumCoil;
import com.CCraze.ThunderAndLightning.items.LightningAttractorBlockItem;
import com.CCraze.ThunderAndLightning.items.TempestuousBlend;
import com.CCraze.ThunderAndLightning.networking.BlueBoltEntityPacket;
import com.CCraze.ThunderAndLightning.networking.TAndLPacketHandler;
import com.CCraze.ThunderAndLightning.setup.ClientProxy;
import com.CCraze.ThunderAndLightning.setup.IProxy;
import com.CCraze.ThunderAndLightning.setup.ModVals;
import com.CCraze.ThunderAndLightning.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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

import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("thunderandlightning")
public class ThunderAndLightning {

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "thunderandlightning";

    public static final ThunderLightningConfig CONFIG = new ThunderLightningConfig();

    public ThunderAndLightning() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        new ModVals();
        proxy.init();
        int packetId = 0;
        TAndLPacketHandler.INSTANCE.registerMessage(packetId++, BlueBoltEntityPacket.class, BlueBoltEntityPacket::encode, BlueBoltEntityPacket::decode, BlueBoltEntityPacket::onReceive);
    }


    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            if (!(boolean)CONFIG.readFromConfig("Creative IsDisabled")) event.getRegistry().register(new CreativeLightningAttractor());
            if (!(boolean)CONFIG.readFromConfig("Iron IsDisabled")) event.getRegistry().register(new IronLightningAttractor());
            if (!(boolean)CONFIG.readFromConfig("Diamond IsDisabled")) event.getRegistry().register(new DiamondLightningAttractor());
            if (!(boolean)CONFIG.readFromConfig("Wool IsDisabled")) event.getRegistry().register(new WoolLightningAttractor());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            if (!(boolean)CONFIG.readFromConfig("Creative IsDisabled")) event.getRegistry().register(new LightningAttractorBlockItem(ModBlocks.CREATIVELIGHTNINGATTRACTOR,
                            new Item.Properties().group(ModVals.modGroup)).setRegistryName("creativelightningattractor"));
            if (!(boolean)CONFIG.readFromConfig("Iron IsDisabled")) event.getRegistry().register(new LightningAttractorBlockItem(ModBlocks.IRONLIGHTNINGATTRACTOR,
                            new Item.Properties().group(ModVals.modGroup)).setRegistryName("ironlightningattractor"));
            if (!(boolean)CONFIG.readFromConfig("Diamond IsDisabled")) event.getRegistry().register(new LightningAttractorBlockItem(ModBlocks.DIAMONDLIGHTNINGATTRACTOR,
                            new Item.Properties().group(ModVals.modGroup)).setRegistryName("diamondlightningattractor"));
            if (!(boolean)CONFIG.readFromConfig("Wool IsDisabled")) event.getRegistry().register(new LightningAttractorBlockItem(ModBlocks.WOOLLIGHTNINGATTRACTOR,
                            new Item.Properties().group(ModVals.modGroup)).setRegistryName("woollightningattractor"));
            event.getRegistry().register(new TempestuousBlend());
            event.getRegistry().register(new ElectrumCoil());
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            List<Block> lightningAttractorBlockList = new ArrayList<>();
            if (!(boolean)CONFIG.readFromConfig("Creative IsDisabled")) lightningAttractorBlockList.add(ModBlocks.CREATIVELIGHTNINGATTRACTOR);
            if (!(boolean)CONFIG.readFromConfig("Iron IsDisabled")) lightningAttractorBlockList.add(ModBlocks.IRONLIGHTNINGATTRACTOR);
            if (!(boolean)CONFIG.readFromConfig("Diamond IsDisabled")) lightningAttractorBlockList.add(ModBlocks.DIAMONDLIGHTNINGATTRACTOR);
            if (!(boolean)CONFIG.readFromConfig("Wool IsDisabled")) lightningAttractorBlockList.add(ModBlocks.WOOLLIGHTNINGATTRACTOR);
            Block[] lightingAttractorBlockArray = lightningAttractorBlockList.toArray(new Block[0]);
            event.getRegistry().register(TileEntityType.Builder.create(LightningAttractorTile::new, lightingAttractorBlockArray).build(null)
                    .setRegistryName("lightningattractortile"));
        }

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event){
            event.getRegistry().register(EntityType.Builder.<BlueLightningBolt>create(EntityClassification.MISC).disableSerialization().size(0, 0).build("bluebolt").setRegistryName(MODID, "bluebolt"));
        }


    }
}

