package com.vulp.druidcraftrg.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;

// null-safe canEntityStandOnPos, matching the 1.16.5 original
public class ImprovedFlyingPathNavigator extends FlyingPathNavigation {

   public ImprovedFlyingPathNavigator(Mob entityIn, Level worldIn) {
      super(entityIn, worldIn);
   }

   @Override
   public boolean isStableDestination(@Nullable BlockPos pos) {
      if (pos == null) {
         return false;
      }
      return super.isStableDestination(pos);
   }
}
