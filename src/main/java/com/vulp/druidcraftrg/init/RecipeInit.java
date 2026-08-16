package com.vulp.druidcraftrg.init;

import com.vulp.druidcraftrg.recipes.WoodcuttingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;

public class RecipeInit {
   public static final RecipeType<WoodcuttingRecipe> WOODCUTTING_TYPE =
      RecipeType.simple(ResourceLocation.fromNamespaceAndPath("druidcraftrg", "woodcutting"));
   public static final RecipeSerializer<WoodcuttingRecipe> WOODCUTTING_SERIALIZER =
      new SingleItemRecipe.Serializer<WoodcuttingRecipe>(WoodcuttingRecipe::new) {};
}
