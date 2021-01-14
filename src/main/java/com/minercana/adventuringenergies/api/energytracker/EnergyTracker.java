package com.minercana.adventuringenergies.api.energytracker;

import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.energytypes.EnergyType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.minercana.adventuringenergies.network.AdventuringEnergiesNetwork.sendEnergyToClient;

public class EnergyTracker implements IEnergyTracker {
    private final Map<EnergyType, Integer> energyMap = new HashMap<>();
    private final Map<EnergyType, Integer> energyCap = new HashMap<>();

    @Override
    public boolean useEnergy(EnergyType type, ServerPlayerEntity player, int count, boolean simulate) {
        final Integer orbCount = energyMap.getOrDefault(type, 0);
        final int newCount = orbCount - count;
        final boolean canRemove = newCount >= 0;
        if (simulate || newCount < 0) {
            return canRemove;
        }
        energyMap.put(type, newCount);
        sendEnergyToClient(player);
        return true;
    }

    @Override
    public int removeEnergy(EnergyType type, int count, ServerPlayerEntity player, boolean simulate) {
        int current = energyMap.getOrDefault(type, 0);
        final int newAmount = current - count;
        if (!simulate) {
            energyMap.put(type, Math.max(newAmount, 0));
            sendEnergyToClient(player);
        }
        return newAmount;
    }

    @Override
    public int addEnergy(EnergyType type, int count, ServerPlayerEntity player, boolean simulate) {
        int cap = energyCap.getOrDefault(type, 0);
        int current = energyMap.getOrDefault(type, 0);
        if (!simulate) {
            energyMap.put(type, Math.min(current + count, cap));
            sendEnergyToClient(player);
        }
        return Math.max(current + count - cap, 0);
    }

    @Override
    public int getEnergy(EnergyType type) {
        return energyMap.getOrDefault(type, 0);
    }

    @Override
    public int increaseMaxEnergy(EnergyType type, int count, ServerPlayerEntity player, boolean simulate) {
        int currentCap = energyCap.getOrDefault(type, 0);
        int availableSlots = 10 - getTotalEnergyCap();
        if (!simulate && availableSlots > 0) {
            final Integer newCap = Math.min(availableSlots, count + currentCap);
            sendEnergyToClient(player);
        }
        return Math.max(currentCap + count - availableSlots, 0);
    }

    @Override
    public int decreaseMaxEnergy(EnergyType type, int count, ServerPlayerEntity player, boolean simulate) {
        int currentCap = energyCap.getOrDefault(type, 0);
        final int newCap = currentCap - count;
        if (!simulate) {
            energyCap.put(type, Math.max(newCap, 0));
            sendEnergyToClient(player);
        }
        return newCap;
    }

    @Override
    public void setEnergyCapForType(EnergyType type, int count, ServerPlayerEntity player) {
        int availableSlots = 10 - getTotalEnergyCap() + energyCap.getOrDefault(type, 0);
        int newCap = count > availableSlots ? availableSlots : Math.max(0, count);
        energyCap.put(type, newCap);
        sendEnergyToClient(player);
    }

    @Override
    public int getEnergyCap(EnergyType type) {
        return energyCap.getOrDefault(type, 0);
    }

    @Override
    public int getTotalEnergyCap() {
        return energyCap.values().stream().mapToInt(value -> value).sum();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        CompoundNBT energyCapNBT = new CompoundNBT();
        energyCap.forEach((type, integer) -> energyCapNBT.putInt(type.getRegistryName().toString(), integer));
        nbt.put("EnergyCap", energyCapNBT);
        CompoundNBT orbMapNBT = new CompoundNBT();
        energyMap.forEach((type, integer) -> orbMapNBT.putInt(type.getRegistryName().toString(), integer));
        nbt.put("EnergyMap", orbMapNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CompoundNBT energyCapNBT = nbt.getCompound("EnergyCap");
        for (String key : energyCapNBT.keySet()) {
            final ResourceLocation location = new ResourceLocation(key);
            if (AdventuringEnergiesAPI.ENERGY_TYPES.get().containsKey(location)) {
                energyCap.put(AdventuringEnergiesAPI.ENERGY_TYPES.get().getValue(location), energyCapNBT.getInt(key));
            }
        }
        CompoundNBT orbMap = nbt.getCompound("EnergyMap");
        for (String key : orbMap.keySet()) {
            final ResourceLocation location = new ResourceLocation(key);
            if (AdventuringEnergiesAPI.ENERGY_TYPES.get().containsKey(location)) {
                energyMap.put(AdventuringEnergiesAPI.ENERGY_TYPES.get().getValue(location), orbMap.getInt(key));
            }
        }
    }
}
