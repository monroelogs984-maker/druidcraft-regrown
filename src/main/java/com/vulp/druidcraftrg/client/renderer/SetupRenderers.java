package com.vulp.druidcraftrg.client.renderer;

import com.vulp.druidcraftrg.client.gui.screen.inventory.BeetleInventoryScreen;
import com.vulp.druidcraftrg.client.gui.screen.inventory.CrateScreen;
import com.vulp.druidcraftrg.client.gui.screen.inventory.TravelPackScreen;
import com.vulp.druidcraftrg.client.gui.screen.inventory.WoodcutterScreen;
import com.vulp.druidcraftrg.client.models.BeetleModel;
import com.vulp.druidcraftrg.client.models.BoneShieldModel;
import com.vulp.druidcraftrg.client.models.ChitinShieldModel;
import com.vulp.druidcraftrg.client.models.DreadfishModel;
import com.vulp.druidcraftrg.client.models.LunarMothModel;
import com.vulp.druidcraftrg.client.models.MoonstoneShieldModel;
import com.vulp.druidcraftrg.client.models.TravelPackModel;
import com.vulp.druidcraftrg.client.particle.FieryGlowParticle;
import com.vulp.druidcraftrg.client.particle.FierySparkParticle;
import com.vulp.druidcraftrg.client.particle.MagicGlitterParticle;
import com.vulp.druidcraftrg.client.particle.MagicSmokeParticle;
import com.vulp.druidcraftrg.client.renderer.entity.BeetleRenderer;
import com.vulp.druidcraftrg.client.renderer.entity.DreadfishRenderer;
import com.vulp.druidcraftrg.client.renderer.entity.LunarMothRenderer;
import com.vulp.druidcraftrg.client.renderer.layers.TravelPackLayer;
import com.vulp.druidcraftrg.client.renderer.tile.BeamRopeRenderer;
import com.vulp.druidcraftrg.client.renderer.tile.RopeKnotRenderer;
import com.vulp.druidcraftrg.init.EntityInit;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.init.ContainerInit;
import com.vulp.druidcraftrg.init.ItemInit;
import com.vulp.druidcraftrg.init.ModelLayerInit;
import com.vulp.druidcraftrg.init.ParticleInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(
   modid = "druidcraftrg",
   value = {Dist.CLIENT},
   bus = Bus.MOD
)
public class SetupRenderers {
   public static void setup(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
         ItemBlockRenderTypes.setRenderLayer(BlockInit.hemp_crop, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.rope, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.platform, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.darkwood_leaves, RenderType.cutoutMipped());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.elder_leaves, RenderType.cutoutMipped());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.darkwood_sapling, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.elder_sapling, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.potted_darkwood_sapling, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.potted_elder_sapling, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.darkwood_door, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.elder_door, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.darkwood_trapdoor, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.elder_trapdoor, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.lavender, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.potted_lavender, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.blueberry_bush, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.elder_fruit, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.elder_leaf_layer, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.white_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.orange_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.magenta_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.light_blue_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.yellow_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.lime_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.pink_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.gray_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.light_gray_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.cyan_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.purple_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.blue_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.brown_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.green_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.red_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.black_soulfire, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.fiery_torch, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.wall_fiery_torch, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.rope_lantern, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.ceramic_lantern, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.growth_lamp, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.woodcutter, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.turquoise_lunar_moth_jar, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.white_lunar_moth_jar, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.lime_lunar_moth_jar, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.yellow_lunar_moth_jar, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.orange_lunar_moth_jar, RenderType.cutout());
         ItemBlockRenderTypes.setRenderLayer(BlockInit.pink_lunar_moth_jar, RenderType.cutout());
         ItemProperties.register(ItemInit.bone_shield, new ResourceLocation("blocking"), SetupRenderers::blockingProperty);
         ItemProperties.register(ItemInit.chitin_shield, new ResourceLocation("blocking"), SetupRenderers::blockingProperty);
         ItemProperties.register(ItemInit.moonstone_shield, new ResourceLocation("blocking"), SetupRenderers::blockingProperty);
         ItemProperties.register(ItemInit.travel_pack, new ResourceLocation("druidcraftrg", "color"),
            (stack, level, entity, seed) -> stack.getTag() != null && stack.getTag().contains("Color")
               ? (float) stack.getTag().getInt("Color") : -1.0F);
      });
      MenuScreens.register(ContainerInit.CRATE_9x3, CrateScreen::new);
      MenuScreens.register(ContainerInit.CRATE_9x6, CrateScreen::new);
      MenuScreens.register(ContainerInit.CRATE_9x12, CrateScreen::new);
      MenuScreens.register(ContainerInit.CRATE_9x24, CrateScreen::new);
      MenuScreens.register(ContainerInit.WOODCUTTER, WoodcutterScreen::new);
      MenuScreens.register(ContainerInit.TRAVEL_PACK, TravelPackScreen::new);
      MenuScreens.register(ContainerInit.BEETLE_INV, BeetleInventoryScreen::new);
   }

   private static float blockingProperty(net.minecraft.world.item.ItemStack stack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity entity, int seed) {
      return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
   }

   @SubscribeEvent
   public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
      event.registerSpriteSet(ParticleInit.magic_smoke, MagicSmokeParticle.Provider::new);
      event.registerSpriteSet(ParticleInit.fiery_glow, FieryGlowParticle.Provider::new);
      event.registerSpriteSet(ParticleInit.fiery_spark, FierySparkParticle.Provider::new);
      event.registerSpriteSet(ParticleInit.magic_glitter, MagicGlitterParticle.Provider::new);
   }

   @SubscribeEvent
   public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
      event.registerReloadListener(DruidShieldRenderer.getInstance());
   }

   // Texture stitching handled by assets/minecraft/atlases/blocks.json since 1.19.3

   @SubscribeEvent
   public static void registerEntityRenders(RegisterRenderers event) {
      event.registerBlockEntityRenderer(BlockEntityInit.beam, BeamRopeRenderer::new);
      event.registerBlockEntityRenderer(BlockEntityInit.rope, RopeKnotRenderer::new);
      event.registerEntityRenderer(EntityInit.beetle, BeetleRenderer::new);
      event.registerEntityRenderer(EntityInit.dreadfish, DreadfishRenderer::new);
      event.registerEntityRenderer(EntityInit.lunar_moth, LunarMothRenderer::new);
   }

   @SubscribeEvent
   public static void registerLayerDefinition(RegisterLayerDefinitions event) {
      event.registerLayerDefinition(ModelLayerInit.BEAM_ROPE_LAYER, BeamRopeRenderer.BeamRopeModel::buildModel);
      event.registerLayerDefinition(ModelLayerInit.ROPE_KNOT_LAYER, RopeKnotRenderer.RopeKnotModel::buildModel);
      event.registerLayerDefinition(ModelLayerInit.BONE_SHIELD_LAYER, BoneShieldModel::createLayer);
      event.registerLayerDefinition(ModelLayerInit.CHITIN_SHIELD_LAYER, ChitinShieldModel::createLayer);
      event.registerLayerDefinition(ModelLayerInit.MOONSTONE_SHIELD_LAYER, MoonstoneShieldModel::createLayer);
      event.registerLayerDefinition(ModelLayerInit.BEETLE_LAYER, BeetleModel::createBodyLayer);
      event.registerLayerDefinition(ModelLayerInit.DREADFISH_LAYER, DreadfishModel::createBodyLayer);
      event.registerLayerDefinition(ModelLayerInit.LUNAR_MOTH_LAYER, LunarMothModel::createBodyLayer);
      event.registerLayerDefinition(ModelLayerInit.TRAVEL_PACK_LAYER, () -> TravelPackModel.createLayer(false));
      event.registerLayerDefinition(ModelLayerInit.BEDROLL_TRAVEL_PACK_LAYER, () -> TravelPackModel.createLayer(true));
   }

   @SubscribeEvent
   public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
      for (String skin : event.getSkins()) {
         if (event.getSkin(skin) instanceof PlayerRenderer renderer) {
            renderer.addLayer(new TravelPackLayer(renderer, event.getEntityModels()));
         }
      }
   }
}
