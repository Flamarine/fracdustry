package com.teamfractal.fracdustry.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamfractal.fracdustry.common.Fracdustry;
import com.teamfractal.fracdustry.common.container.FDMicrowaveGeneratorContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FDMicrowaveGeneratorScreen  extends AbstractContainerScreen<FDMicrowaveGeneratorContainer> {
    private final ResourceLocation BACKGROUND = new ResourceLocation(Fracdustry.MODID, "textures/gui/microwave_generator/screen.png");

    public FDMicrowaveGeneratorScreen(FDMicrowaveGeneratorContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    //render labels

    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        //Background
        RenderSystem.setShaderTexture(0, BACKGROUND);
        int relX = (this.width - 180) / 2;
        int relY = (this.height - 152) / 2;
        blit(matrixStack, relX, relY, 0, 0, 180, 152, 180, 152);
    }
}
