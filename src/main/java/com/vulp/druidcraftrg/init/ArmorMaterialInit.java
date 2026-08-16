package com.vulp.druidcraftrg.init;

import java.util.EnumMap;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArmorMaterialInit {
   public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
      DeferredRegister.create(Registries.ARMOR_MATERIAL, "druidcraftrg");

   public static final DeferredHolder<ArmorMaterial, ArmorMaterial> bone = ARMOR_MATERIALS.register("bone",
      () -> new ArmorMaterial(
         Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
            m.put(ArmorItem.Type.BOOTS,      1);
            m.put(ArmorItem.Type.LEGGINGS,   4);
            m.put(ArmorItem.Type.CHESTPLATE, 5);
            m.put(ArmorItem.Type.HELMET,     2);
         }),
         18,
         SoundEvents.ARMOR_EQUIP_GOLD,
         () -> Ingredient.of(Items.BONE),
         List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath("druidcraftrg", "bone"))),
         0.0F, 0.0F
      ));

   public static final DeferredHolder<ArmorMaterial, ArmorMaterial> chitin = ARMOR_MATERIALS.register("chitin",
      () -> new ArmorMaterial(
         Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
            m.put(ArmorItem.Type.BOOTS,      3);
            m.put(ArmorItem.Type.LEGGINGS,   5);
            m.put(ArmorItem.Type.CHESTPLATE, 7);
            m.put(ArmorItem.Type.HELMET,     3);
         }),
         12,
         SoundEvents.ARMOR_EQUIP_LEATHER,
         () -> Ingredient.of(ItemInit.chitin),
         List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath("druidcraftrg", "chitin"))),
         1.0F, 0.0F
      ));

   public static final DeferredHolder<ArmorMaterial, ArmorMaterial> moonstone = ARMOR_MATERIALS.register("moonstone",
      () -> new ArmorMaterial(
         Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
            m.put(ArmorItem.Type.BOOTS,      3);
            m.put(ArmorItem.Type.LEGGINGS,   7);
            m.put(ArmorItem.Type.CHESTPLATE, 9);
            m.put(ArmorItem.Type.HELMET,     4);
         }),
         15,
         SoundEvents.ARMOR_EQUIP_DIAMOND,
         () -> Ingredient.of(ItemInit.moonstone),
         List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath("druidcraftrg", "moonstone"))),
         3.0F, 0.0F
      ));
}
