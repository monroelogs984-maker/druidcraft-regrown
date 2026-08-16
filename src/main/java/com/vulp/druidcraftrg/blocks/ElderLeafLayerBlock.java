package com.vulp.druidcraftrg.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// leaf litter dropped by decayed elder fruit; rots away over random ticks
public class ElderLeafLayerBlock extends BushBlock {
   public static final IntegerProperty DECAY = IntegerProperty.create("decay", 0, 2);

   public ElderLeafLayerBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(DECAY, 0));
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
   }

   public boolean isMaxDecay(BlockState state) {
      return state.getValue(DECAY) > 1;
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      if (random.nextInt(9) == 0) {
         if (isMaxDecay(state)) {
            level.destroyBlock(pos, false);
         } else {
            level.setBlockAndUpdate(pos, state.setValue(DECAY, state.getValue(DECAY) + 1));
         }
      }
      super.randomTick(state, level, pos, random);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(DECAY);
   }

   @Override
   public MapCodec<? extends ElderLeafLayerBlock> codec() {
      return simpleCodec(ElderLeafLayerBlock::new);
   }
}
