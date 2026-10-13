package com.vulp.druidcraftrg;

import com.vulp.druidcraftrg.init.BlockInit;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// 1.16.5 VanillaIntegrationRegistry — flammables (compostables are the neoforge:compostables data map)
public class CommonSetup {
   public static void init(FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {
         registerFlammables();
      });
   }

   private static void registerFlammables() {
      flammable(BlockInit.hemp_crop, 60, 100);
      flammable(BlockInit.darkwood_log, 5, 5);
      flammable(BlockInit.darkwood_wood, 5, 5);
      flammable(BlockInit.stripped_darkwood_log, 5, 5);
      flammable(BlockInit.stripped_darkwood_wood, 5, 5);
      flammable(BlockInit.darkwood_leaves, 30, 60);
      flammable(BlockInit.darkwood_planks, 5, 20);
      flammable(BlockInit.darkwood_slab, 5, 20);
      flammable(BlockInit.darkwood_stairs, 5, 20);
      flammable(BlockInit.darkwood_fence, 5, 20);
      flammable(BlockInit.darkwood_fence_gate, 5, 20);
      flammable(BlockInit.darkwood_button, 5, 20);
      flammable(BlockInit.darkwood_door, 5, 20);
      flammable(BlockInit.darkwood_pressure_plate, 5, 20);
      flammable(BlockInit.darkwood_trapdoor, 5, 20);
      flammable(BlockInit.elder_log, 5, 5);
      flammable(BlockInit.elder_wood, 5, 5);
      flammable(BlockInit.stripped_elder_log, 5, 5);
      flammable(BlockInit.stripped_elder_wood, 5, 5);
      flammable(BlockInit.elder_leaves, 30, 60);
      flammable(BlockInit.elder_fruit, 30, 60);
      flammable(BlockInit.elder_planks, 5, 20);
      flammable(BlockInit.elder_slab, 5, 20);
      flammable(BlockInit.elder_stairs, 5, 20);
      flammable(BlockInit.elder_fence, 5, 20);
      flammable(BlockInit.elder_fence_gate, 5, 20);
      flammable(BlockInit.elder_button, 5, 20);
      flammable(BlockInit.elder_door, 5, 20);
      flammable(BlockInit.elder_pressure_plate, 5, 20);
      flammable(BlockInit.elder_trapdoor, 5, 20);
      flammable(BlockInit.oak_beam, 5, 5);
      flammable(BlockInit.spruce_beam, 5, 5);
      flammable(BlockInit.birch_beam, 5, 5);
      flammable(BlockInit.jungle_beam, 5, 5);
      flammable(BlockInit.acacia_beam, 5, 5);
      flammable(BlockInit.dark_oak_beam, 5, 5);
      flammable(BlockInit.darkwood_beam, 5, 5);
      flammable(BlockInit.stripped_darkwood_beam, 5, 5);
      flammable(BlockInit.elder_beam, 5, 5);
      flammable(BlockInit.oak_panels, 5, 20);
      flammable(BlockInit.spruce_panels, 5, 20);
      flammable(BlockInit.birch_panels, 5, 20);
      flammable(BlockInit.jungle_panels, 5, 20);
      flammable(BlockInit.acacia_panels, 5, 20);
      flammable(BlockInit.dark_oak_panels, 5, 20);
      flammable(BlockInit.darkwood_panels, 5, 20);
      flammable(BlockInit.elder_panels, 5, 20);
      flammable(BlockInit.lavender, 60, 100);
      flammable(BlockInit.blueberry_bush, 60, 100);
      flammable(BlockInit.rope, 30, 60);
   }

   private static void flammable(Block block, int igniteOdds, int burnOdds) {
      ((FireBlock) Blocks.FIRE).setFlammable(block, igniteOdds, burnOdds);
   }
}
