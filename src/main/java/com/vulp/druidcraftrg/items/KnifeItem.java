package com.vulp.druidcraftrg.items;

import com.vulp.druidcraftrg.blocks.IKnifeable;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class KnifeItem extends Item {
   public KnifeItem(Properties properties) {
      super(properties);
   }

   public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
      Level world = context.getLevel();
      BlockPos pos = context.getClickedPos();
      BlockState state = world.getBlockState(pos);
      Block block = state.getBlock();
      return block instanceof IKnifeable ? ((IKnifeable)block).onKnifed(context.getPlayer(), world, pos, state, context) : super.onItemUseFirst(stack, context);
   }

   public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> text, TooltipFlag flag) {
      text.add(Component.translatable("item.druidcraftrg.knife.desc").withStyle(ChatFormatting.GRAY));
   }
}
