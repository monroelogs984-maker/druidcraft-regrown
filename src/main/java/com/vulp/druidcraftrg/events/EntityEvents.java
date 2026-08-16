package com.vulp.druidcraftrg.events;

import com.vulp.druidcraftrg.DruidcraftRegrown;
import com.vulp.druidcraftrg.DruidcraftRegrownRegistry;
import com.vulp.druidcraftrg.blocks.BedrollBlock;
import com.vulp.druidcraftrg.blocks.RopeBlock;
import com.vulp.druidcraftrg.capabilities.SpawnDataHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.BreakSpeed;

@EventBusSubscriber(modid = DruidcraftRegrown.MODID, bus = Bus.GAME)
public class EntityEvents {
   @SubscribeEvent
   public static void onPlayerBreakBlock(BreakSpeed event) {
      Player player = event.getEntity();
      if (RopeBlock.isEntityInBlock(player) && !player.onGround()) {
         event.setNewSpeed(event.getOriginalSpeed() * 5.0F);
      }
   }

   @SubscribeEvent
   public static void onPlayerSpawnSet(PlayerSetSpawnEvent event) {
      Player player = event.getEntity();
      if (player instanceof ServerPlayer serverPlayer) {
         ServerLevel world = serverPlayer.serverLevel();
         BlockPos pos = event.getNewSpawn();
         if (pos != null && world.getBlockState(pos).getBlock() instanceof BedrollBlock) {
            SpawnDataHolder current = player.getData(DruidcraftRegrownRegistry.TEMP_SPAWN.get());
            ResourceKey<Level> dimension = world.dimension();
            boolean flag = current.hasSpawnData() && pos.equals(current.getPos()) && dimension.equals(current.getDimension());
            if (!flag) {
               player.sendSystemMessage(Component.translatable("block.druidcraftrg.bedroll.set_temp_spawn"));
            }
            player.setData(DruidcraftRegrownRegistry.TEMP_SPAWN.get(), new SpawnDataHolder(pos, dimension, player.getYRot(), false));
            event.setCanceled(true);
         }
      }
   }
}
