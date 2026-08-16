package com.vulp.druidcraftrg.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vulp.druidcraftrg.client.models.TravelPackModel;
import com.vulp.druidcraftrg.init.ItemInit;
import com.vulp.druidcraftrg.init.ModelLayerInit;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

// renders the travel pack on the player's back while it sits in the inventory unheld
@OnlyIn(Dist.CLIENT)
public class TravelPackLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
   private static final ResourceLocation REGULAR_PACK_TEX =
      ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/travel_pack/regular.png");
   private final TravelPackModel normalPack;
   private final TravelPackModel bedrollPack;

   public TravelPackLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, EntityModelSet models) {
      super(parent);
      this.normalPack = new TravelPackModel(models.bakeLayer(ModelLayerInit.TRAVEL_PACK_LAYER));
      this.bedrollPack = new TravelPackModel(models.bakeLayer(ModelLayerInit.BEDROLL_TRAVEL_PACK_LAYER));
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
                      float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (player.isInvisible()) {
         return;
      }
      Inventory inventory = player.getInventory();
      ItemStack pack = ItemStack.EMPTY;
      for (int i = 0; i < inventory.items.size(); i++) {
         if (inventory.items.get(i).is(ItemInit.travel_pack) && inventory.selected != i) {
            pack = inventory.items.get(i);
            break;
         }
      }
      if (pack.isEmpty()) {
         return;
      }
      CompoundTag nbt = pack.has(DataComponents.CUSTOM_DATA) ? pack.get(DataComponents.CUSTOM_DATA).copyTag() : null;
      DyeColor bedrollColor = nbt != null && nbt.contains("Color") && nbt.getInt("Color") != -1
         ? DyeColor.byId(nbt.getInt("Color")) : null;
      TravelPackModel model = bedrollColor == null ? this.normalPack : this.bedrollPack;
      ResourceLocation texture = bedrollColor == null ? REGULAR_PACK_TEX
         : ResourceLocation.fromNamespaceAndPath("druidcraftrg", "textures/entity/travel_pack/bedrolled_" + bedrollColor.getName() + ".png");
      poseStack.pushPose();
      this.getParentModel().body.translateAndRotate(poseStack);
      model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(texture)),
         packedLight, OverlayTexture.NO_OVERLAY, -1);
      poseStack.popPose();
   }
}
