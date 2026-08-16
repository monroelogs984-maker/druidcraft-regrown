package com.vulp.druidcraftrg.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vulp.druidcraftrg.entities.BeetleEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class BeetleModel<T extends BeetleEntity> extends EntityModel<T> {

   private final ModelPart body1;
   private final ModelPart head;
   private final ModelPart antennaeL;
   private final ModelPart antennaeR;
   private final ModelPart mandibles;
   private final ModelPart legLFront;
   private final ModelPart legLMiddle;
   private final ModelPart legLBack;
   private final ModelPart legRFront;
   private final ModelPart legRMiddle;
   private final ModelPart legRBack;
   private final ModelPart harness;
   private final ModelPart chestLFront;
   private final ModelPart chestLBack;
   private final ModelPart chestRFront;
   private final ModelPart chestRBack;
   private final ModelPart saddleMain;
   private final ModelPart saddleFront;
   private final ModelPart saddleBack;

   public BeetleModel(ModelPart root) {
      this.body1 = root.getChild("body_1");
      this.head = this.body1.getChild("head");
      this.antennaeL = this.head.getChild("antennae_l");
      this.antennaeR = this.head.getChild("antennae_r");
      this.mandibles = this.head.getChild("mandibles");
      ModelPart body2 = this.body1.getChild("body_2");
      this.legLFront = body2.getChild("leg_l_front");
      this.legLMiddle = body2.getChild("leg_l_middle");
      this.legLBack = body2.getChild("leg_l_back");
      this.legRFront = body2.getChild("leg_r_front");
      this.legRMiddle = body2.getChild("leg_r_middle");
      this.legRBack = body2.getChild("leg_r_back");
      this.chestLFront = body2.getChild("chest_l_front");
      this.chestLBack = body2.getChild("chest_l_back");
      this.chestRFront = body2.getChild("chest_r_front");
      this.chestRBack = body2.getChild("chest_r_back");
      this.harness = this.body1.getChild("harness");
      this.saddleMain = this.harness.getChild("saddle_main");
      this.saddleFront = this.harness.getChild("saddle_front");
      this.saddleBack = this.harness.getChild("saddle_back");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition body1 = root.addOrReplaceChild("body_1", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-11.0F, -16.5F, -9.0F, 22, 18, 24), PartPose.offset(0.0F, 16.5F, -1.0F));

      PartDefinition head = body1.addOrReplaceChild("head", CubeListBuilder.create()
         .texOffs(68, 42).addBox(-5.0F, -5.0F, -8.0F, 10, 10, 8), PartPose.offset(0.0F, -5.5F, -9.0F));
      head.addOrReplaceChild("antennae_l", CubeListBuilder.create()
         .texOffs(0, 0).addBox(0.0F, -3.0F, -2.0F, 0, 3, 2), PartPose.offset(-2.0F, -3.0F, -8.0F));
      head.addOrReplaceChild("antennae_r", CubeListBuilder.create()
         .texOffs(0, 3).addBox(0.0F, -3.0F, -2.0F, 0, 3, 2), PartPose.offset(2.0F, -3.0F, -8.0F));
      head.addOrReplaceChild("mandibles", CubeListBuilder.create()
         .texOffs(65, 60).addBox(-5.0F, 0.0F, -3.0F, 10, 0, 3), PartPose.offset(0.0F, 4.0F, -8.0F));

      PartDefinition body2 = body1.addOrReplaceChild("body_2", CubeListBuilder.create()
         .texOffs(68, 0).addBox(-8.0F, -8.0F, 0.0F, 16, 16, 6), PartPose.offset(0.0F, -6.5F, 15.0F));

      body2.addOrReplaceChild("leg_l_front", CubeListBuilder.create()
         .texOffs(0, 15).addBox(-8.0F, 0.0F, -2.0F, 8, 4, 4), PartPose.offset(-6.0F, 6.0F, -21.0F));
      body2.addOrReplaceChild("leg_l_middle", CubeListBuilder.create()
         .texOffs(0, 15).addBox(-8.0F, 0.0F, -2.0F, 8, 4, 4), PartPose.offset(-6.0F, 6.0F, -12.0F));
      body2.addOrReplaceChild("leg_l_back", CubeListBuilder.create()
         .texOffs(0, 15).addBox(-8.0F, 0.0F, -2.0F, 8, 4, 4), PartPose.offset(-6.0F, 6.0F, -3.0F));
      body2.addOrReplaceChild("leg_r_front", CubeListBuilder.create()
         .texOffs(0, 15).addBox(0.0F, 0.0F, -2.0F, 8, 4, 4), PartPose.offset(6.0F, 6.0F, -21.0F));
      body2.addOrReplaceChild("leg_r_middle", CubeListBuilder.create()
         .texOffs(0, 15).addBox(0.0F, 0.0F, -2.0F, 8, 4, 4), PartPose.offset(6.0F, 6.0F, -12.0F));
      body2.addOrReplaceChild("leg_r_back", CubeListBuilder.create()
         .texOffs(0, 15).addBox(0.0F, 0.0F, -2.0F, 8, 4, 4), PartPose.offset(6.0F, 6.0F, -3.0F));

      body2.addOrReplaceChild("chest_l_front", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-1.3F, -3.5F, -4.0F, 3, 7, 8), PartPose.offset(-12.7F, 1.5F, -17.0F));
      body2.addOrReplaceChild("chest_l_back", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-1.3F, -3.5F, -4.0F, 3, 7, 8), PartPose.offset(-12.7F, 1.5F, -7.0F));
      body2.addOrReplaceChild("chest_r_front", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-1.7F, -3.5F, -4.0F, 3, 7, 8), PartPose.offset(12.7F, 1.5F, -17.0F));
      body2.addOrReplaceChild("chest_r_back", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-1.7F, -3.5F, -4.0F, 3, 7, 8), PartPose.offset(12.7F, 1.5F, -7.0F));

      PartDefinition harness = body1.addOrReplaceChild("harness", CubeListBuilder.create()
         .texOffs(0, 42).addBox(-11.0F, -22.125F, -10.25F, 22, 18, 24, new CubeDeformation(0.3F)),
         PartPose.offset(0.0F, 5.625F, 1.25F));
      harness.addOrReplaceChild("saddle_main", CubeListBuilder.create()
         .texOffs(44, 84).addBox(-7.0F, -0.5F, -10.0F, 14, 1, 20), PartPose.offset(0.0F, -22.625F, 1.75F));
      harness.addOrReplaceChild("saddle_front", CubeListBuilder.create()
         .texOffs(78, 105).addBox(-7.0F, -1.0F, -1.5F, 14, 2, 3), PartPose.offset(0.0F, -23.125F, -6.75F));
      harness.addOrReplaceChild("saddle_back", CubeListBuilder.create()
         .texOffs(44, 105).addBox(-7.0F, -1.0F, -1.5F, 14, 2, 3), PartPose.offset(0.0F, -23.125F, 10.25F));

      return LayerDefinition.create(mesh, 112, 112);
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
      this.body1.render(poseStack, buffer, packedLight, packedOverlay);
   }

   private void rotationAngles(ModelPart part, float x, float y, float z) {
      part.xRot = x;
      part.yRot = y;
      part.zRot = z;
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.harness.visible = false;
      this.saddleMain.visible = false;
      this.saddleFront.visible = false;
      this.saddleBack.visible = false;
      this.chestLFront.visible = false;
      this.chestLBack.visible = false;
      this.chestRFront.visible = false;
      this.chestRBack.visible = false;

      if (entity.hasSaddle()) {
         this.harness.visible = true;
         this.saddleMain.visible = true;
         this.saddleFront.visible = true;
         this.saddleBack.visible = true;
      }
      if (entity.hasChest()) {
         this.chestLFront.visible = true;
         this.chestLBack.visible = true;
         this.chestRFront.visible = true;
         this.chestRBack.visible = true;
      }

      this.head.yRot = netHeadYaw * 0.012F;
      this.head.xRot = headPitch * 0.012F;
      this.rotationAngles(this.mandibles, 0.4363F, 0.0F, 0.0F);
      this.rotationAngles(this.legLFront, 0.0F, -0.1745F, -0.6109F);
      this.rotationAngles(this.legLMiddle, 0.0F, 0.0F, -0.6109F);
      this.rotationAngles(this.legLBack, 0.0F, 0.1745F, -0.6109F);
      this.rotationAngles(this.legRFront, 0.0F, 0.1745F, 0.6109F);
      this.rotationAngles(this.legRMiddle, 0.0F, 0.0F, 0.6109F);
      this.rotationAngles(this.legRBack, 0.0F, -0.1745F, 0.6109F);
      this.rotationAngles(this.saddleFront, 0.2618F, 0.0F, 0.0F);
      this.rotationAngles(this.saddleBack, -0.2618F, 0.0F, 0.0F);

      float k = entity.isVehicle() ? 8.0F : 4.0F;
      float f3 = -(Mth.cos(limbSwing * 0.6662F * k + 0.0F) * 2.0F) * limbSwingAmount;
      float f4 = -(Mth.cos(limbSwing * 0.6662F * k + (float) Math.PI) * 2.0F) * limbSwingAmount;
      float f5 = -(Mth.cos(limbSwing * 0.6662F * k + ((float) Math.PI / 2F)) * 2.0F) * limbSwingAmount;
      this.legRFront.yRot += f3;
      this.legLFront.yRot += -f3;
      this.legRMiddle.yRot += f4;
      this.legLMiddle.yRot += -f4;
      this.legRBack.yRot += f5;
      this.legLBack.yRot += -f5;

      if (entity.isTamed()) {
         float j1 = (float) Math.abs(((entity.getHealth() / entity.getMaxHealth()) - 1) * 0.75);
         this.rotationAngles(this.antennaeL, j1, 0.0F, 0.0F);
         this.rotationAngles(this.antennaeR, j1, 0.0F, 0.0F);
      }
   }
}
