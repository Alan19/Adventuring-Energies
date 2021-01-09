package com.minercana.adventuringenergies.rendering;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.client.gui.AbstractGui.GUI_ICONS_LOCATION;

@Mod.EventBusSubscriber(modid = AdventuringEnergies.MOD_ID, value = Dist.CLIENT)
public class EnergyHUDOverlay {

    public static final ResourceLocation RESOURCE = new ResourceLocation(AdventuringEnergies.MOD_ID, "textures/gui/astral_health.png");

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (Minecraft.getInstance().player != null && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            event.setCanceled(true);
            Minecraft.getInstance().player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).ifPresent(tracker -> renderEnergy(tracker, event.getMatrixStack(), Minecraft.getInstance()));
        }
    }

    private static void renderEnergy(IEnergyTracker tracker, MatrixStack matrixStack, Minecraft instance) {
        int scaledWidth = instance.getMainWindow().getScaledWidth();
        int scaledHeight = instance.getMainWindow().getScaledHeight();

        instance.getTextureManager().bindTexture(RESOURCE);

        int left = scaledWidth - 16;
        int top = scaledHeight - 16;
        for (int i = 0; i < 5; i++){
            instance.ingameGUI.blit(matrixStack, left, top, 0, 0, 16, 16);
//            AbstractGui.blit(matrixStack, left, top, 0, 25.0F, 171.0F, 10, 9, 256, 512);
            left += 10;
        }
    }
}
