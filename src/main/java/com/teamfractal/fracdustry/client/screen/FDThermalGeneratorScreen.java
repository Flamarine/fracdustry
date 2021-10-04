package com.teamfractal.fracdustry.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamfractal.fracdustry.common.Fracdustry;
import com.teamfractal.fracdustry.common.container.FDThermalGeneratorContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tapio, mcjty
 */
public class FDThermalGeneratorScreen extends AbstractContainerScreen<FDThermalGeneratorContainer> {

    private final ResourceLocation BACKGROUND = new ResourceLocation(Fracdustry.MODID, "textures/gui/thermal_generator/screen.png");
    private final ResourceLocation BUBBLE1 = new ResourceLocation(Fracdustry.MODID, "textures/gui/thermal_generator/bubble1.png");
    private final ResourceLocation BUBBLE2 = new ResourceLocation(Fracdustry.MODID, "textures/gui/thermal_generator/bubble2.png");
    private final ResourceLocation HSHADE = new ResourceLocation(Fracdustry.MODID,"textures/gui/thermal_generator/cvshade.png");
    private final ResourceLocation ESHADE = new ResourceLocation(Fracdustry.MODID,"textures/gui/thermal_generator/energyshade.png");
    private final ResourceLocation BAR1 = new ResourceLocation(Fracdustry.MODID,"textures/gui/thermal_generator/heated_bar_1.png");
    private final ResourceLocation BAR2 = new ResourceLocation(Fracdustry.MODID,"textures/gui/thermal_generator/heated_bar_2.png");

    public FDThermalGeneratorScreen(FDThermalGeneratorContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        int relX = (this.width - 180) / 2;
        int relY = (this.height - 152) / 2;
        List<FormattedCharSequence> tooltip1 = new ArrayList<FormattedCharSequence>();
        List<FormattedCharSequence> tooltip2 = new ArrayList<FormattedCharSequence>();
        //Energy display
        if(mouseX > relX + 166 && mouseX < relX + 173 && mouseY > relY + 4 && mouseY < relY + 37){
            tooltip1.add(new TextComponent(new TranslatableComponent("gui.fracdustry.energy").getString()+
                    menu.getEnergy()+
                    " FE").getVisualOrderText());
            renderTooltip(matrixStack,tooltip1,mouseX - relX,mouseY - relY);}
        //Burn time display
        //Gotcha!
        double tm = menu.getIntArray().get(0);
        int bt = Math.toIntExact(Math.round(tm / 20));
        if(mouseX > relX + 9 && mouseX < relX + 25 && mouseY > relY + 8 && mouseY < relY + 46){
            tooltip2.add(new TextComponent(new TranslatableComponent("gui.fracdustry.burntime").getString()+
                    bt+
                    new TranslatableComponent("units.fracdustry.sec").getString())
                    .getVisualOrderText());
            renderTooltip(matrixStack,tooltip2,mouseX - relX,mouseY - relY);}
        }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        //Background
        RenderSystem.setShaderTexture(0, BACKGROUND);
        int relX = (this.width - 180) / 2;
        int relY = (this.height - 152) / 2;
        blit(matrixStack, relX, relY, 0, 0, 180, 152, 180, 152);
        //Bubbles and heated bars
        if(menu.getPowered())
        {switch (Math.toIntExact(menu.getTime() % 4)){
            default:
            case 0:
            case 1:
                RenderSystem.setShaderTexture(0,BUBBLE1);
                blit(matrixStack, relX + 87, relY + 9, 0, 0,64,64,64,64);
                break;
            case 2:
            case 3:
                RenderSystem.setShaderTexture(0,BUBBLE2);
                blit(matrixStack, relX + 87, relY + 9, 0, 0,64,64,64,64);
        }
            RenderSystem.setShaderTexture(0,BAR2);
            blit(matrixStack, relX + 26, relY + 9, 0, 0, 35, 37, 35, 37);
            RenderSystem.setShaderTexture(0,BAR1);
            blit(matrixStack, relX + 9, relY + 9, 0, 0, 17, 39, 17, 39);
        }
        //Energy Bar Display
        RenderSystem.setShaderTexture(0,ESHADE);
        double ce = menu.getEnergy();
        double me = menu.getMaxEnergy();
        long height = Math.round(((me - ce) / me) * 32);
        blit(matrixStack, relX + 167, relY + 5, 0, 0, 32,
                Math.toIntExact(height),32,32);
        //Heat Bar Display
        RenderSystem.setShaderTexture(0,HSHADE);
        ItemStack stack = menu.getSlot(0).getItem();
        double tm = menu.getIntArray().get(0);
        blit(matrixStack, relX + 10, relY + 9, 0, 0, 7,
                Math.toIntExact(Math.round(37 * ( 1- Math.max(tm - 10000,0) / 10000))),
                7,37);
        blit(matrixStack, relX + 18, relY + 9, 0, 0, 7,
                Math.toIntExact(Math.round(37*(1 - Math.min(tm,10000) / 10000))),7,37);
    }
}
