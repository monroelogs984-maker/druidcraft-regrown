package com.vulp.druidcraftrg.init;

import com.vulp.druidcraftrg.world.worldgen.feature.DoubleCropRandomPatchFeature;
import com.vulp.druidcraftrg.world.worldgen.feature.ElderTreeFeature;
import com.vulp.druidcraftrg.world.worldgen.feature.configuration.DoubleCropRandomPatchConfiguration;
import com.vulp.druidcraftrg.world.worldgen.feature.configuration.ElderTreeConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;

public class FeatureInit {
   // Configured/placed features live in data/druidcraftrg/worldgen JSON as of 1.20.1
   public static final Feature<DoubleCropRandomPatchConfiguration> hemp = new DoubleCropRandomPatchFeature(DoubleCropRandomPatchConfiguration.CODEC);
   public static final Feature<ElderTreeConfiguration> elder_tree = new ElderTreeFeature(ElderTreeConfiguration.CODEC);
}
