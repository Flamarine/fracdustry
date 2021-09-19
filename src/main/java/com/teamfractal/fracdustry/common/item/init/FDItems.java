package com.teamfractal.fracdustry.common.item.init;

import com.teamfractal.fracdustry.common.util.FDRegistryHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

public class FDItems
{
    public static RegistryObject<Item> itemAluminumIngot;

    public static void register()
    {
        itemAluminumIngot = FDRegistryHandler.Items.register("aluminum_ingot", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_BREWING)));
    }
}
