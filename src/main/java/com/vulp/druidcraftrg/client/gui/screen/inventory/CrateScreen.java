package com.vulp.druidcraftrg.client.gui.screen.inventory;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import com.vulp.druidcraftrg.DruidcraftRegrownRegistry;
import com.vulp.druidcraftrg.inventory.container.CrateContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag.Default;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

@OnlyIn(Dist.CLIENT)
public class CrateScreen extends AbstractContainerScreen<CrateContainer> implements MenuAccess<CrateContainer> {
   private static final ResourceLocation CONTAINER_BACKGROUND = DruidcraftRegrownRegistry.location("textures/gui/container/scrollable_container.png");
   private final int containerRows;
   private int scrollOffset;
   private final int scrollOffsetMax = 108;
   private boolean isScrolling;
   private final boolean canScroll;
   private CrateScreen.CrateSearchWidget searchBox;
   private boolean ignoreTextInput;
   private List<Slot> searchList = new ArrayList<>(Collections.emptyList());

   public CrateScreen(CrateContainer container, Inventory playerInventory, Component displayName) {
      super(container, playerInventory, displayName);
      this.containerRows = container.getRowCount();
      this.canScroll = this.containerRows > 6;
      this.imageWidth = 194;
      this.imageHeight = 221;
      this.inventoryLabelY = this.imageHeight - 94;
   }

   public void init() {
      super.init();
      this.searchBox = new CrateScreen.CrateSearchWidget(this.font, 99, 6, 80, 9, Component.translatable("crate.search"));
      this.searchBox.setMaxLength(50);
      this.searchBox.setBordered(false);
      this.searchBox.setVisible(false);
      this.searchBox.setTextColor(16777215);
      this.children.add(this.searchBox);
      this.searchBox.setX(99);
   }

   public void resize(Minecraft minecraft, int width, int height) {
      String s = this.searchBox.getValue();
      this.init(minecraft, width, height);
      this.searchBox.setValue(s);
      if (!this.searchBox.getValue().isEmpty()) {
         this.refreshSearchResults();
      }
   }

