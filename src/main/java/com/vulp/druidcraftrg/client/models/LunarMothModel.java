package com.vulp.druidcraftrg.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vulp.druidcraftrg.entities.LunarMothEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class LunarMothModel<T extends LunarMothEntity> extends EntityModel<T> {
   private final ModelPart main;
   private final ModelPart antennae;
   private final ModelPart wing1;
   private final ModelPart wing2;

   public LunarMothModel(ModelPart root) {
      this.main = root.getChild("main");
      this.antennae = this.main.getChild("antennae");
      this.wing1 = this.main.getChild("wing1");
      this.wing2 = this.main.getChild("wing2");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 0.0F));

      main.addOrReplaceChild("body", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-1.0F, -1.0F, -3.0F, 2, 2, 6), PartPose.offset(0.0F, -1.0F, 0.0F));

      main.addOrReplaceChild("antennae", CubeListBuilder.create()
         .texOffs(4, 8).addBox(-2.5F, 0.0F, -3.0F, 2, 0, 3)
         .mirror().addBox(0.5F, 0.0F, -3.0F, 2, 0, 3), PartPose.offset(0.0F, -2.0F, -3.0F));

      main.addOrReplaceChild("wing1", CubeListBuilder.create()
         .texOffs(0, 8).addBox(-6.0F, 0.0F, -5.5F, 6, 0, 14), PartPose.offset(-1.0F, -2.0F, 1.5F));

      main.addOrReplaceChild("wing2", CubeListBuilder.create()
         .texOffs(0, 8).mirror().addBox(0.0F, 0.0F, -5.5F, 6, 0, 14), PartPose.offset(1.0F, -2.0F, 1.5F));

      return LayerDefinition.create(mesh, 32, 32);
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
      this.main.render(poseStack, buffer, packedLight, packedOverlay);
   }

   private void setRotationAngle(ModelPart part, float x, float y, float z) {
      part.xRot = x;
      part.yRot = y;
      part.zRot = z;
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      setRotationAngle(main, -0.3491F, 0.0F, 0.0F);
      setRotationAngle(antennae, -0.6109F, 0.0F, 0.0F);
      setRotationAngle(wing1, 0.0F, 0.0F, Mth.cos(ageInTicks * 1.3F) * 3.1415927F * 0.25F);
      setRotationAngle(wing2, 0.0F, 0.0F, -(Mth.cos(ageInTicks * 1.3F) * 3.1415927F * 0.25F));
   }
}
