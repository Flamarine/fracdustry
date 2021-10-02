package com.teamfractal.fracdustry.common.blockentity;

import com.mojang.datafixers.DSL;
import com.teamfractal.fracdustry.common.block.FDThermalGeneratorBlock;
import com.teamfractal.fracdustry.common.container.FDThermalGeneratorContainer;
import com.teamfractal.fracdustry.common.container.datasync.FDThermalGeneratorProcessBar;
import com.teamfractal.fracdustry.common.util.energystorage.FDEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDThermalGeneratorBlockEntity extends BlockEntity implements MenuProvider {
    public static final String NAME = "fracdustry:thermal_generator";

    @ObjectHolder(NAME)
    public static BlockEntityType<FDCableBlockEntity> BLOCK_ENTITY_TYPE;

    private final ItemStackHandler itemHandler = createHandler();
    private final FDEnergyStorage energyStorage = createEnergy();

    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    private int timer;
    private FDThermalGeneratorProcessBar processBar = new FDThermalGeneratorProcessBar();

    @SubscribeEvent
    public static void onRegisterBlockEntityType(@Nonnull RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().register(BlockEntityType.Builder.
                of(FDThermalGeneratorBlockEntity::new, FDThermalGeneratorBlock.BLOCK).build(DSL.remainderType()).setRegistryName(NAME));
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("");
    }
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {

        if (level != null) {
            return new FDThermalGeneratorContainer(windowId, level, worldPosition, playerInventory, playerEntity,processBar);
        }
        return null;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
        energy.invalidate();
    }


    public void tickServer(BlockState state) {
        if (timer > 0) {
            timer--;
            //todo:make energy generation adjustable in configs
            energyStorage.addEnergy(10);
            setChanged();
        }

        if (timer <= 0) {
            ItemStack stack = itemHandler.getStackInSlot(0);
            int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
            if (burnTime > 0) {
                itemHandler.extractItem(0, 1, false);
                timer = burnTime;
                setChanged();
            }
        }
        processBar.set(0,timer);


        BlockState blockState = null;
        if (level != null) {
            blockState = level.getBlockState(worldPosition);
        }
        if (blockState != null && blockState.getValue(BlockStateProperties.POWERED) != timer > 0) {
            level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.POWERED, timer > 0),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        }

        sendOutPower();

    }
    private void sendOutPower() {
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        if (capacity.get() > 0) {
            for (Direction direction : Direction.values()) {
                BlockEntity te = null;
                if (level != null) {
                    te = level.getBlockEntity(worldPosition.relative(direction));
                }
                if (te != null) {
                    boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler ->
                            {if (handler.canReceive()) {
                                int received = handler.receiveEnergy(Math.min(capacity.get(), 1000), false);
                                capacity.addAndGet(-received);
                                energyStorage.consumeEnergy(received);
                                setChanged();
                                return capacity.get() > 0;
                            } else {
                                return true;
                            }
                            }
                    ).orElse(true);
                    if (!doContinue) {
                        return;
                    }
                }
            }
        }
    }



    public FDThermalGeneratorBlockEntity(BlockPos pos, BlockState state)
    {
        super(BLOCK_ENTITY_TYPE,pos,state);
    }

    @Override
    public CompoundTag save(@Nonnull  CompoundTag tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.put("energy", energyStorage.serializeNBT());

        tag.putInt("timer", timer);
        return super.save(tag);
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        energyStorage.deserializeNBT(tag.get("energy"));
        timer = tag.getInt("timer");
        super.load(tag);
    }



    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) <= 0) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private FDEnergyStorage createEnergy() {
        return new FDEnergyStorage(100000, 0) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

}
