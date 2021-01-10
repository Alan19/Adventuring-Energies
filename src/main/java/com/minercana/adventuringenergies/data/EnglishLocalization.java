package com.minercana.adventuringenergies.data;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.blocks.AdventuringEnergiesBlocks;
import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Locale;

public class EnglishLocalization extends LanguageProvider {
    public EnglishLocalization(DataGenerator gen) {
        super(gen, AdventuringEnergies.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItems();
        addBlocks();
    }

    private void addBlocks() {
        add(AdventuringEnergiesBlocks.GOLDEN_ALTAR.get(), "Golden Altar");
    }

    private void addItems() {
        add(AdventuringEnergiesItems.GOLDEN_ALTAR.get(), "Golden Altar");
        add(AdventuringEnergiesItems.AMULET_OF_RECOVERY.get(), "Amulet of Recovery");
    }
}
