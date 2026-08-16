package com.vulp.druidcraftrg.recipes;

import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.init.RecipeInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class WoodcuttingRecipe extends SingleItemRecipe {

   public WoodcuttingRecipe(String group, Ingredient ingredient, ItemStack result) {
      super(RecipeInit.WOODCUTTING_TYPE, RecipeInit.WOODCUTTING_SERIALIZER, group, ingredient, result);
   }

   @Override
   public boolean matches(SingleRecipeInput input, Level level) {
      return this.ingredient.test(input.item());
   }

   @Override
   public ItemStack getToastSymbol() {
      return new ItemStack(BlockInit.woodcutter);
   }
}
