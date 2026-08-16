package com.vulp.druidcraftrg.blocks;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallFieryTorchBlock extends FieryTorchBlock {
   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
   private static final Map<Direction, VoxelShape> SHAPES = new java.util.EnumMap<>(ImmutableMap.of(
      Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D),
      Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D),
      Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D),
      Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D)));

   public WallFieryTorchBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, WATERLOGGED);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return SHAPES.get(state.getValue(FACING));
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      Direction direction = state.getValue(FACING);
      BlockPos blockpos = pos.relative(direction.getOpposite());
      return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, direction);
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockState blockstate = this.defaultBlockState();
      LevelReader level = context.getLevel();
      FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
      BlockPos blockpos = context.getClickedPos();
      for (Direction direction : context.getNearestLookingDirections()) {
         if (direction.getAxis().isHorizontal()) {
            blockstate = blockstate.setValue(FACING, direction.getOpposite());
            if (blockstate.canSurvive(level, blockpos)) {
               return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            }
         }
      }
      return null;
   }

   @Override
   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
      FluidState fluidstate = level.getFluidState(currentPos);
      return facing.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, currentPos)
         ? Blocks.AIR.defaultBlockState()
         : state.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
   }

   @Override
   public BlockState rotate(BlockState state, Rotation rotation) {
      return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
   }

   @Override
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      Direction direction = state.getValue(FACING).getOpposite();
      Vec3 offset = new Vec3(direction.getStepX() * 0.27D, 0.22D, direction.getStepZ() * 0.27D);
      double d0 = (double) pos.getX() + 0.5D + offset.x;
      double d1 = (double) pos.getY() + 0.7D + offset.y;
      double d2 = (double) pos.getZ() + 0.5D + offset.z;
      float limit = 0.05F;
      float offset0 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5F));
      float offset1 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5F));
      float offset2 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5F));
      level.addParticle(com.vulp.druidcraftrg.init.ParticleInit.fiery_spark, false, d0 + offset0, d1 + offset1, d2 + offset2, 0.0D, 0.0D, 0.0D);
      level.addParticle(com.vulp.druidcraftrg.init.ParticleInit.fiery_glow, false, d0, d1 - 0.1D, d2, 0.0D, 0.0D, 0.0D);
   }
}
