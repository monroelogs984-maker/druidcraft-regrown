package com.vulp.druidcraftrg.blocks;

import java.util.function.Supplier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

// 1.20.1 replacement for the 1.16.5 AxeItem.BLOCK_STRIPPING_MAP injection
public class StrippablePillarBlock extends RotatedPillarBlock {
   private final Supplier<? extends net.minecraft.world.level.block.Block> stripped;

   public StrippablePillarBlock(Supplier<? extends net.minecraft.world.level.block.Block> stripped, Properties properties) {
      super(properties);
      this.stripped = stripped;
   }

   @Override
   public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
      if (toolAction == ToolActions.AXE_STRIP) {
         return this.stripped.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
      }
      return super.getToolModifiedState(state, context, toolAction, simulate);
   }
}
