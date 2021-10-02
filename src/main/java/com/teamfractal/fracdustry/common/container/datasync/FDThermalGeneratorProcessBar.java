package com.teamfractal.fracdustry.common.container.datasync;

import net.minecraft.world.inventory.ContainerData;

public class FDThermalGeneratorProcessBar implements ContainerData {
    int i = 0;
    @Override
    public int get(int index) {
        return i;
    }

    @Override
    public void set(int index, int value) {
        i = value;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
