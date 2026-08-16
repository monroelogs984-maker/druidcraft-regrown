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

public class MoonstoneShieldModel extends Model {
   private final ModelPart root;

   public MoonstoneShieldModel(ModelPart root) {
      super(RenderType::entitySolid);
      this.root = root;
   }

   public static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      root.addOrReplaceChild("main",
         CubeListBuilder.create()
            .texOffs(30, 6).addBox(-1.0F, -27.0F, -1.0F, 2.0F, 6.0F, 6.0F)
            .texOffs(0, 0).addBox(-3.0F, -35.0F, -2.0F, 6.0F, 22.0F, 1.0F),
         PartPose.offset(0.0F, 24.0F, 0.0F));
      root.addOrReplaceChild("plate_left",
         CubeListBuilder.create()
            .texOffs(14, 0).addBox(-3.0F, -35.0F, 0.0F, 3.0F, 22.0F, 1.0F)
            .texOffs(30, 3).addBox(-4.0F, -15.0F, 0.0F, 1.0F, 2.0F, 1.0F)
            .texOffs(30, 0).addBox(-4.0F, -35.0F, 0.0F, 1.0F, 2.0F, 1.0F),
         PartPose.offsetAndRotation(-3.0F, 24.0F, -2.0F, 0.0F, 0.3491F, 0.0F));
      root.addOrReplaceChild("plate_right",
         CubeListBuilder.create()
            .texOffs(22, 0).addBox(0.0F, -35.0F, 0.0F, 3.0F, 22.0F, 1.0F)
            .texOffs(34, 3).addBox(3.0F, -15.0F, 0.0F, 1.0F, 2.0F, 1.0F)
            .texOffs(34, 0).addBox(3.0F, -35.0F, 0.0F, 1.0F, 2.0F, 1.0F),
         PartPose.offsetAndRotation(3.0F, 24.0F, -2.0F, 0.0F, -0.3491F, 0.0F));

      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
      this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
   }
}
