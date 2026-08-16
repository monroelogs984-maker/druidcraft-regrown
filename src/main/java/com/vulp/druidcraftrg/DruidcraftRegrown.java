package com.vulp.druidcraftrg;

import com.vulp.druidcraftrg.client.renderer.SetupRenderers;
import com.vulp.druidcraftrg.config.DruidcraftConfig;
import com.vulp.druidcraftrg.entities.BeetleEntity;
import com.vulp.druidcraftrg.entities.DreadfishEntity;
import com.vulp.druidcraftrg.entities.LunarMothEntity;
import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.init.EntityInit;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import com.vulp.druidcraftrg.items.TravelPackItem;
import com.vulp.druidcraftrg.network.message.DeployBedrollMessage;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("druidcraftrg")
public class DruidcraftRegrown {
   public static final Logger LOGGER = LogManager.getLogger();
   public static final String MODID = "druidcraftrg";

   public DruidcraftRegrown(IEventBus modEventBus, ModContainer modContainer) {
      modEventBus.addListener(this::commonSetup);
      modEventBus.addListener(CommonSetup::init);
      modEventBus.addListener(this::clientSetup);
      modEventBus.addListener(this::registerPayloads);
      modEventBus.addListener(this::registerSpawnPlacements);
      modEventBus.addListener(this::registerCapabilities);

      DruidcraftRegrownRegistry.register(modEventBus);

      modContainer.registerConfig(ModConfig.Type.COMMON, DruidcraftConfig.COMMON_SPEC);
   }

   private void commonSetup(FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {
         FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
         pot.addPlant(DruidcraftRegrownRegistry.location("darkwood_sapling"), () -> BlockInit.potted_darkwood_sapling);
         pot.addPlant(DruidcraftRegrownRegistry.location("elder_sapling"), () -> BlockInit.potted_elder_sapling);
         pot.addPlant(DruidcraftRegrownRegistry.location("lavender"), () -> BlockInit.potted_lavender);
         // Darkwood forest only generates with TerraBlender installed; the mod runs fine without it
         if (ModList.get().isLoaded("terrablender")) {
            com.vulp.druidcraftrg.world.worldgen.DruidcraftRegion.register();
         }
      });
      LOGGER.info("Common setup event complete!");
   }

   private void clientSetup(FMLClientSetupEvent event) {
      SetupRenderers.setup(event);
      LOGGER.info("Client setup event complete!");
   }

   private void registerPayloads(RegisterPayloadHandlersEvent event) {
      event.registrar("druidcraftrg").playToServer(DeployBedrollMessage.TYPE, DeployBedrollMessage.STREAM_CODEC,
         (msg, ctx) -> ctx.enqueueWork(() -> {
            if (ctx.player() instanceof net.minecraft.server.level.ServerPlayer sender) {
               TravelPackItem.deployBedroll(msg.entityId(), sender.level());
            }
         }));
   }

   private void registerCapabilities(RegisterCapabilitiesEvent event) {
      event.registerBlockEntity(
         Capabilities.ItemHandler.BLOCK,
         BlockEntityInit.crate,
         com.vulp.druidcraftrg.blocks.tile.CrateTileEntity::getItemHandler
      );
   }

   private void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
      event.register(EntityInit.beetle, net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
         Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeetleEntity::placement,
         RegisterSpawnPlacementsEvent.Operation.OR);
      event.register(EntityInit.dreadfish, net.minecraft.world.entity.SpawnPlacementTypes.NO_RESTRICTIONS,
         Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DreadfishEntity::placement,
         RegisterSpawnPlacementsEvent.Operation.OR);
      event.register(EntityInit.lunar_moth, net.minecraft.world.entity.SpawnPlacementTypes.NO_RESTRICTIONS,
         Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LunarMothEntity::placement,
         RegisterSpawnPlacementsEvent.Operation.OR);
   }
}
