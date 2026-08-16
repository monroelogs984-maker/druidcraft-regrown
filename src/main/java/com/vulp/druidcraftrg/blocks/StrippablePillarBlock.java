package com.vulp.druidcraftrg.blocks;

import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;

public class StrippablePillarBlock extends RotatedPillarBlock {
   private final Supplier<? extends Block> stripped;

   public StrippablePillarBlock(Supplier<? extends Block> stripped, Properties properties) {
      super(properties);
      this.stripped = stripped;
   }

   public Block getStripped() {
      return this.stripped.get();
   }
}
