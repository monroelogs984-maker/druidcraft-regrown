package com.vulp.druidcraftrg.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

// item-backed inventory; contents live in the travel pack stack's "Items" NBT
public class TravelPackInventory extends SimpleContainer {
   private final ItemStack stack;
   private final HolderLookup.Provider registries;

   public TravelPackInventory(ItemStack stack, HolderLookup.Provider registries) {
      super(10);
      this.stack = stack;
      this.registries = registries;
      this.readItemStack();
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public void readItemStack() {
      if (this.stack.has(DataComponents.CUSTOM_DATA)) {
         this.readNBT(this.stack.get(DataComponents.CUSTOM_DATA).copyTag());
      }
   }

   public void writeItemStack() {
      CompoundTag nbt = this.stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (this.isEmpty()) {
         nbt.remove("Items");
      } else {
         this.writeNBT(nbt);
      }
      if (!nbt.isEmpty()) {
         this.stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
      } else {
         this.stack.remove(DataComponents.CUSTOM_DATA);
      }
   }

   private void readNBT(CompoundTag compound) {
      NonNullList<ItemStack> list = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      ContainerHelper.loadAllItems(compound, list, this.registries);
      for (int index = 0; index < list.size(); index++) {
         this.setItem(index, list.get(index));
      }
   }

   private void writeNBT(CompoundTag compound) {
      NonNullList<ItemStack> list = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      for (int index = 0; index < list.size(); index++) {
         list.set(index, this.getItem(index));
      }
      ContainerHelper.saveAllItems(compound, list, false, this.registries);
   }
}
