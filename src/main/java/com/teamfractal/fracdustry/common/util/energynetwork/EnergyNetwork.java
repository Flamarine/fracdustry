package com.teamfractal.fracdustry.common.util.energynetwork;

import com.google.common.collect.*;
import com.teamfractal.fracdustry.common.block.FDCableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

public class EnergyNetwork {
    private final LevelAccessor world;
    private final IBlockNetwork blockNetwork;
    private final Queue<Runnable> taskCollection;
    private final Multiset<BlockPos> energyCollection;
    private final SetMultimap<ChunkPos, BlockPos> chunkCollection;
    private final SetMultimap<BlockPos, Direction> machineCollection;

    private EnergyNetwork(LevelAccessor world, IBlockNetwork blockNetwork)
    {
        this.world = world;
        this.blockNetwork = blockNetwork;
        this.taskCollection = Queues.newArrayDeque();
        this.energyCollection = HashMultiset.create();
        this.chunkCollection = Multimaps.newSetMultimap(Maps.newHashMap(), Sets::newHashSet);
        this.machineCollection = Multimaps.newSetMultimap(Maps.newHashMap(), () -> EnumSet.noneOf(Direction.class));
    }

    public int getNetworkSize(BlockPos pos)
    {
        return this.blockNetwork.size(pos);
    }

    public int getNetworkEnergy(BlockPos pos)
    {
        BlockPos root = this.blockNetwork.root(pos);
        return this.energyCollection.count(root);
    }

    public int getSharedEnergy(BlockPos pos)
    {
        int size = this.blockNetwork.size(pos);
        BlockPos root = this.blockNetwork.root(pos);
        int total = this.energyCollection.count(root);
        return root.equals(pos) ? total / size + total % size : total / size;
    }

    public void addEnergy(BlockPos pos, int diff)
    {
        if (diff >= 0)
        {
            this.energyCollection.add(this.blockNetwork.root(pos), diff);
        }
        else
        {
            this.energyCollection.remove(this.blockNetwork.root(pos), -diff);
        }
    }

    public void disableBlock(BlockPos pos, Runnable callback)
    {
        this.taskCollection.offer(() ->
        {
            this.chunkCollection.remove(new ChunkPos(pos), pos);
            for (Direction side : Direction.values())
            {
                this.blockNetwork.cut(pos, side, this::afterSplit);
            }
            this.machineCollection.removeAll(pos);
            callback.run();
        });
    }

    public void enableBlock(BlockPos pos, Runnable callback)
    {
        this.taskCollection.offer(() ->
        {
            this.chunkCollection.put(new ChunkPos(pos), pos.immutable());
            for (Direction side : Direction.values())
            {
                if (this.hasWireConnection(pos, side))
                {
                    if (this.hasWireConnection(pos.offset(side.getNormal()), side.getOpposite()))
                    {
                        this.machineCollection.remove(pos, side);
                        this.blockNetwork.link(pos, side, this::beforeMerge);
                    }
                    else
                    {
                        this.machineCollection.put(pos.immutable(), side);
                        this.blockNetwork.cut(pos, side, this::afterSplit);
                    }
                }
                else
                {
                    this.machineCollection.remove(pos, side);
                    this.blockNetwork.cut(pos, side, this::afterSplit);
                }
            }
            callback.run();
        });
    }

    private boolean hasWireConnection(BlockPos pos, Direction side)
    {
        if (this.world.isAreaLoaded(pos,1))
        {
            BlockState state = this.world.getBlockState(pos);
            //Sure?
            return state.getBlock().equals(FDCableBlock.BLOCK) && state.getValue(FDCableBlock.PROPERTY_MAP.get(side));
        }
        return false;
    }

    private void afterSplit(BlockPos primaryNode, BlockPos secondaryNode)
    {
        int primarySize = this.blockNetwork.size(primaryNode), secondarySize = this.blockNetwork.size(secondaryNode);
        int diff = this.energyCollection.count(primaryNode) * secondarySize / (primarySize + secondarySize);
        this.energyCollection.remove(primaryNode, diff);
        this.energyCollection.add(secondaryNode, diff);
    }

    private void beforeMerge(BlockPos primaryNode, BlockPos secondaryNode)
    {
        int diff = this.energyCollection.count(secondaryNode);
        this.energyCollection.remove(secondaryNode, diff);
        this.energyCollection.add(primaryNode, diff);
    }

    private void markDirty()
    {
        for (ChunkPos chunkPos : this.chunkCollection.keys())
        {
            BlockPos pos = chunkPos.getWorldPosition();
            if (this.world.isAreaLoaded(pos,1))
            {
                this.world.getChunk(pos).setUnsaved(true);
            }
        }
    }

    private void tickStart()
    {
        for (Runnable runnable = this.taskCollection.poll(); runnable != null; runnable = this.taskCollection.poll())
        {
            runnable.run();
        }
    }

    private void tickEnd()
    {
        for (Map.Entry<BlockPos, Direction> entry : this.shuffled(this.machineCollection.entries()))
        {
            Direction direction = entry.getValue();
            BlockPos node = entry.getKey(), root = this.blockNetwork.root(node);
            if (this.world.isAreaLoaded(node.offset(direction.getNormal()),1))
            {
                BlockEntity tileEntity = this.world.getBlockEntity(node.offset(direction.getNormal()));
                if (tileEntity != null)
                {
                    tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(e ->
                    {
                        if (e.canReceive())
                        {
                            int diff = this.energyCollection.count(root);
                            this.energyCollection.remove(root, e.receiveEnergy(diff, false));
                        }
                    });
                }
            }
        }
    }

    private <T> List<T> shuffled(Iterable<? extends T> iterable)
    {
        List<T> list = Lists.newArrayList(iterable);
        Random rand = this.world.getRandom();
        Collections.shuffle(list, rand);
        return list;
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Factory
    {
        private static final Map<LevelAccessor, EnergyNetwork> INSTANCES = Maps.newIdentityHashMap();

        public static EnergyNetwork get(LevelAccessor world)
        {
            return INSTANCES.computeIfAbsent(world, k -> new EnergyNetwork(k, new BlockNetwork()));
        }

        @SubscribeEvent
        public static void onSave(WorldEvent.Save event)
        {
            if (INSTANCES.containsKey(event.getWorld()))
            {
                INSTANCES.get(event.getWorld()).markDirty();
            }
        }

        @SubscribeEvent
        public static void onUnload(WorldEvent.Unload event)
        {
            INSTANCES.remove(event.getWorld());
        }

        @SubscribeEvent
        public static void onWorldTick(TickEvent.WorldTickEvent event)
        {
            if (LogicalSide.SERVER.equals(event.side))
            {
                switch (event.phase)
                {
                    case START:
                    {
                        Factory.get(event.world).tickStart();
                        break;
                    }
                    case END:
                    {
                        Factory.get(event.world).tickEnd();
                        break;
                    }
                    default:
                }
            }
        }
    }
}
