package com.CCraze.ThunderAndLightning.blocks.lightningattractors;

import com.CCraze.ThunderAndLightning.ThunderAndLightning;
import com.CCraze.ThunderAndLightning.blocks.skyseeder.SkySeederTile;
import com.CCraze.ThunderAndLightning.items.LightningAttractorBlockItem;
import com.CCraze.ThunderAndLightning.weather.TempestWeather;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LightningAttractorBlock extends Block{

    protected final double chanceToStrike;
    protected final int maxDist;
    protected final int maxEnergyStorage;
    protected final boolean energyCap;
    private final String registryName;
    protected double modifier;

    public LightningAttractorBlock(Properties blockProperties, String configStr, String registryName){
        super(blockProperties);
        this.chanceToStrike = (double) ThunderAndLightning.CONFIG.readFromConfig(configStr + " ChanceToStrike");
        this.maxDist = (int) ThunderAndLightning.CONFIG.readFromConfig(configStr + " MaxStrikeDistance");
        this.modifier = 1;
        this.maxEnergyStorage = (int) ThunderAndLightning.CONFIG.readFromConfig(configStr + " MaxEnergy");
        this.energyCap = (boolean) ThunderAndLightning.CONFIG.readFromConfig(configStr + " CanStoreEnergy");
        this.registryName = registryName;
        setRegistryName(registryName);
        //System.out.println("Attractor Block created with "+modifier+" modifier");
    }

    @Override
    public boolean hasTileEntity(BlockState state){
        return true;
    }
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){
        //System.out.println("Creating new TileEntity");
        return new LightningAttractorTile(chanceToStrike*modifier, (int)Math.round(maxDist*modifier),
                (int)Math.round(maxEnergyStorage*modifier), energyCap);
    }

    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isRemote && hand.equals(Hand.MAIN_HAND)){
            //System.out.println("lightningAttractorBlock activated on world "+world.toString() + "with hand "+hand.toString());
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof LightningAttractorTile && ((LightningAttractorTile) te).canStoreEnergy()){
                //System.out.println("On world "+world.toString()+" found lightning attractor tile, sending energy message");
                player.sendMessage(new StringTextComponent("Current Energy: "+((LightningAttractorTile)te).getEnergy()));
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (stack.getItem() instanceof LightningAttractorBlockItem && nbt.contains("ability") && worldIn.getTileEntity(pos) instanceof LightningAttractorTile){
            //System.out.println("Attractor block placed by item with modifier "+nbt.getInt("ability")+", setting to "+worldIn.getTileEntity(pos));
            ((LightningAttractorTile) worldIn.getTileEntity(pos)).setModifier((double) nbt.getInt("ability") / 100);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState bs, LootContext.Builder builder) {
        System.out.println("TileEntity is "+builder.get(LootParameters.BLOCK_ENTITY));
        if (builder.get(LootParameters.BLOCK_ENTITY) instanceof LightningAttractorTile) {
            List<ItemStack> drops = new ArrayList<>();
            ItemStack labi = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thunderandlightning:" + registryName)));
            System.out.println("ItemStack is "+labi);
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("ability", (int) Math.round(((LightningAttractorTile) builder.get(LootParameters.BLOCK_ENTITY))
                    .getModifier() * 100));
            labi.setTag(nbt);
            drops.add(labi);
            return drops;
        }
        return null;
    }

    public void onStrike(World world, TileEntity te) {}

    public double getChanceToStrike() {
        return chanceToStrike;
    }

    public int getMaxDist() {
        return maxDist;
    }

    public int getMaxEnergyStorage() {
        return maxEnergyStorage;
    }

    public boolean canAcceptEnergy() {
        return energyCap;
    }
}
