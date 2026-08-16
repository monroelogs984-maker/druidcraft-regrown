package com.vulp.druidcraftrg;

import com.vulp.druidcraftrg.client.renderer.SetupRenderers;
import com.vulp.druidcraftrg.config.DruidcraftConfig;
import com.vulp.druidcraftrg.entities.BeetleEntity;
import com.vulp.druidcraftrg.entities.DreadfishEntity;
import com.vulp.druidcraftrg.entities.LunarMothEntity;
import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.init.EntityInit;
import com.vulp.druidcraftrg.network.PacketHandler;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("druidcraftrg")
public class DruidcraftRegrown {
   public static final Logger LOGGER = LogManager.getLogger();
   public static final String MODID = "druidcraftrg";

   public DruidcraftRegrown() {
      IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
      bus.addListener(this::commonSetup);
      bus.addListener(CommonSetup::init);
      bus.addListener(this::clientSetup);
      ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DruidcraftConfig.COMMON_SPEC);
      MinecraftForge.EVENT_BUS.register(this);
   }

   private void commonSetup(FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {
         PacketHandler.init();
         FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
         pot.addPlant(DruidcraftRegrownRegistry.location("darkwood_sapling"), () -> BlockInit.potted_darkwood_sapling);
         pot.addPlant(DruidcraftRegrownRegistry.location("elder_sapling"), () -> BlockInit.potted_elder_sapling);
         pot.addPlant(DruidcraftRegrownRegistry.location("lavender"), () -> BlockInit.potted_lavender);
         if (ModList.get().isLoaded("terrablender")) {
            com.vulp.druidcraftrg.world.worldgen.DruidcraftRegion.register();
         }
      });
      event.enqueueWork(() -> {
         SpawnPlacements.register(EntityInit.beetle, SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeetleEntity::placement);
         SpawnPlacements.register(EntityInit.dreadfish, SpawnPlacements.Type.NO_RESTRICTIONS,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DreadfishEntity::placement);
         SpawnPlacements.register(EntityInit.lunar_moth, SpawnPlacements.Type.NO_RESTRICTIONS,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LunarMothEntity::placement);
      });
      LOGGER.info("Common setup event complete!");
   }

   private void clientSetup(FMLClientSetupEvent event) {
      SetupRenderers.setup(event);
      LOGGER.info("Client setup event complete!");
   }
}
