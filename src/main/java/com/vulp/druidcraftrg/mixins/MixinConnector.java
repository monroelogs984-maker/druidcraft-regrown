package com.vulp.druidcraftrg.mixins;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {
   public void connect() {
      Mixins.addConfigurations(new String[]{"druidcraftrg.mixins.json"});
   }
}
