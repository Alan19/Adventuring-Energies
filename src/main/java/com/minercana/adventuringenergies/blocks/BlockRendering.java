package com.minercana.adventuringenergies.blocks;

import com.minercana.adventuringenergies.blocks.AdventuringEnergiesBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlockRendering {
    public static void setupRenderTypes() {
        RenderType cutout = RenderType.getCutout();
        RenderTypeLookup.setRenderLayer(AdventuringEnergiesBlocks.GOLDEN_ALTAR.get(), cutout);
    }
}
