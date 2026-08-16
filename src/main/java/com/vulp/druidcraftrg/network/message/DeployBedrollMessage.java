package com.vulp.druidcraftrg.network.message;

import com.vulp.druidcraftrg.items.TravelPackItem;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class DeployBedrollMessage {

   private final int entityId;

   public DeployBedrollMessage(int entityId) {
      this.entityId = entityId;
   }

   public static void encode(DeployBedrollMessage message, FriendlyByteBuf buffer) {
      buffer.writeInt(message.entityId);
   }

   public static DeployBedrollMessage decode(FriendlyByteBuf buffer) {
      return new DeployBedrollMessage(buffer.readInt());
   }

   public static void handle(DeployBedrollMessage message, Supplier<NetworkEvent.Context> supplier) {
      supplier.get().enqueueWork(() -> {
         ServerPlayer sender = supplier.get().getSender();
         if (sender != null) {
            TravelPackItem.deployBedroll(message.entityId, sender.level());
         }
      });
      supplier.get().setPacketHandled(true);
   }
}
