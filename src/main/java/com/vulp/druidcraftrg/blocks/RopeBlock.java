package com.vulp.druidcraftrg.blocks;

import com.mojang.serialization.MapCodec;
import com.vulp.druidcraftrg.blocks.tile.RopeTileEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class RopeBlock extends SixWayConnectBlock {
   public RopeBlock(double branchRadius, double centerRadius, Properties properties) {
      super(branchRadius, centerRadius, properties);
   }

   @Nullable
   public PathType getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
      return PathType.BLOCKED;
   }

   @Override
   public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return true;
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new RopeTileEntity(pos, state);
   }

   public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int a, int b) {
      super.triggerEvent(state, world, pos, a, b);
      BlockEntity tile = world.getBlockEntity(pos);
      return tile != null && tile.triggerEvent(a, b);
   }

   @Override
   public InteractionResult toggleIntersection(@Nullable Player playerEntity, Level world, BlockPos pos, UseOnContext context) {
      BlockEntity tile = world.getBlockEntity(pos);
      return tile instanceof RopeTileEntity ? ((RopeTileEntity)tile).forceToggle() : InteractionResult.FAIL;
   }

   @Override
   public boolean canBeKnifed() {
      return true;
   }

   public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 60;
   }

   public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 30;
   }

   @Override
   public Connections connectInDirection(Direction direction, BlockState currentState, BlockPos currentPos, LevelAccessor world) {
      BlockState dirState = world.getBlockState(currentPos.relative(direction));
      Direction opposite = direction.getOpposite();
      if (currentState.getValue((Property)DIR_TO_PROPERTY_MAP.get(direction)) == Connections.CUT) {
         return Connections.CUT;
      } else {
         return (dirState.getBlock() != this || dirState.getValue((Property)DIR_TO_PROPERTY_MAP.get(opposite)) == Connections.CUT)
               && !dirState.isFaceSturdy(world, currentPos, opposite)
               && !connectToLantern(dirState, direction)
               && !isBeamBlock(dirState)
            ? Connections.NONE
            : Connections.NORMAL;
      }
   }

   public static boolean connectToLantern(BlockState state, Direction direction) {
      if (direction != Direction.DOWN) {
         return false;
      }
      if (state.getBlock() instanceof RopeLanternBlock) {
         return true;
      }
      return state.getBlock() instanceof RopeableLanternBlock
         && state.getValue(RopeableLanternBlock.HANGING)
         && state.getValue(RopeableLanternBlock.ROPED);
   }

   @Override
   public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
      ItemStack itemstack = player.getMainHandItem();
      if (itemstack.getItem() == Items.LANTERN && hit.getDirection() == Direction.DOWN
            && world.getBlockState(pos.below()).canBeReplaced()) {
         if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
         }
         world.setBlockAndUpdate(pos.below(), com.vulp.druidcraftrg.init.BlockInit.rope_lantern.defaultBlockState());
         world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1.0F, 0.88F, true);
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.PASS;
   }

   @Override
   public MapCodec<? extends RopeBlock> codec() {
      return simpleCodec(props -> new RopeBlock(2.5, 2.5, props));
   }

   public static boolean isBeamBlock(BlockState state) {
      return state.getBlock() instanceof BeamBlock;
   }

   public static boolean isEntityInBlock(Entity entity) {
      AABB axisalignedbb = entity.getBoundingBox();
      BlockPos blockpos = BlockPos.containing(axisalignedbb.minX + 0.001, axisalignedbb.minY + 0.001, axisalignedbb.minZ + 0.001);
      BlockPos blockpos1 = BlockPos.containing(axisalignedbb.maxX - 0.001, axisalignedbb.maxY - 0.001, axisalignedbb.maxZ - 0.001);
      MutableBlockPos blockpos$mutable = new MutableBlockPos();
      if (entity.level().hasChunksAt(blockpos, blockpos1)) {
         for (int i = blockpos.getX(); i <= blockpos1.getX(); i++) {
            for (int j = blockpos.getY(); j <= blockpos1.getY(); j++) {
               for (int k = blockpos.getZ(); k <= blockpos1.getZ(); k++) {
                  blockpos$mutable.set(i, j, k);
                  BlockState blockstate = entity.level().getBlockState(blockpos$mutable);
                  if (blockstate.getBlock() instanceof RopeBlock) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
      if (entity instanceof Player player) {
         Vec3 movement = player.getDeltaMovement();
         player.fallDistance = 0.0F;
         double d0 = Mth.clamp(movement.x, -0.15F, 0.15F);
         double d1 = Mth.clamp(movement.z, -0.15F, 0.15F);
         double d2 = Math.max(movement.y, -0.15F);
         if (!player.onGround()) {
            double var10000 = 0.95F;
         }

         if (!player.onGround()) {
            double var17 = 0.95F;
         }

         if (player.jumping) {
            d2 = 0.3;
         } else if (!player.isShiftKeyDown() && d2 < 0.0) {
            d2 = 0.0;
         }

         player.setDeltaMovement(new Vec3(player.onGround() ? movement.x : d0 * 0.95, d2, player.onGround() ? movement.z : d1 * 0.95));
      }
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }
}
