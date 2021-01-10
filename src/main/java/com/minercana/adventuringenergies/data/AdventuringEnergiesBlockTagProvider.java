package com.minercana.adventuringenergies.data;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.AdventuringEnergiesTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class AdventuringEnergiesBlockTagProvider extends BlockTagsProvider {
    public AdventuringEnergiesBlockTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, AdventuringEnergies.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
    }
}
