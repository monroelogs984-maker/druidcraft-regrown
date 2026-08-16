package com.vulp.druidcraftrg.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundEventInit {
   public static final SoundEvent fill_bottle =
      SoundEvent.createVariableRangeEvent(new ResourceLocation("druidcraftrg", "fill_bottle"));
   public static final SoundEvent open_crate =
      SoundEvent.createVariableRangeEvent(new ResourceLocation("druidcraftrg", "open_crate"));
   public static final SoundEvent close_crate =
      SoundEvent.createVariableRangeEvent(new ResourceLocation("druidcraftrg", "close_crate"));
   public static final SoundEvent woodcutter_take_result =
      SoundEvent.createVariableRangeEvent(new ResourceLocation("druidcraftrg", "woodcutter_take_result"));
}
