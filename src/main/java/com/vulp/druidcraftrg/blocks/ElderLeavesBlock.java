package com.vulp.druidcraftrg.blocks;

import com.vulp.druidcraftrg.api.CropLifeStageType;
import com.vulp.druidcraftrg.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

// sprouts elder fruit on adjacent air blocks during the FLOWER life stage
public class ElderLeavesBlock extends LeavesBlock {
   public static final IntegerProperty GROWTH_TRIES = IntegerProperty.create("growth_tries", 0, 2);

   public ElderLeavesBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.defaultBlockState().setValue(GROWTH_TRIES, 0));
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) {
      return true;
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      super.randomTick(state, level, pos, random);
      if (level.getBlockState(pos).getBlock() != this) {
         return; // decayed this tick
      }
      state = level.getBlockState(pos);
      if (random.nextInt(4) == 0 && CropLifeStageType.checkCropLife(level) == CropLifeStageType.FLOWER && CropLifeStageType.getTwoDayCycle(level) <= 12000) {
         if (state.getValue(GROWTH_TRIES) < 2) {
            for (Direction direction : Direction.values()) {
               if (state.getValue(GROWTH_TRIES) >= 2) break;
               if (level.getBlockState(pos.relative(direction)).canBeReplaced()) {
                  if (random.nextInt(3) == 0) {
                     level.setBlockAndUpdate(pos.relative(direction),
                        BlockInit.elder_fruit.defaultBlockState().setValue(ElderFruitBlock.FACING, direction.getOpposite()));
                  }
                  state = state.setValue(GROWTH_TRIES, state.getValue(GROWTH_TRIES) + 1);
                  level.setBlockAndUpdate(pos, state);
               }
            }
         }
      }
      if (CropLifeStageType.checkCropLife(level) != CropLifeStageType.FLOWER && state.getValue(GROWTH_TRIES) > 0) {
         level.setBlockAndUpdate(pos, state.setValue(GROWTH_TRIES, 0));
      }
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(DISTANCE, PERSISTENT, WATERLOGGED, GROWTH_TRIES);
   }
}
