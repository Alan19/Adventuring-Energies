package com.minercana.adventuringenergies.api.energytracker;

import com.minercana.adventuringenergies.energytypes.EnergyType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEnergyTracker extends INBTSerializable<CompoundNBT> {
    /**
     * Attempts to remove the specified amount of energy from a player. If the removal is simulated, the orbs will not be removed even if the player has enough energy.
     * @param type The type of energy to remove
     * @param player The player to remove the energy from
     * @param count The units of energy to remove
     * @param simulate If the removal is simulated
     * @return If the player is able to have that many units of energy removed from them
     */
    boolean useEnergy(EnergyType type, PlayerEntity player, int count, boolean simulate);

    default boolean useEnergy(EnergyType type, PlayerEntity player, int count) {
        return useEnergy(type, player, count, false);
    }

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

    default int increaseMaxEnergy() {
        return increaseMaxEnergy(1);
    }

    int increaseMaxEnergy(int count);

    default int decreaseMaxEnergy() {
        return decreaseMaxEnergy(1);
    }

    int decreaseMaxEnergy(int count);

    void setMaxEnergy(int count);

    int getEnergyCap();
}
