package com.teamfractal.fracdustry.common.block;

import com.teamfractal.fracdustry.common.blockentity.FDMicrowaveGeneratorBlockEntity;
import com.teamfractal.fracdustry.common.itemGroup.FDGroupInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDMicrowaveGeneratorBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final String NAME = "fracdustry:microwave_generator";

    @ObjectHolder(NAME)
    public static FDMicrowaveGeneratorBlock BLOCK;

    public FDMicrowaveGeneratorBlock(){
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .lightLevel(state -> state.getValue(BlockStateProperties.POWERED) ? 14 : 0)
                .strength(2.0f).isValidSpawn(FDMicrowaveGeneratorBlock::never).isRedstoneConductor(FDMicrowaveGeneratorBlock::never)
                .isSuffocating(FDMicrowaveGeneratorBlock::never).isViewBlocking(FDMicrowaveGeneratorBlock::never).noOcclusion());
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {return false;}
    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {return false;}

    @SubscribeEvent
    public static void onRegisterBlock(@Nonnull RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(new FDMicrowaveGeneratorBlock().setRegistryName(NAME));
    }

    //Register BlockItem
    @SubscribeEvent
    public static void onRegisterItem(@Nonnull RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new BlockItem(BLOCK, new Item.Properties().tab(FDGroupInit.fdmachinery)).setRegistryName(NAME));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FDMicrowaveGeneratorBlockEntity(pos, state);
    }

    //Connect BlockEntity Ticking
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        } else {
            return (level1, pos, state1, tile) -> {
                if (tile instanceof FDMicrowaveGeneratorBlockEntity generator) {
                    generator.tickServer(state1);
                }
            };
        }
    }

    //Blockstate:facing and powered
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.POWERED);
    }

    //todo:voxelshape

    //Decides the block's facing direction, and normally it's off when being placed
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()).setValue(BlockStateProperties.POWERED, false);
    }

    //BlockItem's tooltip
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter reader, List<Component> list, TooltipFlag flags) {
        list.add(new TranslatableComponent("tooltips.fracdustry.microwave_generator"));
    }

    //Right-click interaction: opens a GUI with container
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FDMicrowaveGeneratorBlockEntity) {
                NetworkHooks.openGui((ServerPlayer) player, (FDMicrowaveGeneratorBlockEntity)blockEntity, blockEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
}
