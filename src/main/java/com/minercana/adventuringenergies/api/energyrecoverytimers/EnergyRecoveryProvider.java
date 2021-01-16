package com.minercana.adventuringenergies.api.energyrecoverytimers;

import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.api.energytracker.EnergyTracker;
import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyRecoveryProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IEnergyRecoveryTimers recoveryTimers;
    private final LazyOptional<IEnergyRecoveryTimers> recoveryTimerLazyOptional;

    public EnergyRecoveryProvider() {
        this(new EnergyRecoveryTimers());
    }

    public EnergyRecoveryProvider(EnergyRecoveryTimers energyRecoveryTimers) {
        this.recoveryTimers = energyRecoveryTimers;
        this.recoveryTimerLazyOptional = LazyOptional.of(() -> energyRecoveryTimers);
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AdventuringEnergiesAPI.energyRecoveryTimersCapability ? recoveryTimerLazyOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return recoveryTimers.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        recoveryTimers.deserializeNBT(nbt);
    }

}
