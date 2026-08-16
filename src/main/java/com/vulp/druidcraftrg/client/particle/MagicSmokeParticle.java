package com.vulp.druidcraftrg.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

// the "speed" args carry the RGB color, matching the 1.16.5 convention
public class MagicSmokeParticle extends TextureSheetParticle {
   private final SpriteSet spriteSet;

   public MagicSmokeParticle(ClientLevel level, double x, double y, double z, double r, double g, double b, SpriteSet sprites) {
      super(level, x, y, z, r, g, b);
      this.spriteSet = sprites;
      this.xd *= 0.01D;
      this.yd *= 0.01D;
      this.zd *= 0.01D;
      this.quadSize = 0.3F;
      this.gravity = 0.002F;
      this.rCol = (float) r;
      this.gCol = (float) g;
      this.bCol = (float) b;
      this.lifetime = this.random.nextInt(20) + 10;
      this.setSpriteFromAge(sprites);
   }

   @Override
   public ParticleRenderType getRenderType() {
      return DruidParticleRenderTypes.TRANSLUCENT_ADDITIVE;
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ < this.lifetime && this.alpha > 0.0F) {
         this.setSpriteFromAge(this.spriteSet);
         this.xd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
         this.zd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
         this.yd -= this.random.nextFloat() / 5000.0F;
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
         return new MagicSmokeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
      }
   }
}
