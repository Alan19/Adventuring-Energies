package com.minercana.adventuringenergies.data;

import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class AdventuringEnergiesRecipeProvider extends RecipeProvider {
    public AdventuringEnergiesRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(AdventuringEnergiesItems.GOLDEN_ALTAR.get())
                .key('G', Tags.Items.STORAGE_BLOCKS_GOLD)
                .key('D', Tags.Items.DUSTS_GLOWSTONE)
                .key('A', Items.RED_WOOL)
                .key('B', Items.BLUE_WOOL)
                .key('C', Items.GREEN_WOOL)
                .patternLine("DBD")
                .patternLine("AGC")
                .patternLine("DGD")
                .addCriterion("has_gold_block", hasItem(Tags.Items.STORAGE_BLOCKS_GOLD))
                .build(consumer);
    }
}
