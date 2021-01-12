package com.minercana.adventuringenergies.api.energytracker;

import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.energytypes.EnergyType;
import com.minercana.adventuringenergies.network.AdventuringEnergiesNetwork;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EnergyTracker implements IEnergyTracker {
    private final Map<EnergyType, Integer> energyMap = new HashMap<>();
    private int energyCap;

    @Override
    public boolean useEnergy(EnergyType type, PlayerEntity player, int count, boolean simulate) {
        final Integer orbCount = energyMap.getOrDefault(type, 0);
        final int newCount = orbCount - count;
        final boolean canRemove = newCount >= 0;
        if (simulate || newCount < 0) {
            return canRemove;
        }
        energyMap.put(type, newCount);
        return true;
    }

    @Override
    public int removeEnergy(EnergyType type, int count) {
        return energyMap.compute(type, (energyType, orbCount) -> Math.max(Optional.ofNullable(orbCount).map(integer -> integer - count).orElse(count * -1), 0));
    }

    @Override
    public int addEnergy(EnergyType type, int count) {
        return energyMap.compute(type, (energyType, orbCount) -> Math.max(Optional.ofNullable(orbCount).map(integer -> integer + count).orElse(count), 0));
    }

    @Override
    public int getEnergy(EnergyType type) {
        return energyMap.getOrDefault(type, 0);
    }

    @Override
    public int increaseMaxEnergy(int count) {
        energyCap += count;
        return energyCap;
    }

    @Override
    public int decreaseMaxEnergy(int count) {
        this.energyCap = Math.max(energyCap - count, 0);
        return energyCap;
    }

    @Override
    public void setMaxEnergy(int count) {
        energyCap = count;
    }

    @Override
    public int getEnergyCap() {
        return energyCap;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("EnergyCap", energyCap);
        CompoundNBT orbMapNBT = new CompoundNBT();
        energyMap.forEach((type, integer) -> orbMapNBT.putInt(type.getRegistryName().toString(), integer));
        nbt.put("EnergyMap", orbMapNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        energyCap = nbt.getInt("EnergyCap");
        CompoundNBT orbMap = nbt.getCompound("EnergyMap");
        for (String key : orbMap.keySet()) {
            final ResourceLocation location = new ResourceLocation(key);
            if (AdventuringEnergiesAPI.ENERGY_TYPES.get().containsKey(location)) {
                energyMap.put(AdventuringEnergiesAPI.ENERGY_TYPES.get().getValue(location), orbMap.getInt(key));
            }
        }
    }
}
