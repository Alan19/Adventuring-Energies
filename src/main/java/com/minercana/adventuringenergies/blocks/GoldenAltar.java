package com.minercana.adventuringenergies.blocks;

import com.minercana.adventuringenergies.api.AdventuringEnergiesAPI;
import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import com.minercana.adventuringenergies.energytypes.AEEnergyTypes;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class GoldenAltar extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(-5.07107, 8, 9.14214, 4.92893, 10, 19.14214),
            Block.makeCuboidShape(-4.07107, 0, 10.14214, 3.92893, 8, 18.14214),
            Block.makeCuboidShape(-8.07107, 10, -1.27208, 3.92893, 12, 10.72792),
            Block.makeCuboidShape(-6.07107, 0, 0.14214, 3.92893, 10, 10.14214),
            Block.makeCuboidShape(-6.65685, 12, -8, 7.34315, 14, 6),
            Block.makeCuboidShape(-6.07107, 0, -6, 5.92893, 12, 6),
            Block.makeCuboidShape(11.89949, 8, 9.14214, 21.89949, 10, 19.14214),
            Block.makeCuboidShape(12.89949, 0, 10.14214, 20.89949, 8, 18.14214),
            Block.makeCuboidShape(15.51472, 10, 0.97056, 27.51472, 12, 12.97056),
            Block.makeCuboidShape(17.51472, 0, 1.97056, 27.51472, 10, 11.97056),
            Block.makeCuboidShape(10.31371, 12, -8, 24.31371, 14, 6),
            Block.makeCuboidShape(10.89949, 0, -6, 22.89949, 12, 6),
            Block.makeCuboidShape(3, 7, -9, 13, 9, 1),
            Block.makeCuboidShape(1, 0, 1, 15, 14, 15),
            Block.makeCuboidShape(0, 14, 0, 16, 16, 16),
            Block.makeCuboidShape(4, 0, 15, 12, 8, 23),
            Block.makeCuboidShape(3, 10, 15, 13, 12, 25),
            Block.makeCuboidShape(4, 0, -7, 12, 8, 1)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public GoldenAltar() {
        super(AbstractBlock.Properties.create(Material.IRON, MaterialColor.GOLD).setRequiresTool().hardnessAndResistance(3.0F, 6.0F).setLightLevel(value -> 15).sound(SoundType.METAL).notSolid());
        setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    @Nonnull
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
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