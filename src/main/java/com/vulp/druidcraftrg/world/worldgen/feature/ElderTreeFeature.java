package com.vulp.druidcraftrg.world.worldgen.feature;

import com.mojang.serialization.Codec;
import com.vulp.druidcraftrg.blocks.WoodBlock;
import com.vulp.druidcraftrg.world.worldgen.feature.configuration.ElderTreeConfiguration;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

// 1.16.5 ElderTreeFeature + DumbTreeFeature merged; hand-built trunk with wood "roots",
// side branch logs and 4 fixed leaf layers
public class ElderTreeFeature extends Feature<ElderTreeConfiguration> {
   public ElderTreeFeature(Codec<ElderTreeConfiguration> codec) {
      super(codec);
   }

   private static boolean isAirOrLeavesAt(WorldGenLevel level, BlockPos pos) {
      return level.isStateAtPosition(pos, state -> state.isAir() || state.is(BlockTags.LEAVES));
   }

   private static boolean isReplaceableAt(WorldGenLevel level, BlockPos pos) {
      return level.isStateAtPosition(
         pos, state -> state.isAir() || state.is(BlockTags.LEAVES) || state.canBeReplaced() || state.getFluidState().is(FluidTags.WATER)
      );
   }

   private static boolean isReplaceableOrLogAt(WorldGenLevel level, BlockPos pos) {
      return isReplaceableAt(level, pos) || level.isStateAtPosition(pos, state -> state.is(BlockTags.LOGS));
   }

