package com.minercana.adventuringenergies.blocks;

import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import com.minercana.adventuringenergies.energytypes.AEEnergyTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class GoldenAltar extends Block {

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(-16, 0, -8, 32, 16, 24);

    public GoldenAltar() {
        super(AbstractBlock.Properties.create(Material.IRON, MaterialColor.GOLD).setRequiresTool().hardnessAndResistance(3.0F, 6.0F).setLightLevel(value -> 15).sound(SoundType.METAL).notSolid());
    }

    @Override
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (handIn == Hand.MAIN_HAND) {
            // If the player has less than 3 max energy, set their max energy to 3, give them 3 yellow energy, spawn enchant particles, and play the enchanting sound
            final LazyOptional<IEnergyTracker> energyTrackerOptional = player.getCapability(AdventuringEnergiesAPI.energyTrackerCapability);
            Random rand = worldIn.getRandom();
            if (energyTrackerOptional.map(tracker -> tracker.getTotalEnergyCap() == 0).orElse(false)) {
                if (player instanceof ServerPlayerEntity) {
                    energyTrackerOptional.ifPresent(tracker -> {
                        tracker.setEnergyCapForType(AEEnergyTypes.YELLOW.get(),3, (ServerPlayerEntity) player);
                        tracker.addEnergy(AEEnergyTypes.YELLOW.get(), 3, (ServerPlayerEntity) player, false);
                    });
                    ((ServerWorld) worldIn).spawnParticle(ParticleTypes.ENCHANT, player.getPosX(), player.getPosY(), player.getPosZ(), 4, (rand.nextDouble() - rand.nextDouble()) * .5, (rand.nextDouble() - rand.nextDouble()), (rand.nextDouble() - rand.nextDouble()) * .5, .1);
                }
                worldIn.playSound(player, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, rand.nextFloat() + 0.9F);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}