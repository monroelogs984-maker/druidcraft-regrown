package com.vulp.druidcraftrg.blocks.trees;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

// features are data-driven JSON (M6); growth is a no-op until they exist
public class DarkwoodTreeGrower extends AbstractMegaTreeGrower {
   public static final ResourceKey<ConfiguredFeature<?, ?>> DARKWOOD_TREE =
      ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("druidcraftrg", "darkwood_tree"));
   public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_DARKWOOD_TREE =
      ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("druidcraftrg", "mega_darkwood_tree"));

   @Override
   protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
      return DARKWOOD_TREE;
   }

   @Override
   protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
      return MEGA_DARKWOOD_TREE;
   }
}
