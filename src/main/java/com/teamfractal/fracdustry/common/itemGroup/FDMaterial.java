package com.teamfractal.fracdustry.common.itemGroup;

import com.teamfractal.fracdustry.common.item.init.FDItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class FDMaterial extends CreativeModeTab {
    public FDMaterial(){
        super("fracdustry_material");
    }
    @Override
    public ItemStack makeIcon() {
        return new ItemStack(FDItems.itemAluminumIngot.get());
    }
}
