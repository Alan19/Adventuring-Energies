package com.minercana.adventuringenergies.api.energyrecoverytimers;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class EnergyRecoveryTimers implements IEnergyRecoveryTimers {
    private int blueTimer;
    private int greenTimer;
    private int yellowTimer;

    public int getYellowTimer() {
        return yellowTimer;
    }

    @Override
    public int getBlueTimer() {
        return blueTimer;
    }

    @Override
    public int getGreenTimer() {
        return greenTimer;
    }

    @Override
    public boolean incrementBlueTimer(ServerPlayerEntity playerEntity, int i) {
        blueTimer += i;
        final int blueRequiredTime = getBlueRequiredTime(playerEntity);
        if (blueTimer >= blueRequiredTime) {
            blueTimer -= blueRequiredTime;
            return true;
        }
        return false;
    }

    @Override
    public boolean incrementGreenTimer(ServerPlayerEntity playerEntity) {
        greenTimer += 1;
        if (greenTimer >= 400) {
            greenTimer = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean incrementYellowTimer(ServerPlayerEntity player, int i) {
        yellowTimer += i;
        if (yellowTimer >= 1200) {
            yellowTimer = 0;
            return true;
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
    public void resetYellowTimer() {
        yellowTimer = 0;
    }

    @Override
    public void resetBlueTimer() {
        blueTimer = 0;
    }

    @Override
    public void resetGreenTimer() {
        greenTimer = 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("AzureTimer", blueTimer);
        nbt.putInt("VeridianTimer", greenTimer);
        nbt.putInt("YellowTimer", yellowTimer);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        blueTimer = nbt.getInt("AzureTimer");
        greenTimer = nbt.getInt("VeridianTimer");
        yellowTimer = nbt.getInt("YellowTimer");
    }
}
