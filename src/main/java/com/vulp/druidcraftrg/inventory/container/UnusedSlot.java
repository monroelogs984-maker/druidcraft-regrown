package com.vulp.druidcraftrg.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

// reserved slots in the travel pack UI that can't hold anything yet
public class UnusedSlot extends Slot {

   public UnusedSlot(Container container, int index, int posX, int posY) {
      super(container, index, posX, posY);
   }

   @Override
   public boolean mayPlace(ItemStack itemStack) {
      return false;
   }

   @Override
   public boolean mayPickup(net.minecraft.world.entity.player.Player player) {
      return false;
   }
}
