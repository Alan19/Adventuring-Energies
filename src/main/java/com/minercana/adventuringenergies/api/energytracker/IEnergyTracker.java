package com.minercana.adventuringenergies.api.energytracker;

import com.minercana.adventuringenergies.energytypes.EnergyType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEnergyTracker extends INBTSerializable<CompoundNBT> {
    boolean useEnergy(EnergyType type, PlayerEntity player, int count);

    default boolean useEnergy(EnergyType type, PlayerEntity player) {
        return useEnergy(type, player, 1);
    }

    int removeEnergy(EnergyType type, int count);

    default int removeEnergy(EnergyType type) {
        return removeEnergy(type, 1);
    }

    int addEnergy(EnergyType type, int count);

    default int addEnergy(EnergyType type) {
        return addEnergy(type, 1);
    }

    int getEnergy(EnergyType type);
}
