package com.vulp.druidcraftrg.network;

import com.vulp.druidcraftrg.network.message.DeployBedrollMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

   private static final String PROTOCOL_VERSION = "1";
   private static int nextId = 0;

   public static SimpleChannel instance;

   public static void init() {
      instance = NetworkRegistry.ChannelBuilder
         .named(new ResourceLocation("druidcraftrg", "network"))
         .networkProtocolVersion(() -> PROTOCOL_VERSION)
         .clientAcceptedVersions(PROTOCOL_VERSION::equals)
         .serverAcceptedVersions(PROTOCOL_VERSION::equals)
         .simpleChannel();
      instance.registerMessage(nextId++, DeployBedrollMessage.class,
         DeployBedrollMessage::encode, DeployBedrollMessage::decode, DeployBedrollMessage::handle);
   }
}
