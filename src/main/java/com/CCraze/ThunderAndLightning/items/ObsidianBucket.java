package com.CCraze.ThunderAndLightning.items;

import com.CCraze.ThunderAndLightning.setup.ModVals;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class ObsidianBucket extends Item {

    public ObsidianBucket() {
        super(new Item.Properties().group(ModVals.modGroup));
        setRegistryName("obsidian_bucket");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getHeldItem(hand);
        RayTraceResult rayTraceResult = rayTrace(world, playerEntity, RayTraceContext.FluidMode.NONE); //what're you looking at?
        if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK) return new ActionResult<>(ActionResultType.PASS, itemStack); //nothing, apparently. which is acceptable, but not ideal
        else {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult; //eyeing a block
            BlockPos posSelected = world.getBlockState(blockRayTraceResult.getPos()).getMaterial().isReplaceable() ? //check whether the block is replaceable (like grasses)
                    blockRayTraceResult.getPos() : blockRayTraceResult.getPos().offset(blockRayTraceResult.getFace()); //account or do not account for the side highlighted
            if ((world.isAirBlock(posSelected) || world.getBlockState(posSelected).getMaterial().isReplaceable()) &&
                    world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(posSelected)).isEmpty()){ //the main check here is to make sure our selected block has no entities in it
                world.playSound(playerEntity, posSelected, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F); //make a sound
                if (!world.isRemote) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, posSelected, new ItemStack(Items.OBSIDIAN)); //the game can celebrate our placing of obsidian if it wants to
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, posSelected, itemStack); //if some madman wants to add an advancement for a bucket of obsidian, they sure as hell can
                }
                playerEntity.addStat(Stats.ITEM_USED.get(this)); //how else will i brag of my obsidian-ing skills?
                world.setBlockState(posSelected, Blocks.OBSIDIAN.getDefaultState()); //actually do the thing
                return new ActionResult<>(ActionResultType.SUCCESS, playerEntity.abilities.isCreativeMode ? itemStack : new ItemStack(Items.BUCKET)); //did you do it? yes. what did it cost? a loss of obsidian except in creative mode
            }
        }
        return new ActionResult<>(ActionResultType.FAIL, itemStack); //if you got here, congrats! you're a failure!
    }
}
