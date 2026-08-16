package com.vulp.druidcraftrg.events;

import com.vulp.druidcraftrg.blocks.BedrollBlock;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderPlayerEvent.Pre;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "druidcraftrg",
   value = {Dist.CLIENT},
   bus = Bus.GAME
)
public class RenderEvents {
   @SubscribeEvent
   public static void renderPlayerEventPre(Pre event) {
      Player player = event.getEntity();
      if (player.isSleeping() && player.level().getBlockState((BlockPos)player.getSleepingPos().get()).getBlock() instanceof BedrollBlock) {
         HumanoidModel<AbstractClientPlayer> model = (HumanoidModel<AbstractClientPlayer>)event.getRenderer().getModel();
         model.setAllVisible(false);
         model.head.visible = true;
         model.hat.visible = true;
      }
   }
}
