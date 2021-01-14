package com.minercana.adventuringenergies.api.energyrecoverytimers;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEnergyRecoveryTimers extends INBTSerializable<CompoundNBT> {

    int getBlueTimer();

    int getGreenTimer();

    boolean incrementBlueTimer(ServerPlayerEntity playerEntity);

    boolean incrementGreenTimer(ServerPlayerEntity playerEntity);

}
