package com.vulp.druidcraftrg.blocks;

import net.minecraft.util.StringRepresentable;

public enum Connections implements StringRepresentable {
   NONE("none"),
   NORMAL("normal"),
   CUT("cut");

   private final String name;

   private Connections(String string) {
      this.name = string;
   }

   @Override
   public String toString() {
      return this.getSerializedName();
   }

   public String getSerializedName() {
      return this.name;
   }
}
