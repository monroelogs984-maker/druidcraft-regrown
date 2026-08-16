package com.vulp.druidcraftrg.items;

import javax.annotation.Nullable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

// 1.16.5 SmeltableBlockItem: a BlockItem usable as furnace fuel
public class FuelBlockItem extends BlockItem {
   private final int burnTime;

   public FuelBlockItem(Block block, int burnTime, Properties properties) {
      super(block, properties);
      this.burnTime = burnTime;
   }

   @Override
   public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
      return this.burnTime;
   }
}
