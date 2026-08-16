package com.vulp.druidcraftrg.api;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

// two-Minecraft-day cycle driving the elder fruit flower/berry seasons
public enum CropLifeStageType implements StringRepresentable {
   NONE,
   FLOWER,
   BERRY;

   @Override
   public String getSerializedName() {
      return name().toLowerCase(Locale.ROOT);
   }

   public static int getTwoDayCycle(Level level) {
      return (int) level.getLevelData().getDayTime() % (int) 96000L;
   }

   public static CropLifeStageType checkCropLife(Level level) {
      double stageDouble = getTwoDayCycle(level);
      if (stageDouble >= 48000) {
         if (stageDouble >= 72000) {
            return CropLifeStageType.NONE;
         }
         return CropLifeStageType.BERRY;
      }
      return CropLifeStageType.FLOWER;
   }
}
