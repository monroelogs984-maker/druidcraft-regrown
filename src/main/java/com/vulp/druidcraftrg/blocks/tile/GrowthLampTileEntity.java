package com.vulp.druidcraftrg.blocks.tile;

import com.vulp.druidcraftrg.blocks.GrowthLampBlock;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import com.vulp.druidcraftrg.init.ParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

// accelerates plant and baby-animal growth in a 5x5x5 area
public class GrowthLampTileEntity extends BlockEntity {

   public GrowthLampTileEntity(BlockPos pos, BlockState state) {
      super(BlockEntityInit.growth_lamp, pos, state);
   }

   public static void tick(Level level, BlockPos pos, BlockState state, GrowthLampTileEntity entity) {
      RandomSource random = level.random;
      if (random.nextInt(40) != 0) {
         return;
      }

      if (level.isClientSide) {
         float yMod = state.getValue(GrowthLampBlock.HANGING) ? 0.05F : 0.0F;
         for (int i = 0; i < 3; i++) {
            level.addParticle(ParticleInit.magic_smoke,
               (double) pos.getX() + 0.20F + (random.nextInt(7) / 10.0F),
               pos.getY() + 0.05F + yMod + (random.nextInt(6) / 10.0F),
               (double) pos.getZ() + 0.20F + (random.nextInt(7) / 10.0F),
               215 / 255.0F, 255 / 255.0F, 65 / 255.0F);
         }
         return;
      }

      List<Animal> animals = level.getEntitiesOfClass(Animal.class,
         new AABB(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3));
      for (Animal animal : animals) {
         if (animal.isBaby()) {
            animal.ageUp(random.nextInt(2) + 1);
         }
      }

      for (int x = -2; x < 3; x++) {
         for (int y = -2; y < 3; y++) {
            for (int z = -2; z < 3; z++) {
               BlockPos newPos = pos.offset(x, y, z);
               Block block = level.getBlockState(newPos).getBlock();
               if (block instanceof BonemealableBlock) {
                  level.scheduleTick(newPos, block, 0);
               }
            }
         }
      }
   }
}
