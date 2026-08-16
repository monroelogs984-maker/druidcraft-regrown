package com.vulp.druidcraftrg.entities.ai;

import com.vulp.druidcraftrg.entities.TameableMonsterEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;

public class FollowOwnerGoalMonster extends Goal {
   private final TameableMonsterEntity tameable;
   private LivingEntity owner;
   protected final LevelReader world;
   private final double followSpeed;
   protected final PathNavigation navigator;
   private int timeToRecalcPath;
   private final float maxDist;
   private final float minDist;
   private float oldWaterCost;

   public FollowOwnerGoalMonster(TameableMonsterEntity tameableIn, double followSpeedIn, float minDistIn, float maxDistIn) {
      this.tameable = tameableIn;
      this.world = tameableIn.level();
      this.followSpeed = followSpeedIn;
      this.navigator = tameableIn.getNavigation();
      this.minDist = minDistIn;
      this.maxDist = maxDistIn;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      LivingEntity livingentity = this.tameable.getOwner();
      if (livingentity == null) {
         return false;
      } else if (livingentity instanceof Player && livingentity.isSpectator()) {
         return false;
      } else if (this.tameable.isSitting()) {
         return false;
      } else if (this.tameable.distanceToSqr(livingentity) < (double) (this.minDist * this.minDist)) {
         return false;
      } else {
         this.owner = livingentity;
         return true;
      }
   }

   @Override
   public boolean canContinueToUse() {
      return !this.navigator.isDone() && this.tameable.distanceToSqr(this.owner) > (double) (this.maxDist * this.maxDist) && !this.tameable.isSitting();
   }

   @Override
   public void start() {
      this.timeToRecalcPath = 0;
      this.oldWaterCost = this.tameable.getPathfindingMalus(PathType.WATER);
      this.tameable.setPathfindingMalus(PathType.WATER, 0.0F);
   }

   @Override
   public void stop() {
      this.owner = null;
      this.navigator.stop();
      this.tameable.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
   }

   @Override
   public void tick() {
      this.tameable.getLookControl().setLookAt(this.owner, 10.0F, (float) this.tameable.getMaxHeadXRot());
      if (!this.tameable.isSitting()) {
         if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.tameable.isLeashed() && !this.tameable.isPassenger()) {
               if (this.tameable.distanceToSqr(this.owner) > 576.0D) {
                  int i = Mth.floor(this.owner.getX()) - 2;
                  int j = Mth.floor(this.owner.getZ()) - 2;
                  int k = Mth.floor(this.owner.getBoundingBox().minY);

                  for (int l = 0; l <= 4; ++l) {
                     for (int i1 = 0; i1 <= 4; ++i1) {
                        if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.canTeleportToBlock(new BlockPos(i + l, k - 1, j + i1))) {
                           this.tameable.moveTo((float) (i + l) + 0.5F, k + 1, (float) (j + i1) + 0.5F, this.tameable.getYRot(), this.tameable.getXRot());
                           this.navigator.stop();
                        }
                     }
                  }
               }
            }
            this.navigator.moveTo(this.owner, this.followSpeed);
         }
      }
   }

   private boolean canTeleportToBlock(BlockPos pos) {
      BlockState blockstate = this.world.getBlockState(pos);
      return blockstate.entityCanStandOn(this.world, pos, this.tameable)
         && this.world.isEmptyBlock(pos.above(2)) && this.world.isEmptyBlock(pos.above(3));
   }
}
