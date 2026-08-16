package com.vulp.druidcraftrg.inventory.container;

import com.vulp.druidcraftrg.items.TravelPackItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TravelPackSlot extends Slot {

   public TravelPackSlot(Container container, int index, int posX, int posY) {
      super(container, index, posX, posY);
   }

   @Override
   public boolean mayPlace(ItemStack itemStack) {
      return !(itemStack.getItem() instanceof TravelPackItem);
   }
}
