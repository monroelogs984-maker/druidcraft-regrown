package com.vulp.druidcraftrg.util;

import com.vulp.druidcraftrg.items.SickleItem;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public class SickleHarvestUtil {
   public static void breakNeighbours(ItemStack tool, Level level, BlockPos pos, Player player) {
      if (level.isClientSide) {
         return;
      }

      int blocksBroken = 0;

      for (BlockPos target : nearbyBlocks(tool, pos, level, player)) {
         BlockState state = level.getBlockState(target);
         if (SickleItem.isEffectiveOn(state)) {
            level.destroyBlock(target, false);
            state.getBlock().playerDestroy(level, player, target, state, null, tool);
            blocksBroken++;
         }
      }

      tool.hurtAndBreak(Math.round(blocksBroken / 2.0F), player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
   }

   public static Set<BlockPos> nearbyBlocks(ItemStack tool, BlockPos pos, Level level, Player player) {
      int radius = ((SickleItem)tool.getItem()).getRadius();
      Set<BlockPos> result = new HashSet<>();

      for (int y = -1; y < 2; y++) {
         for (int x = -radius; x < radius + 1; x++) {
            for (int z = -radius; z < radius + 1; z++) {
               if (x == z && y == z && z == 0) {
                  continue;
               }

               BlockPos potential = pos.offset(x, y, z);
               BlockState state = level.getBlockState(potential);
               if (state.is(BlockTags.WITHER_IMMUNE)) {
                  continue;
               }

               if (!ForgeHooks.isCorrectToolForDrops(state, player)) {
                  continue;
               }

               if (SickleItem.isEffectiveOn(state)) {
                  result.add(potential);
               }
            }
         }
      }

      return result;
   }
}
