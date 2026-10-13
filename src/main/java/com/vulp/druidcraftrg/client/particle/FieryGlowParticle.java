package com.vulp.druidcraftrg.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

// pulsing glow at the heart of the fiery torch flame — plain translucent sheet (the old additive
// custom render type ignored texture alpha and is not understood by Iris/Sodium)
public class FieryGlowParticle extends TextureSheetParticle {
   private final float rotSpeed;

   public FieryGlowParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
      super(level, x, y, z, xSpeed, ySpeed, zSpeed);
      this.xd = this.xd * 0.01D + xSpeed;
      this.yd = this.yd * 0.01D + ySpeed;
      this.zd = this.zd * 0.01D + zSpeed;
      this.x += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.z += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
      this.rotSpeed = ((float) Math.random() - 0.5F) * 0.05F;
      this.roll = (float) Math.random() * ((float) Math.PI * 2F);
      this.pickSprite(sprites);
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   public float getQuadSize(float scaleFactor) {
      return (float) (Mth.cos((float) this.age / 2) * -0.035 + 0.125);
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.oRoll = this.roll;
      if (this.age++ < this.lifetime) {
         this.roll += (float) Math.PI * this.rotSpeed * 2.0F;
         this.move(this.xd, this.yd, this.zd);
      } else {
         this.remove();
      }
   }

   @Override
   public int getLightColor(float partialTick) {
      return 0xF000F0;
   }

   public static class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Provider(SpriteSet sprites) {
         this.spriteSet = sprites;
      }

      @Override
      public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
         return new FieryGlowParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
      }
   }
}
