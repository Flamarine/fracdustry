package com.teamfractal.fracdustry.common.block.init;

import com.teamfractal.fracdustry.common.util.FDRegistryHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fmllegacy.RegistryObject;


public class FDBlocks
{
    public static RegistryObject<Block> blockBauxiteOre;
    public static RegistryObject<Block> blockCassiteriteOre;
    public static RegistryObject<Block> blockIlmeniteOre;
    public static RegistryObject<Block> blockSpodumeneOre;
    public static RegistryObject<Block> blockDeepslateBauxiteOre;
    public static RegistryObject<Block> blockDeepslateCassiteriteOre;
    public static RegistryObject<Block> blockDeepslateIlmeniteOre;
    public static RegistryObject<Block> blockDeepslateSpodumeneOre;

    public static void register(){
        blockBauxiteOre = FDRegistryHandler.Blocks.register("bauxite_ore", () -> new OreBlock(BlockBehaviour.
                Properties.of(Material.STONE).explosionResistance(2).sound(SoundType.STONE).strength(2).destroyTime(2)));
        blockCassiteriteOre = FDRegistryHandler.Blocks.register("cassiterite_ore", () -> new OreBlock(BlockBehaviour.
                Properties.of(Material.STONE).explosionResistance(2).sound(SoundType.STONE).strength(2).destroyTime(2)));
        blockIlmeniteOre = FDRegistryHandler.Blocks.register("ilmenite_ore", () -> new OreBlock(BlockBehaviour.
                Properties.of(Material.STONE).explosionResistance(2).sound(SoundType.STONE).strength(2).destroyTime(2)));
        blockSpodumeneOre = FDRegistryHandler.Blocks.register("spodumene_ore", () -> new OreBlock(BlockBehaviour.
                Properties.of(Material.STONE).explosionResistance(2).sound(SoundType.STONE).strength(2).destroyTime(2)));
        blockDeepslateBauxiteOre = FDRegistryHandler.Blocks.register("deepslate_bauxite_ore", () -> new OreBlock(BlockBehaviour.
                Properties.of(Material.STONE).explosionResistance(3).sound(SoundType.STONE).strength(2).destroyTime(3)));
        blockDeepslateCassiteriteOre = FDRegistryHandler.Blocks.register("deepslate_cassiterite_ore", () -> new OreBlock(BlockBehaviour.
                Properties.of(Material.STONE).explosionResistance(3).sound(SoundType.STONE).strength(2).destroyTime(3)));
        blockDeepslateIlmeniteOre = FDRegistryHandler.Blocks.register("deepslate_ilmenite_ore", () -> new OreBlock(BlockBehaviour.
                Properties.of(Material.STONE).explosionResistance(3).sound(SoundType.STONE).strength(2).destroyTime(3)));
        blockDeepslateSpodumeneOre = FDRegistryHandler.Blocks.register("deepslate_spodumene_ore", () -> new OreBlock(BlockBehaviour.
                Properties.of(Material.STONE).explosionResistance(3).sound(SoundType.STONE).strength(2).destroyTime(3)));
    }
}
