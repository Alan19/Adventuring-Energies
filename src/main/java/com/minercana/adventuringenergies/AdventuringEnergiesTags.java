package com.minercana.adventuringenergies;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class AdventuringEnergiesTags {
    public static final ITag.INamedTag<Item> GOLDEN_ALTAR_UNLOCK = ItemTags.createOptional(new ResourceLocation(AdventuringEnergies.MOD_ID, "golden_altar_unlock"));
}
