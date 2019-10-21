package com.CCraze.ThunderAndLightning.blocks;

import com.CCraze.ThunderAndLightning.ForgeEventHandler;
import com.CCraze.ThunderAndLightning.behavior.AttractorEnergyStorage;
import com.CCraze.ThunderAndLightning.entity.BlueLightningBolt;
import com.CCraze.ThunderAndLightning.networking.BlueBoltEntityPacket;
import com.CCraze.ThunderAndLightning.networking.TAndLPacketHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.CCraze.ThunderAndLightning.blocks.ModBlocks.LIGHTNINGATTRACTOR_TILE;

public class LightningAttractorTile extends TileEntity implements ITickableTileEntity {
    private double chanceStrike = 0; //chance of being struck by lightning, between 0 and 1
    public int maxDist = 0; //maximum distance the block can be from a source of lightning for it to be struck
    private int maxEnergyStorage;
    private boolean canStoreEnergy;
    private double modifier;

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::genEnergy);

    public LightningAttractorTile(double chanceOfBeingStruck, int maximumDistance, int maxStorage, boolean acceptsEnergy) { //this is the constructor we use, it has the variables we need
        super(LIGHTNINGATTRACTOR_TILE);
        chanceStrike = chanceOfBeingStruck;
        maxDist = maximumDistance;
        maxEnergyStorage = maxStorage;
        canStoreEnergy = acceptsEnergy;
        this.markDirty();
        //System.out.println("Tile registered with chance "+chanceStrike+", max dist "+maxDist+", energy capacity "+maxEnergyStorage+", and ability to store energy "+canStoreEnergy);
    }
    public LightningAttractorTile() {
        super(LIGHTNINGATTRACTOR_TILE);
    }

    @Override
    public void tick() {
        if (world.isRemote || !canStoreEnergy) return;
        energy.ifPresent(e -> {
            if (e.getEnergyStored() > 0){
                if (!sendOutPower()){
                    ((AttractorEnergyStorage)e).dissipateEnergy();
                }
            }
        });

        if (world.getWorldInfo().isThundering()) {
            if (Math.random() > 0.999) {
                BlueLightningBolt blueBolt = new BlueLightningBolt(world, getPos().getX(), getYWithOffset(), getPos().getY(), false);
                world.addEntity(blueBolt);
                TAndLPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                        new BlueBoltEntityPacket(blueBolt.getEntityId(), blueBolt.posX, blueBolt.posY, blueBolt.posZ));
            }
        }
    }

    public boolean isValid(){
        if (Math.random() > chanceStrike) return false; //makes sure the chance of lightning striking is adhered to
        for (int i = this.getPos().getY()+2; i < 255; i++) //iterate over the blocks above the attractor to see if the attractor is blocked
            if (!getWorld().isAirBlock(new BlockPos(getPos().getX(), i, getPos().getZ()))) return false; //if block is not AIR, then attractor is blocked and not valid
        //System.out.println("Is valid");
        return true;
    }
    public int getYWithOffset(){
        if (world.getBlockState(getPos().up()) == Blocks.AIR.getDefaultState()) return 1+getPos().getY();
        else return 2+getPos().getY();
    }
    public void thunderStruck(LightningBoltEntity lightning){
        List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getPos().up()));
        //System.out.println("Iterating over "+entityList.size()+" entities over attractor");
        boolean entityMod = false;
        for (Entity entity : entityList) {
            if (entity instanceof ItemEntity && !entityMod) {
                //System.out.println("Item found, replacing...");
                Item newItem = ForgeEventHandler.recipeParser.swapItem(((ItemEntity) entity).getItem().getItem());
                int maxRepeat = ForgeEventHandler.recipeParser.getMaxRepeat(((ItemEntity) entity).getItem().getItem());
                //System.out.println("Recipeparser returned item "+newItem+" with maxrepeat "+maxRepeat);
                if (!newItem.equals(((ItemEntity) entity).getItem().getItem())){
                    if (((ItemEntity) entity).getItem().getCount() > maxRepeat) {
                        ItemEntity secondEntity = genItemEntity(world, entity.posX, entity.posY, entity.posZ,
                                ((ItemEntity) entity).getItem().copy());
                        secondEntity.getItem().setCount(((ItemEntity) entity).getItem().getCount()-maxRepeat);
                        //System.out.println("Stack amount bigger than maximum repeat, creating second entity with item "+secondEntity.getItem().getItem());
                        world.addEntity(secondEntity);
                    }
                    ItemStack returnStack = newItem.getDefaultInstance();
                    returnStack.setCount(Math.min(((ItemEntity) entity).getItem().getCount(), maxRepeat));
                    //System.out.println("Returning with item "+returnStack.getItem()+" and count "+returnStack.getCount());
                    entity.remove();
                    entity = genItemEntity(world, entity.posX, entity.posY, entity.posZ, returnStack);
                    world.addEntity(entity);
                    //System.out.println("Item now has item "+((ItemEntity) entity).getItem().getItem()+" with count "+((ItemEntity) entity).getItem().getCount());
                    entityMod = true;
                }
            } else if (!(entity instanceof ItemEntity)) {
                entity.onStruckByLightning(lightning);
            }
        }
        if(world.getBlockState(getPos().up()) != Blocks.AIR.getDefaultState()){
            //System.out.println("Block above attractor detected, replacing...");
            world.setBlockState(getPos().up(), ForgeEventHandler.recipeParser.swapBlock(world.getBlockState(getPos().up()).getBlock()).getDefaultState());
        } else if (canStoreEnergy) {
            //System.out.println("No blocks above attractor, no items, giving energy");

            //System.out.println("Lazy optional is present? " + energy.isPresent());
            energy.ifPresent(e -> {
                //System.out.println("Current energy stored is "+e.getEnergyStored());
                if (e.getEnergyStored() <= e.getMaxEnergyStored() - maxEnergyStorage){
                    //System.out.println("Adding energy");
                    ((AttractorEnergyStorage)e).setEnergy(maxEnergyStorage);
                }
            });
        }
        if (getBlockState().getBlock() instanceof LightningAttractorBlock) ((LightningAttractorBlock) getBlockState().getBlock()).onStrike(getTileEntity().getWorld(), getTileEntity());
    } private boolean sendOutPower() {
        if (!canStoreEnergy) return false;
        AtomicBoolean sentPower = new AtomicBoolean(false);
        energy.ifPresent(e -> {
            AtomicInteger totalEnergy = new AtomicInteger(e.getEnergyStored());
            if (totalEnergy.get() > 0){
                for (Direction direction : Direction.values()){
                    TileEntity te = world.getTileEntity(pos.offset(direction));
                    if (te != null){
                        boolean dontExit = te.getCapability(CapabilityEnergy.ENERGY, direction).map(h -> {
                            if (h.canReceive() && h.getEnergyStored() != h.getMaxEnergyStored()){
                                int sentEnergy = h.receiveEnergy(Math.min(totalEnergy.get(), 40000), false);
                                totalEnergy.addAndGet(-sentEnergy);
                                ((AttractorEnergyStorage) e).removeEnergy(sentEnergy);
                                sentPower.set(true);
                                markDirty();
                                return totalEnergy.get() > 0;
                            } else {
                                return true;
                            }
                        }).orElse(true);
                        if (!dontExit) return;
                    }
                }
            }
        });
        return sentPower.get();
    } protected ItemEntity genItemEntity(World world, double x, double y, double z, ItemStack item){
        return new ItemEntity(world, x, y, z, item);
    }
    private IEnergyStorage genEnergy(){
        return new AttractorEnergyStorage(maxEnergyStorage, maxEnergyStorage);
    } private IItemHandler createHandler(){
        return new ItemStackHandler();
    }
    public double getModifier() {return modifier;}
    public boolean canStoreEnergy() {
        return canStoreEnergy;
    }
    public void setModifier(double modifier) {
        this.modifier = modifier;
        markDirty();
    }
    public int getEnergy(){
        if (!canStoreEnergy) return 0;
        AtomicInteger returnVal = new AtomicInteger();
        energy.ifPresent(e -> {
            int energyStored = e.getEnergyStored();
            //System.out.println("Got energy stored value of "+energyStored);
            returnVal.set(energyStored);
        });
        //System.out.println("Returning energy val of "+returnVal.get());
        return returnVal.get();
    }
    @Override
    public void read(CompoundNBT nbt){ //NBT is used for persistence across chunk load/unloads.
        super.read(nbt); //read NBT required by TileEntity class
        maxDist = nbt.getInt("maxDist");
        chanceStrike = nbt.getDouble("chanceStrike");
        maxEnergyStorage = nbt.getInt("energyStorage");
        canStoreEnergy = nbt.getBoolean("energyCap");
        modifier = nbt.getDouble("ability");
        CompoundNBT energyTag = nbt.getCompound("energy");
        if (canStoreEnergy) energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(energyTag));
    }
    @Override
    public CompoundNBT write(CompoundNBT nbt){
        super.write(nbt);
        nbt.putInt("maxDist", maxDist);
        nbt.putDouble("chanceStrike", chanceStrike);
        nbt.putInt("energyStorage", maxEnergyStorage);
        nbt.putBoolean("energyCap", canStoreEnergy);
        nbt.putDouble("ability", modifier);
        if (canStoreEnergy) {
            energy.ifPresent(h -> {
                CompoundNBT compoundNBT = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
                nbt.put("energy", compoundNBT);
            });
        }
        return nbt;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return handler.cast();
        } if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }


}