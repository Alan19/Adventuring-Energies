package com.minercana.adventuringenergies.blocks;

import com.minercana.adventuringenergies.AdventuringEnergies;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class AEBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Block.class, AdventuringEnergies.MOD_ID);
    public static RegistryObject<GoldenAltar> GOLDEN_ALTAR = BLOCKS.register("golden_altar", GoldenAltar::new);
    public static RegistryObject<MagicalGlows> MAGICAL_GLOWS = BLOCKS.register("magical_glows", MagicalGlows::new);
    public static RegistryObject<MagicalCrystals> MAGICAL_CRYSTALS = BLOCKS.register("magical_crystals", MagicalCrystals::new);

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
