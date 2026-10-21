package com.vulp.druidcraftrg.entities;

import com.vulp.druidcraftrg.init.ItemInit;
import com.vulp.druidcraftrg.init.ParticleInit;
import com.vulp.druidcraftrg.init.SoundEventInit;
import com.vulp.druidcraftrg.items.LunarMothJarItem;
import com.vulp.druidcraftrg.pathfinding.ImprovedFlyingPathNavigator;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

public class LunarMothEntity extends Animal {
   // Moths get their own spawn category (META-INF/enumextensions.json, cap 8): in CREATURE the
   // never-despawning farm animals filled the cap, and in AMBIENT cave bats did.
   public static final MobCategory SPAWN_CATEGORY = MobCategory.valueOf("DRUIDCRAFTRG_LUNAR_MOTH");
   private static final EntityDataAccessor<Boolean> RESTING = SynchedEntityData.defineId(LunarMothEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Direction> IDLING = SynchedEntityData.defineId(LunarMothEntity.class, EntityDataSerializers.DIRECTION);
   public static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(LunarMothEntity.class, EntityDataSerializers.INT);
   private static final TargetingConditions ENTITY_PREDICATE = TargetingConditions.forNonCombat().range(4.0D);
   private BlockPos spawnPosition;
   private boolean naturalSpawn;

   public LunarMothEntity(EntityType<? extends LunarMothEntity> type, Level worldIn) {
      super(type, worldIn);
      this.moveControl = new FlyingMoveControl(this, 10, false);
      this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
      this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
      this.setPathfindingMalus(PathType.WATER, -1.0F);
      this.setPathfindingMalus(PathType.WATER_BORDER, -1.0F);
      this.setPathfindingMalus(PathType.COCOA, -1.0F);
      this.setPathfindingMalus(PathType.FENCE, -1.0F);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
      this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, Ingredient.of(
         Items.TORCH, Items.REDSTONE_TORCH, Items.LANTERN, ItemInit.fiery_torch, ItemInit.ceramic_lantern, ItemInit.growth_lamp), false));
      this.goalSelector.addGoal(3, new FollowParentLunarMothGoal(this, 1.25D));
      this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
      this.goalSelector.addGoal(5, new FloatGoal(this));
   }

   public static AttributeSupplier.Builder bakeAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 6.0D)
         .add(Attributes.FLYING_SPEED, 0.6F);
   }

   @Nullable
   @Override
   public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
      return null;
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(RESTING, false);
      builder.define(IDLING, Direction.NORTH);
      builder.define(COLOR, LunarMothColors.colorToInt(LunarMothColors.randomColor(this.random)));
   }

   public boolean isResting() {
      return this.entityData.get(RESTING);
   }

   public void setResting(boolean resting) {
      this.entityData.set(RESTING, resting);
   }

   public Direction getRestingDirection() {
      return this.entityData.get(IDLING);
   }

   public void setRestingDirection(Direction direction) {
      this.entityData.set(IDLING, direction);
   }

   @Override
   public boolean isFood(ItemStack itemStack) {
      return Ingredient.of(Items.SUGAR).test(itemStack);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      Item item = itemstack.getItem();
      if (item == Items.GLASS_BOTTLE) {
         player.level().playSound(player, player.getX(), player.getY(), player.getZ(),
            SoundEventInit.fill_bottle, SoundSource.NEUTRAL, 1.0F, 1.0F);
         this.bottleToMothJar(itemstack, player);
         this.discard();
         return InteractionResult.sidedSuccess(this.level().isClientSide);
      }
      return InteractionResult.PASS;
   }

   protected void bottleToMothJar(ItemStack itemstack, Player player) {
      itemstack.shrink(1);
      ItemStack stack = LunarMothJarItem.getItemStackByColor(getColor());
      CompoundTag entityData = new CompoundTag();
      addAdditionalSaveData(entityData);
      entityData.remove("NaturalSpawn"); // a released moth is never wild again
      CompoundTag stackNbt = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      stackNbt.put("EntityTag", entityData);
      stack.set(DataComponents.CUSTOM_DATA, CustomData.of(stackNbt));
      if (!player.getInventory().add(stack)) {
         player.drop(stack, false);
      }
   }

   public LunarMothColors getColor() {
      return LunarMothColors.colorArray().get(this.entityData.get(COLOR));
   }

   public void setColor(LunarMothColors color) {
      this.entityData.set(COLOR, LunarMothColors.colorToInt(color));
   }

   @Override
   protected void customServerAiStep() {
      super.customServerAiStep();
      BlockPos blockpos = this.blockPosition();
      BlockPos blockpos1 = blockpos.above();
      if (this.isResting()) {
         if (this.level().getBlockState(blockpos1).isRedstoneConductor(this.level(), blockpos)) {
            if (this.random.nextInt(200) == 0) {
               this.yHeadRot = (float) this.random.nextInt(360);
            }
            if (this.level().getNearestPlayer(ENTITY_PREDICATE, this) != null) {
               this.setResting(false);
               this.level().levelEvent(null, 1025, blockpos, 0);
            }
         } else {
            this.setResting(false);
            this.level().levelEvent(null, 1025, blockpos, 0);
         }
      } else {
         if (this.spawnPosition != null && (!this.level().isEmptyBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
            this.spawnPosition = null;
         }
         if (this.spawnPosition == null || this.random.nextInt(30) == 0
               || this.spawnPosition.closerToCenterThan(this.position(), 2.0D)) {
            this.spawnPosition = new BlockPos(
               (int) (this.getX() + this.random.nextInt(7) - this.random.nextInt(7)),
               (int) (this.getY() + this.random.nextInt(6) - 2.0D),
               (int) (this.getZ() + this.random.nextInt(7) - this.random.nextInt(7)));
         }
         double d0 = (double) this.spawnPosition.getX() + 0.5D - this.getX();
         double d1 = (double) this.spawnPosition.getY() + 0.1D - this.getY();
         double d2 = (double) this.spawnPosition.getZ() + 0.5D - this.getZ();
         Vec3 vec3d = this.getDeltaMovement();
         Vec3 vec3d1 = vec3d.add(
            (Math.signum(d0) * 0.5D - vec3d.x) * 0.10000000149011612D,
            (Math.signum(d1) * 0.699999988079071D - vec3d.y) * 0.10000000149011612D,
            (Math.signum(d2) * 0.5D - vec3d.z) * 0.10000000149011612D);
         this.setDeltaMovement(vec3d1);
         float f = (float) (Mth.atan2(vec3d1.z, vec3d1.x) * 57.2957763671875D) - 90.0F;
         float f1 = Mth.wrapDegrees(f - this.getYRot());
         this.zza = 0.5F;
         this.setYRot(this.getYRot() + f1);
      }
   }

   @Override
   public boolean isNoGravity() {
      return true;
   }

   @Override
   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   @Override
   protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
   }

   @Override
   public boolean isIgnoringBlockTriggers() {
      return true;
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (this.isInvulnerableTo(source)) {
         return false;
      }
      if (!this.level().isClientSide && this.isResting()) {
         this.setResting(false);
      }
      return super.hurt(source, amount);
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
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.entityData.set(RESTING, compound.getBoolean("MothResting"));
      this.entityData.set(IDLING, Direction.from3DDataValue(compound.getInt("MothIdleDirection")));
      this.entityData.set(COLOR, compound.getInt("Color"));
      this.naturalSpawn = compound.getBoolean("NaturalSpawn");
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("MothResting", this.entityData.get(RESTING));
      compound.putInt("MothIdleDirection", this.entityData.get(IDLING).get3DDataValue());
      compound.putInt("Color", this.entityData.get(COLOR));
      compound.putBoolean("NaturalSpawn", this.naturalSpawn);
   }

   public static boolean placement(EntityType<LunarMothEntity> entity, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
      if (world.getBlockState(pos.below()).is(Blocks.AIR)) return false;
      // Keep natural spawns out of caves.
      return (reason != MobSpawnType.NATURAL && reason != MobSpawnType.CHUNK_GENERATION) || world.canSeeSky(pos);
   }

   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
      this.naturalSpawn = spawnType == MobSpawnType.NATURAL || spawnType == MobSpawnType.CHUNK_GENERATION;
      return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
   }

   // Only wild moths despawn, so the ambient spawner can keep replacing them. Moths from jars,
   // spawn eggs and worlds saved before this flag existed stay put.
   @Override
   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return this.naturalSpawn;
   }

   @Override
   protected void doPush(Entity entityIn) {
   }

   @Override
   protected void pushEntities() {
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.level().isClientSide) {
         int red = 1;
         int green = 1;
         int blue = 1;
         if (getColor() == LunarMothColors.TURQUOISE) {
            red = 85;
            green = 255;
            blue = 160;
         }
         if (getColor() == LunarMothColors.PINK) {
            red = 255;
            green = 200;
            blue = 240;
         }
         if (getColor() == LunarMothColors.LIME) {
            red = 180;
            green = 255;
            blue = 110;
         }
         if (getColor() == LunarMothColors.ORANGE) {
            red = 255;
            green = 200;
            blue = 80;
         }
         if (getColor() == LunarMothColors.WHITE) {
            red = 255;
            green = 250;
            blue = 240;
         }
         if (getColor() == LunarMothColors.YELLOW) {
            red = 255;
            green = 255;
            blue = 140;
         }
         float colorMod;
         if (random.nextInt(3) == 3) {
            colorMod = 1.1F;
         } else if (random.nextInt(3) == 3) {
            colorMod = 0.9F;
         } else {
            colorMod = 1.0F;
         }
         if (random.nextBoolean()) {
            this.level().addParticle(ParticleInit.magic_glitter, false,
               this.getX() + ((random.nextDouble() - 0.5) / 3),
               this.getY() + ((random.nextDouble() - 0.5) / 3) + 0.2,
               this.getZ() + ((random.nextDouble() - 0.5) / 3),
               (red / 255.0F) * colorMod, (green / 255.0F) * colorMod, (blue / 255.0F) * colorMod);
         }
      }

      if (this.isSleeping() || this.isImmobile()) {
         this.xxa = 0.0F;
         this.zza = 0.0F;
      }
   }

   // standalone rewrite of FollowParentGoal (its fields are private in 1.20.1) with the
   // 1.16.5 lunar moth tweaks: don't follow resting parents
   static class FollowParentLunarMothGoal extends Goal {
      private final LunarMothEntity moth;
      @Nullable
      private LunarMothEntity parent;
      private final double speedModifier;
      private int timeToRecalcPath;

      FollowParentLunarMothGoal(LunarMothEntity moth, double speedModifier) {
         this.moth = moth;
         this.speedModifier = speedModifier;
         this.setFlags(EnumSet.of(Goal.Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         if (this.moth.getAge() >= 0) {
            return false;
         }
         List<? extends LunarMothEntity> nearby = this.moth.level().getEntitiesOfClass(
            this.moth.getClass(), this.moth.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
         LunarMothEntity closest = null;
         double closestDist = Double.MAX_VALUE;
         for (LunarMothEntity candidate : nearby) {
            if (candidate.getAge() >= 0) {
               double dist = this.moth.distanceToSqr(candidate);
               if (dist <= closestDist) {
                  closestDist = dist;
                  closest = candidate;
               }
            }
         }
         if (closest == null || closestDist < 9.0D || closest.isResting()) {
            return false;
         }
         this.parent = closest;
         return true;
      }

      @Override
      public boolean canContinueToUse() {
         if (this.moth.getAge() >= 0) {
            return false;
         }
         if (this.parent == null || !this.parent.isAlive() || this.parent.isResting()) {
            return false;
         }
         double dist = this.moth.distanceToSqr(this.parent);
         return dist >= 9.0D && dist <= 256.0D;
      }

      @Override
      public void start() {
         this.timeToRecalcPath = 0;
      }

      @Override
      public void stop() {
         this.parent = null;
      }

      @Override
      public void tick() {
         if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.moth.getNavigation().moveTo(this.parent, this.speedModifier);
         }
      }
   }
}
