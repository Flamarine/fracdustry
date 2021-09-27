package com.teamfractal.fracdustry.common.item.init;

import com.teamfractal.fracdustry.common.itemGroup.FDGroupInit;
import com.teamfractal.fracdustry.common.util.FDRegistryHandler;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

public class FDItems
{
    public static RegistryObject<Item> itemAluminumIngot;
    public static RegistryObject<Item> itemAluminumPlate;
    public static RegistryObject<Item> itemBronzeIngot;
    public static RegistryObject<Item> itemBronzePlate;
    public static RegistryObject<Item> itemSteelIngot;
    public static RegistryObject<Item> itemSteelPlate;
    public static RegistryObject<Item> itemTinIngot;
    public static RegistryObject<Item> itemTinPlate;
    public static RegistryObject<Item> itemTitaniumIngot;
    public static RegistryObject<Item> itemTitaniumPlate;

    public static void register()
    {
        //Materials
        itemAluminumIngot = FDRegistryHandler.Items.register("aluminum_ingot", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
        itemAluminumPlate = FDRegistryHandler.Items.register("aluminum_plate", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
        itemBronzeIngot = FDRegistryHandler.Items.register("bronze_ingot", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
        itemBronzePlate = FDRegistryHandler.Items.register("bronze_plate", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
        itemSteelIngot = FDRegistryHandler.Items.register("steel_ingot", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
        itemSteelPlate = FDRegistryHandler.Items.register("steel_plate", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
        itemTinIngot = FDRegistryHandler.Items.register("tin_ingot", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
        itemTinPlate = FDRegistryHandler.Items.register("tin_plate", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
        itemTitaniumIngot = FDRegistryHandler.Items.register("titanium_ingot", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
        itemTitaniumPlate = FDRegistryHandler.Items.register("titanium_plate", () -> new Item(new Item.Properties().tab(FDGroupInit.fdmaterial)));
    }
}
