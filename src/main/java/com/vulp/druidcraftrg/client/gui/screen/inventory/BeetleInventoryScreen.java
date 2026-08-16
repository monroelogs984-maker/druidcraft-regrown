package com.vulp.druidcraftrg.client.gui.screen.inventory;

import com.vulp.druidcraftrg.entities.BeetleEntity;
import com.vulp.druidcraftrg.inventory.container.BeetleInventoryContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BeetleInventoryScreen extends AbstractContainerScreen<BeetleInventoryContainer> {
   private static final ResourceLocation BEETLE_GUI_TEXTURES = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/gui/container/beetle.png");
   private final BeetleEntity beetleEntity;
   private float mousePosX;
   private float mousePosY;

   public BeetleInventoryScreen(BeetleInventoryContainer container, Inventory inventory, Component text) {
      super(container, inventory, container.getBeetle().getDisplayName());
      this.imageWidth = 257;
      this.imageHeight = 238;
      this.beetleEntity = container.getBeetle();
   }

   @Override
   protected void renderLabels(GuiGraphics graphics, int x, int y) {
      graphics.drawString(this.font, this.title, 18, 6, 4210752, false);
      graphics.drawString(this.font, this.playerInventoryTitle, 49, 144, 4210752, false);
   }

   @Override
   protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
      int i = (this.width - this.imageWidth) / 2;
      int j = (this.height - this.imageHeight) / 2;
      graphics.blit(BEETLE_GUI_TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
      if (this.beetleEntity != null && this.beetleEntity.hasChest()) {
         for (int k = 0; k < 7; k++) {
            graphics.blit(BEETLE_GUI_TEXTURES, i + 84, j + 17 + (k * 18), 0, this.imageHeight, 162, 18, 256, 256);
         }
      }
      if (this.beetleEntity != null) {
         InventoryScreen.renderEntityInInventoryFollowsMouse(graphics,
            i + 43 - 30, j + 62 - 50, i + 43 + 30, j + 62 + 20, 17, 0.0F,
            (float) (i + 43) - this.mousePosX, (float) (j + 48) - this.mousePosY, this.beetleEntity);
      }
   }

   @Override
   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(graphics, mouseX, mouseY, partialTicks);
      this.mousePosX = (float) mouseX;
      this.mousePosY = (float) mouseY;
      super.render(graphics, mouseX, mouseY, partialTicks);
      this.renderTooltip(graphics, mouseX, mouseY);
   }
}
