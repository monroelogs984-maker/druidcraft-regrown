package com.vulp.druidcraftrg.init;

import com.vulp.druidcraftrg.recipes.WoodcuttingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeInit {
   public static final RecipeType<WoodcuttingRecipe> WOODCUTTING_TYPE =
      RecipeType.simple(new ResourceLocation("druidcraftrg", "woodcutting"));
   public static final RecipeSerializer<WoodcuttingRecipe> WOODCUTTING_SERIALIZER =
      new WoodcuttingRecipe.Serializer();
}
