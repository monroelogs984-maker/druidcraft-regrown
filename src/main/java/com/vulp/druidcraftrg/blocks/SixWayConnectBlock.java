package com.vulp.druidcraftrg.blocks;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class SixWayConnectBlock extends BaseEntityBlock implements IKnifeable, SimpleWaterloggedBlock {
   public static final EnumProperty<Connections> NORTH = EnumProperty.create("north", Connections.class);
   public static final EnumProperty<Connections> EAST = EnumProperty.create("east", Connections.class);
   public static final EnumProperty<Connections> SOUTH = EnumProperty.create("south", Connections.class);
   public static final EnumProperty<Connections> WEST = EnumProperty.create("west", Connections.class);
   public static final EnumProperty<Connections> UP = EnumProperty.create("up", Connections.class);
   public static final EnumProperty<Connections> DOWN = EnumProperty.create("down", Connections.class);
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private final Map<Direction, VoxelShape> SHAPE_ARRAY;
   public static final Map<Direction, EnumProperty<Connections>> DIR_TO_PROPERTY_MAP = (Map<Direction, EnumProperty<Connections>>)Util.make(
      new java.util.EnumMap<Direction, EnumProperty<Connections>>(Direction.class), map -> {
         map.put(Direction.NORTH, NORTH);
         map.put(Direction.EAST, EAST);
         map.put(Direction.SOUTH, SOUTH);
         map.put(Direction.WEST, WEST);
         map.put(Direction.UP, UP);
         map.put(Direction.DOWN, DOWN);
      }
   );

   public SixWayConnectBlock(double branchRadius, double centerRadius, Properties properties) {
      super(properties);
      this.SHAPE_ARRAY = this.createShapes(branchRadius, centerRadius);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(NORTH, Connections.NONE))
                           .setValue(EAST, Connections.NONE))
                        .setValue(SOUTH, Connections.NONE))
                     .setValue(WEST, Connections.NONE))
                  .setValue(UP, Connections.NONE))
               .setValue(DOWN, Connections.NONE))
            .setValue(WATERLOGGED, false)
      );
   }

   public abstract InteractionResult toggleIntersection(@Nullable Player var1, Level var2, BlockPos var3, UseOnContext var4);

   public abstract boolean canBeKnifed();

   @Override
   public InteractionResult onKnifed(@Nullable Player player, Level world, BlockPos pos, BlockState state, UseOnContext context) {
      if (this.canBeKnifed()) {
         if (player != null && player.isShiftKeyDown()) {
            return this.toggleIntersection(player, world, pos, context);
         }

         Vec3 vecRelative = context.getClickLocation().subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());

         for (Entry<Direction, VoxelShape> entry : this.SHAPE_ARRAY.entrySet()) {
            if (collision(vecRelative, entry.getValue())) {
               if (entry.getKey() == null) {
                  Direction dir = context.getClickedFace();
                  EnumProperty<Connections> property = DIR_TO_PROPERTY_MAP.get(dir);
                  boolean flag = false;
                  if (state.getValue(property) == Connections.CUT) {
                     Connections connection = this.connectInDirection(dir, (BlockState)state.setValue(property, Connections.NONE), pos, world);
                     if (connection != Connections.NONE) {
                        world.setBlock(pos, (BlockState)state.setValue(property, connection), 2);
                        return InteractionResult.SUCCESS;
                     }

                     flag = true;
                  }

                  if (state.getValue(property) != Connections.CUT || flag) {
                     BlockState oppositeState = world.getBlockState(pos.relative(dir));
                     EnumProperty<Connections> oppositeProperty = DIR_TO_PROPERTY_MAP.get(dir.getOpposite());
                     if (oppositeState.getBlock() == this && oppositeState.getValue(oppositeProperty) == Connections.CUT) {
                        world.setBlock(pos, (BlockState)state.setValue(property, Connections.NORMAL), 2);
                        world.setBlock(pos.relative(dir), (BlockState)oppositeState.setValue(oppositeProperty, Connections.NORMAL), 2);
                        return InteractionResult.SUCCESS;
                     }
                  }

                  return InteractionResult.FAIL;
               }

               world.setBlock(pos, (BlockState)state.setValue((Property)DIR_TO_PROPERTY_MAP.get(entry.getKey()), Connections.CUT), 2);
               return InteractionResult.SUCCESS;
            }
         }
      }

      return InteractionResult.FAIL;
   }

   public static boolean collision(Vec3 vec, VoxelShape shape) {
      return shape.bounds().minX <= vec.x
         && shape.bounds().minY <= vec.y
         && shape.bounds().minZ <= vec.z
         && shape.bounds().maxX >= vec.x
         && shape.bounds().maxY >= vec.y
         && shape.bounds().maxZ >= vec.z;
   }

   public static boolean hasIntersection(BlockState state) {
      AtomicInteger i = new AtomicInteger();
      DIR_TO_PROPERTY_MAP.forEach((direction, connection) -> {
         if (i.get() < 3 && state.getValue(connection) == Connections.NORMAL) {
            i.getAndIncrement();
         }
      });
      return i.get() > 2;
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos pos = context.getClickedPos();
      Level world = context.getLevel();
      BlockState state = this.defaultBlockState();

      for (Entry<Direction, EnumProperty<Connections>> entry : DIR_TO_PROPERTY_MAP.entrySet()) {
         state = (BlockState)state.setValue((Property)entry.getValue(), this.connectInDirection(entry.getKey(), state, pos, world));
      }

      return (BlockState)state.setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
      }

      for (Entry<Direction, EnumProperty<Connections>> entry : DIR_TO_PROPERTY_MAP.entrySet()) {
         state = (BlockState)state.setValue((Property)entry.getValue(), this.connectInDirection(entry.getKey(), state, currentPos, world));
      }

      return state;
   }

   public Connections connectInDirection(Direction direction, BlockState currentState, BlockPos currentPos, LevelAccessor world) {
      BlockState dirState = world.getBlockState(currentPos.relative(direction));
      Direction opposite = direction.getOpposite();
      if (currentState.getValue((Property)DIR_TO_PROPERTY_MAP.get(direction)) == Connections.CUT) {
         return Connections.CUT;
      } else {
         return dirState.getBlock() == this && dirState.getValue((Property)DIR_TO_PROPERTY_MAP.get(opposite)) != Connections.CUT
            ? Connections.NORMAL
            : Connections.NONE;
      }
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return false;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED});
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   private Map<Direction, VoxelShape> createShapes(double branchRadius, double centerRadius) {
      centerRadius = Mth.clamp(centerRadius, 0.0, 8.0);
      branchRadius = Mth.clamp(branchRadius, 0.0, 8.0);
      double a = 8.0 - centerRadius;
      double b = 8.0 + centerRadius;
      double c = 8.0 - branchRadius;
      double d = 8.0 + branchRadius;
      Map<Direction, VoxelShape> map = new HashMap<>();
      map.put(null, Block.box(a, a, a, b, b, b));
      map.put(Direction.NORTH, Block.box(c, c, 0.0, d, d, a));
      map.put(Direction.EAST, Block.box(d, a, c, 16.0, d, d));
      map.put(Direction.SOUTH, Block.box(c, c, b, d, d, 16.0));
      map.put(Direction.WEST, Block.box(0.0, c, c, a, d, d));
      map.put(Direction.UP, Block.box(c, b, c, d, 16.0, d));
      map.put(Direction.DOWN, Block.box(c, 0.0, c, d, a, d));
      return map;
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      VoxelShape shape = this.SHAPE_ARRAY.get(null);

      for (Entry<Direction, EnumProperty<Connections>> entry : DIR_TO_PROPERTY_MAP.entrySet()) {
         if (state.getValue((Property)entry.getValue()) == Connections.NORMAL) {
            shape = Shapes.or(shape, this.SHAPE_ARRAY.get(entry.getKey()));
         }
      }

      return shape;
   }
}
