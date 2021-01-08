package com.minercana.adventuringenergies.energytypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class EnergyType extends ForgeRegistryEntry<EnergyType> implements INBTSerializable<CompoundNBT> {
    private int color;

    public EnergyType(int color) {
        this.color = color;
    }

    public void onOrbUse(PlayerEntity player) {
        // By default, orbs have no on use effects
    }

    public int getColor() {
        return color;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("color", color);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        color = nbt.getInt("color");
    }
}
