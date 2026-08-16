package com.vulp.druidcraftrg.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// lantern that can stand, hang from blocks, or hang from ropes
public class RopeableLanternBlock extends Block implements SimpleWaterloggedBlock {
   public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
   public static final BooleanProperty ROPED = BooleanProperty.create("roped");
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

   public RopeableLanternBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, false).setValue(ROPED, false).setValue(WATERLOGGED, false));
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
      for (Direction direction : context.getNearestLookingDirections()) {
         if (direction.getAxis() == Direction.Axis.Y) {
            boolean flag = direction == Direction.UP;
            BlockState blockstate = this.defaultBlockState().setValue(HANGING, flag)
               .setValue(ROPED, flag && context.getLevel().getBlockState(context.getClickedPos().above()).getBlock() instanceof RopeBlock);
            if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
               return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            }
         }
      }
      return null;
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      VoxelShape grounded = Shapes.or(Shapes.or(Block.box(4, 0, 4, 12, 2, 12), Block.box(3, 2, 3, 13, 8, 13)), Block.box(6, 7, 6, 12, 9, 12));
      VoxelShape hanging = Shapes.or(Shapes.or(Block.box(4, 1, 4, 12, 3, 12), Block.box(3, 3, 3, 13, 9, 13)), Block.box(5, 9, 5, 11, 11, 11));
      if (state.getValue(HANGING)) {
         if (state.getValue(ROPED)) {
            return Shapes.or(hanging, Shapes.or(Block.box(5, 11, 5, 11, 14, 11), Block.box(6, 14, 6, 10, 16, 10)));
         }
         return Shapes.or(hanging, Block.box(5, 11, 5, 11, 16, 11));
      }
      return Shapes.or(grounded, Block.box(5, 10, 5, 11, 13, 11));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(HANGING, ROPED, WATERLOGGED);
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      Direction direction = getConnectedDirection(state).getOpposite();
      return Block.canSupportCenter(level, pos.relative(direction), direction.getOpposite())
         || (state.getValue(ROPED) && level.getBlockState(pos.above()).getBlock() instanceof RopeBlock);
   }

   protected static Direction getConnectedDirection(BlockState state) {
      return state.getValue(HANGING) ? Direction.DOWN : Direction.UP;
   }

   @Override
   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
      if (state.getValue(WATERLOGGED)) {
         level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
      }
      return getConnectedDirection(state).getOpposite() == facing && !state.canSurvive(level, currentPos)
         ? Blocks.AIR.defaultBlockState()
         : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
   }

   @Override
   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   @Override
   public boolean isPathfindable(BlockState state, PathComputationType type) {
      return false;
   }
}
