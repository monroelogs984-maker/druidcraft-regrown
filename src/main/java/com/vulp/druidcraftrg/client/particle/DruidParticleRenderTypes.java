package com.vulp.druidcraftrg.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public class DruidParticleRenderTypes {
   // 1.16.5 ICustomParticleRender.PARTICLE_SHEET_TRANSLUCENT_ADDITIVE
   public static final ParticleRenderType TRANSLUCENT_ADDITIVE = new ParticleRenderType() {
      @Override
      public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
         RenderSystem.depthMask(false);
         RenderSystem.setShader(GameRenderer::getParticleShader);
         RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
         return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
      }

      @Override
      public String toString() {
         return "druidcraftrg:PARTICLE_SHEET_TRANSLUCENT_ADDITIVE";
      }
   };
}
