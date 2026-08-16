package com.vulp.druidcraftrg.init;

import java.util.function.Supplier;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

// 1.16.5 ToolMaterialRegistry (fiery tier omitted with the rest of the disabled fiery set)
public enum TierInit implements Tier {
   bone(1.0F, 5.0F, 250, 1, 18, () -> Items.BONE),
   moonstone(4.0F, 9.0F, 1921, 4, 15, () -> ItemInit.moonstone);

   private final float attackDamage;
   private final float speed;
   private final int durability;
   private final int level;
   private final int enchantability;
   private final LazyLoadedValue<Ingredient> repairIngredient;

   TierInit(float attackDamage, float speed, int durability, int level, int enchantability, Supplier<Item> repairMaterial) {
      this.attackDamage = attackDamage;
      this.speed = speed;
      this.durability = durability;
      this.level = level;
      this.enchantability = enchantability;
      this.repairIngredient = new LazyLoadedValue<>(() -> Ingredient.of(repairMaterial.get()));
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
   public int getLevel() {
      return this.level;
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
