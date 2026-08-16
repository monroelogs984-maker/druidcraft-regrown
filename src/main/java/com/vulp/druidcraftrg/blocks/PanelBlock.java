package com.vulp.druidcraftrg.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

// alternates the model in a checkerboard pattern based on world position
public class PanelBlock extends Block {
   public static final BooleanProperty VAR = BooleanProperty.create("var");

   public PanelBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(VAR, false));
   }

   private boolean checkVariant(BlockPos pos) {
      return (pos.getX() + pos.getZ() + (pos.getY() % 2)) % 2 == 0;
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return super.getStateForPlacement(context).setValue(VAR, checkVariant(context.getClickedPos()));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(VAR);
   }

   @Override
   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
      return super.updateShape(state, facing, facingState, level, currentPos, facingPos).setValue(VAR, checkVariant(currentPos));
   }
}
