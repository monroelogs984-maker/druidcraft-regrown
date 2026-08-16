package com.vulp.druidcraftrg.blocks;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

public class DoubleCropBlock extends CropBlock {
   public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;
   private final VoxelShape[] SHAPE_ARRAY;
   private final ItemLike SEED_ITEM;

   public DoubleCropBlock(double shapeRadius, ItemLike seedItem, Properties properties) {
      super(properties);
      shapeRadius = Mth.clamp(shapeRadius, 0.0, 8.0);
      double a = 8.0 - shapeRadius;
      double b = 8.0 + shapeRadius;
      this.SEED_ITEM = seedItem;
      this.SHAPE_ARRAY = new VoxelShape[]{
         Block.box(a, 0.0, a, b, 2.0, b),
         Block.box(a, 0.0, a, b, 4.0, b),
         Block.box(a, 0.0, a, b, 6.0, b),
         Block.box(a, 0.0, a, b, 8.0, b),
         Block.box(a, 0.0, a, b, 10.0, b),
         Block.box(a, 0.0, a, b, 12.0, b),
         Block.box(a, 0.0, a, b, 14.0, b),
         Block.box(a, 0.0, a, b, 16.0, b)
      };
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(BOTTOM, true));
   }

   public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
      return this.SHAPE_ARRAY[state.getValue(this.getAgeProperty())];
   }

   protected ItemLike getBaseSeedId() {
      return this.SEED_ITEM;
   }

   private boolean isBottom(BlockState state) {
      return (Boolean)state.getValue(BOTTOM);
   }

   public boolean isRandomlyTicking(BlockState state) {
      return true;
   }

   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
      if (!this.isMaxAge(state) || this.canGrowUpward(world, state, pos)) {
         if (!world.isAreaLoaded(pos, 1)) {
            return;
         }

         if (world.getRawBrightness(pos, 0) >= 9) {
            float f = getGrowthSpeed(this, world, pos);
            if (ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0)) {
               this.naturalGrowth(world, pos, state);
               ForgeHooks.onCropsGrowPost(world, pos, state);
            }
         }
      }
   }

   public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 60;
   }

   public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
      return 30;
   }

   private boolean isAirAbove(BlockGetter reader, BlockPos pos) {
      return reader.getBlockState(pos.above()).getBlock() instanceof AirBlock;
   }

   private boolean isTopHalfAbove(BlockGetter reader, BlockPos pos) {
      BlockState state = reader.getBlockState(pos.above());
      return state.getBlock() == this && !(Boolean)state.getValue(BOTTOM);
   }

   public void growCrops(Level world, BlockPos pos, BlockState state) {
      int bonemeal = this.getBonemealAgeIncrease(world);
      if (this.isMaxAge(state) && this.isBottom(state)) {
         if (this.isAirAbove(world, pos)) {
            world.setBlock(pos.above(), (BlockState)this.getStateForAge(0).setValue(BOTTOM, false), 2);
         } else if (this.isTopHalfAbove(world, pos)) {
            BlockState aboveState = world.getBlockState(pos.above());
            int aboveAge = (Integer)aboveState.getValue(AGE);
            if (!this.isMaxAge(aboveState)) {
               world.setBlock(pos.above(), (BlockState)aboveState.setValue(AGE, Mth.clamp(aboveAge + bonemeal, 0, this.getMaxAge())), 2);
            }
         }
      } else if (!this.isMaxAge(state)) {
         world.setBlock(
            pos,
            (BlockState)this.getStateForAge(Mth.clamp(this.getAge(state) + bonemeal, 0, this.getMaxAge())).setValue(BOTTOM, (Boolean)state.getValue(BOTTOM)),
            2
         );
      }
   }

   public void naturalGrowth(Level world, BlockPos pos, BlockState state) {
      if (this.isMaxAge(state) && this.isBottom(state)) {
         if (this.isAirAbove(world, pos)) {
            world.setBlock(pos.above(), (BlockState)this.getStateForAge(0).setValue(BOTTOM, false), 2);
         }
      } else if (!this.isMaxAge(state)) {
         world.setBlock(pos, (BlockState)this.getStateForAge(this.getAge(state) + 1).setValue(BOTTOM, (Boolean)state.getValue(BOTTOM)), 2);
      }
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter reader, BlockPos pos) {
      return super.mayPlaceOn(state, reader, pos) || state.getBlock() == this && this.isMaxAge(state) || state.is(BlockTags.DIRT);
   }

   public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
      return super.canSustainPlant(state, world, pos, facing, plantable);
   }

   private boolean canGrowUpward(BlockGetter reader, BlockState state, BlockPos pos) {
      return (Boolean)state.getValue(BOTTOM) && this.isAirAbove(reader, pos);
   }

   public boolean isValidBonemealTarget(BlockGetter reader, BlockPos pos, BlockState state, boolean isRemote) {
      BlockState aboveState = reader.getBlockState(pos.above());
      return !this.isMaxAge(state) || this.canGrowUpward(reader, state, pos) || aboveState.getBlock() == this && !this.isMaxAge(aboveState);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> stateContainer) {
      stateContainer.add(new Property[]{AGE, BOTTOM});
   }
}
