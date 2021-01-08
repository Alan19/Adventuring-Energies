package com.minercana.adventuringenergies.api;

import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import com.minercana.adventuringenergies.energytypes.EnergyType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class AdventuringEnergiesAPI {
    public static final Lazy<IForgeRegistry<EnergyType>> ENERGY_TYPES = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(EnergyType.class));

    @CapabilityInject(IEnergyTracker.class)
    public static Capability<IEnergyTracker> energyTrackerCapability;
}
