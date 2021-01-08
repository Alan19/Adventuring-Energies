package com.minercana.adventuringenergies.api.energytracker;

import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.energytypes.EnergyType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EnergyTracker implements IEnergyTracker {
    private final Map<EnergyType, Integer> energyMap = new HashMap<>();

    @Override
    public boolean useEnergy(EnergyType type, PlayerEntity player, int count) {
        final Integer computedOrbCount = energyMap.compute(type, (energyType, orbCount) -> Optional.ofNullable(orbCount).orElse(0));
        if (computedOrbCount <= 0) {
            return false;
        }
        else {
            energyMap.computeIfPresent(type, (energyType, orbCount) -> orbCount - 1);
            type.onOrbUse(player);
            return true;
        }
    }

    @Override
    public int removeEnergy(EnergyType type, int count) {
        return energyMap.compute(type, (energyType, orbCount) -> Optional.ofNullable(orbCount).map(integer -> integer - 1).orElse(-1));
    }

    @Override
    public int addEnergy(EnergyType type, int count) {
        return energyMap.compute(type, (energyType, orbCount) -> Optional.ofNullable(orbCount).map(integer -> integer + 1).orElse(1));
    }

    @Override
    public int getEnergy(EnergyType type) {
        return energyMap.getOrDefault(type, 0);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        energyMap.forEach((type, integer) -> nbt.putInt(type.getRegistryName().toString(), integer));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (String key : nbt.keySet()) {
            final ResourceLocation location = new ResourceLocation(key);
            if (AdventuringEnergiesAPI.ENERGY_TYPES.get().containsKey(location)) {
                energyMap.put(AdventuringEnergiesAPI.ENERGY_TYPES.get().getValue(location), nbt.getInt(key));
            }
        }
    }
}
