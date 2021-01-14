package com.minercana.adventuringenergies.rendering;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import com.minercana.adventuringenergies.energytypes.EnergyType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = AdventuringEnergies.MOD_ID, value = Dist.CLIENT)
public class EnergyHUDOverlay {

    public static final ResourceLocation RESOURCE = new ResourceLocation(AdventuringEnergies.MOD_ID, "textures/gui/orbs_desaturated.png");
    static int animationFrameX = 0;

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (Minecraft.getInstance().player != null && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft.getInstance().player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).ifPresent(tracker -> renderEnergy(tracker, event.getMatrixStack(), Minecraft.getInstance()));
        }
    }

    private static void renderEnergy(IEnergyTracker tracker, MatrixStack matrixStack, Minecraft instance) {
        int right = instance.getMainWindow().getScaledWidth();
        int bottom = instance.getMainWindow().getScaledHeight();

        instance.getTextureManager().bindTexture(RESOURCE);

        float radius = 25;
        // In case players get more than 10 orbs, all of them will render properly
        int orbCount = Math.max(10, AdventuringEnergiesAPI.ENERGY_TYPES.get().getValues().stream().mapToInt(tracker::getEnergy).sum());
        int resourceX = 16;
        int resourceY = 16;
        int offsetX = (int) (radius * 2);
        int offsetY = (int) (radius * 2);
        int orbIndex = 0;
        // Loop through each energy type to group same energy types together, we assume there's 10 slots by default and space them evenly
        for (EnergyType type : AdventuringEnergiesAPI.ENERGY_TYPES.get().getValues()) {
            Color orbColor = new Color(type.getColor());
            for (int i = 0; i < tracker.getEnergy(type); i++) {
                float angle = ((orbIndex + 1) / (float) orbCount) * 360;
                double rotX = Math.cos(Math.toRadians(angle)) * radius;
                double rotY = -Math.sin(Math.toRadians(angle)) * radius;
                RenderSystem.color4f(orbColor.getRed() / 256f, orbColor.getGreen() / 256f, orbColor.getBlue() / 256f, 1f);
                instance.ingameGUI.blit(matrixStack, right - offsetX + (int) rotX, bottom - offsetY + (int) rotY, animationFrameX * resourceX, 16, resourceX, resourceY);
                orbIndex++;
            }
        }

        // Clear color and render remaining slots as blank orbs
        RenderSystem.color4f(1, 1, 1, 1f);
        for (int leftoverOrb = orbIndex; leftoverOrb < tracker.getTotalEnergyCap(); leftoverOrb++) {
            float angle = ((leftoverOrb + 1) / (float) orbCount) * 360;
            double rotX = Math.cos(Math.toRadians(angle)) * radius;
            double rotY = -Math.sin(Math.toRadians(angle)) * radius;
            instance.ingameGUI.blit(matrixStack, right - offsetX + (int) rotX, bottom - offsetY + (int) rotY, animationFrameX * resourceX, 16, resourceX, resourceY);
        }

        // Grab frameTimer, use it to control animation speed
        int frameTimerIndex = instance.frameTimer.getIndex();

        // Higher Number, Slower Animation
        if (frameTimerIndex % 20 == 0) {
            animationFrameX++;
            if (animationFrameX > 3)
                animationFrameX = 0;
        }
    }
}
