package com.vulp.druidcraftrg.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vulp.druidcraftrg.DruidcraftRegrownRegistry;
import com.vulp.druidcraftrg.blocks.tile.RopeTileEntity;
import com.vulp.druidcraftrg.init.ModelLayerInit;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class RopeKnotRenderer implements BlockEntityRenderer<RopeTileEntity> {
   public static final ResourceLocation TEXTURE = DruidcraftRegrownRegistry.location("entity/rope/rope_knot");
   private final RopeKnotRenderer.RopeKnotModel MODEL;

   public RopeKnotRenderer(Context context) {
      this.MODEL = new RopeKnotRenderer.RopeKnotModel(context.bakeLayer(ModelLayerInit.ROPE_KNOT_LAYER));
   }

   public void render(RopeTileEntity tileEntity, float v, PoseStack poseStack, MultiBufferSource iRenderTypeBuffer, int i, int i1) {
      poseStack.pushPose();
      poseStack.translate(0.5, -0.5, 0.5);
      this.MODEL.knot.visible = tileEntity.hasKnot();
      Material material = new Material(TextureAtlas.LOCATION_BLOCKS, TEXTURE);
      VertexConsumer vertexConsumer = material.buffer(iRenderTypeBuffer, this.MODEL::renderType);
      this.MODEL.renderToBuffer(poseStack, vertexConsumer, i, i1, -1);
      poseStack.popPose();
   }

   public static class RopeKnotModel extends Model {
      private final ModelPart knot;

      public RopeKnotModel(ModelPart root) {
         super(RenderType::entityCutout);
         this.knot = root.getChild("knot");
      }

      public static LayerDefinition buildModel() {
         MeshDefinition meshdefinition = new MeshDefinition();
         PartDefinition partdefinition = meshdefinition.getRoot();
         partdefinition.addOrReplaceChild(
            "knot",
            CubeListBuilder.create()
               .texOffs(0, 8)
               .addBox(-10.0F, -10.0F, 6.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F))
               .texOffs(0, 0)
               .addBox(-10.0F, -10.0F, 6.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.8F)),
            PartPose.offset(8.0F, 24.0F, -8.0F)
         );
         return LayerDefinition.create(meshdefinition, 16, 16);
      }

      public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
         this.knot.render(poseStack, buffer, packedLight, packedOverlay);
      }
   }
}
