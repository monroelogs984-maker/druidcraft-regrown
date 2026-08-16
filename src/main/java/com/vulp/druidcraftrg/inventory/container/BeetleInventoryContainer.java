package com.vulp.druidcraftrg.inventory.container;

import com.vulp.druidcraftrg.entities.BeetleEntity;
import com.vulp.druidcraftrg.init.ContainerInit;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BeetleInventoryContainer extends AbstractContainerMenu {
   private final Container beetleInventory;
   private final BeetleEntity beetle;

   public BeetleInventoryContainer(int windowID, Inventory playerInventory, Container inventory, int id) {
      super(ContainerInit.BEETLE_INV, windowID);
      this.beetleInventory = inventory;
      this.beetle = (BeetleEntity) playerInventory.player.level().getEntity(id);
      inventory.startOpen(playerInventory.player);
      this.addSlot(new Slot(inventory, 0, 18, 72) {
         @Override
         public boolean mayPlace(ItemStack stack) {
            return stack.getItem() == Items.SADDLE && !this.hasItem();
         }

         @Override
         public boolean isActive() {
            return true;
         }
      });

      int j1;
      int k1;
      if (beetle != null && beetle.hasChest()) {
         for (j1 = 0; j1 < 7; ++j1) {
            for (k1 = 0; k1 < 9; ++k1) {
               this.addSlot(new Slot(inventory, 2 + k1 + j1 * 9, 85 + (k1 * 18), 18 + (j1 * 18)));
            }
         }
      }

      for (j1 = 0; j1 < 3; ++j1) {
         for (k1 = 0; k1 < 9; ++k1) {
            this.addSlot(new Slot(playerInventory, k1 + j1 * 9 + 9, 49 + k1 * 18, 156 + j1 * 18));
         }
      }

      for (j1 = 0; j1 < 9; ++j1) {
         this.addSlot(new Slot(playerInventory, j1, 49 + j1 * 18, 214));
      }
   }

   public BeetleEntity getBeetle() {
      return beetle;
   }

   @Override
   public boolean stillValid(Player playerIn) {
      return this.beetleInventory.stillValid(playerIn) && this.beetle.isAlive() && this.beetle.distanceTo(playerIn) < 8.0F;
   }

   @Override
   public ItemStack quickMoveStack(Player playerIn, int index) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack itemstack1 = slot.getItem();
         itemstack = itemstack1.copy();
         if (index < this.beetleInventory.getContainerSize()) {
            if (!this.moveItemStackTo(itemstack1, this.beetleInventory.getContainerSize(), this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
            if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
               return ItemStack.EMPTY;
            }
         } else if (this.getSlot(0).mayPlace(itemstack1)) {
            if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
               return ItemStack.EMPTY;
            }
         } else if (this.beetleInventory.getContainerSize() <= 2
               || !this.moveItemStackTo(itemstack1, 2, this.beetleInventory.getContainerSize(), false)) {
            return ItemStack.EMPTY;
         }

         if (itemstack1.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }
      return itemstack;
   }

   @Override
   public void removed(Player playerIn) {
      super.removed(playerIn);
      this.beetleInventory.stopOpen(playerIn);
   }
}
