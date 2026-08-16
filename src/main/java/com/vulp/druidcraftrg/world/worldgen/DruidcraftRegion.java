package com.vulp.druidcraftrg.world.worldgen;

import com.mojang.datafixers.util.Pair;
import com.vulp.druidcraftrg.DruidcraftRegrownRegistry;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;

// Only classload this when TerraBlender is installed (see DruidcraftRegrown.commonSetup).
// Replaces a share of taiga slots with the darkwood forest — 1.16.5 registered it as a
// COOL-band biome (temp 0.25 / downfall 0.8, same climate as taiga).
public class DruidcraftRegion extends Region {
   public static final ResourceKey<Biome> DARKWOOD_FOREST =
      ResourceKey.create(Registries.BIOME, DruidcraftRegrownRegistry.location("darkwood_forest"));

   public DruidcraftRegion(int weight) {
      super(DruidcraftRegrownRegistry.location("overworld"), RegionType.OVERWORLD, weight);
   }

   @Override
   public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
      this.addModifiedVanillaOverworldBiomes(mapper, builder -> builder.replaceBiome(Biomes.TAIGA, DARKWOOD_FOREST));
   }

   public static void register() {
      Regions.register(new DruidcraftRegion(2));
   }
}
