package com.vulp.druidcraftrg.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DeployBedrollMessage(int entityId) implements CustomPacketPayload {
   public static final Type<DeployBedrollMessage> TYPE = new Type<>(
      ResourceLocation.fromNamespaceAndPath("druidcraftrg", "deploy_bedroll"));
   public static final StreamCodec<ByteBuf, DeployBedrollMessage> STREAM_CODEC =
      StreamCodec.composite(ByteBufCodecs.INT, DeployBedrollMessage::entityId, DeployBedrollMessage::new);

   @Override
   public Type<DeployBedrollMessage> type() {
      return TYPE;
   }
}
