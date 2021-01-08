package com.minercana.adventuringenergies.api.energytracker;

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
        return null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
