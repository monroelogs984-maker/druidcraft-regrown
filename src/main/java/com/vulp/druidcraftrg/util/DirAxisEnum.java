package com.vulp.druidcraftrg.util;

import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

public enum DirAxisEnum {
   X(Axis.X, Direction.EAST, Direction.WEST),
   Y(Axis.Y, Direction.UP, Direction.DOWN),
   Z(Axis.Z, Direction.NORTH, Direction.SOUTH);

   private final Axis axis;
   private final Direction[] directions;

   private DirAxisEnum(Axis axis, Direction... directions) {
      this.axis = axis;
      this.directions = directions;
   }

   public Axis getAxis() {
      return this.axis;
   }

   public Direction[] getDirections() {
      return this.directions;
   }

   @Nullable
   public static DirAxisEnum getFromAxis(Axis axis) {
      for (DirAxisEnum value : values()) {
         if (value.getAxis() == axis) {
            return value;
         }
      }

      return null;
   }

   @Nullable
   public static DirAxisEnum getFromDirection(Direction dir) {
      for (DirAxisEnum value : values()) {
         if (Arrays.stream(value.getDirections()).allMatch(direction -> direction == dir)) {
            return value;
         }
      }

      return null;
   }
}
