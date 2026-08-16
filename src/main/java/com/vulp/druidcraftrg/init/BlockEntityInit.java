package com.vulp.druidcraftrg.init;

import com.vulp.druidcraftrg.blocks.tile.BeamTileEntity;
import com.vulp.druidcraftrg.blocks.tile.CrateTileEntity;
import com.vulp.druidcraftrg.blocks.tile.GrowthLampTileEntity;
import com.vulp.druidcraftrg.blocks.tile.RopeTileEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;

public class BlockEntityInit {
   public static BlockEntityType<RopeTileEntity> rope;
   public static BlockEntityType<BeamTileEntity> beam;
   public static BlockEntityType<CrateTileEntity> crate;
   public static BlockEntityType<GrowthLampTileEntity> growth_lamp;

   public static <T extends BlockEntity> BlockEntityType<T> build(Builder<T> builder) {
      return builder.build(null);
   }
}
