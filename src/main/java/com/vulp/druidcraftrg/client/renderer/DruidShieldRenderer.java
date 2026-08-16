package com.vulp.druidcraftrg.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vulp.druidcraftrg.client.models.BoneShieldModel;
import com.vulp.druidcraftrg.client.models.ChitinShieldModel;
import com.vulp.druidcraftrg.client.models.MoonstoneShieldModel;
import com.vulp.druidcraftrg.init.ItemInit;
import com.vulp.druidcraftrg.init.ModelLayerInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DruidShieldRenderer extends BlockEntityWithoutLevelRenderer {
   public static final ResourceLocation BONE_SHIELD_TEX = new ResourceLocation("druidcraftrg", "entity/shields/bone");
   public static final ResourceLocation CHITIN_SHIELD_TEX = new ResourceLocation("druidcraftrg", "entity/shields/chitin");
   public static final ResourceLocation MOONSTONE_SHIELD_TEX = new ResourceLocation("druidcraftrg", "entity/shields/moonstone");

   private static DruidShieldRenderer instance;

   private BoneShieldModel boneShield;
   private ChitinShieldModel chitinShield;
   private MoonstoneShieldModel moonstoneShield;

   public DruidShieldRenderer() {
      super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
   }

   public static DruidShieldRenderer getInstance() {
      if (instance == null) {
         instance = new DruidShieldRenderer();
      }
      return instance;
   }

   @Override
   public void onResourceManagerReload(ResourceManager resourceManager) {
      // drop baked parts so the next render picks up the reloaded model set
      this.boneShield = null;
      this.chitinShield = null;
      this.moonstoneShield = null;
   }

   private void bakeModels() {
      EntityModelSet models = Minecraft.getInstance().getEntityModels();
      this.boneShield = new BoneShieldModel(models.bakeLayer(ModelLayerInit.BONE_SHIELD_LAYER));
      this.chitinShield = new ChitinShieldModel(models.bakeLayer(ModelLayerInit.CHITIN_SHIELD_LAYER));
      this.moonstoneShield = new MoonstoneShieldModel(models.bakeLayer(ModelLayerInit.MOONSTONE_SHIELD_LAYER));
   }

   @Override
   public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      if (this.boneShield == null) {
         this.bakeModels();
      }
      Model model;
      ResourceLocation texture;
      Item item = stack.getItem();
      if (item == ItemInit.bone_shield) {
         model = this.boneShield;
         texture = BONE_SHIELD_TEX;
      } else if (item == ItemInit.chitin_shield) {
         model = this.chitinShield;
         texture = CHITIN_SHIELD_TEX;
      } else {
         model = this.moonstoneShield;
         texture = MOONSTONE_SHIELD_TEX;
      }
      poseStack.pushPose();
      poseStack.scale(1.0F, -1.0F, -1.0F);
      Material material = new Material(InventoryMenu.BLOCK_ATLAS, texture);
      VertexConsumer vertexConsumer = material.sprite()
         .wrap(ItemRenderer.getFoilBufferDirect(buffer, model.renderType(material.atlasLocation()), true, stack.hasFoil()));
      model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
      poseStack.popPose();
   }
}
