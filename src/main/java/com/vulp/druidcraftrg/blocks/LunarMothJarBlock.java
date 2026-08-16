package com.vulp.druidcraftrg.blocks;

import com.vulp.druidcraftrg.init.ParticleInit;
import com.vulp.druidcraftrg.items.LunarMothJarItem;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LunarMothJarBlock extends RopeableLanternBlock {

   public static final IntegerProperty COLOR = IntegerProperty.create("color", 1, 6);
   public static final IntegerProperty FULLNESS = IntegerProperty.create("fullness", 0, 3);

   public LunarMothJarBlock(Properties properties, int mothColor) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any()
         .setValue(HANGING, false).setValue(ROPED, false).setValue(WATERLOGGED, false)
         .setValue(COLOR, mothColor).setValue(FULLNESS, 0));
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      VoxelShape lantern_grounded = Shapes.or(Block.box(4, 0, 4, 12, 8, 12), Block.box(5, 8, 5, 11, 10, 11));
      VoxelShape lantern_hanging = Block.box(4, 1, 4, 12, 12, 12);
      if (state.getValue(HANGING)) {
         return Shapes.or(lantern_hanging, Shapes.or(Block.box(5, 14, 5, 14, 14, 11), Block.box(6, 14, 6, 10, 16, 10)));
      }
      return lantern_grounded;
   }

   @Override
   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
      if (!isFull(state) && rand.nextInt(20) == 0) {
         world.setBlockAndUpdate(pos, state.setValue(FULLNESS, state.getValue(FULLNESS) + 1));
      }
      super.randomTick(state, world, pos, rand);
   }

   public boolean isFull(BlockState state) {
      return state.getValue(FULLNESS) > 2;
   }

   @Override
   public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hitResult) {
      if (state.getValue(FULLNESS) > 0) {
         popResource(world, pos, new ItemStack(Items.GLOWSTONE_DUST, state.getValue(FULLNESS)));
         world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
         world.setBlock(pos, state.setValue(FULLNESS, 0), 2);
         return InteractionResult.SUCCESS;
      }
      return super.useWithoutItem(state, world, pos, player, hitResult);
   }

   @Override
   public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player entity) {
      if (state.getValue(FULLNESS) > 0) {
         popResource(world, pos, new ItemStack(Items.GLOWSTONE_DUST, state.getValue(FULLNESS)));
      }
      return super.playerWillDestroy(world, pos, state, entity);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(HANGING, ROPED, WATERLOGGED, COLOR, FULLNESS);
   }

   public int[] colorLib(BlockState stateIn, RandomSource rand) {
      switch (stateIn.getValue(COLOR)) {
         case 1:
            return rand.nextBoolean() ? new int[]{80, 255, 155} : new int[]{145, 255, 185};
         case 2:
            return rand.nextBoolean() ? new int[]{245, 240, 220} : new int[]{255, 255, 255};
         case 3:
            return rand.nextBoolean() ? new int[]{175, 255, 105} : new int[]{215, 255, 150};
         case 4:
            return rand.nextBoolean() ? new int[]{255, 255, 150} : new int[]{255, 255, 200};
         case 5:
            return rand.nextBoolean() ? new int[]{255, 190, 75} : new int[]{255, 220, 130};
         case 6:
            return rand.nextBoolean() ? new int[]{255, 180, 230} : new int[]{255, 225, 255};
         default:
            return new int[]{0, 0, 0};
      }
   }

   @Override
   public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
      double d0 = (double) pos.getX() + 0.5D;
      double d1 = (double) pos.getY() + (stateIn.getValue(HANGING) ? 0.3D : 0.2D);
      double d2 = (double) pos.getZ() + 0.5D;
      int[] color = colorLib(stateIn, rand);
      float limit = 0.15F;
      float offset0 = Math.min(limit, Math.max(-limit, rand.nextFloat() - 0.5F));
      float offset1 = Math.min(limit, Math.max(-limit, rand.nextFloat() - 0.5F));
      float offset2 = Math.min(limit, Math.max(-limit, rand.nextFloat() - 0.5F));
      worldIn.addParticle(ParticleInit.magic_glitter, false, d0 + offset0, d1 + offset1, d2 + offset2,
         color[0] / 255.0F, color[1] / 255.0F, color[2] / 255.0F);
   }

   @Override
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
      if (!Screen.hasShiftDown()) {
         tooltip.add(Component.translatable("block.druidcraftrg.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      } else if (stack.getItem() instanceof LunarMothJarItem jarItem) {
         tooltip.add(Component.translatable("block.druidcraftrg.lunar_moth_lantern.description1")
            .withStyle(getTextColor(jarItem), ChatFormatting.ITALIC));
      }
   }

   public ChatFormatting getTextColor(LunarMothJarItem item) {
      switch (item.getColor()) {
         case ORANGE:
            return ChatFormatting.GOLD;
         case WHITE:
            return ChatFormatting.WHITE;
         case LIME:
            return ChatFormatting.GREEN;
         case PINK:
            return ChatFormatting.LIGHT_PURPLE;
         case YELLOW:
            return ChatFormatting.YELLOW;
         default:
            return ChatFormatting.AQUA;
      }
   }
}
