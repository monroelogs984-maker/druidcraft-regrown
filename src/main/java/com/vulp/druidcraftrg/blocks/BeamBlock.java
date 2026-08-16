package com.vulp.druidcraftrg.blocks;

import com.google.common.collect.Maps;
import com.vulp.druidcraftrg.blocks.tile.BeamTileEntity;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.util.DirAxisEnum;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BeamBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
   public static final BooleanProperty X_AXIS = BooleanProperty.create("x_axis");
   public static final BooleanProperty Y_AXIS = BooleanProperty.create("y_axis");
   public static final BooleanProperty Z_AXIS = BooleanProperty.create("z_axis");
   public static final EnumProperty<Axis> DEFAULT_AXIS = EnumProperty.create("def_axis", Axis.class);
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private static final Map<Axis, VoxelShape> SHAPES = createShapes();
   public static final Map<Axis, BooleanProperty> AXIS_TO_BOOLEAN_PROPERTY_MAP = (Map<Axis, BooleanProperty>)Util.make(
      new java.util.EnumMap<Axis, BooleanProperty>(Axis.class), map -> {
         map.put(Axis.X, X_AXIS);
         map.put(Axis.Y, Y_AXIS);
         map.put(Axis.Z, Z_AXIS);
      }
   );

   public BeamBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(X_AXIS, true)).setValue(Y_AXIS, true)).setValue(Z_AXIS, true))
               .setValue(DEFAULT_AXIS, Axis.X))
            .setValue(WATERLOGGED, false)
      );
   }

   @Nullable
   public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
      return BlockPathTypes.BLOCKED;
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new BeamTileEntity(pos, state);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
      return createTickerHelper(blockEntity, BlockEntityInit.beam, BeamTileEntity::serverTick);
   }

   public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int a, int b) {
      super.triggerEvent(state, world, pos, a, b);
      BlockEntity tile = world.getBlockEntity(pos);
      return tile != null && tile.triggerEvent(a, b);
   }

   public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
      VoxelShape shape = Shapes.empty();

      for (Entry<Axis, BooleanProperty> entry : AXIS_TO_BOOLEAN_PROPERTY_MAP.entrySet()) {
         if ((Boolean)state.getValue((Property)entry.getValue())) {
            shape = Shapes.or(shape, SHAPES.get(entry.getKey()));
         }
      }

      return shape;
   }

   private static Map<Axis, VoxelShape> createShapes() {
      Map<Axis, VoxelShape> shapes = new HashMap<>();
      shapes.put(Axis.X, Block.box(0.0, 5.0, 5.0, 16.0, 11.0, 11.0));
      shapes.put(Axis.Y, Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0));
      shapes.put(Axis.Z, Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 16.0));
      return shapes;
   }

   private static List<Direction> getOpenSides(BlockState state) {
      List<Direction> list = new LinkedList<>(Arrays.asList(Direction.values()));
      DirAxisEnum.getFromAxis((Axis)state.getValue(DEFAULT_AXIS));

      for (Entry<Axis, BooleanProperty> entry : AXIS_TO_BOOLEAN_PROPERTY_MAP.entrySet()) {
         if ((Boolean)state.getValue((Property)entry.getValue())) {
            for (Direction dir : DirAxisEnum.getFromAxis(entry.getKey()).getDirections()) {
               list.remove(dir);
            }
         }
      }

      return list;
   }

   public boolean hasLashing(Level world, BlockPos pos, BlockState state) {
      List<Direction> list = getOpenSides(state);
      if (list.size() < 4) {
         return true;
      } else {
         list.removeIf(dir -> {
            BlockState checkState = world.getBlockState(pos.relative(dir));
            return checkState.getBlock() == this && (Boolean)checkState.getValue((Property)AXIS_TO_BOOLEAN_PROPERTY_MAP.get(dir.getAxis()));
         });
         return list.size() < 4;
      }
   }

   public static List<Direction> getRopeDirections(Level world, BlockPos pos, BlockState state) {
      List<Direction> list = getOpenSides(state);
      list.removeIf(
         dir -> {
            BlockState checkState = world.getBlockState(pos.relative(dir));
            return checkState.getBlock() != BlockInit.rope
               || checkState.getValue((Property)RopeBlock.DIR_TO_PROPERTY_MAP.get(dir.getOpposite())) != Connections.NORMAL;
         }
      );
      return list;
   }

   private Axis getDefaultAxis(BlockState state) {
      return (Axis)state.getValue(DEFAULT_AXIS);
   }

   private boolean shouldConnectOnAxis(LevelAccessor world, BlockPos pos, Axis axis) {
      DirAxisEnum axisEnum = DirAxisEnum.getFromAxis(axis);
      if (axisEnum != null) {
         for (Direction direction : axisEnum.getDirections()) {
            BlockState state = world.getBlockState(pos.relative(direction));
            if (state.getBlock() == this && (Boolean)state.getValue((Property)AXIS_TO_BOOLEAN_PROPERTY_MAP.get(axis))) {
               return true;
            }
         }
      }

      return false;
   }

   public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 20;
   }

   public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 5;
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos pos = context.getClickedPos();
      Level world = context.getLevel();
      Axis facing = context.getClickedFace().getAxis();
      FluidState fluidstate = world.getFluidState(pos);
      return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState()
                     .setValue(X_AXIS, facing == Axis.X || this.shouldConnectOnAxis(world, pos, Axis.X)))
                  .setValue(Y_AXIS, facing == Axis.Y || this.shouldConnectOnAxis(world, pos, Axis.Y)))
               .setValue(Z_AXIS, facing == Axis.Z || this.shouldConnectOnAxis(world, pos, Axis.Z)))
            .setValue(DEFAULT_AXIS, facing))
         .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
      }

      state = (BlockState)((BlockState)((BlockState)state.setValue(
               X_AXIS, this.getDefaultAxis(state) == Axis.X || this.shouldConnectOnAxis(world, currentPos, Axis.X)
            ))
            .setValue(Y_AXIS, this.getDefaultAxis(state) == Axis.Y || this.shouldConnectOnAxis(world, currentPos, Axis.Y)))
         .setValue(Z_AXIS, this.getDefaultAxis(state) == Axis.Z || this.shouldConnectOnAxis(world, currentPos, Axis.Z));
      BlockEntity tile = world.getBlockEntity(currentPos);
      if (tile instanceof BeamTileEntity) {
         ((BeamTileEntity)tile).update(state);
      }

      return state;
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
      return true;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{X_AXIS, Y_AXIS, Z_AXIS, DEFAULT_AXIS, WATERLOGGED});
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }
}
