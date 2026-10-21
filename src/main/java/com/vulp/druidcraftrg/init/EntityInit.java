package com.vulp.druidcraftrg.init;

import com.vulp.druidcraftrg.entities.BeetleEntity;
import com.vulp.druidcraftrg.entities.DreadfishEntity;
import com.vulp.druidcraftrg.entities.LunarMothEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityInit {
   public static EntityType<DreadfishEntity> dreadfish;
   public static EntityType<BeetleEntity> beetle;
   public static EntityType<LunarMothEntity> lunar_moth;

   // Moths get their own spawn category (cap 8): in CREATURE the never-despawning farm animals
   // filled the cap, and in AMBIENT cave bats do. NaturalSpawner snapshots the category list when
   // it first loads, so this must be created during mod construction (see DruidcraftRegrown).
   public static final MobCategory LUNAR_MOTH_CATEGORY =
      MobCategory.create("DRUIDCRAFTRG_LUNAR_MOTH", "druidcraftrg_lunar_moth", 8, true, false, 128);
}
