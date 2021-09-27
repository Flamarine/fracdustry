package com.teamfractal.fracdustry.common.world.gen;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.placement.RangeDecorator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = "fracdustry",bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDOreGeneration {
    @SubscribeEvent
    public static void oreGeneration (FMLLoadCompleteEvent event){
        for (Biome biome : ForgeRegistries.BIOMES){
            //The End
            if (biome.getBiomeCategory() == Biome.BiomeCategory.THEEND){}
            //Nether
            else if (biome.getBiomeCategory() == Biome.BiomeCategory.NETHER){}
            //Overworld
            else{

            };
        }
    }
    //TODO:
    private static void genOre(Biome biome, int count, int bottomOffset, int topOffset, int max
            , OreConfiguration.Predicates filler, BlockState defaultBlockState, int size){


    }
}
