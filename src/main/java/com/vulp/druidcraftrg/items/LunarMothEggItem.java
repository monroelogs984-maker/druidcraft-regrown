package com.vulp.druidcraftrg.items;

import com.vulp.druidcraftrg.entities.LunarMothColors;
import com.vulp.druidcraftrg.entities.LunarMothEntity;
import com.vulp.druidcraftrg.init.EntityInit;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LunarMothEggItem extends Item {

   private final LunarMothColors color;

   public LunarMothEggItem(LunarMothColors color, Properties properties) {
      super(properties);
      this.color = color;
   }

   @Override
   public InteractionResult useOn(UseOnContext context) {
      Level world = context.getLevel();
      if (world.isClientSide) {
         return InteractionResult.SUCCESS;
      }
      ServerLevel serverWorld = (ServerLevel) world;
      ItemStack itemstack = context.getItemInHand();
      BlockPos blockpos = context.getClickedPos();
      Direction direction = context.getClickedFace();
      BlockState blockstate = serverWorld.getBlockState(blockpos);
      BlockPos blockpos1 = blockstate.getCollisionShape(serverWorld, blockpos).isEmpty()
         ? blockpos
         : blockpos.relative(direction);

      Player player = context.getPlayer();
      LunarMothEntity moth = EntityInit.lunar_moth.spawn(serverWorld, itemstack, player, blockpos1,
         MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
      if (moth != null) {
         moth.setColor(this.color);
         itemstack.shrink(1);
      }
      return InteractionResult.SUCCESS;
   }
}
