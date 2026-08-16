package com.vulp.druidcraftrg.mixins;

import com.vulp.druidcraftrg.blocks.RopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin {
   @Shadow
   public boolean jumping;

   @Shadow
   public abstract Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 var1, float var2);

   @Shadow
   public abstract void calculateEntityAnimation(boolean var1);

   @Inject(
      at = {@At("HEAD")},
      method = {"travel"},
      cancellable = true
   )
   private void travel(Vec3 vec, CallbackInfo ci) {
      if (this.getThis() instanceof Player
         && (this.getThis().isEffectiveAi() || this.getThis().isControlledByLocalInstance())
         && RopeBlock.isEntityInBlock(this.getThis())
         && !this.jumping
         && !this.getThis().isCrouching()
         && !this.getThis().onGround()) {
         float f3 = this.getThis()
            .level()
            .getBlockState(BlockPos.containing(this.getThis().getX(), this.getThis().getBoundingBox().minY - 0.5000001, this.getThis().getZ()))
            .getFriction(
               this.getThis().level(),
               BlockPos.containing(this.getThis().getX(), this.getThis().getBoundingBox().minY - 0.5000001, this.getThis().getZ()),
               this.getThis()
            );
         float f4 = this.getThis().onGround() ? f3 * 0.91F : 0.91F;
         Vec3 vector3d5 = this.handleRelativeFrictionAndCalculateMovement(vec, f3);
         this.getThis().setDeltaMovement(vector3d5.x * (double)f4, 0.0, vector3d5.z * (double)f4);
         this.calculateEntityAnimation(this.getThis() instanceof FlyingAnimal);
         ci.cancel();
      }
   }

   private LivingEntity getThis() {
      return (LivingEntity)(Object)this;
   }
}
