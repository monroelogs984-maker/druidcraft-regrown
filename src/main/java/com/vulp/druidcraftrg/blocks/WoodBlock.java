package com.vulp.druidcraftrg.blocks;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

// drop_self=false is used by the tree features (M6) so generated branches drop logs
public class WoodBlock extends StrippablePillarBlock {
   public static final BooleanProperty DROP_SELF = BooleanProperty.create("drop_self");
   private final Supplier<Item> item;

   public WoodBlock(Supplier<Item> logItem, Supplier<? extends Block> stripped, Properties properties) {
      super(stripped, properties);
      this.item = logItem;
      this.registerDefaultState(this.defaultBlockState().setValue(DROP_SELF, true).setValue(AXIS, Direction.Axis.Y));
   }

   @Override
   public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
      if (!state.getValue(DROP_SELF)) {
         player.awardStat(Stats.BLOCK_MINED.get(this));
         player.causeFoodExhaustion(0.005F);
         popResource(level, pos, new ItemStack(this.item.get()));
      } else {
         super.playerDestroy(level, player, pos, state, blockEntity, stack);
      }
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(AXIS, DROP_SELF);
   }
}
