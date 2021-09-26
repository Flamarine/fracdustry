package com.teamfractal.fracdustry.common.blockentity;

import com.teamfractal.fracdustry.common.block.FDCableBlock;
import com.teamfractal.fracdustry.common.util.energynetwork.EnergyNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import com.mojang.datafixers.DSL;

import javax.annotation.Nonnull;
import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDCableBlockEntity extends BlockEntity {
    public static final String NAME = "fracdustry:cable";

    @ObjectHolder(NAME)
    public static BlockEntityType<FDCableBlockEntity> BLOCK_ENTITY_TYPE;

    @SubscribeEvent
    public static void onRegisterBlockEntityType(@Nonnull RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().register(BlockEntityType.Builder.of(FDCableBlockEntity::new, FDCableBlock.BLOCK).build(DSL.remainderType()).setRegistryName(NAME));
    }

    private final LazyOptional<IEnergyStorage> lazyOptional = LazyOptional.of(() -> new IEnergyStorage()
    {
        private final EnergyNetwork network = EnergyNetwork.Factory.get(FDCableBlockEntity.this.level);

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            int energy = this.getEnergyStored();
            int diff = Math.min(500, Math.min(this.getMaxEnergyStored() - energy, maxReceive));
            if (!simulate) {
                this.network.addEnergy(FDCableBlockEntity.this.getBlockPos(), diff);
                if (diff != 0)
                {
                    FDCableBlockEntity.this.setChanged();
                }
            }
            return diff;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            int energy = this.getEnergyStored();
            int diff = Math.min(500, Math.min(energy, maxExtract));
            if (!simulate)
            {
                this.network.addEnergy(FDCableBlockEntity.this.getBlockPos(), -diff);
                if (diff != 0)
                {
                    FDCableBlockEntity.this.setChanged();
                }
            }
            return diff;
        }

        @Override
        public int getEnergyStored()
        {
            return Math.min(this.getMaxEnergyStored(), this.network.getNetworkEnergy(FDCableBlockEntity.this.getBlockPos()));
        }

        @Override
        public int getMaxEnergyStored()
        {
            return 1_000 * this.network.getNetworkSize(FDCableBlockEntity.this.getBlockPos());
        }

        @Override
        public boolean canExtract()
        {
            return true;
        }

        @Override
        public boolean canReceive()
        {
            return true;
        }
});
    private Integer tmpEnergy = null;

    public FDCableBlockEntity(BlockPos pos, BlockState state)
    {
        super(BLOCK_ENTITY_TYPE,pos,state);
    }

    @Override
    public void load(@Nonnull CompoundTag compound)
    {
        this.tmpEnergy = compound.getInt("WireEnergy");
        super.load(compound);
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compound)
    {
        EnergyNetwork network = EnergyNetwork.Factory.get(this.level);
        compound.putInt("WireEnergy", network.getSharedEnergy(this.getBlockPos()));
        return super.save(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side)
    {
        boolean isEnergy = Objects.equals(cap, CapabilityEnergy.ENERGY);
        return isEnergy ? this.lazyOptional.cast() : super.getCapability(cap, side);
    }

    @Override
    public void onLoad()
    {
        if (this.level != null && !this.level.isClientSide)
        {
            EnergyNetwork network = EnergyNetwork.Factory.get(this.level);
            if (this.tmpEnergy != null)
            {
                int diff = this.tmpEnergy - network.getSharedEnergy(this.getBlockPos());
                network.addEnergy(this.getBlockPos(), diff);
                this.tmpEnergy = null;
            }
            network.enableBlock(this.getBlockPos(), this::setChanged);
        }
        super.onLoad();
    }

    @Override
    public void onChunkUnloaded()
    {
        if (this.level != null && !this.level.isClientSide)
        {
            EnergyNetwork network = EnergyNetwork.Factory.get(this.level);
            network.disableBlock(this.getBlockPos(), this::setChanged);
        }
        super.onChunkUnloaded();
    }

    @Override
    public void setRemoved()
    {
        if (this.level != null && !this.level.isClientSide)
        {
            EnergyNetwork network = EnergyNetwork.Factory.get(this.level);
            network.disableBlock(this.getBlockPos(), () ->
            {
                int diff = network.getSharedEnergy(this.getBlockPos());
                network.addEnergy(this.getBlockPos(), -diff);
                this.setChanged();
            });
        }
        super.setRemoved();
    }
}
