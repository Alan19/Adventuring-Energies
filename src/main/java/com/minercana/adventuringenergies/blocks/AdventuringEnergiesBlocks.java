package com.minercana.adventuringenergies.blocks;

import com.minercana.adventuringenergies.AdventuringEnergies;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class AdventuringEnergiesBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Block.class, AdventuringEnergies.MOD_ID);
    public static RegistryObject<GoldenAltar> GOLDEN_ALTAR = BLOCKS.register("golden_altar", GoldenAltar::new);

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
