package com.minercana.adventuringenergies.data;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.AdventuringEnergiesTags;
import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;

public class AdventuringEnergiesItemTagProvider extends ItemTagsProvider {
    public AdventuringEnergiesItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, AdventuringEnergies.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(ItemTags.createOptional(new ResourceLocation(CuriosApi.MODID, "necklace"))).add(AdventuringEnergiesItems.AMULET_OF_RECOVERY.get());
        getOrCreateBuilder(AdventuringEnergiesTags.GOLDEN_ALTAR_UNLOCK).add(Items.SUNFLOWER, Items.HONEYCOMB, Items.CLOCK);
    }
}
