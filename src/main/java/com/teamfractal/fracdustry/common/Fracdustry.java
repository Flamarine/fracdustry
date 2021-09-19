package com.teamfractal.fracdustry.common;

import com.teamfractal.fracdustry.common.util.FDRegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(Fracdustry.MODID)
public class Fracdustry
{
    public static final String MODID = "fracdustry";
    public static final String MODNAME = "Fracdustry";

    public Fracdustry()
    {
        FDRegistryHandler.register();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
