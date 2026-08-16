package com.vulp.druidcraftrg.world.worldgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

// 1.16.5 DummyTreeFeatureConfig — field names kept so the JSON matches the original codec
public record ElderTreeConfiguration(
   BlockStateProvider trunkProvider,
   BlockStateProvider woodProvider,
   BlockStateProvider leavesProvider,
   BlockStateProvider saplingProvider,
   List<TreeDecorator> decorators,
   Heightmap.Types heightmap
) implements FeatureConfiguration {
   public static final Codec<ElderTreeConfiguration> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(ElderTreeConfiguration::trunkProvider),
            BlockStateProvider.CODEC.fieldOf("wood_provider").forGetter(ElderTreeConfiguration::woodProvider),
            BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(ElderTreeConfiguration::leavesProvider),
            BlockStateProvider.CODEC.fieldOf("sapling_provider").forGetter(ElderTreeConfiguration::saplingProvider),
            TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(ElderTreeConfiguration::decorators),
            Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(ElderTreeConfiguration::heightmap)
         )
         .apply(instance, ElderTreeConfiguration::new)
   );
}
