package com.minercana.adventuringenergies.rendering;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AdventuringEnergies.MOD_ID, value = Dist.CLIENT)
public class EnergyHUDOverlay {

	public static final ResourceLocation RESOURCE = new ResourceLocation(AdventuringEnergies.MOD_ID, "textures/gui/orbs.png");

	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
		if (Minecraft.getInstance().player != null && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			// event.setCanceled(true);
			Minecraft.getInstance().player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).ifPresent(tracker -> renderEnergy(tracker, event.getMatrixStack(), Minecraft.getInstance()));
		}
	}

	static int animationFrameX = 0;

	private static void renderEnergy(IEnergyTracker tracker, MatrixStack matrixStack, Minecraft instance) {
		int right = instance.getMainWindow().getScaledWidth();
		int bottom = instance.getMainWindow().getScaledHeight();

		instance.getTextureManager().bindTexture(RESOURCE);

		float radius = 25;
		int orbCount = 15;
		int resourceX = 16;
		int resourceY = 16;
		int left = 0;
		int top = 0;
		int offsetX = (int) (radius * 2);
		int offsetY = (int) (radius * 2);
		for (int i = 0; i < orbCount; i++) {
			float angle = ((i + 1) / (float) orbCount) * 360;
			double rotX = Math.cos(Math.toRadians(angle)) * radius;
			double rotY = -Math.sin(Math.toRadians(angle)) * radius;
			instance.ingameGUI.blit(matrixStack, right - offsetX + (int) rotX, bottom - offsetY + (int) rotY, animationFrameX * resourceX, 16, resourceX, resourceY);

		}

		// Grab frameTimer, use it to control animation speed
		int i = instance.frameTimer.getIndex();

		// Higher Number, Slower Animation
		if (i % 5 == 0) {
			animationFrameX++;
			if (animationFrameX > 3)
				animationFrameX = 0;
		}
	}
}
