package com.teamfractal.fracdustry.common.container.init;

import com.teamfractal.fracdustry.common.container.FDThermalGeneratorContainer;
import com.teamfractal.fracdustry.common.container.datasync.FDThermalGeneratorProcessBar;
import com.teamfractal.fracdustry.common.util.FDRegistryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fmllegacy.RegistryObject;

public class FDContainers {
    public static RegistryObject<MenuType<FDThermalGeneratorContainer>> containerThermalGenerator;

    public static void register(){
        containerThermalGenerator = FDRegistryHandler.Containers.register("thermal_generator", () -> IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            Level world = inv.player.getCommandSenderWorld();
            FDThermalGeneratorProcessBar processBar = new FDThermalGeneratorProcessBar();
            return new FDThermalGeneratorContainer(windowId, world, pos, inv, inv.player, processBar);
        }));
    }

}