   private static boolean isDirtOrGrass(WorldGenLevel level, BlockPos pos) {
      return level.isStateAtPosition(pos, state -> state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND));
   }

   private void setElderLogAt(
      WorldGenLevel level, RandomSource rand, BlockPos pos, boolean isBaseWood, @Nullable Direction.Axis setAxis, ElderTreeConfiguration config, boolean forcePlacement
   ) {
      if (isReplaceableAt(level, pos) || forcePlacement) {
         BlockState blockType = config.trunkProvider().getState(rand, pos);
         BlockState baseType = config.woodProvider().getState(rand, pos).setValue(WoodBlock.DROP_SELF, false);
         if (setAxis == null) {
            if (isBaseWood) {
               if (rand.nextInt(3) == 0) {
                  blockType = baseType.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
               } else if (rand.nextInt(3) == 0) {
                  blockType = baseType.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
               } else {
                  blockType = baseType.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
               }
            }
         } else {
            blockType = blockType.setValue(RotatedPillarBlock.AXIS, setAxis);
         }
         this.setBlock(level, pos, blockType);
      }
   }

   private void placeRotatedLog(WorldGenLevel level, RandomSource rand, BlockPos pos, Direction.Axis setAxis, ElderTreeConfiguration config) {
      this.setElderLogAt(level, rand, pos, false, setAxis, config, false);
   }

   private void placeLogAt(WorldGenLevel level, RandomSource rand, BlockPos pos, boolean isBaseWood, ElderTreeConfiguration config, boolean forcePlacement) {
      this.setElderLogAt(level, rand, pos, isBaseWood, null, config, forcePlacement);
   }

   private void placeLeafAt(WorldGenLevel level, RandomSource rand, BlockPos pos, ElderTreeConfiguration config) {
      if (isAirOrLeavesAt(level, pos)) {
         this.setBlock(level, pos, config.leavesProvider().getState(rand, pos));
      }
   }

   // places a base-wood "root" beside the trunk, preferring one block down
   private void placeRootAt(WorldGenLevel level, RandomSource rand, BlockPos pos, ElderTreeConfiguration config) {
      if (isReplaceableAt(level, pos.below())) {
         this.placeLogAt(level, rand, pos.below(), true, config, false);
      } else if (isReplaceableAt(level, pos)) {
         this.placeLogAt(level, rand, pos, true, config, false);
      }
   }

   private boolean canGrow(WorldGenLevel level, BlockPos pos, int height) {
      if (pos.getY() < level.getMinBuildHeight() + 1 || pos.getY() + height + 1 > level.getMaxBuildHeight()) {
         return false;
      }
      BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
      for (int cy = pos.getY(); cy <= pos.getY() + 1 + height; ++cy) {
         int k = 1;
         if (cy == pos.getY()) {
            k = 0;
         }
         if (cy >= pos.getY() + 1 + height - 2) {
            k = 2;
         }
         for (int cx = pos.getX() - k; cx <= pos.getX() + k; ++cx) {
            for (int cz = pos.getZ() - k; cz <= pos.getZ() + k; ++cz) {
               if (cy < level.getMinBuildHeight() || cy >= level.getMaxBuildHeight()
                  || !isReplaceableOrLogAt(level, mutable.set(cx, cy, cz))) {
                  return false;
               }
            }
         }
      }
      return true;
   }

   @Override
   public boolean place(FeaturePlaceContext<ElderTreeConfiguration> context) {
      WorldGenLevel level = context.level();
      RandomSource rand = context.random();
      BlockPos position = context.origin();
      ElderTreeConfiguration config = context.config();

      int height = rand.nextInt(2) + 6;
      if (!this.canGrow(level, position, height)) {
         return false;
      } else if (isDirtOrGrass(level, position.below()) && position.getY() < level.getMaxBuildHeight() - height - 1) {
         int posX = position.getX();
         int posZ = position.getZ();
         int posY = 0;
         // 1.16.5 used nextInt(3), which made the west branch unreachable — fixed to 4
         int logSide = rand.nextInt(4);

         for (int base = 0; base < height; ++base) {
            int currentY = position.getY() + base;
            BlockPos blockpos = new BlockPos(posX, currentY, posZ);
            if (isAirOrLeavesAt(level, blockpos) || level.isStateAtPosition(blockpos, state -> state.getBlock() instanceof SaplingBlock)) {
               if (base == 0) {
                  if (rand.nextBoolean()) {
                     this.placeRootAt(level, rand, blockpos.north(), config);
                  }
                  if (rand.nextBoolean()) {
                     this.placeRootAt(level, rand, blockpos.east(), config);
                  }
                  if (rand.nextBoolean()) {
                     this.placeRootAt(level, rand, blockpos.south(), config);
                  }
                  if (rand.nextBoolean()) {
                     this.placeRootAt(level, rand, blockpos.west(), config);
                  }
                  if (rand.nextBoolean()) {
                     this.placeRootAt(level, rand, blockpos.north().east(), config);
                  }
                  if (rand.nextBoolean()) {
                     this.placeRootAt(level, rand, blockpos.north().west(), config);
                  }
                  if (rand.nextBoolean()) {
                     this.placeRootAt(level, rand, blockpos.south().east(), config);
                  }
                  if (rand.nextBoolean()) {
                     this.placeRootAt(level, rand, blockpos.south().west(), config);
                  }
                  this.placeLogAt(level, rand, blockpos, false, config, true);
               }
               this.placeLogAt(level, rand, blockpos, false, config, false);
               if (base == height - 2 || base == height - 3) {
                  if (logSide == 0) {
                     this.placeRotatedLog(level, rand, blockpos.north(), Direction.Axis.Z, config);
                  }
                  if (logSide == 1) {
                     this.placeRotatedLog(level, rand, blockpos.east(), Direction.Axis.X, config);
                  }
                  if (logSide == 2) {
                     this.placeRotatedLog(level, rand, blockpos.south(), Direction.Axis.Z, config);
                  }
                  if (logSide == 3) {
                     this.placeRotatedLog(level, rand, blockpos.west(), Direction.Axis.X, config);
                  }
               }
               posY = currentY;
            }
         }

         // layers 1 and 3: 7x7 rounded, at canopy top-1 and top-3
         BlockPos layerPos = new BlockPos(posX, posY, posZ).below();
         for (int lx = -3; lx <= 3; ++lx) {
            for (int lz = -3; lz <= 3; ++lz) {
               if (!((lx == 3 || lx == -3) && (lz == 3 || lz == -3))) {
                  boolean edge = (lx == -3 && (lz == -2 || lz == 2))
                     || (lx == 3 && (lz == -2 || lz == 2))
                     || (lz == -3 && (lx == -2 || lx == 2))
                     || (lz == 3 && (lx == -2 || lx == 2));
                  if (edge) {
                     if (rand.nextBoolean() || rand.nextBoolean()) {
                        this.placeLeafAt(level, rand, layerPos.offset(lx, 0, lz), config);
                     }
                     if (rand.nextBoolean() || rand.nextBoolean()) {
                        this.placeLeafAt(level, rand, layerPos.offset(lx, -2, lz), config);
                     }
                  } else {
                     this.placeLeafAt(level, rand, layerPos.offset(lx, 0, lz), config);
                     this.placeLeafAt(level, rand, layerPos.offset(lx, -2, lz), config);
                  }
               }
            }
         }
         // layer 4: 5x5 rounded at canopy top
         layerPos = layerPos.above();
         for (int lx = -2; lx <= 2; ++lx) {
            for (int lz = -2; lz <= 2; ++lz) {
               if (!((lx == -2 || lx == 2) && (lz == -2 || lz == 2))) {
                  this.placeLeafAt(level, rand, layerPos.offset(lx, 0, lz), config);
               } else if (rand.nextBoolean()) {
                  this.placeLeafAt(level, rand, layerPos.offset(lx, 0, lz), config);
               }
            }
         }
         // layer 5: 3x3 plus-shape crown
         layerPos = layerPos.above();
         for (int lx = -1; lx <= 1; ++lx) {
            for (int lz = -1; lz <= 1; ++lz) {
               if (!((lx == -1 || lx == 1) && (lz == -1 || lz == 1))) {
                  this.placeLeafAt(level, rand, layerPos.offset(lx, 0, lz), config);
               }
            }
         }
         // layer 2: 7x7 rounded skirt below the canopy
         layerPos = layerPos.below(3);
         for (int lx = -3; lx <= 3; ++lx) {
            for (int lz = -3; lz <= 3; ++lz) {
               if (!((lx == -3 || lx == 3) && (lz == -3 || lz == 3))) {
                  this.placeLeafAt(level, rand, layerPos.offset(lx, 0, lz), config);
               }
            }
         }
         return true;
      } else {
         return false;
      }
   }
}
