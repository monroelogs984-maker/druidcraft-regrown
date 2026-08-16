package com.vulp.druidcraftrg.blocks.tile;

import com.vulp.druidcraftrg.blocks.RopeBlock;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RopeTileEntity extends BlockEntity {
   private boolean forceKnot = false;

   public RopeTileEntity(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state) {
      super(tileEntityType, pos, state);
   }

   public RopeTileEntity(BlockPos pos, BlockState state) {
      super(BlockEntityInit.rope, pos, state);
   }

   public boolean hasKnot() {
      BlockState state = this.getBlockState();
      return this.forceKnot || state.getBlock() instanceof RopeBlock && RopeBlock.hasIntersection(state);
   }

   public void forceKnot(boolean force) {
      this.forceKnot = force;
   }

   public InteractionResult forceToggle() {
      BlockState state = this.getBlockState();
      if (state.getBlock() instanceof RopeBlock && !RopeBlock.hasIntersection(state)) {
         this.forceKnot = !this.forceKnot;
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.FAIL;
      }
   }

   public void saveAdditional(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
      super.saveAdditional(compound, registries);
      compound.putBoolean("Knot", this.forceKnot);
   }

   public void loadAdditional(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
      super.loadAdditional(compound, registries);
      this.forceKnot = compound.getBoolean("Knot");
   }

   public CompoundTag getUpdateTag() {
      CompoundTag nbtTagCompound = new CompoundTag();
      this.saveAdditional(nbtTagCompound, this.level.registryAccess());
      return nbtTagCompound;
   }

   public void handleUpdateTag(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
      this.loadAdditional(tag, this.level != null ? this.level.registryAccess() : net.minecraft.core.RegistryAccess.EMPTY);
   }
}
