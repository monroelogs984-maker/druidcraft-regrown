package com.vulp.druidcraftrg.events;

import com.vulp.druidcraftrg.blocks.BedrollBlock;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "druidcraftrg",
   value = {Dist.CLIENT},
   bus = Bus.FORGE
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
