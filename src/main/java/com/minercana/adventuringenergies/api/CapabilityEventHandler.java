package com.minercana.adventuringenergies.api;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.api.energytracker.EnergyTrackerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEventHandler {
    @SubscribeEvent
    public static void attachCapabilitiesToPlayers(AttachCapabilitiesEvent<PlayerEntity> event) {
        event.addCapability(new ResourceLocation(AdventuringEnergies.MOD_ID, "energy_tracker"), new EnergyTrackerProvider());
    }
}
