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

public class ChitinShieldModel extends Model {
   private final ModelPart root;

   public ChitinShieldModel(ModelPart root) {
      super(RenderType::entitySolid);
      this.root = root;
   }

   public static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      root.addOrReplaceChild("bb_main",
         CubeListBuilder.create()
            .texOffs(32, 17).addBox(-1.0F, -27.0F, -1.0F, 2.0F, 6.0F, 6.0F)
            .texOffs(32, 0).addBox(-6.0F, -32.0F, -2.0F, 12.0F, 16.0F, 1.0F),
         PartPose.offset(0.0F, 24.0F, 0.0F));
      root.addOrReplaceChild("chitin_bot_left",
         CubeListBuilder.create().texOffs(0, 10).addBox(-6.9826F, -20.9374F, 0.6326F, 7.0F, 9.0F, 1.0F),
         PartPose.offsetAndRotation(-0.5F, 21.5F, -2.0F, 0.0873F, 0.0873F, 0.0F));
      root.addOrReplaceChild("chitin_bot_right",
         CubeListBuilder.create().texOffs(16, 10).addBox(-0.0174F, -20.9374F, 0.6326F, 7.0F, 9.0F, 1.0F),
         PartPose.offsetAndRotation(0.5F, 21.5F, -2.0F, 0.0873F, -0.0873F, 0.0F));
      root.addOrReplaceChild("chitin_top_left",
         CubeListBuilder.create().texOffs(0, 0).addBox(-6.9826F, -35.8798F, -3.5525F, 7.0F, 9.0F, 1.0F),
         PartPose.offsetAndRotation(-0.5F, 26.5F, -2.0F, -0.0873F, 0.0873F, 0.0F));
      root.addOrReplaceChild("chitin_top_right",
         CubeListBuilder.create().texOffs(16, 0).addBox(-0.0174F, -35.8798F, -3.5525F, 7.0F, 9.0F, 1.0F),
         PartPose.offsetAndRotation(0.5F, 26.5F, -2.0F, -0.0873F, -0.0873F, 0.0F));

      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
      this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
   }
}
