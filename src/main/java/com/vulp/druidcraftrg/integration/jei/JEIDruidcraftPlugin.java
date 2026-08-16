package com.vulp.druidcraftrg.integration.jei;

import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.init.RecipeInit;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIDruidcraftPlugin implements IModPlugin {
   private static final ResourceLocation UID = new ResourceLocation("druidcraftrg", "main");

   @Override
   public ResourceLocation getPluginUid() {
      return UID;
   }

   @Override
   public void registerCategories(IRecipeCategoryRegistration registration) {
      registration.addRecipeCategories(new WoodcuttingCategory(registration.getJeiHelpers().getGuiHelper()));
   }

   @Override
   public void registerRecipes(IRecipeRegistration registration) {
      registration.addRecipes(WoodcuttingCategory.WOODCUTTING,
         Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeInit.WOODCUTTING_TYPE));
   }

   @Override
   public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
      registration.addRecipeCatalyst(new ItemStack(BlockInit.woodcutter), WoodcuttingCategory.WOODCUTTING);
   }
}
