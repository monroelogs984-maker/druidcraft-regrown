package com.vulp.druidcraftrg.entities;

import com.vulp.druidcraftrg.entities.ai.NonTamedTargetGoalMonster;
import com.vulp.druidcraftrg.entities.ai.OwnerHurtByTargetGoalMonster;
import com.vulp.druidcraftrg.entities.ai.OwnerHurtTargetGoalMonster;
import com.vulp.druidcraftrg.events.EventFactory;
import com.vulp.druidcraftrg.init.ItemInit;
import com.vulp.druidcraftrg.inventory.container.BeetleInventoryContainer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;

public class BeetleEntity extends TameableMonsterEntity implements ContainerListener, MenuProvider {
   private static final EntityDataAccessor<Boolean> SADDLE = SynchedEntityData.defineId(BeetleEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> CHEST = SynchedEntityData.defineId(BeetleEntity.class, EntityDataSerializers.BOOLEAN);
   private SimpleContainer beetleChest;
   private LazyOptional<?> itemHandler = null;

   public BeetleEntity(EntityType<? extends BeetleEntity> type, Level worldIn) {
      super(type, worldIn);
      this.xpReward = 10;
      this.initBeetleChest();
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(SADDLE, false);
      this.entityData.define(CHEST, false);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.2F));
      this.goalSelector.addGoal(4, new AttackGoal(this));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new OwnerHurtByTargetGoalMonster(this));
      this.targetSelector.addGoal(2, new OwnerHurtTargetGoalMonster(this));
      this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(4, new NonTamedTargetGoalMonster<>(this, Player.class, false, null));
      this.targetSelector.addGoal(5, new NonTamedTargetGoalMonster<>(this, IronGolem.class, false));
   }

   public static AttributeSupplier.Builder bakeAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.ARMOR, 20.0D)
         .add(Attributes.MAX_HEALTH, 40.0D)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
         .add(Attributes.MOVEMENT_SPEED, 0.15D)
         .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
         .add(Attributes.ATTACK_DAMAGE, 3.0D);
   }

   public static boolean placement(EntityType<? extends Mob> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
      if (worldIn.getMaxLocalRawBrightness(pos) >= 8 && randomIn.nextBoolean()) {
         return false;
      }
      return worldIn.getDifficulty() != Difficulty.PEACEFUL && worldIn.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK);
   }

   public boolean hasSaddle() {
      return this.entityData.get(SADDLE);
   }

   private void setSaddled(boolean saddled) {
      this.entityData.set(SADDLE, saddled);
   }

   public boolean hasChest() {
      return this.entityData.get(CHEST);
   }

   private void setChested(boolean chested) {
      this.entityData.set(CHEST, chested);
   }

   @Override
   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return 0.8F;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("saddled", this.hasSaddle());
      compound.putBoolean("chested", this.hasChest());
      if (this.hasChest()) {
         ListTag listnbt = new ListTag();
         for (int i = 2; i < this.beetleChest.getContainerSize(); ++i) {
            ItemStack itemstack = this.beetleChest.getItem(i);
            if (!itemstack.isEmpty()) {
               CompoundTag compoundnbt = new CompoundTag();
               compoundnbt.putByte("Slot", (byte) i);
               itemstack.save(compoundnbt);
               listnbt.add(compoundnbt);
            }
         }
         compound.put("Items", listnbt);
      }
      if (!this.beetleChest.getItem(0).isEmpty()) {
         compound.put("SaddleItem", this.beetleChest.getItem(0).save(new CompoundTag()));
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setSaddled(compound.getBoolean("saddled"));
      this.setChested(compound.getBoolean("chested"));
      if (this.hasChest()) {
         ListTag listnbt = compound.getList("Items", 10);
         this.initBeetleChest();
         for (int i = 0; i < listnbt.size(); ++i) {
            CompoundTag compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getByte("Slot") & 255;
            if (j >= 2 && j < this.beetleChest.getContainerSize()) {
               this.beetleChest.setItem(j, ItemStack.of(compoundnbt));
            }
         }
      }
      if (compound.contains("SaddleItem", 10)) {
         ItemStack itemstack = ItemStack.of(compound.getCompound("SaddleItem"));
         if (itemstack.getItem() == Items.SADDLE) {
            this.beetleChest.setItem(0, itemstack);
         }
      }
      this.updateBeetleSlots();
   }

   @Override
   public MobType getMobType() {
      return MobType.ARTHROPOD;
   }

   private int getInventorySize() {
      return hasChest() ? 65 : 1;
   }

   private boolean canBeSaddled() {
      return true;
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

   private void updateBeetleSlots() {
      if (!this.level().isClientSide) {
         this.setSaddled(!this.beetleChest.getItem(0).isEmpty() && this.canBeSaddled());
      }
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      Item item = itemstack.getItem();
      if (this.isTamed() && player.isShiftKeyDown()) {
         this.openGUI(player);
         return InteractionResult.sidedSuccess(this.level().isClientSide);
      }
      if (!itemstack.isEmpty()) {
         if (item == Items.APPLE && this.getHealth() < this.getMaxHealth()) {
            if (!player.getAbilities().instabuild) {
               itemstack.shrink(1);
            }
            this.heal(8.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
         }
         if (!this.isTamed() || item == Items.NAME_TAG) {
            if (item == Items.GOLDEN_APPLE) {
               if (!player.getAbilities().instabuild) {
                  itemstack.shrink(1);
               }
               if (!this.level().isClientSide) {
                  if (this.random.nextBoolean() && !EventFactory.onMonsterTame(this, player)) {
                     this.playTameEffect(true);
                     this.setTamedBy(player);
                     this.getNavigation().stop();
                     this.setTarget(null);
                     this.setHealth(32.0F);
                     this.level().broadcastEntityEvent(this, (byte) 7);
                  } else {
                     this.playTameEffect(false);
                     this.level().broadcastEntityEvent(this, (byte) 6);
                  }
               }
               return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            InteractionResult result = itemstack.interactLivingEntity(player, this, hand);
            if (result.consumesAction()) {
               return result;
            }
         } else if (this.isTamed()) {
            if (!this.level().isClientSide) {
               if (!this.hasChest() && item == Items.CHEST) {
                  this.setChested(true);
                  this.playChestEquipSound();
                  this.initBeetleChest();
                  if (!player.isCreative()) {
                     itemstack.shrink(1);
                  }
                  return InteractionResult.sidedSuccess(this.level().isClientSide);
               }
               if (!this.hasSaddle() && item == Items.SADDLE) {
                  this.openGUI(player);
                  return InteractionResult.sidedSuccess(this.level().isClientSide);
               }
            }
         }
      } else {
         if (!this.level().isClientSide && this.hasSaddle() && !this.isVehicle()) {
            this.mountTo(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
         }
      }
      return InteractionResult.PASS;
   }

   private void mountTo(Player player) {
      if (!this.level().isClientSide) {
         player.setYRot(this.getYRot());
         player.setXRot(this.getXRot());
         player.startRiding(this);
      }
   }

   @Nullable
   @Override
   public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
      return new BeetleInventoryContainer(i, playerInventory, beetleChest, this.getId());
   }

   private void openGUI(Player playerEntity) {
      if (!this.level().isClientSide && (!this.isVehicle() || this.hasPassenger(playerEntity)) && this.isTamed()
            && playerEntity instanceof ServerPlayer serverPlayer) {
         NetworkHooks.openScreen(serverPlayer, this, packetBuffer -> {
            packetBuffer.writeInt(this.getId());
            packetBuffer.writeInt(getInventorySize());
         });
      }
   }

   private void initBeetleChest() {
      SimpleContainer inventory = this.beetleChest;
      this.beetleChest = new SimpleContainer(this.getInventorySize());
      if (inventory != null) {
         inventory.removeListener(this);
         int i = Math.min(inventory.getContainerSize(), this.beetleChest.getContainerSize());
         for (int j = 0; j < i; ++j) {
            ItemStack itemstack = inventory.getItem(j);
            if (!itemstack.isEmpty()) {
               this.beetleChest.setItem(j, itemstack.copy());
            }
         }
      }
      this.beetleChest.addListener(this);
      this.updateBeetleSlots();
      this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.beetleChest));
   }

   private void playChestEquipSound() {
      this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
   }

   @Override
   protected void dropEquipment() {
      super.dropEquipment();
      if (!this.level().isClientSide) {
         if (isTamed()) {
            if (this.hasChest()) {
               this.spawnAtLocation(Blocks.CHEST);
               this.setChested(false);
               for (int i = 0; i < this.beetleChest.getContainerSize(); ++i) {
                  ItemStack itemstack = this.beetleChest.getItem(i);
                  if (!itemstack.isEmpty()) {
                     this.spawnAtLocation(itemstack);
                  }
               }
            }
            if (this.hasSaddle()) {
               this.spawnAtLocation(Items.SADDLE);
               this.setSaddled(false);
            }
         } else {
            int j = this.random.nextInt(2);
            for (int k = 0; k <= j; ++k) {
               this.spawnAtLocation(ItemInit.chitin);
            }
         }
      }
   }

   @Override
   public void containerChanged(Container invBasic) {
      boolean flag = this.hasSaddle();
      this.updateBeetleSlots();
      if (this.tickCount > 20 && !flag && this.hasSaddle()) {
         this.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
      }
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable net.minecraft.core.Direction facing) {
      if (this.isAlive() && capability == ForgeCapabilities.ITEM_HANDLER && itemHandler != null) {
         return itemHandler.cast();
      }
      return super.getCapability(capability, facing);
   }

   @Override
   public double getPassengersRidingOffset() {
      return this.getBbHeight() - 0.15D;
   }

   @Nullable
   @Override
   public LivingEntity getControllingPassenger() {
      if (this.hasSaddle() && this.getFirstPassenger() instanceof LivingEntity living) {
         return living;
      }
      return null;
   }

   @Override
   protected void tickRidden(Player player, Vec3 input) {
      super.tickRidden(player, input);
      this.setRot(player.getYRot(), player.getXRot() * 0.5F);
      this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
      this.setMaxUpStep(1.0F);
   }

   @Override
   protected Vec3 getRiddenInput(Player player, Vec3 input) {
      return new Vec3(player.xxa * 0.5F, 0.0D, player.zza);
   }

   @Override
   protected float getRiddenSpeed(Player player) {
      return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
   }

   static class AttackGoal extends MeleeAttackGoal {
      public AttackGoal(BeetleEntity beetle) {
         super(beetle, 0.85D, true);
      }

      @Override
      protected double getAttackReachSqr(LivingEntity attackTarget) {
         return 4.0F + attackTarget.getBbWidth();
      }
   }
}
