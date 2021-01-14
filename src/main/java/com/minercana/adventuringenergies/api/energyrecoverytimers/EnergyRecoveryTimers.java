package com.minercana.adventuringenergies.api.energyrecoverytimers;

import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.energytypes.AEEnergyTypes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;

public class EnergyRecoveryTimers implements IEnergyRecoveryTimers {
    private int blueTimer;
    private int greenTimer;

    @Override
    public int getBlueTimer() {
        return blueTimer;
    }

    @Override
    public int getGreenTimer() {
        return greenTimer;
    }

    @Override
    public boolean incrementBlueTimer(ServerPlayerEntity playerEntity) {
        if (playerEntity.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).map(tracker -> tracker.addEnergy(AEEnergyTypes.AZURE.get(), playerEntity, true) == 0).orElse(false)) {
            blueTimer++;
            if (blueTimer >= getBlueRequiredTime(playerEntity)) {
                return playerEntity.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).map(tracker -> tracker.addEnergy(AEEnergyTypes.AZURE.get(), playerEntity, false) == 0).orElse(false);

            }
        }
        else {
            blueTimer = 0;
        }
        return false;
    }

    @Override
    public boolean incrementGreenTimer(ServerPlayerEntity playerEntity) {
        if (playerEntity.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).map(tracker -> tracker.addEnergy(AEEnergyTypes.VERDANT.get(), playerEntity, true) == 0).orElse(false)){
            if (playerEntity.isAirBorne) {
                greenTimer++;
            }
            else {
                greenTimer = 0;
            }
            if (greenTimer >= 400) {
                playerEntity.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).map(tracker -> tracker.addEnergy(AEEnergyTypes.VERDANT.get(), playerEntity));
                return true;
            }
        }
        return false;
    }

    private int getBlueRequiredTime(ServerPlayerEntity playerEntity) {
        int base = 3600;
        if (playerEntity.isInWater()) {
            base -= 400;
        }
        if (playerEntity.isSwimming()) {
            base -= 400;
        }
        final Biome biomePlayerIsIn = playerEntity.getServerWorld().getBiome(playerEntity.getPosition());
        if (biomePlayerIsIn.getRegistryName() != null && BiomeDictionary.hasType(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biomePlayerIsIn.getRegistryName()), BiomeDictionary.Type.WATER)) {
            base -= 400;
        }
        return base;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("AzureTimer", blueTimer);
        nbt.putInt("VeridianTimer", greenTimer);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        blueTimer = nbt.getInt("AzureTimer");
        greenTimer = nbt.getInt("VeridianTimer");
    }
}
