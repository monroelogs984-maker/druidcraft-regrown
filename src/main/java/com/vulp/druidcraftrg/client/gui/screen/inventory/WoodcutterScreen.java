package com.vulp.druidcraftrg.client.gui.screen.inventory;

import com.vulp.druidcraftrg.inventory.container.WoodcutterMenu;
import com.vulp.druidcraftrg.recipes.WoodcuttingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

// stonecutter screen clone with the woodcutter background
public class WoodcutterScreen extends AbstractContainerScreen<WoodcutterMenu> {
   private static final ResourceLocation BG_LOCATION = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/gui/container/woodcutter.png");
   private float scrollOffs;
   private boolean scrolling;
   private int startIndex;
   private boolean displayRecipes;

   public WoodcutterScreen(WoodcutterMenu menu, Inventory inventory, Component title) {
      super(menu, inventory, title);
      menu.registerUpdateListener(this::containerChanged);
      --this.titleLabelY;
   }

   @Override
   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      super.render(graphics, mouseX, mouseY, partialTick);
      this.renderTooltip(graphics, mouseX, mouseY);
   }

   @Override
   protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
      this.renderBackground(graphics, mouseX, mouseY, partialTick);
      int x = this.leftPos;
      int y = this.topPos;
      graphics.blit(BG_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight);
      int scrollY = (int) (41.0F * this.scrollOffs);
      graphics.blit(BG_LOCATION, x + 119, y + 15 + scrollY, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
      int recipesX = this.leftPos + 52;
      int recipesY = this.topPos + 14;
      int lastVisible = this.startIndex + 12;
      this.renderButtons(graphics, mouseX, mouseY, recipesX, recipesY, lastVisible);
      this.renderRecipes(graphics, recipesX, recipesY, lastVisible);
   }

   @Override
   protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
      super.renderTooltip(graphics, mouseX, mouseY);
      if (this.displayRecipes) {
         int recipesX = this.leftPos + 52;
         int recipesY = this.topPos + 14;
         int lastVisible = this.startIndex + 12;
         List<RecipeHolder<WoodcuttingRecipe>> list = this.menu.getRecipes();

         for (int i = this.startIndex; i < lastVisible && i < this.menu.getNumRecipes(); i++) {
            int j = i - this.startIndex;
            int slotX = recipesX + j % 4 * 16;
            int slotY = recipesY + j / 4 * 18 + 2;
            if (mouseX >= slotX && mouseX < slotX + 16 && mouseY >= slotY && mouseY < slotY + 18) {
               graphics.renderTooltip(this.font, list.get(i).value().getResultItem(this.minecraft.level.registryAccess()), mouseX, mouseY);
            }
         }
      }
   }

   private void renderButtons(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int lastVisible) {
      for (int i = this.startIndex; i < lastVisible && i < this.menu.getNumRecipes(); i++) {
         int j = i - this.startIndex;
         int slotX = x + j % 4 * 16;
         int slotY = y + j / 4 * 18 + 2;
         int vOffset = this.imageHeight;
         if (i == this.menu.getSelectedRecipeIndex()) {
            vOffset += 18;
         } else if (mouseX >= slotX && mouseY >= slotY && mouseX < slotX + 16 && mouseY < slotY + 18) {
            vOffset += 36;
         }
         graphics.blit(BG_LOCATION, slotX, slotY - 1, 0, vOffset, 16, 18);
      }
   }

   private void renderRecipes(GuiGraphics graphics, int x, int y, int lastVisible) {
      List<RecipeHolder<WoodcuttingRecipe>> list = this.menu.getRecipes();
      for (int i = this.startIndex; i < lastVisible && i < this.menu.getNumRecipes(); i++) {
         int j = i - this.startIndex;
         int slotX = x + j % 4 * 16;
         int slotY = y + j / 4 * 18 + 2;
         graphics.renderItem(list.get(i).value().getResultItem(this.minecraft.level.registryAccess()), slotX, slotY);
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.scrolling = false;
      if (this.displayRecipes) {
         int recipesX = this.leftPos + 52;
         int recipesY = this.topPos + 14;
         int lastVisible = this.startIndex + 12;

         for (int i = this.startIndex; i < lastVisible; i++) {
            int j = i - this.startIndex;
            double dx = mouseX - (double) (recipesX + j % 4 * 16);
            double dy = mouseY - (double) (recipesY + j / 4 * 18);
            if (dx >= 0.0D && dy >= 0.0D && dx < 16.0D && dy < 18.0D && this.menu.clickMenuButton(this.minecraft.player, i)) {
               Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
               this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i);
               return true;
            }
         }

         recipesX = this.leftPos + 119;
         recipesY = this.topPos + 9;
         if (mouseX >= (double) recipesX && mouseX < (double) (recipesX + 12) && mouseY >= (double) recipesY && mouseY < (double) (recipesY + 54)) {
            this.scrolling = true;
         }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
      if (this.scrolling && this.isScrollBarActive()) {
         int top = this.topPos + 14;
         int bottom = top + 54;
         this.scrollOffs = ((float) mouseY - (float) top - 7.5F) / ((float) (bottom - top) - 15.0F);
         this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
         this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5D) * 4;
         return true;
      }
      return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
      if (this.isScrollBarActive()) {
         int offscreenRows = this.getOffscreenRows();
         float f = (float) scrollY / (float) offscreenRows;
         this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
         this.startIndex = (int) ((double) (this.scrollOffs * (float) offscreenRows) + 0.5D) * 4;
      }
      return true;
   }

   private boolean isScrollBarActive() {
      return this.displayRecipes && this.menu.getNumRecipes() > 12;
   }

   protected int getOffscreenRows() {
      return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
   }

   private void containerChanged() {
      this.displayRecipes = this.menu.hasInputItem();
      if (!this.displayRecipes) {
         this.scrollOffs = 0.0F;
         this.startIndex = 0;
      }
   }
}
