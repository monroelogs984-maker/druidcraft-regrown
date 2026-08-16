package com.vulp.druidcraftrg.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class DrinkableItem extends Item {

   public DrinkableItem(Properties properties) {
      super(properties);
   }

   @Override
   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      super.finishUsingItem(stack, level, entity);
      return new ItemStack(Items.GLASS_BOTTLE);
   }

   @Override
   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.DRINK;
   }

   @Override
   public int getUseDuration(ItemStack stack) {
      return 32;
   }
}
