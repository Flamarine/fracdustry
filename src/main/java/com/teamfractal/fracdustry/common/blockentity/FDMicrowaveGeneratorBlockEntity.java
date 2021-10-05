package com.teamfractal.fracdustry.common.blockentity;

import com.mojang.datafixers.DSL;
import com.teamfractal.fracdustry.common.block.FDMicrowaveGeneratorBlock;
import com.teamfractal.fracdustry.common.container.FDMicrowaveGeneratorContainer;
import com.teamfractal.fracdustry.common.sound.FDSounds;
import com.teamfractal.fracdustry.common.util.energystorage.FDEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDMicrowaveGeneratorBlockEntity extends BlockEntity implements MenuProvider {
    public static final String NAME = "fracdustry:microwave_generator";

    @ObjectHolder(NAME)
    public static BlockEntityType<FDMicrowaveGeneratorBlockEntity> BLOCK_ENTITY_TYPE;

    //private final ItemStackHandler itemHandler = createHandler();
    private final FDEnergyStorage energyStorage = createEnergy();

    //private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    @SubscribeEvent
    public static void onRegisterBlockEntityType(@Nonnull RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().register(BlockEntityType.Builder.
                of(FDMicrowaveGeneratorBlockEntity::new, FDMicrowaveGeneratorBlock.BLOCK).build(DSL.remainderType()).setRegistryName(NAME));
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("");
    }

    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {

        if (level != null) {
            return new FDMicrowaveGeneratorContainer(windowId, level, worldPosition, playerInventory, playerEntity);
        }
        return null;
    }

    //Disable capabilities when block is removed
    @Override
    public void setRemoved() {
        super.setRemoved();
        //handler.invalidate();
        energy.invalidate();
    }
    //Block's energy generation and sound playing

    public void tickServer(BlockState state) {
        //todo:make energy generation adjustable in configs
        energyStorage.addEnergy(1);
        setChanged();
        if (level != null && level.getGameTime() % 20 == 0) {
            level.playSound(null, worldPosition, FDSounds.thermal_generator_loop.get(), SoundSource.BLOCKS, 1, 1);
        }
        //Currently no extra conditions for activating the generator.
        BlockState blockState = null;
        if (level != null) {
            blockState = level.getBlockState(worldPosition);
        }
        if (blockState != null) {
            level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.POWERED, true),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        }

        sendOutPower();
    }

    //Sending energy to neighbor blocks
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
                                int received = handler.receiveEnergy(Math.min(capacity.get(), 100), false);
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

    public FDMicrowaveGeneratorBlockEntity(BlockPos pos, BlockState state)
    {
        super(BLOCK_ENTITY_TYPE,pos,state);
    }

    //NBT saving and loading
    @Override
    public CompoundTag save(@Nonnull  CompoundTag tag) {
        //tag.put("inv", itemHandler.serializeNBT());
        tag.put("energy", energyStorage.serializeNBT());

        return super.save(tag);
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        //itemHandler.deserializeNBT(tag.getCompound("inv"));
        energyStorage.deserializeNBT(tag.get("energy"));
        super.load(tag);
    }

    //Itemhandler

    private FDEnergyStorage createEnergy() {
        return new FDEnergyStorage(10000, 0) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        /*if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }*/
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }
}
