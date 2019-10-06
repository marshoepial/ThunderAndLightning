package com.CCraze.LightningCraft.blocks;

import com.CCraze.LightningCraft.items.LightningAttractorBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LightningAttractorBlock extends Block{

    private final double chanceToStrike;
    private final int maxDist;
    private final int maxEnergyStorage;
    private final boolean energyCap;
    private double modifier;

    public LightningAttractorBlock(Properties blockProperties, double chanceToStrike, int maxDist, int maxEnergy, boolean canStoreEnergy, String registryName){
        super(blockProperties);
        this.chanceToStrike = chanceToStrike;
        this.maxDist = maxDist;
        this.modifier = 1;
        this.maxEnergyStorage = maxEnergy;
        this.energyCap = canStoreEnergy;
        setRegistryName(registryName);
    }

    @Override
    public boolean hasTileEntity(BlockState state){
        return true;
    }
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){
        return new LightningAttractorTile(chanceToStrike*modifier, (int)Math.round(maxDist*modifier),
                (int)Math.round(maxEnergyStorage*modifier), energyCap);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isRemote && hand.equals(Hand.MAIN_HAND) && energyCap){
            System.out.println("lightningAttractorBlock activated on world "+world.toString() + "with hand "+hand.toString());
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof LightningAttractorTile){
                System.out.println("On world "+world.toString()+" found lightning attractor tile, sending energy message");
                player.sendMessage(new StringTextComponent("Current Energy: "+((LightningAttractorTile)te).getEnergy()));
                return true;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (stack.getItem() instanceof LightningAttractorBlockItem && nbt.contains("ability")){
            this.modifier = (double)nbt.getInt("ability") / 100;
        }
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
