package com.vulp.druidcraftrg.blocks;

import com.vulp.druidcraftrg.blocks.tile.GrowthLampTileEntity;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrowthLampBlock extends RopeableLanternBlock implements EntityBlock {

   public GrowthLampBlock(Properties properties) {
      super(properties);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      VoxelShape grounded = Shapes.or(Shapes.or(Block.box(4, 1, 4, 12, 9, 12), Block.box(5, 0, 5, 11, 1, 11)), Block.box(5, 9, 5, 11, 10, 11));
      VoxelShape hanging = Shapes.or(Shapes.or(Block.box(4, 2, 4, 12, 10, 12), Block.box(5, 1, 5, 11, 2, 11)), Block.box(5, 10, 5, 11, 11, 11));
      if (state.getValue(HANGING)) {
         return Shapes.or(hanging, Block.box(6, 11, 6, 10, 16, 10));
      }
      return Shapes.or(grounded, Block.box(7, 10, 7, 9, 11, 9));
   }

   @Nullable
   @Override
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new GrowthLampTileEntity(pos, state);
   }

   @Nullable
   @Override
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return type == BlockEntityInit.growth_lamp
         ? (lvl, pos, st, be) -> GrowthLampTileEntity.tick(lvl, pos, st, (GrowthLampTileEntity) be)
         : null;
   }

   @Override
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      if (!Screen.hasShiftDown()) {
         tooltip.add(Component.translatable("block.druidcraftrg.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      } else {
         tooltip.add(Component.translatable("block.druidcraftrg.growth_lamp.description1").withStyle(ChatFormatting.GREEN, ChatFormatting.ITALIC));
      }
   }
}
