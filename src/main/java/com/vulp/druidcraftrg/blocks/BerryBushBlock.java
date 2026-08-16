package com.vulp.druidcraftrg.blocks;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BerryBushBlock extends SweetBerryBushBlock {
   private final Supplier<Item> item;
   private final boolean thorns;

   public BerryBushBlock(Supplier<Item> berryItem, boolean hasThorns, Properties properties) {
      super(properties);
      this.item = berryItem;
      this.thorns = hasThorns;
   }

   @Override
   public ItemStack getCloneItemStack(net.minecraft.world.level.LevelReader level, BlockPos pos, BlockState state) {
      return new ItemStack(this.item.get());
   }

   @Override
   public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
      int i = state.getValue(AGE);
      boolean flag = i == 3;
      if (!flag && player.getMainHandItem().is(Items.BONE_MEAL)) {
         return InteractionResult.PASS;
      } else if (i > 1) {
         int j = 1 + level.random.nextInt(2);
         popResource(level, pos, new ItemStack(this.item.get(), j + (flag ? 1 : 0)));
         level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
         level.setBlock(pos, state.setValue(AGE, 1), 2);
         return InteractionResult.sidedSuccess(level.isClientSide);
      } else {
         return super.useWithoutItem(state, level, pos, player, hit);
      }
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX) {
         entity.makeStuckInBlock(state, new Vec3(0.8D, 0.75D, 0.8D));
         if (!level.isClientSide && state.getValue(AGE) > 0 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
            double d0 = Math.abs(entity.getX() - entity.xOld);
            double d1 = Math.abs(entity.getZ() - entity.zOld);
            if ((d0 >= 0.003D || d1 >= 0.003D) && this.thorns) {
               entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
            }
         }
      }
   }
}
