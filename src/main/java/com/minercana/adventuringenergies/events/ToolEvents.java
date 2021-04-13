package com.minercana.adventuringenergies.events;

import com.minercana.adventuringenergies.AdventuringEnergies;
import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.energytypes.AEEnergyTypes;
import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Triple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = AdventuringEnergies.MOD_ID)
public class ToolEvents {
    @SubscribeEvent
    public static void amuletOfRecoveryOnDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
            CuriosApi.getCuriosHelper().findEquippedCurio(AdventuringEnergiesItems.AMULET_OF_RECOVERY.get(), player).ifPresent(foundCurioTriple -> {
                final Boolean canUseEnergy = player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability).map(tracker -> tracker.useEnergy(AEEnergyTypes.YELLOW.get(), player)).orElse(false);
                if (canUseEnergy) {
                    player.setHealth(1f);
                    player.clearActivePotions();
                    final Triple<ServerWorld, Vector3d, Float> respawnLocation = findRespawnLocation(player);
                    final Vector3d respawnPos = respawnLocation.getMiddle();
                    player.teleport(respawnLocation.getLeft(), respawnPos.getX(), respawnPos.getY(), respawnPos.getZ(), respawnLocation.getRight(), 0);
                    event.setCanceled(true);
                }
            });
        }
    }

    /**
     * Finds the respawn world and position for a player
     *
     * Based off PlayerList#func_232644_a_
     * @param playerEntity The player to find respawn info for
     * @return A pair with the world and blockpos to spawn the player in
     */
    private static Triple<ServerWorld, Vector3d, Float> findRespawnLocation(ServerPlayerEntity playerEntity) {
        BlockPos spawnPosition = playerEntity.func_241140_K_();
        float defaultRespawnAngle = playerEntity.func_242109_L();
        boolean forceRespawn = playerEntity.func_241142_M_();
        ServerWorld serverworld = playerEntity.getServer().getWorld(playerEntity.func_241141_L_());
        // Finds respawn position when the player has both a respawn anchor or a bed, use respawn anchor charge if using respawn anchor
        Optional<Vector3d> optional;
        if (serverworld != null && spawnPosition != null) {
            optional = PlayerEntity.func_242374_a(serverworld, spawnPosition, defaultRespawnAngle, forceRespawn, false);
        }
        else {
            optional = Optional.empty();
        }

        ServerWorld respawnDimension = serverworld != null && optional.isPresent() ? serverworld : playerEntity.getServer().func_241755_D_();

        // Get the respawn angle of the player, use default respawn angle if player doesn't have a bed or respawn anchor
        float respawnAngle = defaultRespawnAngle;
        if (spawnPosition != null) {
            BlockState blockstate = respawnDimension.getBlockState(spawnPosition);
            boolean usingRespawnAnchor = blockstate.matchesBlock(Blocks.RESPAWN_ANCHOR);
            if (blockstate.isIn(BlockTags.BEDS) || usingRespawnAnchor) {
                if (optional.isPresent()){
                    // Adjust the respawn angle if player is using a repsawn anchor or bed
                    Vector3d vector3d1 = Vector3d.copyCenteredHorizontally(spawnPosition).subtract(optional.get()).normalize();
                    respawnAngle = (float) MathHelper.wrapDegrees(MathHelper.atan2(vector3d1.z, vector3d1.x) * (double) (180F / (float) Math.PI) - 90.0D);
                }
            }
        }

        // Use world spawn if optional is empty (no bed or respawn anchor)
        final BlockPos worldSpawn = playerEntity.getServerWorld().getSpawnPoint();
        return Triple.of(respawnDimension, optional.orElseGet(() -> new Vector3d(worldSpawn.getX(), worldSpawn.getY(), worldSpawn.getZ())), respawnAngle);
    }

    @SubscribeEvent
    public static void chaliceRegeneration(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayerEntity) {
            // TODO Query capability here
            if (!event.player.isPotionActive(Effects.REGENERATION)) {
                event.player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 5));
            }
        }
    }
}

