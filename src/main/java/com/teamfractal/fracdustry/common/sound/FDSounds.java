package com.teamfractal.fracdustry.common.sound;

import com.teamfractal.fracdustry.common.Fracdustry;
import com.teamfractal.fracdustry.common.util.FDRegistryHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fmllegacy.RegistryObject;

public class FDSounds {
    private FDSounds(){}
    public static RegistryObject<SoundEvent> modernization;
    public static RegistryObject<SoundEvent> thermal_generator_loop;
    public static void register(){
        modernization = FDRegistryHandler.Sounds.register("modernization", () -> new SoundEvent(new ResourceLocation(Fracdustry.MODID, "modernization")));
        thermal_generator_loop = FDRegistryHandler.Sounds.register("thermal_generator_loop", () -> new SoundEvent(new ResourceLocation(Fracdustry.MODID, "thermal_generator_loop")));
    }
}
