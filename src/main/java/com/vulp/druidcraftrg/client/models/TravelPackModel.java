package com.vulp.druidcraftrg.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// 1.16.5 TravelPackModel/BedrollTravelPackModel merged; cubes are in body-local space and
// the layer renders this under the body part's pose, so sneak/swing follow automatically
@OnlyIn(Dist.CLIENT)
public class TravelPackModel extends Model {
   private final ModelPart pack;

   public TravelPackModel(ModelPart root) {
      super(RenderType::entityCutoutNoCull);
      this.pack = root.getChild("pack");
   }

   public static LayerDefinition createLayer(boolean bedroll) {
      MeshDefinition mesh = new MeshDefinition();
      CubeListBuilder cubes = CubeListBuilder.create()
         .texOffs(0, 16).addBox(-4.0F, 1.0F, 3.1F, 8.0F, 10.0F, 4.0F)
         .texOffs(24, 16).addBox(-3.5F, 6.0F, 7.1F, 7.0F, 4.0F, 2.0F)
         .texOffs(12, 30).mirror().addBox(-6.0F, 6.0F, 3.1F, 2.0F, 4.0F, 4.0F).mirror(false)
         .texOffs(0, 30).addBox(4.0F, 6.0F, 3.1F, 2.0F, 4.0F, 4.0F)
         .texOffs(24, 22).addBox(-4.0F, 1.0F, 7.1F, 8.0F, 4.0F, 1.0F);
      if (bedroll) {
         cubes.texOffs(24, 0).addBox(-5.5F, -3.0F, 3.1F, 11.0F, 4.0F, 4.0F);
      }
      PartDefinition pack = mesh.getRoot().addOrReplaceChild("pack", cubes, PartPose.ZERO);
      pack.addOrReplaceChild("straps", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.1F)), PartPose.ZERO);
      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.pack.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
