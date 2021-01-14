package com.minercana.adventuringenergies.events;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.energytypes.AEEnergyTypes;
import com.minercana.adventuringenergies.network.AdventuringEnergiesNetwork;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AdventuringEnergies.MOD_ID)
public class EnergyRecoveryEvents {
    @SubscribeEvent
    public static void goldenOrbRecovery(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayerEntity && event.player.getEntityWorld().getDayTime() % 1200 == 0 && isInDaylight(event.player)) {
            event.player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).ifPresent(tracker -> tracker.addEnergy(AEEnergyTypes.YELLOW.get(), (ServerPlayerEntity) event.player));
        }
    }

    protected static boolean isInDaylight(PlayerEntity playerEntity) {
        if (playerEntity.world.isDaytime() && !playerEntity.world.isRemote) {
            float f = playerEntity.getBrightness();
            BlockPos blockpos = playerEntity.getRidingEntity() instanceof BoatEntity ? (new BlockPos(playerEntity.getPosX(), (double) Math.round(playerEntity.getPosY()), playerEntity.getPosZ())).up() : new BlockPos(playerEntity.getPosX(), (double) Math.round(playerEntity.getPosY()), playerEntity.getPosZ());
            return f > 0.5F && playerEntity.world.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && playerEntity.world.canSeeSky(blockpos);
        }

        return false;
    }

    @SubscribeEvent
    public static void sendEnergyToClientOnLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            AdventuringEnergiesNetwork.sendEnergyToClient((ServerPlayerEntity) event.getPlayer());
        }
    }
}
