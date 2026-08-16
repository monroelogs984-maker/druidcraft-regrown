package com.vulp.druidcraftrg.items;

import com.vulp.druidcraftrg.entities.LunarMothColors;
import com.vulp.druidcraftrg.entities.LunarMothEntity;
import com.vulp.druidcraftrg.init.EntityInit;
import com.vulp.druidcraftrg.init.ItemInit;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LunarMothJarItem extends BlockItem {

   private final LunarMothColors color;

   public LunarMothJarItem(Block block, LunarMothColors color, Properties properties) {
      super(block, properties);
      this.color = color;
   }

   public LunarMothColors getColor() {
      return this.color;
   }

   @Override
   public InteractionResult useOn(UseOnContext context) {
      if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
         InteractionResult result = this.place(new BlockPlaceContext(context));
         return !result.consumesAction()
            ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult()
            : result;
      }
      InteractionResult result = this.tryRelease(context);
      return !result.consumesAction() && context.getPlayer() != null
         ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult()
         : result;
   }

   public InteractionResult tryRelease(UseOnContext context) {
      Level world = context.getLevel();
      if (world.isClientSide) {
         return InteractionResult.SUCCESS;
      }
      ServerLevel serverWorld = (ServerLevel) world;
      ItemStack itemstack = context.getItemInHand();
      BlockPos blockpos = context.getClickedPos();
      Direction direction = context.getClickedFace();
      BlockState blockstate = serverWorld.getBlockState(blockpos);
      BlockPos blockpos1 = blockstate.getCollisionShape(serverWorld, blockpos).isEmpty()
         ? blockpos
         : blockpos.relative(direction);

      Player player = context.getPlayer();
      LunarMothEntity moth = EntityInit.lunar_moth.spawn(serverWorld, itemstack, player, blockpos1,
         MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
      if (moth != null) {
         moth.setColor(this.color);
         if (player != null && !player.isCreative()) {
            itemstack.shrink(1);
         }
         ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
         if (player != null) {
            if (!player.getInventory().add(bottle)) {
               player.drop(bottle, false);
            }
         } else {
            Containers.dropItemStack(serverWorld, blockpos1.getX() + 0.5, blockpos1.getY() + 0.5, blockpos1.getZ() + 0.5, bottle);
         }
      }
      return InteractionResult.SUCCESS;
   }

   public static LunarMothJarItem getItemByColor(LunarMothColors color) {
      switch (color) {
         case YELLOW:
            return (LunarMothJarItem) ItemInit.lunar_moth_jar_yellow;
         case PINK:
            return (LunarMothJarItem) ItemInit.lunar_moth_jar_pink;
         case LIME:
            return (LunarMothJarItem) ItemInit.lunar_moth_jar_lime;
         case WHITE:
            return (LunarMothJarItem) ItemInit.lunar_moth_jar_white;
         case ORANGE:
            return (LunarMothJarItem) ItemInit.lunar_moth_jar_orange;
         default:
            return (LunarMothJarItem) ItemInit.lunar_moth_jar_turquoise;
      }
   }

   public static ItemStack getItemStackByColor(LunarMothColors color) {
      return new ItemStack(getItemByColor(color));
   }
}
