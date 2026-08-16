package com.vulp.druidcraftrg.init;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class WoodTypeInit {
   public static final BlockSetType DARKWOOD_SET = BlockSetType.register(new BlockSetType("druidcraftrg:darkwood"));
   public static final BlockSetType ELDER_SET = BlockSetType.register(new BlockSetType("druidcraftrg:elder"));
   public static final WoodType DARKWOOD = WoodType.register(new WoodType("druidcraftrg:darkwood", DARKWOOD_SET));
   public static final WoodType ELDER = WoodType.register(new WoodType("druidcraftrg:elder", ELDER_SET));
}
