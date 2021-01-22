package com.minercana.adventuringenergies.events;

import com.minercana.adventuringenergies.AdventuringEnergies;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AdventuringEnergies.MOD_ID)
public class LootInjectionEvents {
    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event) {
        if (event.getName().equals(new ResourceLocation("chests/simple_dungeon"))) {
            event.getTable().addPool(getInjectPool("chests/simple_dungeon"));
        }
    }

    private static LootPool getInjectPool(String pool) {
        return LootPool.builder()
                .addEntry(getInjectEntry(pool, 1))
                .name("adventuring_energies_inject")
                .build();
    }

    private static LootEntry.Builder<?> getInjectEntry(String name, int weight) {
        ResourceLocation injectedFolder = new ResourceLocation(AdventuringEnergies.MOD_ID, "inject/" + name);
        return TableLootEntry.builder(injectedFolder).weight(weight);
    }
}
