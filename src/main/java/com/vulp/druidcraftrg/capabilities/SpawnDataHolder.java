package com.vulp.druidcraftrg.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpawnDataHolder {
   public static final SpawnDataHolder EMPTY = new SpawnDataHolder(null, Level.OVERWORLD, 0.0F, false);

   public static final Codec<SpawnDataHolder> CODEC = RecordCodecBuilder.create(inst -> inst.group(
      BlockPos.CODEC.optionalFieldOf("pos").forGetter(h -> Optional.ofNullable(h.pos)),
      Level.RESOURCE_KEY_CODEC.fieldOf("dim").forGetter(h -> h.dimension),
      Codec.FLOAT.fieldOf("angle").forGetter(h -> h.angle),
      Codec.BOOL.fieldOf("forced").forGetter(h -> h.forced)
   ).apply(inst, (pos, dim, angle, forced) -> new SpawnDataHolder(pos.orElse(null), dim, angle, forced)));

   private final BlockPos pos;
   private final ResourceKey<Level> dimension;
   private final float angle;
   private final boolean forced;

   public SpawnDataHolder() {
      this(null, Level.OVERWORLD, 0.0F, false);
   }

   public SpawnDataHolder(BlockPos pos, ResourceKey<Level> dimension, float angle, boolean forced) {
      this.pos = pos;
      this.dimension = dimension;
      this.angle = angle;
      this.forced = forced;
   }

   public boolean hasSpawnData() {
      return this != EMPTY && this.pos != null;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public ResourceKey<Level> getDimension() {
      return this.dimension;
   }

   public float getAngle() {
      return this.angle;
   }

   public boolean isForced() {
      return this.forced;
   }
}
