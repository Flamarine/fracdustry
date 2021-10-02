package com.teamfractal.fracdustry.common.util;

import com.teamfractal.fracdustry.client.util.FDClientRegistryHandler;
import com.teamfractal.fracdustry.common.Fracdustry;
import com.teamfractal.fracdustry.common.block.init.FDBlocks;
import com.teamfractal.fracdustry.common.container.init.FDContainers;
import com.teamfractal.fracdustry.common.item.init.FDItems;
import com.teamfractal.fracdustry.common.sound.FDSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FDRegistryHandler
{
    public static final DeferredRegister<Item> Items = DeferredRegister.create(ForgeRegistries.ITEMS, Fracdustry.MODID);
    public static final DeferredRegister<Block> Blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, Fracdustry.MODID);
    public static final DeferredRegister<SoundEvent> Sounds = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Fracdustry.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BlockEntities = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Fracdustry.MODID);
    public static final DeferredRegister<MenuType<?>> Containers = DeferredRegister.create(ForgeRegistries.CONTAINERS, Fracdustry.MODID);

    public static void register()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        FDItems.register();
        FDBlocks.register();
        FDContainers.register();
        FDSounds.register();

        Items.register(eventBus);
        Blocks.register(eventBus);
        Containers.register(eventBus);
        Sounds.register(eventBus);
    }
}
