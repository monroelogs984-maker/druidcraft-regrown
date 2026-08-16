package com.vulp.druidcraftrg.items;

import com.vulp.druidcraftrg.util.SickleHarvestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class SickleItem extends DiggerItem {
   // Replaces the 1.16 Material.PLANTS/OCEAN_PLANT/TALL_PLANTS check
   public static final TagKey<Block> SICKLE_EFFICIENT =
      TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), new ResourceLocation("druidcraftrg", "sickle_efficient"));

   private final int radius;

   public SickleItem(float attackDamage, float attackSpeed, Tier tier, int radius, Properties properties) {
      super(attackDamage, attackSpeed, tier, SICKLE_EFFICIENT, properties);
      this.radius = radius;
   }

   public int getRadius() {
      return this.radius;
   }

   public static boolean isEffectiveOn(BlockState state) {
      return state.is(SICKLE_EFFICIENT);
   }

   @Override
   public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
      if (entity instanceof Player player) {
         SickleHarvestUtil.breakNeighbours(stack, level, pos, player);
      }

      return super.mineBlock(stack, level, state, pos, entity);
   }
}
