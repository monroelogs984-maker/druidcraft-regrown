package com.vulp.druidcraftrg.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class FierySparkParticle extends TextureSheetParticle {

   public FierySparkParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
      super(level, x, y, z, xSpeed, ySpeed, zSpeed);
      this.xd *= 0.01D;
      this.yd *= 0.05D;
      this.zd *= 0.01D;
      this.quadSize = 0.3F;
      this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
      this.pickSprite(sprites);
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   @Override
   public float getQuadSize(float scaleFactor) {
      float f = ((float) this.age + scaleFactor) / (float) this.lifetime;
      return this.quadSize * (1.0F - f * f * 0.5F);
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ < this.lifetime) {
         this.xd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
         this.zd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
         this.yd -= this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
         this.move(this.xd, this.yd, this.zd);
      } else {
         this.remove();
      }
   }

   @Override
   public int getLightColor(float partialTick) {
      float f = Mth.clamp(((float) this.age + partialTick) / (float) this.lifetime, 0.0F, 1.0F);
      int i = super.getLightColor(partialTick);
      int j = i & 255;
      int k = i >> 16 & 255;
      j += (int) (f * 15.0F * 16.0F);
      if (j > 240) {
         j = 240;
      }
      return j | k << 16;
   }

   public static class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Provider(SpriteSet sprites) {
         this.spriteSet = sprites;
      }

      @Override
      public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
         return new FierySparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
      }
   }
}
