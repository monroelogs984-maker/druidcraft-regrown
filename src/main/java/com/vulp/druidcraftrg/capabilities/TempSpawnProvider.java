package com.vulp.druidcraftrg.capabilities;

import javax.annotation.Nonnull;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class TempSpawnProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
   public static Capability<TempSpawnCapability> TEMP_SPAWN_CAPABILITY = CapabilityManager.get(new CapabilityToken<TempSpawnCapability>() {
   });
   private TempSpawnCapability cache = null;
   private final LazyOptional<TempSpawnCapability> INSTANCE = LazyOptional.of(this::createInstance);

   @Nonnull
   private TempSpawnCapability createInstance() {
      if (this.cache == null) {
         this.cache = new TempSpawnCapability();
      }

      return this.cache;
   }

   public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      return cap == TEMP_SPAWN_CAPABILITY ? this.INSTANCE.cast() : LazyOptional.empty();
   }

   public CompoundTag serializeNBT() {
      return this.createInstance().serializeNBT();
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.createInstance().deserializeNBT(nbt);
   }
}
