package com.minercana.adventuringenergies.events;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.energytypes.AEEnergyTypes;
import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
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
                final Boolean canUseEnergy = player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).map(tracker -> tracker.useEnergy(AEEnergyTypes.YELLOW.get(), player)).orElse(false);
                if (canUseEnergy) {
                    foundCurioTriple.getRight().shrink(1);
                    player.setHealth(1f);
                    player.clearActivePotions();
                    final BlockPos respawnLocation = player.func_241140_K_();
                    if (respawnLocation != null && player.getServer() != null){
                        final ServerWorld spawnDimension = player.getServer().getWorld(player.func_241141_L_());
                        if (spawnDimension != null) {
                            player.teleport(spawnDimension, respawnLocation.getX(), respawnLocation.getY(), respawnLocation.getZ(), 0, 0);
                        }
                        event.setCanceled(true);
                    }
                }
            });
        }
    }
}

