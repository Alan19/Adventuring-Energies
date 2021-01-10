package com.minercana.adventuringenergies.events;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = AdventuringEnergies.MOD_ID)
public class ToolEvents {
    @SubscribeEvent
    public void amuletOfRecoveryOnDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
            CuriosApi.getCuriosHelper().findEquippedCurio(AdventuringEnergiesItems.AMULET_OF_RECOVERY.get(), player).ifPresent(foundCurioTriple -> {
                foundCurioTriple.getRight().shrink(1);
                player.setHealth(1f);
                player.clearActivePotions();
                player.wakeUp();
                event.setCanceled(true);
            });
        }
    }
}

