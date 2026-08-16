package com.vulp.druidcraftrg.entities.ai;

import com.vulp.druidcraftrg.entities.TameableMonsterEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class SitGoalMonster extends Goal {
   private final TameableMonsterEntity tameable;
   private boolean isSitting;

   public SitGoalMonster(TameableMonsterEntity entityIn) {
      this.tameable = entityIn;
      this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
   }

   @Override
   public boolean canContinueToUse() {
      return this.isSitting;
   }

   @Override
   public boolean canUse() {
      if (!this.tameable.isTamed()) {
         return false;
      } else if (this.tameable.isInWaterOrBubble()) {
         return false;
      } else {
         LivingEntity livingentity = this.tameable.getOwner();
         if (livingentity == null) {
            return true;
         }
         return (!(this.tameable.distanceToSqr(livingentity) < 144.0D) || livingentity.getLastHurtByMob() == null) && this.isSitting;
      }
   }

   @Override
   public void start() {
      this.tameable.getNavigation().stop();
      this.tameable.setSitting(true);
   }

   @Override
   public void stop() {
      this.tameable.setSitting(false);
   }

   public void setSitting(boolean sitting) {
      this.isSitting = sitting;
   }
}
