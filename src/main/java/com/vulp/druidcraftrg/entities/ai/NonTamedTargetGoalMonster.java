package com.vulp.druidcraftrg.entities.ai;

import com.vulp.druidcraftrg.entities.TameableMonsterEntity;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class NonTamedTargetGoalMonster<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
   private final TameableMonsterEntity tameable;

   public NonTamedTargetGoalMonster(TameableMonsterEntity entity, Class<T> target, boolean mustSee, @Nullable Predicate<LivingEntity> predicate) {
      super(entity, target, 10, mustSee, false, predicate);
      this.tameable = entity;
   }

   public NonTamedTargetGoalMonster(TameableMonsterEntity entity, Class<T> target, boolean mustSee) {
      super(entity, target, 10, mustSee, false, null);
      this.tameable = entity;
   }

   @Override
   public boolean canUse() {
      return !this.tameable.isTamed() && super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      return this.targetConditions != null && this.target != null
         ? this.targetConditions.test(this.mob, this.target)
         : super.canContinueToUse();
   }
}
