package com.vulp.druidcraftrg.blocks;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WetMudBlock extends Block {
   private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);
   private final Block block;
   private int maxTicks = 0;

   public WetMudBlock(Block convertedBlock, Properties properties) {
      super(properties);
      this.block = convertedBlock;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      super.randomTick(state, level, pos, random);
      if (!isNextToWater(level, pos)) {
         if (!level.isClientSide && (level.random.nextInt(6) == 0 || this.maxTicks >= 10)) {
            level.setBlockAndUpdate(pos, this.block.defaultBlockState());
         } else {
            this.maxTicks++;
         }
      }
   }

   @Override
   public void appendHoverText(ItemStack stack, @Nullable BlockGetter reader, List<Component> tooltip, TooltipFlag flag) {
      if (reader == null) {
         return;
      }

      if (!Screen.hasShiftDown()) {
         tooltip.add(Component.translatable("block.druidcraftrg.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      } else {
         tooltip.add(Component.translatable("block.druidcraftrg.wet_mud_bricks.description1").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
      }
   }

   private boolean isNextToWater(BlockGetter reader, BlockPos pos) {
      for (Direction direction : Direction.values()) {
         FluidState fluidstate = reader.getFluidState(pos.relative(direction));
         if (fluidstate.is(FluidTags.WATER)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 1.0, 0.5));
   }

   @Override
   public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
      return false;
   }
}