   public boolean charTyped(char character, int i) {
      if (this.ignoreTextInput) {
         return false;
      } else {
         String s = this.searchBox.getValue();
         if (this.searchBox.charTyped(character, i)) {
            if (!Objects.equals(s, this.searchBox.getValue())) {
               this.refreshSearchResults();
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public boolean keyPressed(int i, int j, int k) {
      this.ignoreTextInput = false;
      boolean flag = this.hoveredSlot != null && this.hoveredSlot.hasItem();
      boolean flag1 = InputConstants.getKey(i, j).getNumericKeyValue().isPresent();
      if (flag && flag1 && this.checkHotbarKeyPressed(i, j)) {
         this.ignoreTextInput = true;
         return true;
      } else {
         String s = this.searchBox.getValue();
         if (this.searchBox.keyPressed(i, j, k)) {
            if (!Objects.equals(s, this.searchBox.getValue())) {
               this.refreshSearchResults();
            }

            return true;
         } else {
            return this.searchBox.isFocused() && this.searchBox.isVisible() && i != 256 || super.keyPressed(i, j, k);
         }
      }
   }

   public boolean keyReleased(int i, int j, int k) {
      this.ignoreTextInput = false;
      return super.keyReleased(i, j, k);
   }

   private void refreshSearchResults() {
      this.searchList = new ArrayList<>(Collections.emptyList());
      if (!this.searchBox.getValue().isEmpty()) {
         String search = this.searchBox.getValue().toLowerCase(Locale.ROOT);

         for (Slot slot : ((CrateContainer)this.menu).slots) {
            if (!(slot.container instanceof Inventory) && slot.getItem().getItem() != Items.AIR) {
               for (Component line : slot.getItem().getTooltipLines(net.minecraft.world.item.Item.TooltipContext.EMPTY, this.minecraft.player, this.minecraft.options.advancedItemTooltips ? Default.ADVANCED : Default.NORMAL)) {
                  if (ChatFormatting.stripFormatting(line.getString()).toLowerCase(Locale.ROOT).contains(search)) {
                     this.searchList.add(slot);
                     break;
                  }
               }
            }
         }
      }
   }

   protected void containerTick() {
      if (this.searchBox != null) {
         this.searchBox.setTextColor(!this.searchBox.getValue().isEmpty() && this.searchList.isEmpty() ? 16711680 : 16777215);
      }

      super.containerTick();
   }

   public void render(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
      this.renderBg(gfx, delta, mouseX, mouseY);
      NeoForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Background(this, gfx, mouseX, mouseY));

      for (Renderable widget : this.renderables) {
         widget.render(gfx, mouseX, mouseY, delta);
      }

      int i = this.leftPos;
      int j = this.topPos;
      gfx.pose().pushPose();
      gfx.pose().translate((double)i, (double)j, 0.0);
      this.hoveredSlot = null;
      int scrollAmount = (int)((float)this.scrollOffset * (((float)this.containerRows - 6.0F) / 6.0F));
      List<Integer> invSlots = new ArrayList<>(Collections.emptyList());

      for (int slot = 0; slot < ((CrateContainer)this.menu).slots.size(); slot++) {
         Slot currentSlot = (Slot)((CrateContainer)this.menu).slots.get(slot);
         if (currentSlot.isActive()) {
            boolean isCrateInv = slot < this.containerRows * 9;
            this.renderSlot(gfx, currentSlot, isCrateInv, scrollAmount);
            int slotX = currentSlot.x;
            int slotY = currentSlot.y;
            if (isCrateInv) {
               slotY -= scrollAmount + 219;
               if ((!this.searchList.isEmpty() || !this.searchBox.getValue().isEmpty()) && !this.searchList.contains(currentSlot)) {
                  if (slotY > 1 && slotY < 123) {
                     renderSlotHighlightBig(gfx, slotX, slotY, 0, -1072689136);
                  }
               } else if (this.isHovering(currentSlot, (double)mouseX, (double)mouseY)
                  && slotY > 1
                  && slotY < 123
                  && mouseY > this.topPos + 17
                  && mouseY < this.topPos + 124) {
                  this.hoveredSlot = currentSlot;
                  renderSlotHighlight(gfx, slotX, slotY, 0, this.getSlotColor(slot));
               }
            } else {
               invSlots.add(slot);
            }
         }
      }

      this.renderForeground(gfx, delta, mouseX, mouseY);

      for (Integer slotNum : invSlots) {
         Slot currentSlot = (Slot)((CrateContainer)this.menu).slots.get(slotNum);
         if (this.isHovering(currentSlot, (double)mouseX, (double)mouseY)) {
            this.hoveredSlot = currentSlot;
            renderSlotHighlight(gfx, currentSlot.x, currentSlot.y, 0, this.getSlotColor(slotNum));
         }
      }

      gfx.pose().pushPose();
      gfx.pose().translate(0.0, 0.0, 500.0);
      this.renderLabels(gfx, mouseX, mouseY);
      this.searchBox.render(gfx, mouseX, mouseY, delta);
      gfx.pose().popPose();
      NeoForge.EVENT_BUS.post(new ContainerScreenEvent.Render.Foreground(this, gfx, mouseX, mouseY));
      ItemStack currentStack = this.draggingItem.isEmpty() ? ((CrateContainer)this.menu).getCarried() : this.draggingItem;
      if (!currentStack.isEmpty()) {
         int i2 = this.draggingItem.isEmpty() ? 8 : 16;
         String s = null;
         if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
            currentStack = currentStack.copy();
            currentStack.setCount(Mth.ceil((float)currentStack.getCount() / 2.0F));
         } else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
            currentStack = currentStack.copy();
            currentStack.setCount(this.quickCraftingRemainder);
            if (currentStack.isEmpty()) {
               s = ChatFormatting.YELLOW + "0";
            }
         }

         this.renderFloatingItem(gfx, currentStack, mouseX - i - 8, mouseY - j - i2, s);
      }

      if (!this.snapbackItem.isEmpty()) {
         float f = (float)(Util.getMillis() - this.snapbackTime) / 100.0F;
         if (f >= 1.0F) {
            f = 1.0F;
            this.snapbackItem = ItemStack.EMPTY;
         }

         int j2 = this.snapbackEnd.x - this.snapbackStartX;
         int k2 = this.snapbackEnd.y - this.snapbackStartY;
         int j1 = this.snapbackStartX + (int)((float)j2 * f);
         int k1 = this.snapbackStartY + (int)((float)k2 * f);
         this.renderFloatingItem(gfx, this.snapbackItem, j1, k1, (String)null);
      }

      gfx.pose().popPose();
      this.renderTooltip(gfx, mouseX, mouseY);
   }

   public static void renderSlotHighlightBig(GuiGraphics gfx, int xPos, int yPos, int blitOffset, int slotColor) {
      gfx.fillGradient(xPos - 1, yPos - 1, xPos - 1 + 18, yPos - 1 + 18, blitOffset, slotColor, slotColor);
   }

   protected void renderLabels(GuiGraphics gfx, int p_230451_2_, int p_230451_3_) {
      gfx.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
      gfx.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
   }

   protected void renderTooltip(GuiGraphics gfx, int mouseX, int mouseY) {
      if (this.minecraft.player.inventoryMenu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
         if (!(this.hoveredSlot.container instanceof Inventory) && (mouseY <= this.topPos + 17 || mouseY >= this.topPos + 124)) {
            return;
         }

         gfx.renderTooltip(this.font, this.hoveredSlot.getItem(), mouseX, mouseY);
      }
   }

   public boolean mouseScrolled(double x, double y, double scrollAmount) {
      if (!this.canScroll) {
         return false;
      } else {
         this.scrollOffset = (int)((double)this.scrollOffset - scrollAmount * (double)Math.abs(this.containerRows - 21));
         this.scrollOffset = Mth.clamp(this.scrollOffset, 0, 108);
         return true;
      }
   }

   public boolean mouseClicked(double x, double y, int buttonNumber) {
      if (buttonNumber == 0) {
         if (this.insideSearchBox(x, y)) {
            this.searchBox.setFocused(true);
            this.searchBox.setCanLoseFocus(false);
            this.searchBox.setVisible(true);
            return true;
         }

         this.searchBox.setCanLoseFocus(true);
         this.searchBox.setFocused(false);
         if (this.insideScrollbar(x, y)) {
            this.isScrolling = this.canScroll;
            return true;
         }
      }

      return super.mouseClicked(x, y, buttonNumber);
   }

   public boolean mouseDragged(double x, double y, int buttonNumber, double xMovement, double yMovement) {
      if (this.isScrolling) {
         int i = this.topPos + 18;
         int j = i + 197;
         this.scrollOffset = (int)(108.0F * ((float)y - (float)i - 7.5F) / ((float)(j - i) - 15.0F));
         this.scrollOffset = Mth.clamp(this.scrollOffset, 0, 108);
         return true;
      } else {
         return super.mouseDragged(x, y, buttonNumber, xMovement, yMovement);
      }
   }

   public boolean mouseReleased(double x, double y, int buttonNumber) {
      if (buttonNumber == 0) {
         this.isScrolling = false;
      }

      boolean mouseReleased = super.mouseReleased(x, y, buttonNumber);
      int i = this.leftPos;
      int j = this.topPos;
      Slot slot = this.findSlot(x, y);
      boolean flag = this.hasClickedOutside(x, y, i, j, buttonNumber);
      if (slot != null) {
         flag = false;
      }

      int k = -1;
      if (slot != null) {
         k = slot.index;
      }

      if (flag) {
         k = -999;
      }

      if ((!this.doubleclick || slot == null || buttonNumber == 0 || !((CrateContainer)this.menu).canTakeItemForPickAll(ItemStack.EMPTY, slot))
         && this.clickedSlot != null
         && this.minecraft.options.touchscreen().get()
         && (buttonNumber == 0 || buttonNumber == 1)) {
         if (this.draggingItem.isEmpty() && slot != this.clickedSlot) {
            this.draggingItem = this.clickedSlot.getItem();
         }

         if (k != -1 && !this.draggingItem.isEmpty() && AbstractContainerMenu.canItemQuickReplace(slot, this.draggingItem, false)) {
            this.slotClicked(this.clickedSlot, this.clickedSlot.index, buttonNumber, ClickType.PICKUP);
            this.slotClicked(slot, k, 0, ClickType.PICKUP);
            if (((CrateContainer)this.menu).getCarried().isEmpty()) {
               this.snapbackItem = ItemStack.EMPTY;
            } else {
               this.slotClicked(this.clickedSlot, this.clickedSlot.index, buttonNumber, ClickType.PICKUP);
               this.snapbackStartX = Mth.floor(x - (double)i);
               this.snapbackStartY = Mth.floor(y - (double)j);
               this.snapbackEnd = this.clickedSlot;
               this.snapbackItem = this.draggingItem;
               this.snapbackTime = Util.getMillis();
            }
         } else if (!this.draggingItem.isEmpty()) {
            this.snapbackStartX = Mth.floor(x - (double)i);
            this.snapbackStartY = Mth.floor(y - (double)j);
            this.snapbackEnd = this.clickedSlot;
            this.snapbackItem = this.draggingItem;
            this.snapbackTime = Util.getMillis();
         }

         this.draggingItem = ItemStack.EMPTY;
         this.clickedSlot = null;
      }

      return mouseReleased;
   }

   protected boolean insideScrollbar(double x, double y) {
      int i = this.leftPos;
      int j = this.topPos;
      int k = i + 174;
      int l = j + 18;
      int i1 = k + 14;
      int j1 = l + 197;
      return x >= (double)k && y >= (double)l && x < (double)i1 && y < (double)j1;
   }

   protected boolean insideSearchBox(double x, double y) {
      int i = this.leftPos;
      int j = this.topPos;
      int k = i + 98;
      int l = j + 5;
      int i1 = k + 90;
      int j1 = l + 12;
      return x >= (double)k && y >= (double)l && x < (double)i1 && y < (double)j1;
   }

   private void renderSlot(GuiGraphics gfx, Slot slot, boolean scrollable, int scrollValue) {
      if (!scrollable || slot.y - 219 - scrollValue > 1 && slot.y - 219 - scrollValue < 123) {
         int i = slot.x;
         int j = scrollable ? slot.y - scrollValue - 219 : slot.y;
         ItemStack itemstack = slot.getItem();
         boolean flag = false;
         boolean flag1 = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
         ItemStack itemstack1 = this.minecraft.player.inventoryMenu.getCarried();
         String s = null;
         if (slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
         } else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !itemstack1.isEmpty()) {
            if (this.quickCraftSlots.size() == 1) {
               return;
            }

            if (AbstractContainerMenu.canItemQuickReplace(slot, itemstack1, true) && ((CrateContainer)this.menu).canDragTo(slot)) {
               itemstack = itemstack1.copy();
               flag = true;
               int k = Math.min(itemstack.getMaxStackSize(), slot.getMaxStackSize(itemstack));
               int l = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
               int i1 = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, itemstack) + l;
               if (i1 > k) {
                  i1 = k;
                  s = ChatFormatting.YELLOW.toString() + k;
               }

               itemstack.setCount(i1);
            } else {
               this.quickCraftSlots.remove(slot);
               this.recalculateQuickCraftRemaining();
            }
         }

         int offset = scrollable ? 100 : 250;
         gfx.pose().pushPose();
         gfx.pose().translate(0.0, 0.0, (double)offset);
         if (itemstack.isEmpty() && slot.isActive()) {
            Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
            if (pair != null) {
               TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.minecraft
                  .getTextureAtlas((ResourceLocation)pair.getFirst())
                  .apply((ResourceLocation)pair.getSecond());
               gfx.blit(i, j, 0, 16, 16, textureatlassprite);
               flag1 = true;
            }
         }

         if (!flag1) {
            if (flag) {
               gfx.fill(i, j, i + 16, j + 16, -2130706433);
            }

            gfx.renderItem(itemstack, i, j);
            gfx.renderItemDecorations(this.font, itemstack, i, j, s);
         }

         gfx.pose().popPose();
      }
   }

   private boolean isHovering(Slot slot, double mouseX, double mouseY) {
      return slot.container instanceof Inventory
         ? this.isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY)
         : this.isHovering(
            slot.x, slot.y - 219 - (int)((float)this.scrollOffset * (((float)this.containerRows - 6.0F) / 6.0F)), 16, 16, mouseX, mouseY
         );
   }

   protected boolean isHovering(int slotX, int slotY, int slotWidth, int slotHeight, double mouseX, double mouseY) {
      int i = this.leftPos;
      int j = this.topPos;
      mouseX -= (double)i;
      mouseY -= (double)j;
      return mouseX >= (double)(slotX - 1)
         && mouseX < (double)(slotX + slotWidth + 1)
         && mouseY >= (double)(slotY - 1)
         && mouseY < (double)(slotY + slotHeight + 1);
   }

   protected void slotClicked(@Nullable Slot slot, int slotNumber, int buttonNumber, ClickType clickType) {
      if (slot != null) {
         this.searchBox.moveCursorToEnd(false);
         this.searchBox.setHighlightPos(0);
         if (slotNumber >= this.containerRows * 9
            & (slot.y < 0 || slot.y - 219 - (int)((float)this.scrollOffset * (((float)this.containerRows - 6.0F) / 6.0F)) > 124)) {
            return;
         }
      }

      this.searchBox.moveCursorToEnd(false);
      this.searchBox.setHighlightPos(this.searchBox.getCursorPosition());
      this.minecraft.gameMode.handleInventoryMouseClick(((CrateContainer)this.menu).containerId, slotNumber, buttonNumber, clickType, this.minecraft.player);
      this.refreshSearchResults();
   }

   public Slot findSlot(double mouseX, double mouseY) {
      for (int i = 0; i < ((CrateContainer)this.menu).slots.size(); i++) {
         Slot slot = (Slot)((CrateContainer)this.menu).slots.get(i);
         if (this.isHovering(slot, mouseX, mouseY)
            && slot.isActive()
            && (slot.container instanceof Inventory || mouseY > (double)(this.topPos + 18) && mouseY < (double)(this.topPos + 124))) {
            return slot;
         }
      }

      return null;
   }

   private void renderFloatingItem(GuiGraphics gfx, ItemStack stack, int mouseX, int mouseY, String string) {
      gfx.pose().pushPose();
      gfx.pose().translate(0.0, 0.0, 332.0);
      Font font = IClientItemExtensions.of(stack).getFont(stack, IClientItemExtensions.FontContext.ITEM_COUNT);
      if (font == null) {
         font = this.font;
      }

      gfx.renderItem(stack, mouseX, mouseY);
      gfx.renderItemDecorations(font, stack, mouseX, mouseY - (this.draggingItem.isEmpty() ? 0 : 8), string);
      gfx.pose().popPose();
   }

   private void recalculateQuickCraftRemaining() {
      ItemStack itemstack = this.minecraft.player.inventoryMenu.getCarried();
      if (!itemstack.isEmpty() && this.isQuickCrafting) {
         if (this.quickCraftingType == 2) {
            this.quickCraftingRemainder = itemstack.getMaxStackSize();
         } else {
            this.quickCraftingRemainder = itemstack.getCount();

            for (Slot slot : this.quickCraftSlots) {
               ItemStack itemstack2 = slot.getItem();
               int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
               int j = Math.min(itemstack.getMaxStackSize(), slot.getMaxStackSize(itemstack));
               int k = Math.min(AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, itemstack) + i, j);
               this.quickCraftingRemainder -= k - i;
            }
         }
      }
   }

   protected void renderForeground(GuiGraphics gfx, float delta, int mouseX, int mouseY) {
      gfx.pose().pushPose();
      gfx.pose().translate(0.0, 0.0, 350.0);
      gfx.blit(CONTAINER_BACKGROUND, 0, 0, 0, 0, 194, 18);
      gfx.blit(CONTAINER_BACKGROUND, 0, 18, 0, 18, 8, 106);
      gfx.blit(CONTAINER_BACKGROUND, 168, 18, 168, 18, 26, 106);
      gfx.blit(CONTAINER_BACKGROUND, 0, 124, 0, 124, 194, 97);
      gfx.blit(CONTAINER_BACKGROUND, 174, 18 + (int)(180.0F * ((float)this.scrollOffset / 108.0F)), this.canScroll ? 0 : 12, 221, 12, 15);
      gfx.pose().popPose();
   }

   protected void renderBg(GuiGraphics gfx, float delta, int mouseX, int mouseY) {
      int i = this.leftPos;
      int j = this.topPos;
      gfx.blit(CONTAINER_BACKGROUND, i + 8, j + 18, 8, 18, 160, 106);

      for (int y = 0; y < this.containerRows; y++) {
         int yPos = j + 17 + y * 18 - (int)((float)this.scrollOffset * (((float)this.containerRows - 6.0F) / 6.0F));
         if (yPos >= j && yPos <= j + 123) {
            int ySize = yPos < 18 ? Math.abs(yPos - 18) : 18;

            for (int x = 0; x < 9; x++) {
               gfx.blit(CONTAINER_BACKGROUND, i + 7 + x * 18, ySize != 18 ? j + 18 : yPos, 194, 18 - ySize, 18, ySize);
            }
         }
      }
   }

   private class CrateSearchWidget extends EditBox {
      public CrateSearchWidget(Font fontRenderer, int x, int y, int width, int height, Component text) {
         super(fontRenderer, x, y, width, height, text);
      }

   }
}
