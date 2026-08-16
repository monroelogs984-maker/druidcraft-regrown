package com.vulp.druidcraftrg.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

// item-backed inventory; contents live in the travel pack stack's "Items" NBT
public class TravelPackInventory extends SimpleContainer {
   private final ItemStack stack;

   public TravelPackInventory(ItemStack stack) {
      super(10);
      this.stack = stack;
      this.readItemStack();
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public void readItemStack() {
      if (this.stack.getTag() != null) {
         this.readNBT(this.stack.getTag());
      }
   }

   public void writeItemStack() {
      if (this.isEmpty()) {
         this.stack.removeTagKey("Items");
      } else {
         this.writeNBT(this.stack.getOrCreateTag());
      }
   }

   private void readNBT(CompoundTag compound) {
      NonNullList<ItemStack> list = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      ContainerHelper.loadAllItems(compound, list);
      for (int index = 0; index < list.size(); index++) {
         this.setItem(index, list.get(index));
      }
   }

   private void writeNBT(CompoundTag compound) {
      NonNullList<ItemStack> list = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      for (int index = 0; index < list.size(); index++) {
         list.set(index, this.getItem(index));
      }
      ContainerHelper.saveAllItems(compound, list, false);
   }
}
