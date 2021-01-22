package com.minercana.adventuringenergies.events;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.api.energyrecoverytimers.IEnergyRecoveryTimers;
import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import com.minercana.adventuringenergies.energytypes.AEEnergyTypes;
import com.minercana.adventuringenergies.energytypes.EnergyType;
import com.minercana.adventuringenergies.network.AdventuringEnergiesNetwork;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = AdventuringEnergies.MOD_ID)
public class EnergyRecoveryEvents {
    private static final Map<UUID, Pair<LazyOptional<IEnergyRecoveryTimers>, LazyOptional<IEnergyTracker>>> timersMap = new HashMap<>();

    @SubscribeEvent
    public static void goldenOrbRecovery(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            final Pair<LazyOptional<IEnergyRecoveryTimers>, LazyOptional<IEnergyTracker>> timerTrackerPair = getTimerTrackerPair(player);
            handleGoldOrbRecovery(player, timerTrackerPair);
            handleAzureOrbRecovery(player, timerTrackerPair);
        }
    }

    @Nonnull
    private static Pair<LazyOptional<IEnergyRecoveryTimers>, LazyOptional<IEnergyTracker>> getTimerTrackerPair(ServerPlayerEntity player) {
        return timersMap.computeIfAbsent(player.getUniqueID(), uuid -> {
            final LazyOptional<IEnergyRecoveryTimers> timerCap = player.getCapability(AdventuringEnergiesAPI.energyRecoveryTimersCapability);
            timerCap.addListener(optional -> {
                if (timersMap.containsKey(uuid)) {
                    timersMap.remove(uuid);
                }
            });
            final LazyOptional<IEnergyTracker> trackerCap = player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability);
            trackerCap.addListener(optional -> {
                if (timersMap.containsKey(uuid)) {
                    timersMap.remove(uuid);
                }
            });
            return Pair.of(timerCap, trackerCap);
        });
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            getTimerTrackerPair((ServerPlayerEntity) event.getEntityLiving()).getLeft().ifPresent(IEnergyRecoveryTimers::resetBlueTimer);
        }
    }

    // TODO Make XP orbs give progress towards next orb
    @SubscribeEvent
    public static void onPlayerPickupXP(PlayerXpEvent.PickupXp event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            final Pair<LazyOptional<IEnergyRecoveryTimers>, LazyOptional<IEnergyTracker>> timerTrackerPair = getTimerTrackerPair(player);
            if (canAddOrb(player, AEEnergyTypes.AZURE.get())) {
                final Boolean timerFilled = timerTrackerPair.getLeft().map(timer -> timer.incrementBlueTimer(player, event.getOrb().getXpValue() * 20)).orElse(false);
                if (timerFilled) {
                    timerTrackerPair.getRight().ifPresent(tracker -> tracker.addEnergy(AEEnergyTypes.AZURE.get(), player, false));
                }
            }
        }
    }

    private static void handleAzureOrbRecovery(ServerPlayerEntity player, Pair<LazyOptional<IEnergyRecoveryTimers>, LazyOptional<IEnergyTracker>> timerTrackerPair) {
        if (!canAddOrb(player, AEEnergyTypes.AZURE.get())) {
            timerTrackerPair.getLeft().ifPresent(IEnergyRecoveryTimers::resetBlueTimer);
        }
        else {
            final Boolean timerFilled = timerTrackerPair.getLeft().map(timers -> timers.incrementBlueTimer(player, 1)).orElse(false);
            if (timerFilled) {
                timerTrackerPair.getRight().ifPresent(tracker -> tracker.addEnergy(AEEnergyTypes.AZURE.get(), player, false));
            }
        }
    }

    private static void handleGoldOrbRecovery(ServerPlayerEntity player, Pair<LazyOptional<IEnergyRecoveryTimers>, LazyOptional<IEnergyTracker>> timersLazyOptional) {
        if (!isInDaylight(player) || !canAddOrb(player, AEEnergyTypes.YELLOW.get())) {
            timersLazyOptional.getLeft().ifPresent(IEnergyRecoveryTimers::resetYellowTimer);
        }
        else {
            final Boolean timerFilled = timersLazyOptional.getLeft().map(timers -> timers.incrementYellowTimer(player, 1)).orElse(false);
            if (timerFilled) {
                timersLazyOptional.getRight().ifPresent(tracker -> tracker.addEnergy(AEEnergyTypes.YELLOW.get(), player, false));
            }
        }
    }

    @Nonnull
    private static Boolean canAddOrb(ServerPlayerEntity player, EnergyType energyType) {
        return player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).map(tracker -> tracker.addEnergy(energyType, player, true) == 0).orElse(false);
    }

    protected static boolean isInDaylight(PlayerEntity playerEntity) {
        if (playerEntity.world.isDaytime() && !playerEntity.world.isRemote) {
            float f = playerEntity.getBrightness();
            BlockPos blockpos = playerEntity.getRidingEntity() instanceof BoatEntity ? (new BlockPos(playerEntity.getPosX(), (double) Math.round(playerEntity.getPosY()), playerEntity.getPosZ())).up() : new BlockPos(playerEntity.getPosX(), (double) Math.round(playerEntity.getPosY()), playerEntity.getPosZ());
            return f > 0.5F && playerEntity.world.canSeeSky(blockpos);
        }

        return false;
    }

    @SubscribeEvent
    public static void sendEnergyToClientOnWorldJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            AdventuringEnergiesNetwork.sendEnergyToClient((ServerPlayerEntity) event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerRemoved(EntityLeaveWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            timersMap.remove(event.getEntity().getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            final LazyOptional<IEnergyTracker> energyTrackerLazyOptional = event.getOriginal().getCapability(AdventuringEnergiesAPI.energyTrackerCapability);
            energyTrackerLazyOptional.ifPresent(tracker -> event.getPlayer().getCapability(AdventuringEnergiesAPI.energyTrackerCapability).ifPresent(newTracker -> newTracker.deserializeNBT(tracker.serializeNBT())));
            energyTrackerLazyOptional.invalidate();
        }
    }
}
