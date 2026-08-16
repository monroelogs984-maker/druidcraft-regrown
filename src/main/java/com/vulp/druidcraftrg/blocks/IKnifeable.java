package com.vulp.druidcraftrg.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface IKnifeable {
   InteractionResult onKnifed(@Nullable Player var1, Level var2, BlockPos var3, BlockState var4, UseOnContext var5);
}
