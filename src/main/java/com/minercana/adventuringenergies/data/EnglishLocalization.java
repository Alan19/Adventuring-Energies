package com.minercana.adventuringenergies.data;

import com.minercana.adventuringenergies.blocks.AdventuringEnergiesBlocks;
import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Locale;

public class EnglishLocalization extends LanguageProvider {
    public EnglishLocalization(DataGenerator gen, String modid, String locale) {
        super(gen, modid, "en_us");
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
    }
}
