package com.teamfractal.fracdustry.client.util;

import com.teamfractal.fracdustry.client.screen.FDMicrowaveGeneratorScreen;
import com.teamfractal.fracdustry.client.screen.FDThermalGeneratorScreen;
import com.teamfractal.fracdustry.common.Fracdustry;
import com.teamfractal.fracdustry.common.container.init.FDContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Fracdustry.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDClientRegistryHandler {
    @SubscribeEvent
    public static void setup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(FDContainers.containerThermalGenerator.get(), FDThermalGeneratorScreen::new);
            MenuScreens.register(FDContainers.containerMicrowaveGenerator.get(), FDMicrowaveGeneratorScreen::new);
        });
    }
}
