package com.vulp.druidcraftrg.blocks.trees;

import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class DarkwoodTreeGrower {
   public static final ResourceKey<ConfiguredFeature<?, ?>> DARKWOOD_TREE =
      ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("druidcraftrg", "darkwood_tree"));
   public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_DARKWOOD_TREE =
      ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("druidcraftrg", "mega_darkwood_tree"));

   public static final TreeGrower INSTANCE = new TreeGrower(
      "darkwood",
      0.0F,
      Optional.of(DARKWOOD_TREE),
      Optional.<ResourceKey<ConfiguredFeature<?, ?>>>empty(),
      Optional.of(MEGA_DARKWOOD_TREE),
      Optional.<ResourceKey<ConfiguredFeature<?, ?>>>empty(),
      Optional.<ResourceKey<ConfiguredFeature<?, ?>>>empty(),
      Optional.<ResourceKey<ConfiguredFeature<?, ?>>>empty()
   );
}
