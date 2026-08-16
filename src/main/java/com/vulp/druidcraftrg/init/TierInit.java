package com.vulp.druidcraftrg.init;

import java.util.function.Supplier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

// 1.16.5 ToolMaterialRegistry (fiery tier omitted with the rest of the disabled fiery set)
public enum TierInit implements Tier {
   bone(1.0F, 5.0F, 250, BlockTags.INCORRECT_FOR_STONE_TOOL, 18, () -> Items.BONE),
   moonstone(4.0F, 9.0F, 1921, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 15, () -> ItemInit.moonstone);

   private final float attackDamage;
   private final float speed;
   private final int durability;
   private final TagKey<Block> incorrectBlocksForDrops;
   private final int enchantability;
   private final Supplier<Ingredient> repairIngredient;

   TierInit(float attackDamage, float speed, int durability, TagKey<Block> incorrectBlocksForDrops, int enchantability, Supplier<Item> repairMaterial) {
      this.attackDamage = attackDamage;
      this.speed = speed;
      this.durability = durability;
      this.incorrectBlocksForDrops = incorrectBlocksForDrops;
      this.enchantability = enchantability;
      this.repairIngredient = () -> Ingredient.of(repairMaterial.get());
   }

   @Override
   public int getUses() {
      return this.durability;
   }

   @Override
   public float getSpeed() {
      return this.speed;
   }

   @Override
   public float getAttackDamageBonus() {
      return this.attackDamage;
   }

   @Override
   public TagKey<Block> getIncorrectBlocksForDrops() {
      return this.incorrectBlocksForDrops;
   }

   @Override
   public int getEnchantmentValue() {
      return this.enchantability;
   }

   @Override
   public Ingredient getRepairIngredient() {
      return this.repairIngredient.get();
   }
}
