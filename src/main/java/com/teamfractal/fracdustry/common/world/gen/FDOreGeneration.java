package com.teamfractal.fracdustry.common.world.gen;

import com.teamfractal.fracdustry.common.block.init.FDBlocks;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.SquareDecorator;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "fracdustry",bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FDOreGeneration {
    @SubscribeEvent
    public static void oreGeneration (final BiomeLoadingEvent event){
        //The End
        if (event.getCategory() == Biome.BiomeCategory.THEEND){}
        //Nether
        else if (event.getCategory() == Biome.BiomeCategory.NETHER){}
        //Overworld
        else{
            genOre(event.getGeneration(), OreConfiguration.Predicates.STONE_ORE_REPLACEABLES
                    , FDBlocks.blockBauxiteOre.get().defaultBlockState(), 6,3, 64, 12);
            genOre(event.getGeneration(), OreConfiguration.Predicates.STONE_ORE_REPLACEABLES
                    , FDBlocks.blockCassiteriteOre.get().defaultBlockState(), 8,3, 64, 14);
            genOre(event.getGeneration(), OreConfiguration.Predicates.STONE_ORE_REPLACEABLES
                    , FDBlocks.blockIlmeniteOre.get().defaultBlockState(), 4,3, 64, 11);
            genOre(event.getGeneration(), OreConfiguration.Predicates.STONE_ORE_REPLACEABLES
                    , FDBlocks.blockSpodumeneOre.get().defaultBlockState(), 10,3, 64, 15);
            genOre(event.getGeneration(), OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES
                    , FDBlocks.blockDeepslateBauxiteOre.get().defaultBlockState(), 4,3, 64, 8);
            genOre(event.getGeneration(), OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES
                    , FDBlocks.blockDeepslateCassiteriteOre.get().defaultBlockState(), 6,3, 64, 10);
            genOre(event.getGeneration(), OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES
                    , FDBlocks.blockDeepslateIlmeniteOre.get().defaultBlockState(), 4,3, 64, 7);
            genOre(event.getGeneration(), OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES
                    , FDBlocks.blockDeepslateSpodumeneOre.get().defaultBlockState(), 8,3, 64, 11);
        }

    }
    private static void genOre(BiomeGenerationSettingsBuilder settings, RuleTest fillertype
            , BlockState state, int veinSize, int minHeight, int maxHeight, int maxperchunk){
        settings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                Feature.ORE.configured(new OreConfiguration(fillertype, state, veinSize))
                        .decorated(SquareDecorator.RANGE.configured(new RangeDecoratorConfiguration
                                (UniformHeight.of(VerticalAnchor.absolute(minHeight),VerticalAnchor.absolute(maxHeight))))).squared().count(maxperchunk));
    }
}
