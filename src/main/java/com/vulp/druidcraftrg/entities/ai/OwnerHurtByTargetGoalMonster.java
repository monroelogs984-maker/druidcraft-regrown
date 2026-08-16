package com.vulp.druidcraftrg.entities.ai;

import com.vulp.druidcraftrg.entities.TameableMonsterEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class OwnerHurtByTargetGoalMonster extends TargetGoal {
   private final TameableMonsterEntity tameable;
   private LivingEntity attacker;
   private int timestamp;

   public OwnerHurtByTargetGoalMonster(TameableMonsterEntity theDefendingTameableIn) {
      super(theDefendingTameableIn, false);
      this.tameable = theDefendingTameableIn;
      this.setFlags(EnumSet.of(Goal.Flag.TARGET));
   }

   @Override
   public boolean canUse() {
      if (this.tameable.isTamed() && !this.tameable.isSitting()) {
         LivingEntity livingentity = this.tameable.getOwner();
         if (livingentity == null) {
            return false;
         }
         this.attacker = livingentity.getLastHurtByMob();
         int i = livingentity.getLastHurtByMobTimestamp();
         return i != this.timestamp && this.canAttack(this.attacker, TargetingConditions.DEFAULT)
            && this.tameable.shouldAttackEntity(this.attacker, livingentity);
      }
      return false;
   }

   @Override
   public void start() {
      this.mob.setTarget(this.attacker);
      LivingEntity livingentity = this.tameable.getOwner();
      if (livingentity != null) {
         this.timestamp = livingentity.getLastHurtByMobTimestamp();
      }
      super.start();
   }
}
