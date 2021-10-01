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

    private final ResourceLocation BACKGROUND = new ResourceLocation(Fracdustry.MODID, "textures/gui/thermal_generator_screen.png");
    private final ResourceLocation BUBBLE1 = new ResourceLocation(Fracdustry.MODID, "textures/gui/bubble1.png");
    private final ResourceLocation BUBBLE2 = new ResourceLocation(Fracdustry.MODID, "textures/gui/bubble2.png");
    private final ResourceLocation CVSHADE = new ResourceLocation(Fracdustry.MODID,"textures/gui/cvshade.png");
    private final ResourceLocation ESHADE = new ResourceLocation(Fracdustry.MODID,"textures/gui/energyshade.png");

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
            tooltip1.add(new TextComponent(new TranslatableComponent("gui.fracdustry.energy").getString()+menu.getEnergy()).getVisualOrderText());
            renderTooltip(matrixStack,tooltip1,mouseX - relX,mouseY - relY);}
        //Burn time display
        if(mouseX > relX + 9 && mouseX < relX + 25 && mouseY > relY + 8 && mouseY < relY + 46){
            tooltip2.add(new TextComponent(new TranslatableComponent("gui.fracdustry.calorific_value").getString()+ 0)
                    .getVisualOrderText());
            //menu.getSlot(0).getItem().getCount() * ForgeHooks.getBurnTime(menu.getSlot(0).getItem(), RecipeType.SMELTING))
            renderTooltip(matrixStack,tooltip2,mouseX - relX,mouseY - relY);}
        }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        //Background
        RenderSystem.setShaderTexture(0, BACKGROUND);
        int relX = (this.width - 180) / 2;
        int relY = (this.height - 152) / 2;
        blit(matrixStack, relX, relY, 0, 0, 180, 152, 180, 152);
        //Bubbles
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
        }}
        //todo: fix the display issues.
        //Energy Bar Display(not working)
        RenderSystem.setShaderTexture(0,ESHADE);
        blit(matrixStack, relX + 167, relY + 5, 0, 0, 32,
                ((menu.getMaxEnergy()-menu.getEnergy())/menu.getMaxEnergy())*32,32,((menu.getMaxEnergy()-menu.getEnergy())/menu.getMaxEnergy())*32);
        //Heat Bar Display(not working)
        RenderSystem.setShaderTexture(0,CVSHADE);
        ItemStack stack = menu.getSlot(0).getItem();
        blit(matrixStack, relX + 18, relY + 9, 0, 0, 7,
                37 - ((Math.max((stack.getCount()*(ForgeHooks.getBurnTime(stack,RecipeType.SMELTING))) - 640000,0) / 640000)* 37),
                7,37 - ((Math.max((stack.getCount()*(ForgeHooks.getBurnTime(stack,RecipeType.SMELTING))) - 640000,0) / 640000)* 37));
        blit(matrixStack, relX + 10, relY + 9, 0, 0, 7,
                37 - ((Math.max((stack.getCount()*(ForgeHooks.getBurnTime(
                        stack,RecipeType.SMELTING))),640000) / 640000)* 37),7,37);
    }
}
