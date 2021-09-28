package com.teamfractal.fracdustry.common;

import com.teamfractal.fracdustry.common.util.FDRegistryHandler;
import com.teamfractal.fracdustry.common.world.gen.FDOreGeneration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;

@Mod(Fracdustry.MODID)
public class Fracdustry
{
    public static final String MODID = "fracdustry";
    public static final String MODNAME = "Fracdustry";

    public Fracdustry()
    {
        FDRegistryHandler.register();
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, FDOreGeneration::oreGeneration);
        MinecraftForge.EVENT_BUS.register(this);

    }
}
