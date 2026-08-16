package com.vulp.druidcraftrg.init;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

// 1.16.5 ArmorMaterialRegistry (fiery omitted with the disabled fiery set)
public enum ArmorMaterialInit implements ArmorMaterial {
   bone("bone", 15, new int[]{1, 4, 5, 2}, 18, () -> Items.BONE, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F),
   chitin("chitin", 24, new int[]{3, 5, 7, 3}, 12, () -> ItemInit.chitin, SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 0.0F),
   moonstone("moonstone", 40, new int[]{3, 7, 9, 4}, 15, () -> ItemInit.moonstone, SoundEvents.ARMOR_EQUIP_DIAMOND, 3.0F, 0.0F);

   // indexed by EquipmentSlot index: feet, legs, chest, head
   private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
   private final String name;
   private final int durability;
   private final int[] damageReductionAmount;
   private final int enchantability;
   private final SoundEvent equipSound;
   private final float toughness;
   private final float knockbackResistance;
   private final LazyLoadedValue<Ingredient> repairIngredient;

   ArmorMaterialInit(String name, int durability, int[] damageReductionAmount, int enchantability, Supplier<Item> repairItem, SoundEvent equipSound, float toughness, float knockbackResistance) {
      this.name = name;
      this.durability = durability;
      this.damageReductionAmount = damageReductionAmount;
      this.enchantability = enchantability;
      this.equipSound = equipSound;
      this.toughness = toughness;
      this.knockbackResistance = knockbackResistance;
      this.repairIngredient = new LazyLoadedValue<>(() -> Ingredient.of(repairItem.get()));
   }

   @Override
   public int getDurabilityForType(ArmorItem.Type type) {
      return MAX_DAMAGE_ARRAY[type.getSlot().getIndex()] * this.durability;
   }

   @Override
   public int getDefenseForType(ArmorItem.Type type) {
      return this.damageReductionAmount[type.getSlot().getIndex()];
   }

   @Override
   public int getEnchantmentValue() {
      return this.enchantability;
   }

   @Override
   public SoundEvent getEquipSound() {
      return this.equipSound;
   }

   @Override
   public Ingredient getRepairIngredient() {
      return this.repairIngredient.get();
   }

   @Override
   public String getName() {
      return "druidcraftrg:" + this.name;
   }

   @Override
   public float getToughness() {
      return this.toughness;
   }

   @Override
   public float getKnockbackResistance() {
      return this.knockbackResistance;
   }
}
