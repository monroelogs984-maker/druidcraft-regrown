package com.vulp.druidcraftrg.events;

import com.vulp.druidcraftrg.entities.TameableMonsterEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;

public class MonsterTameEvent extends LivingEvent {
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
