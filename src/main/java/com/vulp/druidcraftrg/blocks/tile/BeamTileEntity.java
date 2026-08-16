package com.vulp.druidcraftrg.blocks.tile;

import com.vulp.druidcraftrg.blocks.BeamBlock;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BeamTileEntity extends BlockEntity {
   private Axis lashingAxis;
   private List<Direction> ropeDirections = Collections.emptyList();
   private boolean initTick = false;

   public BeamTileEntity(BlockPos pos, BlockState state) {
      super(BlockEntityInit.beam, pos, state);
   }

   public BeamTileEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
      super(blockEntityType, pos, state);
   }

   public void saveAdditional(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
      super.saveAdditional(compound, registries);
      if (this.lashingAxis != null) {
         compound.putInt("Axis", this.lashingAxis.ordinal());
      } else {
         compound.remove("Axis");
      }

      List<Integer> directions = new ArrayList<>(Collections.emptyList());
      this.ropeDirections.forEach(dir -> directions.add(dir.ordinal()));
      compound.putIntArray("Dir", directions);
   }

   public void loadAdditional(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
      super.loadAdditional(compound, registries);
      this.lashingAxis = compound.contains("Axis") ? Axis.values()[compound.getInt("Axis")] : null;
      int[] directions = compound.getIntArray("Dir");
      this.ropeDirections = new ArrayList<>(Collections.emptyList());

      for (int dir : directions) {
         this.ropeDirections.add(Direction.values()[dir]);
      }
   }

   public static void serverTick(Level level, BlockPos pos, BlockState state, BeamTileEntity blockEntity) {
      if (!blockEntity.initTick) {
         blockEntity.initTick = true;
         blockEntity.update(state);
      }
   }

   public CompoundTag getUpdateTag() {
      CompoundTag nbtTagCompound = new CompoundTag();
      this.saveAdditional(nbtTagCompound, this.level.registryAccess());
      return nbtTagCompound;
   }

   public void handleUpdateTag(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
      this.loadAdditional(tag, this.level != null ? this.level.registryAccess() : net.minecraft.core.RegistryAccess.EMPTY);
      super.handleUpdateTag(tag, registries);
   }

   @Nullable
   public Axis findAxis(BlockState state) {
      return !(state.getBlock() instanceof BeamBlock)
            || !((BeamBlock)state.getBlock()).hasLashing(this.level, this.getBlockPos(), state) && this.ropeDirections.isEmpty()
         ? null
         : (Axis)state.getValue(BeamBlock.DEFAULT_AXIS);
   }

   public void update(BlockState state) {
      this.ropeDirections = BeamBlock.getRopeDirections(this.level, this.getBlockPos(), state);
      this.lashingAxis = this.findAxis(state);
   }

   public Axis getLashingAxis() {
      return this.lashingAxis;
   }

   public List<Direction> getRopeDirections() {
      return this.ropeDirections;
   }
}
