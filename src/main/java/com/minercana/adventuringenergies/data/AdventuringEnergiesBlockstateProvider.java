package com.minercana.adventuringenergies.data;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.blocks.AdventuringEnergiesBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AdventuringEnergiesBlockstateProvider extends BlockStateProvider {

    public AdventuringEnergiesBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, AdventuringEnergies.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(AdventuringEnergiesBlocks.GOLDEN_ALTAR.get());
    }
}
