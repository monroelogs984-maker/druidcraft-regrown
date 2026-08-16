package com.vulp.druidcraftrg.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vulp.druidcraftrg.client.models.DreadfishModel;
import com.vulp.druidcraftrg.entities.DreadfishEntity;
import com.vulp.druidcraftrg.init.ModelLayerInit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DreadfishRenderer extends MobRenderer<DreadfishEntity, DreadfishModel> {
   private static final ResourceLocation DREADFISH_HEALTH_FULL = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/dreadfish/dreadfish_0.png");
   private static final ResourceLocation DREADFISH_HEALTH_HIGH = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/dreadfish/dreadfish_1.png");
   private static final ResourceLocation DREADFISH_HEALTH_MEDIUM = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/dreadfish/dreadfish_2.png");
   private static final ResourceLocation DREADFISH_HEALTH_LOW = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/dreadfish/dreadfish_3.png");

   public DreadfishRenderer(EntityRendererProvider.Context context) {
      super(context, new DreadfishModel(context.bakeLayer(ModelLayerInit.DREADFISH_LAYER)), 0.3F);
   }

   @Override
   public ResourceLocation getTextureLocation(DreadfishEntity entity) {
      if (entity.isTamed()) {
         if (entity.getHealth() >= entity.getMaxHealth()) {
            return DREADFISH_HEALTH_FULL;
         } else if (entity.getHealth() >= 16.0F) {
            return DREADFISH_HEALTH_HIGH;
         } else if (entity.getHealth() >= 8.0F) {
            return DREADFISH_HEALTH_MEDIUM;
         } else {
            return DREADFISH_HEALTH_LOW;
         }
      }
      return DREADFISH_HEALTH_FULL;
   }

   @Override
   protected void setupRotations(DreadfishEntity entityLiving, PoseStack matrix, float ageInTicks, float rotationYaw, float partialTicks, float scale) {
      super.setupRotations(entityLiving, matrix, ageInTicks, rotationYaw, partialTicks, scale);
      float f2 = 4.3F * Mth.sin(0.6F * ageInTicks);
      if (!entityLiving.isSitting()) {
         matrix.mulPose(Axis.YP.rotationDegrees(f2));
      } else {
         matrix.mulPose(Axis.XP.rotationDegrees(f2 / 3));
      }
      matrix.translate(0.0D, 0.05 * Mth.sin(ageInTicks * 0.2F), -0.4000000059604645D);
   }
}
