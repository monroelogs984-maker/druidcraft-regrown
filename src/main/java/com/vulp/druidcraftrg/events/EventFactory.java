package com.vulp.druidcraftrg.events;

import com.vulp.druidcraftrg.entities.TameableMonsterEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;

public class EventFactory extends ForgeEventFactory {

   public static boolean onMonsterTame(TameableMonsterEntity monster, Player tamer) {
      return MinecraftForge.EVENT_BUS.post(new MonsterTameEvent(monster, tamer));
   }
}
