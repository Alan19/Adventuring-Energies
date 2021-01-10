package com.minercana.adventuringenergies.data;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class AdventuringEnergiesItemModels extends ItemModelProvider {
    private final ResourceLocation generatedItem = mcLoc("item/generated");

    public AdventuringEnergiesItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AdventuringEnergies.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        forItem(AdventuringEnergiesItems.AMULET_OF_RECOVERY);
        forBlockItemWithParent(AdventuringEnergiesItems.GOLDEN_ALTAR);
    }

    private void forItem(RegistryObject<? extends Item> item) {
        this.singleTexture(item.getId().getPath(), mcLoc("item/handheld"), "layer0", modLoc("item/" + item.getId().getPath()));
    }

    private void forBlockItem(RegistryObject<? extends BlockNamedItem> item) {
        getBuilder(item.getId().getPath()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(AdventuringEnergies.MOD_ID, "block/" + item.get().getBlock().getRegistryName().getPath())));
    }

    private void forBlockItem(RegistryObject<? extends BlockNamedItem> item, ResourceLocation modelLocation) {
        getBuilder(item.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modelLocation));
    }

    private void forBlockItemWithParent(RegistryObject<? extends BlockNamedItem> item, ResourceLocation modelLocation) {
        singleTexture(item.getId().getPath(), generatedItem, "layer0", modelLocation);
    }

    private void forBlockItemWithParent(RegistryObject<? extends BlockNamedItem> item) {
        singleTexture(item.getId().getPath(), generatedItem, "layer0", modLoc("block/" + item.getId().getPath()));
    }


}
