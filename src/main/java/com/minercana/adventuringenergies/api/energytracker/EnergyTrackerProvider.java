package com.minercana.adventuringenergies.api.energytracker;

import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyTrackerProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IEnergyTracker energyTracker;
    private final LazyOptional<IEnergyTracker> trackerOptional;

    public EnergyTrackerProvider() {
        this(new EnergyTracker());
    }

    public EnergyTrackerProvider(EnergyTracker energyTracker) {
        this.energyTracker = energyTracker;
        this.trackerOptional = LazyOptional.of(() -> energyTracker);
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AdventuringEnergiesAPI.energyTrackerCapability ? trackerOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return energyTracker.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        energyTracker.deserializeNBT(nbt);
    }
}
