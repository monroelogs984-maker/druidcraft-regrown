package com.vulp.druidcraftrg.blocks;

import com.vulp.druidcraftrg.inventory.container.WoodcutterMenu;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WoodcutterBlock extends StonecutterBlock {
   private static final Component CONTAINER_NAME = Component.translatable("container.druidcraftrg.woodcutter");

   public WoodcutterBlock(Properties properties) {
      super(properties);
   }

   @Nullable
   @Override
   public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
      return new SimpleMenuProvider((containerId, inventory, player) ->
         new WoodcutterMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_NAME);
   }
}
