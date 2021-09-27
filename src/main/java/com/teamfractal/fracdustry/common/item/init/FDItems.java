package com.teamfractal.fracdustry.common.item.init;

import com.teamfractal.fracdustry.common.block.init.FDBlocks;
import com.teamfractal.fracdustry.common.itemGroup.FDGroupInit;
import com.teamfractal.fracdustry.common.util.FDRegistryHandler;
import net.minecraft.world.item.BlockItem;
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
    public static RegistryObject<Item> blockBauxiteOre;
    public static RegistryObject<Item> blockCassiteriteOre;
    public static RegistryObject<Item> blockIlmeniteOre;
    public static RegistryObject<Item> blockSpodumeneOre;
    public static RegistryObject<Item> blockDeepslateBauxiteOre;
    public static RegistryObject<Item> blockDeepslateCassiteriteOre;
    public static RegistryObject<Item> blockDeepslateIlmeniteOre;
    public static RegistryObject<Item> blockDeepslateSpodumeneOre;

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

        //Oreblocks
        blockBauxiteOre = FDRegistryHandler.Items.register("bauxite_ore"
                , () -> new BlockItem(FDBlocks.blockBauxiteOre.get(),new Item.Properties().tab(FDGroupInit.fdmaterial)));
        blockCassiteriteOre = FDRegistryHandler.Items.register("cassiterite_ore"
                , () -> new BlockItem(FDBlocks.blockCassiteriteOre.get(),new Item.Properties().tab(FDGroupInit.fdmaterial)));
        blockIlmeniteOre = FDRegistryHandler.Items.register("ilmenite_ore"
                , () -> new BlockItem(FDBlocks.blockIlmeniteOre.get(),new Item.Properties().tab(FDGroupInit.fdmaterial)));
        blockSpodumeneOre = FDRegistryHandler.Items.register("spodumene_ore"
                , () -> new BlockItem(FDBlocks.blockSpodumeneOre.get(),new Item.Properties().tab(FDGroupInit.fdmaterial)));
        blockDeepslateBauxiteOre = FDRegistryHandler.Items.register("deepslate_bauxite_ore"
                , () -> new BlockItem(FDBlocks.blockDeepslateBauxiteOre.get(),new Item.Properties().tab(FDGroupInit.fdmaterial)));
        blockDeepslateCassiteriteOre = FDRegistryHandler.Items.register("deepslate_cassiterite_ore"
                , () -> new BlockItem(FDBlocks.blockDeepslateCassiteriteOre.get(),new Item.Properties().tab(FDGroupInit.fdmaterial)));
        blockDeepslateIlmeniteOre = FDRegistryHandler.Items.register("deepslate_ilmenite_ore"
                , () -> new BlockItem(FDBlocks.blockDeepslateIlmeniteOre.get(),new Item.Properties().tab(FDGroupInit.fdmaterial)));
        blockDeepslateSpodumeneOre = FDRegistryHandler.Items.register("deepslate_spodumene_ore"
                , () -> new BlockItem(FDBlocks.blockDeepslateSpodumeneOre.get(),new Item.Properties().tab(FDGroupInit.fdmaterial)));

    }

}
