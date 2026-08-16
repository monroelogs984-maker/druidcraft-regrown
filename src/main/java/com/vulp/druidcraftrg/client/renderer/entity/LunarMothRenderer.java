package com.vulp.druidcraftrg.client.renderer.entity;

import com.vulp.druidcraftrg.client.models.LunarMothModel;
import com.vulp.druidcraftrg.entities.LunarMothEntity;
import com.vulp.druidcraftrg.init.ModelLayerInit;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LunarMothRenderer extends MobRenderer<LunarMothEntity, LunarMothModel<LunarMothEntity>> {

   public static final ResourceLocation MOTH_TURQUOISE = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/lunar_moth/lunar_moth_turquoise.png");
   public static final ResourceLocation MOTH_WHITE = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/lunar_moth/lunar_moth_white.png");
   public static final ResourceLocation MOTH_LIME = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/lunar_moth/lunar_moth_lime.png");
   public static final ResourceLocation MOTH_ORANGE = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/lunar_moth/lunar_moth_orange.png");
   public static final ResourceLocation MOTH_PINK = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/lunar_moth/lunar_moth_pink.png");
   public static final ResourceLocation MOTH_YELLOW = ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/lunar_moth/lunar_moth_yellow.png");

   public LunarMothRenderer(EntityRendererProvider.Context context) {
      super(context, new LunarMothModel<>(context.bakeLayer(ModelLayerInit.LUNAR_MOTH_LAYER)), 0.2F);
   }

   @Override
   public ResourceLocation getTextureLocation(LunarMothEntity entity) {
      switch (entity.getColor()) {
         case WHITE:
            return MOTH_WHITE;
         case LIME:
            return MOTH_LIME;
         case ORANGE:
            return MOTH_ORANGE;
         case PINK:
            return MOTH_PINK;
         case YELLOW:
            return MOTH_YELLOW;
         default:
            return MOTH_TURQUOISE;
      }
   }

   // moths glow while flying (fullbright), like the 1.16.5 custom "glow" render type
   @Nullable
   @Override
   protected RenderType getRenderType(LunarMothEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
      if (!entity.isResting()) {
         return RenderType.entityTranslucentEmissive(this.getTextureLocation(entity));
      }
      return super.getRenderType(entity, bodyVisible, translucent, glowing);
   }
}
