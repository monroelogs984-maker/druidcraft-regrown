package com.vulp.druidcraftrg.blocks;

import com.vulp.druidcraftrg.init.ParticleInit;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulfireBlock extends Block {
   private final DyeColor color;
   private static final Map<DyeColor, int[]> DYE_COLOR_MAP = new HashMap<>();
   private static final VoxelShape SHAPE = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);

   public SoulfireBlock(DyeColor color, Properties properties) {
      super(properties);
      this.color = color;
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   static {
      DYE_COLOR_MAP.put(DyeColor.BLACK, new int[]{15, 15, 15});
      DYE_COLOR_MAP.put(DyeColor.RED, new int[]{255, 20, 10});
      DYE_COLOR_MAP.put(DyeColor.GREEN, new int[]{18, 150, 13});
      DYE_COLOR_MAP.put(DyeColor.BROWN, new int[]{90, 45, 5});
      DYE_COLOR_MAP.put(DyeColor.BLUE, new int[]{15, 20, 255});
      DYE_COLOR_MAP.put(DyeColor.PURPLE, new int[]{140, 15, 220});
      DYE_COLOR_MAP.put(DyeColor.CYAN, new int[]{15, 150, 130});
      DYE_COLOR_MAP.put(DyeColor.LIGHT_GRAY, new int[]{125, 125, 125});
      DYE_COLOR_MAP.put(DyeColor.GRAY, new int[]{60, 60, 60});
      DYE_COLOR_MAP.put(DyeColor.PINK, new int[]{255, 115, 170});
      DYE_COLOR_MAP.put(DyeColor.LIME, new int[]{130, 255, 20});
      DYE_COLOR_MAP.put(DyeColor.YELLOW, new int[]{255, 230, 2});
      DYE_COLOR_MAP.put(DyeColor.LIGHT_BLUE, new int[]{50, 200, 255});
      DYE_COLOR_MAP.put(DyeColor.MAGENTA, new int[]{205, 60, 155});
      DYE_COLOR_MAP.put(DyeColor.ORANGE, new int[]{250, 135, 5});
      DYE_COLOR_MAP.put(DyeColor.WHITE, new int[]{215, 215, 215});
   }

   private int[] getSmokeColorArray() {
      return DYE_COLOR_MAP.getOrDefault(this.color, new int[]{0, 0, 0});
   }

   @Override
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      if (random.nextInt(4) != 0) {
         super.animateTick(state, level, pos, random);
         double d0 = (double) pos.getX() + 0.5D;
         double d1 = (double) pos.getY() + 0.5D;
         double d2 = (double) pos.getZ() + 0.5D;
         int[] color = this.getSmokeColorArray();
         float limit = 0.1F;
         float offset1 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5F));
         float offset2 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5F));
         float offset3 = Math.min(limit, Math.max(-limit, random.nextFloat() - 0.5F));
         level.addParticle(ParticleInit.magic_smoke, false, d0 + offset1, d1 + offset2, d2 + offset3,
            color[0] / 255.0F, color[1] / 255.0F, color[2] / 255.0F);
      }
   }
}
