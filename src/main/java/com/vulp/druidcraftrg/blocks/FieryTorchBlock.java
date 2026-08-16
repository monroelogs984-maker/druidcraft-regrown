package com.vulp.druidcraftrg.blocks;

import com.vulp.druidcraftrg.init.ParticleInit;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class FieryTorchBlock extends TorchBlock implements SimpleWaterloggedBlock {
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

   public FieryTorchBlock(Properties properties) {
      super(null, properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(WATERLOGGED);
   }

   @Override
   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
      if (state.getValue(WATERLOGGED)) {
         level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
      }
      return super.updateShape(state, facing, facingState, level, pos, facingPos);
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
      BlockState state = super.getStateForPlacement(context);
      return state == null ? null : state.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
   }

   @Override
   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   @Override
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      if (!Screen.hasShiftDown()) {
         tooltip.add(Component.translatable("block.druidcraftrg.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      } else {
         tooltip.add(Component.translatable("block.druidcraftrg.fiery_torch.description1").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
      }
   }

   @Override
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      double d0 = (double) pos.getX() + 0.5D;
      double d1 = (double) pos.getY() + 0.7D;
      double d2 = (double) pos.getZ() + 0.5D;
      float limit = 0.05F;
      float offset0 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5F));
      float offset1 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5F));
      float offset2 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5F));
      level.addParticle(ParticleInit.fiery_spark, false, d0 + offset0, d1 + offset1, d2 + offset2, 0.0D, 0.0D, 0.0D);
      level.addParticle(ParticleInit.fiery_glow, false, d0, d1 - 0.1D, d2, 0.0D, 0.0D, 0.0D);
   }
}
