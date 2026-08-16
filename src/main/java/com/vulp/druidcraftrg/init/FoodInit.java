package com.vulp.druidcraftrg.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

// 1.16.5 FoodRegistry
public class FoodInit {
   public static final FoodProperties blueberries = new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).build();
   public static final FoodProperties elderberries = new FoodProperties.Builder().nutrition(2).saturationMod(0.1F)
      .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.5F).build();
   public static final FoodProperties apple_elderberry_crumble = new FoodProperties.Builder().nutrition(7).saturationMod(0.7F).build();
   public static final FoodProperties blueberry_muffin = new FoodProperties.Builder().nutrition(6).saturationMod(0.4F).build();
   public static final FoodProperties elderflower_cordial = new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build();
}
