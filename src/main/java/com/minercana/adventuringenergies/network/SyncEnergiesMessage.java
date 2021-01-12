package com.minercana.adventuringenergies.network;

import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncEnergiesMessage {
    private final CompoundNBT energy;

    public SyncEnergiesMessage(IEnergyTracker energy) {
        this.energy = energy.serializeNBT();
    }

    public SyncEnergiesMessage(CompoundNBT energyNBT){
        this.energy = energyNBT;
    }

    public static SyncEnergiesMessage decode(PacketBuffer packetBuffer) {
        return new SyncEnergiesMessage(packetBuffer.readCompoundTag());
    }

    public static void encode(SyncEnergiesMessage syncEnergiesMessage, PacketBuffer packetBuffer) {
        packetBuffer.writeCompoundTag(syncEnergiesMessage.energy);
    }

    public static void handle(SyncEnergiesMessage syncEnergiesMessage, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).ifPresent(tracker -> tracker.deserializeNBT(syncEnergiesMessage.energy));
            }
        });
    }
}
