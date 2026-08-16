package com.vulp.druidcraftrg.events;

import com.vulp.druidcraftrg.entities.TameableMonsterEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class MonsterTameEvent extends LivingEvent implements ICancellableEvent {
   private final TameableMonsterEntity monster;
   private final Player tamer;

   public MonsterTameEvent(TameableMonsterEntity monster, Player tamer) {
      super(monster);
      this.monster = monster;
      this.tamer = tamer;
   }

   public TameableMonsterEntity getMonster() {
      return monster;
   }

   public Player getTamer() {
      return tamer;
   }
}
