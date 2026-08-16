package com.vulp.druidcraftrg.inventory.container;

import com.vulp.druidcraftrg.blocks.BedrollBlock;
import com.vulp.druidcraftrg.init.ContainerInit;
import com.vulp.druidcraftrg.inventory.TravelPackInventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;

public class TravelPackContainer extends AbstractContainerMenu {

   private final Container travelPackInventory;
   private final ItemStack travelPack;

   // Client
   public static TravelPackContainer createClientContainer(int id, Inventory playerInventory) {
      return new TravelPackContainer(id, playerInventory, new SimpleContainer(10), playerInventory.getSelected());
   }

   // Server
   public TravelPackContainer(int id, Inventory playerInventory, Container inventory, ItemStack travelPack) {
      super(ContainerInit.TRAVEL_PACK, id);
      this.travelPackInventory = inventory;
      this.travelPack = travelPack;

      // Storage
      for (int i = 0; i < 2; i++) {
         for (int j = 0; j < 3; j++) {
            this.addSlot(new TravelPackSlot(inventory, 4 + j + i * 3, 62 + j * 18, 20 + i * 18));
         }
      }

      // Misc Slots
      this.addSlot(new TravelPackBedrollSlot(inventory, 0, 26, 20));
      this.addSlot(new UnusedSlot(inventory, 1, 26, 38));
      this.addSlot(new UnusedSlot(inventory, 2, 134, 20));
      this.addSlot(new UnusedSlot(inventory, 3, 134, 38));

      // Player Inventory
      for (int row = 0; row < 3; row++) {
         for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 69 + row * 18));
         }
      }
      for (int col = 0; col < 9; col++) {
         this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 127));
      }
   }

   @Override
   public boolean stillValid(Player player) {
      return true;
   }

   public boolean hasBedroll() {
      return this.travelPackInventory.getItem(0).getItem() instanceof BedItem bed && bed.getBlock() instanceof BedrollBlock;
   }

   @Override
   public void broadcastChanges() {
      super.broadcastChanges();
      if (this.travelPackInventory instanceof TravelPackInventory inventory) {
         inventory.writeItemStack();
      }
   }

   @Override
   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack slotStack = slot.getItem();
         itemstack = slotStack.copy();
         if (index < 10) {
            if (!this.moveItemStackTo(slotStack, 10, this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(slotStack, 0, 10, false)) {
            return ItemStack.EMPTY;
         }

         if (slotStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }
      return itemstack;
   }

   @Override
   public void clicked(int slotId, int button, ClickType clickType, Player player) {
      // never let the player move the pack they're looking inside of
      if (slotId >= 0 && slotId < this.slots.size()) {
         Slot slot = this.slots.get(slotId);
         if (slot.container == player.getInventory() && slot.getContainerSlot() == player.getInventory().selected) {
            return;
         }
      }
      if (clickType == ClickType.SWAP) {
         ItemStack stack = player.getInventory().getItem(button);
         if (stack == player.getInventory().getSelected()) {
            return;
         }
      }
      super.clicked(slotId, button, clickType, player);
   }
}
