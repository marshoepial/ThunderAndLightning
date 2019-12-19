package com.CCraze.ThunderAndLightning;

import com.CCraze.ThunderAndLightning.blocks.*;
import com.CCraze.ThunderAndLightning.blocks.lightningattractors.*;
import com.CCraze.ThunderAndLightning.blocks.skyseeder.SkySeederBlock;
import com.CCraze.ThunderAndLightning.blocks.skyseeder.SkySeederTile;
import com.CCraze.ThunderAndLightning.config.ThunderLightningConfig;
import com.CCraze.ThunderAndLightning.entity.BlueLightningBolt;
import com.CCraze.ThunderAndLightning.items.*;
import com.CCraze.ThunderAndLightning.networking.BlueBoltEntityPacket;
import com.CCraze.ThunderAndLightning.networking.TAndLPacketHandler;
import com.CCraze.ThunderAndLightning.setup.ClientProxy;
import com.CCraze.ThunderAndLightning.setup.IProxy;
import com.CCraze.ThunderAndLightning.setup.ModVals;
import com.CCraze.ThunderAndLightning.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("thunderandlightning")
public class ThunderAndLightning {

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "thunderandlightning";

    public static final ThunderLightningConfig CONFIG = new ThunderLightningConfig();

    //-----Fluid DeferredRegistries------
    public static DeferredRegister<Fluid> FLUID_REGISTRY = new DeferredRegister<>(ForgeRegistries.FLUIDS, MODID);
    public static DeferredRegister<Item> ITEM_REGISTRY = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static DeferredRegister<Block> BLOCK_REGISTRY = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);

    private static final ResourceLocation stillTexture = new ResourceLocation("minecraft:block/water_still");
    private static final ResourceLocation flowingTexture  = new ResourceLocation("minecraft:block/water_flow");

    private static RegistryObject<FlowingFluid> TempFluidSource = FLUID_REGISTRY.register("tempestuous_fluid", () ->
            new ForgeFlowingFluid.Source(ThunderAndLightning.fluidProperties));
    private static RegistryObject<FlowingFluid> TempFluidFlowing = FLUID_REGISTRY.register("tempestuous_fluid_flowing", () ->
            new ForgeFlowingFluid.Flowing(ThunderAndLightning.fluidProperties));
    private static RegistryObject<FlowingFluidBlock> TempFluidBlock = BLOCK_REGISTRY.register("tempestuous_fluid_block", () ->
            new FlowingFluidBlock(TempFluidSource, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0f).noDrops()));
    private static RegistryObject<BucketItem> TempFluidBucket = ITEM_REGISTRY.register("tempestuous_fluid_bucket", () ->
            new BucketItem(TempFluidSource, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ModVals.modGroup)));

    private static final ForgeFlowingFluid.Properties fluidProperties = new ForgeFlowingFluid.Properties(TempFluidSource, TempFluidFlowing,
                    FluidAttributes.builder(stillTexture, flowingTexture).color(0x881bbfFF)).bucket(TempFluidBucket).block(TempFluidBlock);

    public ThunderAndLightning() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        eventBus.addListener(this::setup);

        BLOCK_REGISTRY.register(eventBus);
        ITEM_REGISTRY.register(eventBus);
        FLUID_REGISTRY.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        new ModVals();
        proxy.init();
        int packetId = 0;
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> { FMLJavaModLoadingContext.get().getModEventBus().register(ClientEventHandler.class); });
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
            event.getRegistry().register(new SkySeederBlock());
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
            event.getRegistry().register(new SkySeederBlockItem(ModBlocks.SKYSEEDER, new Item.Properties().group(ModVals.modGroup)).setRegistryName("skyseeder"));

            event.getRegistry().register(new TempestuousBlend());
            event.getRegistry().register(new ElectrumCoil());
            event.getRegistry().register(new ObsidianBucket());
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
            event.getRegistry().register(TileEntityType.Builder.create(LightningAttractorTile::new, ModBlocks.SKYSEEDER).build(null)
                    .setRegistryName("skyseedertile"));
        }

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event){
            event.getRegistry().register(EntityType.Builder.<BlueLightningBolt>create(EntityClassification.MISC).disableSerialization().size(0, 0).build("bluebolt").setRegistryName(MODID, "bluebolt"));
        }

        /*@SubscribeEvent
        public static void onModelRegistry(final ModelRegistryEvent event){
            ModelLoader.addSpecialModel(ModVals.SKYSEEDER_MODEL_BASE);
            ModelLoader.addSpecialModel(ModVals.SKYSEEDER_MODEL_FAN);
            ModelLoader.addSpecialModel(ModVals.SKYSEEDER_MODEL_HEAD);
        }
        */
    }
}

