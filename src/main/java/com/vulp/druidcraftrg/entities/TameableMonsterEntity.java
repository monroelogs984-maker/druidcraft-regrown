package com.vulp.druidcraftrg.entities;

import com.vulp.druidcraftrg.entities.ai.SitGoalMonster;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;

public class TameableMonsterEntity extends PathfinderMob {
   static final EntityDataAccessor<Byte> TAMED = SynchedEntityData.defineId(TameableMonsterEntity.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(TameableMonsterEntity.class, EntityDataSerializers.OPTIONAL_UUID);
   protected SitGoalMonster sitGoal;

   protected TameableMonsterEntity(EntityType<? extends TameableMonsterEntity> type, Level level) {
      super(type, level);
      this.xpReward = 5;
   }

   @Override
   public PathNavigation getNavigation() {
      if (this.isPassenger() && this.getVehicle() instanceof TameableMonsterEntity mob) {
         return mob.getNavigation();
      }
      return this.navigation;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(TAMED, (byte) 0);
      this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
   }

   public static AttributeSupplier.Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      if (this.getOwnerId() != null) {
         compound.putUUID("Owner", this.getOwnerId());
      }
      compound.putBoolean("Sitting", this.isSitting());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      UUID uuid;
      if (compound.hasUUID("Owner")) {
         uuid = compound.getUUID("Owner");
      } else {
         String s = compound.getString("Owner");
         uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
      }

      if (uuid != null) {
         try {
            this.setOwnerId(uuid);
            this.setTamed(true);
         } catch (Throwable throwable) {
            this.setTamed(false);
         }
      }

      if (this.sitGoal != null) {
         this.sitGoal.setSitting(compound.getBoolean("Sitting"));
      }
      this.setSitting(compound.getBoolean("Sitting"));
   }

   @Override
   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return !this.isTamed() && !this.hasCustomName();
   }

   @Override
   public boolean requiresCustomPersistence() {
      return this.isTamed() || this.hasCustomName();
   }

   @Override
   public boolean canBeLeashed(Player player) {
      return !this.isLeashed();
   }

   public boolean preventPlayerSleep(Player player) {
      return !this.isTamed();
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level().isClientSide && this.level().getDifficulty() == Difficulty.PEACEFUL && !this.isTamed()) {
         this.discard();
      }
   }

   public boolean burnsInDaylight() {
      return false;
   }

   @Override
   public void aiStep() {
      if (this.isSunBurnTick() && this.burnsInDaylight()) {
         this.setSecondsOnFire(8);
      }
      this.updateSwingTime();
      super.aiStep();
   }

   // hearts or smoke depending on taming success
   protected void playTameEffect(boolean play) {
      ParticleOptions particle = play ? ParticleTypes.HEART : ParticleTypes.SMOKE;
      for (int i = 0; i < 7; ++i) {
         double d0 = this.random.nextGaussian() * 0.02D;
         double d1 = this.random.nextGaussian() * 0.02D;
         double d2 = this.random.nextGaussian() * 0.02D;
         this.level().addParticle(particle,
            this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
            this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getBbHeight()),
            this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(),
            d0, d1, d2);
      }
   }

   @Override
   public void handleEntityEvent(byte id) {
      if (id == 7) {
         this.playTameEffect(true);
      } else if (id == 6) {
         this.playTameEffect(false);
      } else {
         super.handleEntityEvent(id);
      }
   }

   public boolean isTamed() {
      return (this.entityData.get(TAMED) & 4) != 0;
   }

   public void setTamed(boolean tamed) {
      byte b0 = this.entityData.get(TAMED);
      if (tamed) {
         this.entityData.set(TAMED, (byte) (b0 | 4));
      } else {
         this.entityData.set(TAMED, (byte) (b0 & -5));
      }
   }

   public boolean isSitting() {
      return (this.entityData.get(TAMED) & 1) != 0;
   }

   public void setSitting(boolean sitting) {
      byte b0 = this.entityData.get(TAMED);
      if (sitting) {
         this.entityData.set(TAMED, (byte) (b0 | 1));
      } else {
         this.entityData.set(TAMED, (byte) (b0 & -2));
      }
   }

   @Nullable
   public UUID getOwnerId() {
      return this.entityData.get(OWNER_UNIQUE_ID).orElse(null);
   }

   public void setOwnerId(@Nullable UUID uuid) {
      this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
   }

   public void setTamedBy(Player player) {
      this.setTamed(true);
      this.setOwnerId(player.getUUID());
   }

   @Nullable
   public LivingEntity getOwner() {
      try {
         UUID uuid = this.getOwnerId();
         return uuid == null ? null : this.level().getPlayerByUUID(uuid);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   @Override
   public boolean canAttack(LivingEntity target) {
      return !this.isOwner(target) && super.canAttack(target);
   }

   public boolean isOwner(LivingEntity entityIn) {
      return entityIn == this.getOwner();
   }

   public SitGoalMonster getAISit() {
      return this.sitGoal;
   }

   public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
      return true;
   }

   @Override
   public Team getTeam() {
      if (this.isTamed()) {
         LivingEntity livingentity = this.getOwner();
         if (livingentity != null) {
            return livingentity.getTeam();
         }
      }
      return super.getTeam();
   }

   @Override
   public boolean isAlliedTo(Entity entityIn) {
      if (this.isTamed()) {
         LivingEntity livingentity = this.getOwner();
         if (entityIn == livingentity) {
            return true;
         }
         if (livingentity != null) {
            return livingentity.isAlliedTo(entityIn);
         }
      }
      return super.isAlliedTo(entityIn);
   }

   @Override
   public void die(DamageSource cause) {
      if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)
            && this.getOwner() instanceof ServerPlayer) {
         this.getOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
      }
      super.die(cause);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      return !this.isInvulnerableTo(source) && super.hurt(source, amount);
   }

   @Override
   protected boolean shouldDropLoot() {
      return true;
   }

   @Override
   public SoundSource getSoundSource() {
      return isTamed() ? SoundSource.NEUTRAL : SoundSource.HOSTILE;
   }

   @Override
   protected SoundEvent getSwimSound() {
      return isTamed() ? SoundEvents.GENERIC_SWIM : SoundEvents.HOSTILE_SWIM;
   }

   @Override
   protected SoundEvent getSwimSplashSound() {
      return isTamed() ? SoundEvents.GENERIC_SPLASH : SoundEvents.HOSTILE_SPLASH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return isTamed() ? SoundEvents.GENERIC_HURT : SoundEvents.HOSTILE_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return isTamed() ? SoundEvents.GENERIC_DEATH : SoundEvents.HOSTILE_DEATH;
   }

   @Override
   public LivingEntity.Fallsounds getFallSounds() {
      return isTamed()
         ? new LivingEntity.Fallsounds(SoundEvents.GENERIC_SMALL_FALL, SoundEvents.GENERIC_BIG_FALL)
         : new LivingEntity.Fallsounds(SoundEvents.HOSTILE_SMALL_FALL, SoundEvents.HOSTILE_BIG_FALL);
   }
}
