package com.vulp.druidcraftrg.entities;

import com.vulp.druidcraftrg.entities.ai.FollowOwnerGoalMonster;
import com.vulp.druidcraftrg.entities.ai.NonTamedTargetGoalMonster;
import com.vulp.druidcraftrg.entities.ai.OwnerHurtByTargetGoalMonster;
import com.vulp.druidcraftrg.entities.ai.OwnerHurtTargetGoalMonster;
import com.vulp.druidcraftrg.entities.ai.SitGoalMonster;
import com.vulp.druidcraftrg.events.EventFactory;
import com.vulp.druidcraftrg.init.ParticleInit;
import com.vulp.druidcraftrg.pathfinding.ImprovedFlyingPathNavigator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;

public class DreadfishEntity extends TameableMonsterEntity implements FlyingAnimal {
   private static final Predicate<LivingEntity> isPlayer = entity -> entity.getType() == EntityType.PLAYER;
   private static final EntityDataAccessor<Integer> SMOKE_COLOR = SynchedEntityData.defineId(DreadfishEntity.class, EntityDataSerializers.INT);
   private static final Map<DyeColor, int[]> DYE_COLOR_MAP = new HashMap<>();
   private DyeColor smokeColor = null;

   public DreadfishEntity(EntityType<? extends TameableMonsterEntity> type, Level worldIn) {
      super(type, worldIn);
      this.moveControl = new FlyingMoveControl(this, 10, false);
      this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
      this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
      this.setPathfindingMalus(PathType.WATER, -1.0F);
      this.setPathfindingMalus(PathType.WATER_BORDER, -1.0F);
      this.setPathfindingMalus(PathType.COCOA, -1.0F);
      this.setPathfindingMalus(PathType.FENCE, -1.0F);
      this.setTamed(false);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.sitGoal = new SitGoalMonster(this);
      this.goalSelector.addGoal(1, this.sitGoal);
      this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 3.0D, true));
      this.goalSelector.addGoal(3, new FollowOwnerGoalMonster(this, 2.0D, 5.0F, 1.0F));
      this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
      this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));

      this.targetSelector.addGoal(1, new OwnerHurtByTargetGoalMonster(this));
      this.targetSelector.addGoal(2, new OwnerHurtTargetGoalMonster(this));
      this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(4, new NonTamedTargetGoalMonster<>(this, Player.class, false, isPlayer));
      this.targetSelector.addGoal(5, new NonTamedTargetGoalMonster<>(this, IronGolem.class, false));
   }

   public static AttributeSupplier.Builder bakeAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.FLYING_SPEED, 0.4F)
         .add(Attributes.MOVEMENT_SPEED, 0.2F)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.975D)
         .add(Attributes.ATTACK_DAMAGE, 4.0D);
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(SMOKE_COLOR, DyeColor.PURPLE.getId());
   }

   @Override
   public boolean isNoGravity() {
      return true;
   }

   @Override
   public boolean isFlying() {
      return !this.onGround();
   }

   @Override
   protected void playStepSound(BlockPos pos, BlockState blockIn) {
   }

   public static boolean placement(EntityType<? extends Mob> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
      BlockState below = worldIn.getBlockState(pos.below());
      return worldIn.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(worldIn, pos, randomIn)
         && (below.isValidSpawn(worldIn, pos.below(), type) || below.isValidSpawn(worldIn, pos.below(2), type));
   }

   @Override
   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   private static boolean isValidLightLevel(ServerLevelAccessor world, BlockPos pos, RandomSource rand) {
      if (world.getBrightness(LightLayer.SKY, pos) > rand.nextInt(32)) {
         return false;
      }
      int i = world.getLevel().isThundering() ? world.getMaxLocalRawBrightness(pos, 10) : world.getMaxLocalRawBrightness(pos);
      return i <= rand.nextInt(8);
   }

   @Override
   protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (this.isInvulnerableTo(source)) {
         return false;
      }
      Entity entity = source.getEntity();
      if (this.sitGoal != null) {
         this.sitGoal.setSitting(false);
      }
      if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
         amount = (amount + 1.0F) / 2.0F;
      }
      return super.hurt(source, amount);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Hostile", this.isHostile());
      compound.putByte("SmokeColor", (byte) this.getSmokeColor().getId());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setHostile(compound.getBoolean("Hostile"));
      if (compound.contains("SmokeColor", 99)) {
         this.setSmokeColor(DyeColor.byId(compound.getInt("SmokeColor")));
      }
   }

   @Override
   protected PathNavigation createNavigation(Level worldIn) {
      ImprovedFlyingPathNavigator flyingpathnavigator = new ImprovedFlyingPathNavigator(this, worldIn);
      flyingpathnavigator.setCanOpenDoors(false);
      flyingpathnavigator.setCanFloat(false);
      flyingpathnavigator.setCanPassDoors(true);
      return flyingpathnavigator;
   }

   @Override
   public void setTamed(boolean tamed) {
      super.setTamed(tamed);
      if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tamed ? 24.0D : 16.0D);
      }
   }

   @Override
   public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
      super.setTarget(entitylivingbaseIn);
      if (entitylivingbaseIn == null) {
         this.setHostile(false);
      } else if (!this.isTamed()) {
         this.setHostile(true);
      }
   }

   public int[] getSmokeColorArray() {
      return DYE_COLOR_MAP.getOrDefault(getSmokeColor(), new int[]{0, 0, 0});
   }

   public DyeColor getSmokeColor() {
      if (smokeColor == null) {
         smokeColor = DyeColor.byId(this.entityData.get(SMOKE_COLOR));
      }
      return smokeColor;
   }

   public void setSmokeColor(DyeColor smokeColor) {
      this.entityData.set(SMOKE_COLOR, smokeColor.getId());
      this.smokeColor = smokeColor;
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      Item item = itemstack.getItem();
      if (this.isTamed()) {
         if (!itemstack.isEmpty()) {
            if (item == Items.BONE && this.getHealth() < this.getMaxHealth()) {
               if (!player.getAbilities().instabuild) {
                  itemstack.shrink(1);
               }
               this.heal(4.0F);
               return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else if (item instanceof DyeItem dyeItem) {
               DyeColor dyecolor = dyeItem.getDyeColor();
               if (dyecolor != this.getSmokeColor()) {
                  this.setSmokeColor(dyecolor);
                  if (!player.getAbilities().instabuild) {
                     itemstack.shrink(1);
                  }
                  return InteractionResult.sidedSuccess(this.level().isClientSide);
               }
            }
         }
         if (this.isOwner(player) && !this.level().isClientSide) {
            this.sitGoal.setSitting(!this.isSitting());
            this.getNavigation().stop();
            this.setTarget(null);
         }
      } else if (item == Items.PRISMARINE_CRYSTALS) {
         if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
         }
         if (!this.level().isClientSide) {
            if (this.random.nextInt(3) == 0 && !EventFactory.onMonsterTame(this, player)) {
               this.playTameEffect(true);
               this.setTamedBy(player);
               this.getNavigation().stop();
               this.setTarget(null);
               this.sitGoal.setSitting(true);
               this.setHealth(24.0F);
               this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
               this.playTameEffect(false);
               this.level().broadcastEntityEvent(this, (byte) 6);
            }
         }
         return InteractionResult.sidedSuccess(this.level().isClientSide);
      }
      return InteractionResult.PASS;
   }

   @Override
   public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
      if (target instanceof Creeper) {
         return false;
      }
      if (target instanceof TameableMonsterEntity monsterEntity && monsterEntity.isTamed() && monsterEntity.getOwner() == this.getOwner()) {
         return false;
      }
      if (target instanceof TamableAnimal tameableEntity && tameableEntity.isTame() && tameableEntity.getOwner() == this.getOwner()) {
         return false;
      }
      if (target instanceof Player targetPlayer && owner instanceof Player ownerPlayer && !ownerPlayer.canHarmPlayer(targetPlayer)) {
         return false;
      } else if (target instanceof AbstractHorse horse && horse.isTamed()) {
         return false;
      } else {
         return !(target instanceof Cat cat) || !cat.isTame();
      }
   }

   @Override
   public boolean canBeLeashed() {
      return !this.isHostile() && !this.isTamed();
   }

   @Override
   public boolean isPushable() {
      return true;
   }

   @Override
   protected void doPush(Entity entityIn) {
      if (!(entityIn instanceof Player) && !(entityIn instanceof DreadfishEntity)) {
         super.doPush(entityIn);
      }
   }

   public boolean isHostile() {
      return (this.entityData.get(TAMED) & 2) != 0;
   }

   public void setHostile(boolean hostile) {
      byte b0 = this.entityData.get(TAMED);
      if (hostile) {
         this.entityData.set(TAMED, (byte) (b0 | 2));
      } else {
         this.entityData.set(TAMED, (byte) (b0 & -3));
      }
   }

   @Override
   public boolean burnsInDaylight() {
      return !isTamed();
   }

   public void particle() {
      int[] color = getSmokeColorArray();
      this.level().addParticle(ParticleInit.magic_smoke, false,
         this.getX(),
         this.getY() + (((random.nextDouble() - 0.5) + 0.25) / 4) + 0.1F,
         this.getZ() + (((random.nextDouble() - 0.5) + 0.2) / 3),
         color[0] / 255.0F, color[1] / 255.0F, color[2] / 255.0F);
   }

   @Override
   public void aiStep() {
      if (this.level().isClientSide) {
         int swing = (int) (this.walkAnimation.position() * 10);
         if (swing > 0 && random.nextInt(swing) != 0) {
            if (random.nextInt(3) == 0) {
               particle();
            }
         } else if (random.nextInt(8) == 0) {
            particle();
         }
      }
      if (!this.level().isClientSide && this.getTarget() == null && this.isHostile()) {
         this.setHostile(false);
      }
      super.aiStep();
   }

   @Override
   public boolean onClimbable() {
      return false;
   }

   static {
      DYE_COLOR_MAP.put(DyeColor.BLACK, new int[]{15, 15, 15});
      DYE_COLOR_MAP.put(DyeColor.RED, new int[]{255, 20, 10});
      DYE_COLOR_MAP.put(DyeColor.GREEN, new int[]{18, 150, 13});
      DYE_COLOR_MAP.put(DyeColor.BROWN, new int[]{90, 45, 5});
      DYE_COLOR_MAP.put(DyeColor.BLUE, new int[]{15, 20, 255});
      DYE_COLOR_MAP.put(DyeColor.PURPLE, new int[]{140, 15, 220});
      DYE_COLOR_MAP.put(DyeColor.CYAN, new int[]{15, 150, 130});
      DYE_COLOR_MAP.put(DyeColor.LIGHT_GRAY, new int[]{125, 125, 125});
      DYE_COLOR_MAP.put(DyeColor.GRAY, new int[]{60, 60, 60});
      DYE_COLOR_MAP.put(DyeColor.PINK, new int[]{255, 115, 170});
      DYE_COLOR_MAP.put(DyeColor.LIME, new int[]{130, 255, 20});
      DYE_COLOR_MAP.put(DyeColor.YELLOW, new int[]{255, 230, 2});
      DYE_COLOR_MAP.put(DyeColor.LIGHT_BLUE, new int[]{50, 200, 255});
      DYE_COLOR_MAP.put(DyeColor.MAGENTA, new int[]{205, 60, 155});
      DYE_COLOR_MAP.put(DyeColor.ORANGE, new int[]{250, 135, 5});
      DYE_COLOR_MAP.put(DyeColor.WHITE, new int[]{215, 215, 215});
   }
}
