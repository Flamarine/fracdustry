package com.teamfractal.fracdustry.common.itemGroup;

import com.teamfractal.fracdustry.common.block.FDThermalGeneratorBlock;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class FDMachinery extends CreativeModeTab {
    public FDMachinery(){
        super("fracdustry_machinery");
    }
    @Override
    public ItemStack makeIcon() {
        return new ItemStack(FDThermalGeneratorBlock.BLOCK);
    }
}
