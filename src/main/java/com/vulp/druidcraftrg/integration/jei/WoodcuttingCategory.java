package com.vulp.druidcraftrg.integration.jei;

import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.recipes.WoodcuttingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

// stonecutter-style layout: input, arrow, output (1.16.5 borrowed JEI's gui_vanilla sheet,
// which no longer exists in JEI 15 — built from the slot/arrow drawables instead)
public class WoodcuttingCategory implements IRecipeCategory<WoodcuttingRecipe> {
   public static final RecipeType<WoodcuttingRecipe> WOODCUTTING =
      RecipeType.create("druidcraftrg", "woodcutting", WoodcuttingRecipe.class);
   private final IDrawable background;
   private final IDrawable icon;
   private final IDrawableStatic slot;
   private final IDrawableStatic arrow;

   public WoodcuttingCategory(IGuiHelper guiHelper) {
      this.background = guiHelper.createBlankDrawable(82, 34);
      this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockInit.woodcutter));
      this.slot = guiHelper.getSlotDrawable();
      this.arrow = guiHelper.getRecipeArrow();
   }

   @Override
   public RecipeType<WoodcuttingRecipe> getRecipeType() {
      return WOODCUTTING;
   }

   @Override
   public Component getTitle() {
      return Component.translatable("container.druidcraftrg.woodcutter");
   }

   @Override
   public IDrawable getBackground() {
      return this.background;
   }

   @Override
   public IDrawable getIcon() {
      return this.icon;
   }

   @Override
   public void draw(WoodcuttingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
      this.arrow.draw(guiGraphics, 27, 9);
   }

   @Override
   public void setRecipe(IRecipeLayoutBuilder builder, WoodcuttingRecipe recipe, IFocusGroup focuses) {
      builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
         .setBackground(this.slot, -1, -1)
         .addIngredients(recipe.getIngredients().get(0));
      builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9)
         .setBackground(this.slot, -1, -1)
         .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
   }
}
