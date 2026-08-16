package com.vulp.druidcraftrg.client.gui.screen.inventory;

import com.vulp.druidcraftrg.inventory.container.TravelPackContainer;
import com.vulp.druidcraftrg.network.message.DeployBedrollMessage;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TravelPackScreen extends AbstractContainerScreen<TravelPackContainer> {
   private static final ResourceLocation TRAVEL_PACK_GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/gui/container/travel_pack.png");
   private PackButton bedrollButton;

   public TravelPackScreen(TravelPackContainer container, Inventory playerInv, Component title) {
      super(container, playerInv, title);
      this.imageWidth = 176;
      this.imageHeight = 151;
      this.inventoryLabelY = 57;
   }

   @Override
   protected void init() {
      super.init();
      int i = (this.width - this.imageWidth) / 2;
      int j = (this.height - this.imageHeight) / 2;
      this.bedrollButton = this.addRenderableWidget(new BedrollButton(i + 18, j + 20, "item.druidcraftrg.travel_pack.button1"));
      this.addRenderableWidget(new PackButton(i + 152, j + 20, "item.druidcraftrg.travel_pack.button2"));
      this.addRenderableWidget(new PackButton(i + 18, j + 38, "item.druidcraftrg.travel_pack.button3"));
      this.addRenderableWidget(new PackButton(i + 152, j + 38, "item.druidcraftrg.travel_pack.button4"));
      this.bedrollButton.active = false;
   }

   @Override
   protected void containerTick() {
      super.containerTick();
      this.bedrollButton.active = this.menu.hasBedroll();
   }

   @Override
   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      this.renderBackground(graphics, mouseX, mouseY, partialTick);
      super.render(graphics, mouseX, mouseY, partialTick);
      this.renderTooltip(graphics, mouseX, mouseY);
   }

   @Override
   protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
      int i = (this.width - this.imageWidth) / 2;
      int j = (this.height - this.imageHeight) / 2;
      graphics.blit(TRAVEL_PACK_GUI_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
   }

   class BedrollButton extends PackButton {
      public BedrollButton(int x, int y, String translationKey) {
         super(x, y, translationKey);
      }

      @Override
      public void onPress() {
         Minecraft minecraft = TravelPackScreen.this.minecraft;
         if (minecraft != null && minecraft.player != null) {
            int playerId = minecraft.player.getId();
            TravelPackScreen.this.onClose();
            PacketDistributor.sendToServer(new DeployBedrollMessage(playerId));
         }
      }
   }

   class PackButton extends AbstractButton {
      public PackButton(int x, int y, String translationKey) {
         super(x, y, 6, 16, CommonComponents.EMPTY);
         this.setTooltip(Tooltip.create(Component.translatable(translationKey)));
      }

      @Override
      public void onPress() {
      }

      @Override
      public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
         int u = 0;
         if (!this.active) {
            u += this.width * 2;
         } else if (this.isHoveredOrFocused()) {
            u += this.width * 3;
         }
         graphics.blit(TRAVEL_PACK_GUI_TEXTURE, this.getX(), this.getY(), u, 151, this.width, this.height);
      }

      @Override
      protected void updateWidgetNarration(NarrationElementOutput output) {
         this.defaultButtonNarrationText(output);
      }
   }
}
