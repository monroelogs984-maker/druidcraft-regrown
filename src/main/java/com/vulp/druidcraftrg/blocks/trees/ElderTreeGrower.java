package com.vulp.druidcraftrg.blocks.trees;

import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ElderTreeGrower {
   public static final ResourceKey<ConfiguredFeature<?, ?>> ELDER_TREE =
      ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("druidcraftrg", "elder_tree"));

   public static final TreeGrower INSTANCE = new TreeGrower(
      "elder",
      Optional.of(ELDER_TREE),
      Optional.<ResourceKey<ConfiguredFeature<?, ?>>>empty(),
      Optional.<ResourceKey<ConfiguredFeature<?, ?>>>empty()
   );
}
