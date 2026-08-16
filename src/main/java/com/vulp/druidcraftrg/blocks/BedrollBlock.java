package com.vulp.druidcraftrg.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BedrollBlock extends BedBlock {
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   protected static final VoxelShape[] HEAD_SHAPES = new VoxelShape[]{
      Shapes.or(
         Shapes.or(Block.box(1.0, 0.0, 0.0, 15.0, 4.0, 2.0), Block.box(0.0, 0.0, 2.0, 16.0, 5.0, 9.0)),
         Shapes.or(Block.box(2.0, 2.0, 9.0, 14.0, 4.0, 15.0), Block.box(1.0, 0.0, 9.0, 15.0, 2.0, 16.0))
      ),
      Shapes.or(
         Shapes.or(Block.box(14.0, 0.0, 1.0, 16.0, 4.0, 15.0), Block.box(7.0, 0.0, 0.0, 14.0, 5.0, 16.0)),
         Shapes.or(Block.box(1.0, 2.0, 2.0, 7.0, 4.0, 14.0), Block.box(0.0, 0.0, 1.0, 7.0, 2.0, 15.0))
      ),
      Shapes.or(
         Shapes.or(Block.box(1.0, 0.0, 14.0, 15.0, 4.0, 16.0), Block.box(0.0, 0.0, 7.0, 16.0, 5.0, 14.0)),
         Shapes.or(Block.box(2.0, 2.0, 1.0, 14.0, 4.0, 7.0), Block.box(1.0, 0.0, 0.0, 15.0, 2.0, 7.0))
      ),
      Shapes.or(
         Shapes.or(Block.box(0.0, 0.0, 1.0, 2.0, 4.0, 15.0), Block.box(2.0, 0.0, 0.0, 9.0, 5.0, 16.0)),
         Shapes.or(Block.box(9.0, 2.0, 2.0, 15.0, 4.0, 14.0), Block.box(9.0, 0.0, 1.0, 16.0, 2.0, 15.0))
      )
   };
   protected static final VoxelShape[] FOOT_SHAPES = new VoxelShape[]{
      Block.box(1.0, 0.0, 0.0, 15.0, 4.0, 16.0),
      Block.box(0.0, 0.0, 1.0, 16.0, 4.0, 15.0),
      Block.box(1.0, 0.0, 0.0, 15.0, 4.0, 16.0),
      Block.box(0.0, 0.0, 1.0, 16.0, 4.0, 15.0)
   };
   protected static final VoxelShape[] HEAD_SHAPES_OPEN = new VoxelShape[]{
      Shapes.or(
         Shapes.or(Block.box(1.0, 0.0, 0.0, 15.0, 7.0, 2.0), Block.box(0.0, 0.0, 2.0, 16.0, 8.0, 9.0)),
         Shapes.or(Block.box(2.0, 2.0, 9.0, 14.0, 4.0, 15.0), Block.box(1.0, 0.0, 9.0, 15.0, 2.0, 16.0))
      ),
      Shapes.or(
         Shapes.or(Block.box(14.0, 0.0, 1.0, 16.0, 7.0, 15.0), Block.box(7.0, 0.0, 0.0, 14.0, 8.0, 16.0)),
         Shapes.or(Block.box(1.0, 2.0, 2.0, 7.0, 4.0, 14.0), Block.box(0.0, 0.0, 1.0, 7.0, 2.0, 15.0))
      ),
      Shapes.or(
         Shapes.or(Block.box(1.0, 0.0, 14.0, 15.0, 7.0, 16.0), Block.box(0.0, 0.0, 7.0, 16.0, 8.0, 14.0)),
         Shapes.or(Block.box(2.0, 2.0, 1.0, 14.0, 4.0, 7.0), Block.box(1.0, 0.0, 0.0, 15.0, 2.0, 7.0))
      ),
      Shapes.or(
         Shapes.or(Block.box(0.0, 0.0, 1.0, 2.0, 7.0, 15.0), Block.box(2.0, 0.0, 0.0, 9.0, 8.0, 16.0)),
         Shapes.or(Block.box(9.0, 2.0, 2.0, 15.0, 4.0, 14.0), Block.box(9.0, 0.0, 1.0, 16.0, 2.0, 15.0))
      )
   };
   protected static final VoxelShape[] FOOT_SHAPES_OPEN = new VoxelShape[]{
      Block.box(1.0, 0.0, 0.0, 15.0, 7.0, 16.0),
      Block.box(0.0, 0.0, 1.0, 16.0, 7.0, 15.0),
      Block.box(1.0, 0.0, 0.0, 15.0, 7.0, 16.0),
      Block.box(0.0, 0.0, 1.0, 16.0, 7.0, 15.0)
   };

   public BedrollBlock(DyeColor color, Properties properties) {
      super(color, properties);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(WATERLOGGED, false));
   }

   public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
      int i = ((Direction)state.getValue(FACING)).get2DDataValue();
      return state.getValue(PART) == BedPart.HEAD
         ? (state.getValue(OCCUPIED) ? HEAD_SHAPES_OPEN[i] : HEAD_SHAPES[i])
         : (state.getValue(OCCUPIED) ? FOOT_SHAPES_OPEN[i] : FOOT_SHAPES[i]);
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
      int i = state.getValue(FACING).get2DDataValue();
      boolean sleeping = context instanceof EntityCollisionContext ecc
         && ecc.getEntity() instanceof Player player && player.isSleeping();
      boolean head = state.getValue(PART) == BedPart.HEAD;
      if (sleeping) {
         return head ? HEAD_SHAPES_OPEN[i] : FOOT_SHAPES_OPEN[i];
      }
      return head ? HEAD_SHAPES[i] : FOOT_SHAPES[i];
   }

   public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult rayTrace) {
      if (world.isClientSide) {
         return InteractionResult.CONSUME;
      } else {
         if (state.getValue(PART) != BedPart.HEAD) {
            pos = pos.relative((Direction)state.getValue(FACING));
            state = world.getBlockState(pos);
            if (!state.is(this)) {
               return InteractionResult.CONSUME;
            }
         }

         if (!canSetSpawn(world)) {
            world.removeBlock(pos, false);
            BlockPos blockpos = pos.relative(((Direction)state.getValue(FACING)).getOpposite());
            if (world.getBlockState(blockpos).is(this)) {
               world.removeBlock(blockpos, false);
            }

            Vec3 center = new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
            world.explode(
               null,
               world.damageSources().badRespawnPointExplosion(center),
               null,
               center.x,
               center.y,
               center.z,
               5.0F,
               true,
               Level.ExplosionInteraction.BLOCK
            );
            return InteractionResult.SUCCESS;
         } else if ((Boolean)state.getValue(OCCUPIED)) {
            player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
            return InteractionResult.SUCCESS;
         } else {
            player.startSleepInBed(pos).ifLeft(sleepResult -> {
               if (sleepResult != null) {
                  player.displayClientMessage(sleepResult.getMessage(), true);
               }
            });
            return InteractionResult.SUCCESS;
         }
      }
   }

   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide && player.isCreative()) {
         BedPart bedpart = (BedPart)state.getValue(PART);
         if (bedpart == BedPart.FOOT) {
            BlockPos blockpos = pos.relative((Direction)state.getValue(FACING));
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(this) && blockstate.getValue(PART) == BedPart.HEAD) {
               level.setBlock(blockpos, blockstate.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
               level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            }
         }
      }

      return super.playerWillDestroy(level, pos, state, player);
   }

   public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 20;
   }

   public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 5;
   }

   public void updateEntityAfterFallOn(BlockGetter reader, Entity entity) {
      entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockState state = super.getStateForPlacement(context);
      return state != null ? (BlockState)state.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER) : null;
   }

   public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
      }

      return super.updateShape(state, direction, facingState, accessor, currentPos, facingPos);
   }

   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float amount) {
      super.fallOn(level, state, pos, entity, amount * 0.5F);
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{WATERLOGGED});
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   @Override
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return null;
   }
   @Override
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      if (!Screen.hasShiftDown()) {
         tooltip.add(Component.translatable("block.druidcraftrg.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      } else {
         tooltip.add(Component.translatable("block.druidcraftrg.bedroll.description1").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      }
   }
}
