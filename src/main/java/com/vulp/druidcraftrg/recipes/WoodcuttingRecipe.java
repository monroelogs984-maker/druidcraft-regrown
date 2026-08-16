package com.vulp.druidcraftrg.recipes;

import com.google.gson.JsonObject;
import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.init.RecipeInit;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;

public class WoodcuttingRecipe extends SingleItemRecipe {

   public WoodcuttingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
      super(RecipeInit.WOODCUTTING_TYPE, RecipeInit.WOODCUTTING_SERIALIZER, id, group, ingredient, result);
   }

   @Override
   public boolean matches(Container container, Level level) {
      return this.ingredient.test(container.getItem(0));
   }

   @Override
   public ItemStack getToastSymbol() {
      return new ItemStack(BlockInit.woodcutter);
   }

   Ingredient getIngredientRaw() {
      return this.ingredient;
   }

   ItemStack getResultRaw() {
      return this.result;
   }

   // vanilla SingleItemRecipe.Serializer's factory interface is package-private, so clone it
   public static class Serializer implements RecipeSerializer<WoodcuttingRecipe> {
      @Override
      public WoodcuttingRecipe fromJson(ResourceLocation id, JsonObject json) {
         String group = GsonHelper.getAsString(json, "group", "");
         Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
         String resultName = GsonHelper.getAsString(json, "result");
         int count = GsonHelper.getAsInt(json, "count");
         ItemStack result = new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(resultName)), count);
         return new WoodcuttingRecipe(id, group, ingredient, result);
      }

      @Override
      public WoodcuttingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
         String group = buffer.readUtf();
         Ingredient ingredient = Ingredient.fromNetwork(buffer);
         ItemStack result = buffer.readItem();
         return new WoodcuttingRecipe(id, group, ingredient, result);
      }

      @Override
      public void toNetwork(FriendlyByteBuf buffer, WoodcuttingRecipe recipe) {
         buffer.writeUtf(recipe.getGroup());
         recipe.getIngredientRaw().toNetwork(buffer);
         buffer.writeItem(recipe.getResultRaw());
      }
   }
}
