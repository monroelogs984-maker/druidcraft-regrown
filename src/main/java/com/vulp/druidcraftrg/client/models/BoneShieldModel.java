package com.vulp.druidcraftrg.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class BoneShieldModel extends Model {
   private final ModelPart root;

   public BoneShieldModel(ModelPart root) {
      super(RenderType::entitySolid);
      this.root = root;
   }

   public static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition cageRight = root.addOrReplaceChild("cage_right",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -31.0F, 0.0F, 6.0F, 10.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, 24.0F, -2.0F, 0.0F, -0.1745F, 0.0F));
      cageRight.addOrReplaceChild("bone2",
         CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -22.6506F, 3.9939F, 4.0F, 4.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

      PartDefinition main = root.addOrReplaceChild("main",
         CubeListBuilder.create()
            .texOffs(20, 0).addBox(-1.0F, -27.0F, -1.0F, 2.0F, 6.0F, 6.0F)
            .texOffs(14, 0).addBox(-1.0F, -31.0F, -2.7F, 2.0F, 10.0F, 1.0F),
         PartPose.offset(0.0F, 24.0F, 0.0F));
      main.addOrReplaceChild("bone",
         CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -22.6506F, 3.9939F, 2.0F, 6.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, 2.0F, -2.7F, 0.1745F, 0.0F, 0.0F));

      PartDefinition cageLeft = root.addOrReplaceChild("cage_left",
         CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-6.0F, -31.0F, 0.0F, 6.0F, 10.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, 24.0F, -2.0F, 0.0F, 0.1745F, 0.0F));
      cageLeft.addOrReplaceChild("bone3",
         CubeListBuilder.create().texOffs(0, 11).mirror().addBox(-4.0F, -22.6506F, 3.9939F, 4.0F, 4.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
      this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
   }
}
