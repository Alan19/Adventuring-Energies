package com.minercana.adventuringenergies.items;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.blocks.AdventuringEnergiesBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class AdventuringEnergiesItems {
    public static final ItemGroup ADVENTURING_ENERGIES_ITEM_GROUP = new ItemGroup(AdventuringEnergies.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(GOLDEN_ALTAR.get());
        }
    };

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Item.class, AdventuringEnergies.MOD_ID);

    public static final RegistryObject<BlockNamedItem> GOLDEN_ALTAR = ITEMS.register("golden_altar", () -> convertToBlockItem(AdventuringEnergiesBlocks.GOLDEN_ALTAR.get()));

    /**
     * Converts a block into a BlockNamedItem that belongs to the Astral tab
     *
     * @param block The block to be converted
     * @return A converted block
     */
    private static BlockNamedItem convertToBlockItem(Block block) {
        return new BlockNamedItem(block, new Item.Properties().group(ADVENTURING_ENERGIES_ITEM_GROUP));
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

}
