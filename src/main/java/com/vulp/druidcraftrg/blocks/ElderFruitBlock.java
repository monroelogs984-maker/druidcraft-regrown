package com.vulp.druidcraftrg.blocks;

import com.vulp.druidcraftrg.api.CropLifeStageType;
import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.init.ItemInit;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// grows on elder leaves; flowers, then bears elderberries on the two-day life cycle
public class ElderFruitBlock extends CropBlock {
   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
   public static final DirectionProperty FACING = DirectionalBlock.FACING;
   public static final BooleanProperty MID_BERRY = BooleanProperty.create("mid_berry");
   public static final EnumProperty<CropLifeStageType> LIFE_STAGE = EnumProperty.create("life_stage", CropLifeStageType.class);

   public ElderFruitBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any()
         .setValue(AGE, 0).setValue(LIFE_STAGE, CropLifeStageType.FLOWER).setValue(MID_BERRY, false).setValue(FACING, Direction.NORTH));
   }

   @Override
   public IntegerProperty getAgeProperty() {
      return AGE;
   }

   @Override
   public int getMaxAge() {
      return 3;
   }

   @Override
   protected ItemLike getBaseSeedId() {
      return ItemInit.elderberries;
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      VoxelShape voxelshape = Shapes.empty();
      Vec3 vec3 = getCustomOffset(state, level, pos);
      switch (state.getValue(FACING)) {
         case UP -> voxelshape = Block.box(2.0D, 15.0D, 2.0D, 14.0D, 16.0D, 14.0D);
         case DOWN -> voxelshape = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D);
         case NORTH -> voxelshape = Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 1.0D);
         case EAST -> voxelshape = Block.box(15.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D);
         case SOUTH -> voxelshape = Block.box(2.0D, 2.0D, 15.0D, 14.0D, 14.0D, 16.0D);
         case WEST -> voxelshape = Block.box(0.0D, 2.0D, 2.0D, 1.0D, 14.0D, 14.0D);
      }
      return voxelshape.move(vec3.x, vec3.y, vec3.z);
   }

   public Vec3 getCustomOffset(BlockState state, BlockGetter level, BlockPos pos) {
      long i = Mth.getSeed(pos.getX(), pos.getY(), pos.getZ());
      Direction facing = state.getValue(FACING);
      return new Vec3(
         !(facing == Direction.EAST || facing == Direction.WEST) ? (((i & 15L) / 15.0F) - 0.5D) * 0.5D : 0.0D,
         !(facing == Direction.UP || facing == Direction.DOWN) ? (((i >> 4 & 15L) / 15.0F) - 0.5D) * 0.5D : 0.0D,
         !(facing == Direction.NORTH || facing == Direction.SOUTH) ? (((i >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D : 0.0D);
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      for (Direction direction : context.getNearestLookingDirections()) {
         BlockState blockstate = this.defaultBlockState().setValue(FACING, direction);
         if (context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite())).getBlock() instanceof ElderLeavesBlock) {
            return blockstate;
         }
      }
      return null;
   }

   public boolean isGrowable(LevelReader level, BlockPos pos) {
      BlockState state = level.getBlockState(pos);
      if (state.getBlock() == this) {
         return state.getValue(LIFE_STAGE) == CropLifeStageType.FLOWER || state.getValue(MID_BERRY);
      }
      return false;
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      if (!level.isAreaLoaded(pos, 1)) return;
      if (random.nextInt(8) == 0) {
         if (CropLifeStageType.checkCropLife(level) == CropLifeStageType.NONE) {
            level.destroyBlock(pos, false);
            if (random.nextInt(7) == 0) {
               createLeafLayer(level, state, pos, random);
            }
            return;
         }
      }
      if (level.getRawBrightness(pos, 0) >= 9 && isGrowable(level, pos)) {
         int i = this.getAge(state);
         float f = getGrowthChance(this, level, pos);
         if (i < this.getMaxAge()) {
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
               level.setBlockAndUpdate(pos, state.setValue(AGE, state.getValue(AGE) + 1));
               net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }
         } else if (CropLifeStageType.checkCropLife(level) == CropLifeStageType.BERRY && state.getValue(LIFE_STAGE) != CropLifeStageType.BERRY || state.getValue(MID_BERRY)) {
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0) && i == this.getMaxAge()) {
               if (state.getValue(LIFE_STAGE) == CropLifeStageType.BERRY && state.getValue(MID_BERRY)) {
                  level.setBlockAndUpdate(pos, state.setValue(MID_BERRY, false));
               } else if (state.getValue(LIFE_STAGE) != CropLifeStageType.BERRY) {
                  level.setBlockAndUpdate(pos, state.setValue(MID_BERRY, true).setValue(LIFE_STAGE, CropLifeStageType.BERRY));
               }
               net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }
         }
      }
   }

   public void createLeafLayer(ServerLevel level, BlockState state, BlockPos pos, RandomSource random) {
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
               BlockPos checkPos = new BlockPos(pos.getX() + (j - 1), pos.getY() - i - 1, pos.getZ() + (k - 1));
               if (level.getBlockState(checkPos.above()).canBeReplaced()) {
                  if (level.getBlockState(checkPos).isFaceSturdy(level, checkPos, Direction.UP)) {
                     if (random.nextInt(3) == 0) {
                        level.setBlockAndUpdate(checkPos.above(), BlockInit.elder_leaf_layer.defaultBlockState());
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void growCrops(Level level, BlockPos pos, BlockState state) {
      if (isGrowable(level, pos)) {
         if (CropLifeStageType.checkCropLife(level) == CropLifeStageType.BERRY && state.getValue(LIFE_STAGE) != CropLifeStageType.BERRY && isMaxAge(state)) {
            level.setBlockAndUpdate(pos, state.setValue(MID_BERRY, true).setValue(LIFE_STAGE, CropLifeStageType.BERRY));
         } else if (state.getValue(MID_BERRY) && isMaxAge(state)) {
            level.setBlockAndUpdate(pos, state.setValue(MID_BERRY, false).setValue(LIFE_STAGE, CropLifeStageType.BERRY));
         } else {
            int i = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(level), this.getMaxAge());
            level.setBlockAndUpdate(pos, state.setValue(AGE, i));
         }
      }
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) {
      return true;
   }

   @Override
   protected int getBonemealAgeIncrease(Level level) {
      return 1;
   }

   protected static float getGrowthChance(Block block, BlockGetter level, BlockPos pos) {
      float f = 5.0F;
      if (level.getLightEmission(pos) >= 9) {
         return f;
      }
      return f / 1.2F;
   }

   public static boolean isOnLeaves(LevelReader level, BlockPos pos) {
      Direction direction = level.getBlockState(pos).getValue(FACING).getOpposite();
      return level.getBlockState(pos.relative(direction)).getBlock() instanceof ElderLeavesBlock;
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      return true;
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      return state.getBlock() != this || isOnLeaves(level, pos);
   }

   @Override
   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (level.isClientSide) {
         return InteractionResult.CONSUME;
      }
      if (isMaxAge(state) && !(state.getValue(MID_BERRY) && state.getValue(LIFE_STAGE) == CropLifeStageType.BERRY)) {
         level.destroyBlock(pos, true);
      } else if (isMaxAge(state) && state.getValue(MID_BERRY)) {
         return InteractionResult.FAIL;
      }
      return super.use(state, level, pos, player, hand, hit);
   }

   @Override
   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
      return !this.isMaxAge(state) && isGrowable(level, pos);
   }

   @Override
   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
      return isGrowable(level, pos);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(AGE, FACING, LIFE_STAGE, MID_BERRY);
   }
}
