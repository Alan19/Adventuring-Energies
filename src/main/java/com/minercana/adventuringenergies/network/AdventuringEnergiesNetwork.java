package com.minercana.adventuringenergies.network;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class AdventuringEnergiesNetwork {
    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(AdventuringEnergies.MOD_ID, "network");
    private static final String NETWORK_VERSION = new ResourceLocation(AdventuringEnergies.MOD_ID, "1").toString();

    public static SimpleChannel getNetworkChannel() {
        final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> NETWORK_VERSION)
                .simpleChannel();

        channel.messageBuilder(SyncEnergiesMessage.class, 1)
                .decoder(SyncEnergiesMessage::decode)
                .encoder(SyncEnergiesMessage::encode)
                .consumer(SyncEnergiesMessage::handle)
                .add();

        return channel;
    }

    public static void sendEnergyToClient(ServerPlayerEntity player) {
        player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).ifPresent(tracker -> AdventuringEnergies.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SyncEnergiesMessage(tracker)));
    }
}
