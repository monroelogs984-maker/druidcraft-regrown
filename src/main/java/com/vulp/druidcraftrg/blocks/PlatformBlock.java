package com.vulp.druidcraftrg.blocks;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlatformBlock extends Block implements SimpleWaterloggedBlock, IKnifeable {
   public static final BooleanProperty WALL = BooleanProperty.create("wall");
   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
   public static final DirectionProperty FACING = DirectionProperty.create("facing", dir -> dir != Direction.UP && dir != Direction.DOWN);
   private static final Map<Direction, VoxelShape> SHAPES = createShapes();
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

   public PlatformBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(WALL, false)).setValue(FACING, Direction.NORTH)).setValue(OPEN, true))
            .setValue(WATERLOGGED, false)
      );
   }

   @Override
   public InteractionResult onKnifed(@Nullable Player player, Level world, BlockPos pos, BlockState state, UseOnContext context) {
      world.setBlock(pos, (BlockState)state.setValue(OPEN, !(Boolean)state.getValue(OPEN)), 2);
      return InteractionResult.SUCCESS;
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
      return (!context.isAbove(Shapes.block(), pos, true) || context.isDescending()) && state.getValue(OPEN)
         ? Shapes.empty()
         : this.getShape(state, reader, pos, context);
   }

   public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
      VoxelShape shape = SHAPES.get(null);
      if ((Boolean)state.getValue(WALL)) {
         shape = Shapes.or(shape, SHAPES.get(state.getValue(FACING)));
      }

      return shape;
   }

   private static Map<Direction, VoxelShape> createShapes() {
      Map<Direction, VoxelShape> shapes = new HashMap<>();
      shapes.put(null, Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0));
      shapes.put(Direction.NORTH, Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0));
      shapes.put(Direction.EAST, Block.box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0));
      shapes.put(Direction.SOUTH, Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0));
      shapes.put(Direction.WEST, Block.box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0));
      return shapes;
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos pos = context.getClickedPos();
      Level world = context.getLevel();
      Direction face = context.getClickedFace();
      BlockState clickedBlock = world.getBlockState(pos.relative(face.getOpposite()));
      Player player = context.getPlayer();
      boolean cardinal = face != Direction.UP && face != Direction.DOWN;
      return (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(WALL, cardinal && clickedBlock.isFaceSturdy(world, pos, face)))
               .setValue(OPEN, player == null || !player.isShiftKeyDown()))
            .setValue(FACING, cardinal ? face : context.getHorizontalDirection().getOpposite()))
         .setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
      }

      Direction face = (Direction)state.getValue(FACING);
      boolean wall = world.getBlockState(currentPos.relative(face.getOpposite())).isFaceSturdy(world, currentPos, face);
      return (BlockState)state.setValue(WALL, wall);
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
      return false;
   }

   public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 20;
   }

   public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 5;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, OPEN, WALL, WATERLOGGED});
   }
}
