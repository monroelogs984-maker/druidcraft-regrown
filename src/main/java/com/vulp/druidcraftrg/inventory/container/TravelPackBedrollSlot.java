package com.vulp.druidcraftrg.inventory.container;

import com.vulp.druidcraftrg.blocks.BedrollBlock;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;

public class TravelPackBedrollSlot extends Slot {

   public TravelPackBedrollSlot(Container container, int index, int posX, int posY) {
      super(container, index, posX, posY);
   }

   @Override
   public boolean mayPlace(ItemStack itemStack) {
      return itemStack.getItem() instanceof BedItem bed && bed.getBlock() instanceof BedrollBlock;
   }
}
