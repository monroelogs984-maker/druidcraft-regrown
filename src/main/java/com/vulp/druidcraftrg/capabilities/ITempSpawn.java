package com.vulp.druidcraftrg.capabilities;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITempSpawn extends INBTSerializable<CompoundTag> {
   @Nullable
   SpawnDataHolder getSpawnData();

   void setSpawnData(SpawnDataHolder var1);

   void removeSpawnData();

   boolean hasSpawnData();
}
