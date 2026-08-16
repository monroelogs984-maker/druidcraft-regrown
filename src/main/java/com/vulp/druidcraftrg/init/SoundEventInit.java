package com.vulp.druidcraftrg.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundEventInit {
   public static final SoundEvent fill_bottle =
      SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("druidcraftrg", "fill_bottle"));
   public static final SoundEvent open_crate =
      SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("druidcraftrg", "open_crate"));
   public static final SoundEvent close_crate =
      SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("druidcraftrg", "close_crate"));
   public static final SoundEvent woodcutter_take_result =
      SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("druidcraftrg", "woodcutter_take_result"));
}
