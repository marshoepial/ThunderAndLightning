package com.CCraze.LightningCraft.items;

import com.CCraze.LightningCraft.blocks.LightningAttractorBlock;
import com.CCraze.LightningCraft.blocks.lightningattractors.WoolLightningAttractor;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class LightningAttractorBlockItem extends BlockItem {
    private final int abilityMod;

    public LightningAttractorBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
        abilityMod = 100;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(this.isInGroup(group)){
            ItemStack itemStack = new ItemStack(this);
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("ability", abilityMod);
            itemStack.setTag(nbt);
            items.add(itemStack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null && stack.getTag().contains("ability") && getBlock() instanceof LightningAttractorBlock){
            if (Screen.hasShiftDown()) {
                LightningAttractorBlock itemBlock = (LightningAttractorBlock) getBlock();
                if (itemBlock instanceof WoolLightningAttractor) tooltip.add(new StringTextComponent("One time use only.")
                        .applyTextStyle(TextFormatting.RED).applyTextStyle(TextFormatting.BOLD));
                tooltip.add(new StringTextComponent("Lightning Strike Chance: " + itemBlock.getChanceToStrike() * 100 + "%").applyTextStyle(TextFormatting.GRAY));
                tooltip.add(new StringTextComponent("Attracting Distance: " + itemBlock.getMaxDist() + " blocks").applyTextStyle(TextFormatting.GRAY));
                if (itemBlock.canAcceptEnergy())
                    tooltip.add(new StringTextComponent("Maximum Energy: " + itemBlock.getMaxEnergyStorage() + " FE")
                            .applyTextStyle(TextFormatting.GRAY));
                else tooltip.add(new StringTextComponent("Does NOT collect energy.").applyTextStyle(TextFormatting.RED));
                tooltip.add(new StringTextComponent("Modifier: " + stack.getTag().getInt("ability") + "%").applyTextStyle(TextFormatting.GRAY));
            } else {
                tooltip.add(new StringTextComponent("<Hold Shift...>").applyTextStyle(TextFormatting.GRAY).applyTextStyle(TextFormatting.ITALIC));
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
