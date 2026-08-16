package com.vulp.druidcraftrg.events;

import com.vulp.druidcraftrg.entities.TameableMonsterEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;

public class EventFactory {

   public static boolean onMonsterTame(TameableMonsterEntity monster, Player tamer) {
      MonsterTameEvent event = new MonsterTameEvent(monster, tamer);
      NeoForge.EVENT_BUS.post(event);
      return event.isCanceled();
   }
}
