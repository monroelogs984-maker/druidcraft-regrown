package com.vulp.druidcraftrg.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vulp.druidcraftrg.entities.DreadfishEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class DreadfishModel extends EntityModel<DreadfishEntity> {
   private final ModelPart spine1;
   private final ModelPart spine2;

   public DreadfishModel(ModelPart root) {
      this.spine1 = root.getChild("spine1");
      this.spine2 = this.spine1.getChild("spine2");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition spine1 = root.addOrReplaceChild("spine1", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-1.5F, -2.5F, -3.0F, 3, 5, 6), PartPose.offset(0.0F, 21.0F, 6.0F));

      PartDefinition spine2 = spine1.addOrReplaceChild("spine2", CubeListBuilder.create()
         .texOffs(18, 0).addBox(-1.0F, -2.5F, 0.0F, 2, 4, 4), PartPose.offset(0.0F, 0.0F, 3.0F));

      PartDefinition head1 = spine1.addOrReplaceChild("head1", CubeListBuilder.create()
         .texOffs(8, 11).addBox(-1.0F, -2.0F, -3.0F, 2, 3, 3), PartPose.offset(0.0F, 0.0F, -3.0F));
      head1.addOrReplaceChild("head2", CubeListBuilder.create()
         .texOffs(0, 20).addBox(-1.0F, 0.0F, -3.0F, 2, 1, 3), PartPose.offset(0.0F, 1.0F, 0.0F));

      spine2.addOrReplaceChild("tail", CubeListBuilder.create()
         .texOffs(0, 11).addBox(0.0F, -2.5F, 0.0F, 0, 5, 4), PartPose.offset(0.0F, 0.0F, 4.0F));

      spine1.addOrReplaceChild("sidefin_r", CubeListBuilder.create()
         .texOffs(16, 20).addBox(-2.0F, 0.0F, -1.0F, 2, 0, 2),
         PartPose.offsetAndRotation(-1.5F, 1.5F, -2.0F, 0.0F, 0.0F, -0.7853982F));
      spine1.addOrReplaceChild("sidefin_l", CubeListBuilder.create()
         .texOffs(0, 25).addBox(0.0F, 0.0F, -1.0F, 2, 0, 2),
         PartPose.offsetAndRotation(1.5F, 1.5F, -2.0F, 0.0F, 0.0F, 0.7853982F));
      spine1.addOrReplaceChild("topfin", CubeListBuilder.create()
         .texOffs(10, 20).addBox(0.0F, -7.5F, 6.0F, 0, 2, 3), PartPose.offset(0.0F, 3.0F, -6.0F));

      return LayerDefinition.create(mesh, 32, 32);
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.spine1.render(poseStack, buffer, packedLight, packedOverlay);
   }

   @Override
   public void setupAnim(DreadfishEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float f = 1.5F;
      float f1 = 1.5F;
      float j = 1.0F;
      float k = 0;
      if (entity.isSitting()) {
         k = 1.575F;
         this.spine2.yRot = -f * 0.25F * Mth.sin(f1 * 0.6F * ageInTicks / 4 * j) / 2;
      } else {
         this.spine2.yRot = -f * 0.25F * Mth.sin(f1 * 0.6F * ageInTicks * j);
      }
      this.spine1.zRot = k;
   }
}
