package com.vulp.druidcraftrg.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class DruidcraftConfig {
   public static final ModConfigSpec COMMON_SPEC;
   public static ModConfigSpec.IntValue hemp_seed_drops;
   public static ModConfigSpec.BooleanValue drop_seeds;

   static {
      ModConfigSpec.Builder common = new ModConfigSpec.Builder();
      common.comment("Drop Rate Config");
      hemp_seed_drops = common.comment("Rate of hemp seed drops from grass blocks.")
         .defineInRange("droprate.hemp_seed_rate", 8, 0, 100);
      drop_seeds = common.comment("Whether to have seeds from this mod drop from grass at all.")
         .define("droprate.drop_seeds", true);
      COMMON_SPEC = common.build();
   }
}
