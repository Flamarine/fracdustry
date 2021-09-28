package com.teamfractal.fracdustry.common.sound;

import com.teamfractal.fracdustry.common.Fracdustry;
import com.teamfractal.fracdustry.common.util.FDRegistryHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fmllegacy.RegistryObject;

public class FDSounds {
    public static final RegistryObject<SoundEvent> modernization = FDRegistryHandler.Sounds.register("modernization", () -> new SoundEvent(new ResourceLocation(Fracdustry.MODID, "modernization")));
}
