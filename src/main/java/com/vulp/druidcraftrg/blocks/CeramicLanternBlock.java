package com.vulp.druidcraftrg.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CeramicLanternBlock extends RopeableLanternBlock {

   public CeramicLanternBlock(Properties properties) {
      super(properties);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      VoxelShape grounded = Shapes.or(Shapes.or(Block.box(4, 0, 4, 12, 2, 12), Block.box(3, 2, 3, 13, 8, 13)), Block.box(5, 8, 5, 11, 10, 11));
      VoxelShape hanging = Shapes.or(Shapes.or(Block.box(4, 1, 4, 12, 3, 12), Block.box(3, 3, 3, 13, 9, 13)), Block.box(5, 9, 5, 11, 11, 11));
      if (state.getValue(HANGING)) {
         if (state.getValue(ROPED)) {
            return Shapes.or(hanging, Shapes.or(Block.box(5, 11, 5, 11, 14, 11), Block.box(6, 14, 6, 10, 16, 10)));
         }
         return Shapes.or(hanging, Block.box(5, 11, 5, 11, 16, 11));
      }
      return Shapes.or(grounded, Block.box(5, 10, 5, 11, 13, 11));
   }
}
