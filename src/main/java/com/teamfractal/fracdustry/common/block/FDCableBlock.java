package com.teamfractal.fracdustry.common.block;

import com.google.common.collect.Maps;
import com.teamfractal.fracdustry.common.blockentity.FDCableBlockEntity;
import com.teamfractal.fracdustry.common.itemGroup.FDGroupInit;
import com.teamfractal.fracdustry.common.util.energynetwork.EnergyNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

/**
 * @author Tapio, ustc-zzzz
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDCableBlock extends Block implements EntityBlock {
    public static final String NAME = "fracdustry:cable";

    @ObjectHolder(NAME)
    public static FDCableBlock BLOCK;

    @SubscribeEvent
    public static void onRegisterBlock(@Nonnull RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(new FDCableBlock().setRegistryName(NAME));
    }

    @SubscribeEvent
    public static void onRegisterItem(@Nonnull RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new BlockItem(BLOCK, new Item.Properties().tab(FDGroupInit.fdmachinery)).setRegistryName(NAME));
    }

    public static final Map<Direction, BooleanProperty> PROPERTY_MAP;

    static
    {
        Map<Direction, BooleanProperty> map = Maps.newEnumMap(Direction.class);
        map.put(Direction.NORTH, BlockStateProperties.NORTH);
        map.put(Direction.EAST, BlockStateProperties.EAST);
        map.put(Direction.SOUTH, BlockStateProperties.SOUTH);
        map.put(Direction.WEST, BlockStateProperties.WEST);
        map.put(Direction.UP, BlockStateProperties.UP);
        map.put(Direction.DOWN, BlockStateProperties.DOWN);
        PROPERTY_MAP = Collections.unmodifiableMap(map);
    }

    private FDCableBlock()
    {
        super(Block.Properties.of(Material.GLASS).strength(2).explosionResistance(2));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder)
    {
        //Sure?
        builder.add(PROPERTY_MAP.values().toArray(new BooleanProperty[0]));
        super.createBlockStateDefinition(builder);
    }


    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context)
    {
        return Block.box(4, 4, 4, 12, 12, 12);
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context)
    {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        for (Direction facing : Direction.values())
        {
            Level world = context.getLevel();
            BlockPos facingPos = context.getClickedPos().offset(facing.getNormal());
            BlockState facingState = world.getBlockState(facingPos);
            state = state.setValue(PROPERTY_MAP.get(facing), this.canConnect(world, facing.getOpposite(), facingPos, facingState));
        }
        return state;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor world, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos)
    {
        //Sure?
        return state.setValue(PROPERTY_MAP.get(facing), this.canConnect(world, facing.getOpposite(), facingPos, facingState));
    }

    private boolean canConnect(@Nonnull LevelAccessor world, @Nonnull Direction facing, @Nonnull BlockPos pos, @Nonnull BlockState state)
    {
        if (!state.getBlock().equals(BLOCK))
        {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            return tileEntity != null && tileEntity.getCapability(CapabilityEnergy.ENERGY, facing).isPresent();
        }
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block fromBlock, @Nonnull BlockPos fromPos, boolean isMoving)
    {
        if (!world.isClientSide())
        {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FDCableBlockEntity)
            {
                EnergyNetwork.Factory.get(world).enableBlock(pos, tileEntity::setChanged);
            }
        }
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FDCableBlockEntity(pos, state);
    }

}
