package com.vulp.druidcraftrg.client.renderer.entity;

import com.vulp.druidcraftrg.client.models.BeetleModel;
import com.vulp.druidcraftrg.entities.BeetleEntity;
import com.vulp.druidcraftrg.init.ModelLayerInit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BeetleRenderer extends MobRenderer<BeetleEntity, BeetleModel<BeetleEntity>> {
   private static final ResourceLocation BEETLE = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/beetle/beetle.png");

   public BeetleRenderer(EntityRendererProvider.Context context) {
      super(context, new BeetleModel<>(context.bakeLayer(ModelLayerInit.BEETLE_LAYER)), 1.2F);
   }

   @Override
   public ResourceLocation getTextureLocation(BeetleEntity entity) {
      return BEETLE;
   }
}
