package com.minercana.adventuringenergies.api.energytracker;

import com.minercana.adventuringenergies.energytypes.EnergyType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEnergyTracker extends INBTSerializable<CompoundNBT> {
    /**
     * Attempts to remove the specified amount of energy from a player. If the removal is simulated, the orbs will not be removed even if the player has enough energy.
     *
     * @param type     The type of energy to remove
     * @param player   The player to remove the energy from
     * @param count    The units of energy to remove
     * @param simulate If the removal is simulated
     * @return If the player is able to have that many units of energy removed from them
     */
    boolean useEnergy(EnergyType type, ServerPlayerEntity player, int count, boolean simulate);

    default boolean useEnergy(EnergyType type, ServerPlayerEntity player, int count) {
        return useEnergy(type, player, count, false);
    }

    default boolean useEnergy(EnergyType type, ServerPlayerEntity player) {
        return useEnergy(type, player, 1);
    }

    int removeEnergy(EnergyType type, int count, ServerPlayerEntity player, boolean simulate);

    default int removeEnergy(EnergyType type, ServerPlayerEntity player, boolean simulate) {
        return removeEnergy(type, 1, player, simulate);
    }

    int addEnergy(EnergyType type, int count, ServerPlayerEntity player, boolean simulate);

    default int addEnergy(EnergyType type, ServerPlayerEntity player, boolean simulate) {
        return addEnergy(type, 1, player, simulate);
    }

    int getEnergy(EnergyType type);

    default int increaseMaxEnergy(EnergyType type, ServerPlayerEntity player, boolean simulate) {
        return increaseMaxEnergy(type, 1, player, simulate);
    }

    int increaseMaxEnergy(EnergyType type, int count, ServerPlayerEntity player, boolean simulate);

    default int decreaseMaxEnergy(EnergyType type, ServerPlayerEntity player, boolean simulate) {
        return decreaseMaxEnergy(type,1, player, simulate);
    }

    int decreaseMaxEnergy(EnergyType type, int count, ServerPlayerEntity player, boolean simulate);

    void setEnergyCapForType(EnergyType type, int count, ServerPlayerEntity player);

    int getEnergyCap(EnergyType type);

    int getTotalEnergyCap();
}
