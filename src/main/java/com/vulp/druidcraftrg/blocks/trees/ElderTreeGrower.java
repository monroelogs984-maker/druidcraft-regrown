package com.vulp.druidcraftrg.blocks.trees;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ElderTreeGrower extends AbstractTreeGrower {
   public static final ResourceKey<ConfiguredFeature<?, ?>> ELDER_TREE =
      ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("druidcraftrg", "elder_tree"));

   @Override
   protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
      return ELDER_TREE;
   }
}
