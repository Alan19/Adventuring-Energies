package com.minercana.adventuringenergies.api.energyrecoverytimers;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEnergyRecoveryTimers extends INBTSerializable<CompoundNBT> {

    int getBlueTimer();

    int getGreenTimer();

    boolean incrementBlueTimer(ServerPlayerEntity playerEntity, int i);

    boolean incrementGreenTimer(ServerPlayerEntity playerEntity);

    boolean incrementYellowTimer(ServerPlayerEntity playerEntity, int i);

    void resetYellowTimer();

    void resetBlueTimer();

     void resetGreenTimer();
}
