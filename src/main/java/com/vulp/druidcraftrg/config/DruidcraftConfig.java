package com.vulp.druidcraftrg.config;

import net.minecraftforge.common.ForgeConfigSpec;

// 1.16.5 DropRateConfig — the only config left to port; ore sizes/weights and entity
// spawns moved into worldgen JSON / biome modifiers (M5/M6) and are no longer configurable
public class DruidcraftConfig {
   public static final ForgeConfigSpec COMMON_SPEC;
   public static ForgeConfigSpec.IntValue hemp_seed_drops;
   public static ForgeConfigSpec.BooleanValue drop_seeds;

   static {
      ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();
      common.comment("Drop Rate Config");
      hemp_seed_drops = common.comment("Rate of hemp seed drops from grass blocks.")
         .defineInRange("droprate.hemp_seed_rate", 8, 0, 100);
      drop_seeds = common.comment("Whether to have seeds from this mod drop from grass at all.")
         .define("droprate.drop_seeds", true);
      COMMON_SPEC = common.build();
   }
}
