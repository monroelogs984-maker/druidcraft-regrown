package com.vulp.druidcraftrg.events;

import com.vulp.druidcraftrg.DruidcraftRegrownRegistry;
import com.vulp.druidcraftrg.blocks.BedrollBlock;
import com.vulp.druidcraftrg.capabilities.TempSpawnProvider;
import com.vulp.druidcraftrg.config.DruidcraftConfig;
import com.vulp.druidcraftrg.init.ItemInit;
import com.vulp.druidcraftrg.inventory.TravelPackInventory;
import com.vulp.druidcraftrg.items.TravelPackItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "druidcraftrg",
   bus = Bus.FORGE
)
public class MiscEvents {
   // 1.16.5 EventHandler.onBlockBroken seed drops — grass has a chance to drop hemp seeds
   @SubscribeEvent
   public static void onBlockBroken(BlockEvent.BreakEvent event) {
      if (!DruidcraftConfig.drop_seeds.get()) {
         return;
      }
      LevelAccessor level = event.getLevel();
      if (level.isClientSide() || event.getPlayer().isCreative()
         || event.getPlayer().getMainHandItem().is(Items.SHEARS)) {
         return;
      }
      BlockState state = event.getState();
      if (state.is(Blocks.GRASS) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.FERN) || state.is(Blocks.LARGE_FERN)) {
         if (Math.random() <= DruidcraftConfig.hemp_seed_drops.get() / 100.0D) {
            BlockPos pos = event.getPos();
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            ((Level) level).addFreshEntity(new ItemEntity(
               (Level) level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, new ItemStack(ItemInit.hemp_seeds)));
         }
      }
   }

   @SubscribeEvent
   public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof Player) {
         event.addCapability(DruidcraftRegrownRegistry.location("temprespawn"), new TempSpawnProvider());
      }
   }

   // bedrolls are camping gear, not respawn points
   @SubscribeEvent
   public static void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
      Player player = event.getEntity();
      BlockPos pos = event.getNewSpawn();
      if (pos != null && !player.level().isClientSide && player.level().getBlockState(pos).getBlock() instanceof BedrollBlock) {
         event.setCanceled(true);
      }
   }

   // return a pack-deployed bedroll to the travel pack when the player wakes
   @SubscribeEvent
   public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
      Player player = event.getEntity();
      ItemStack itemStack = player.getMainHandItem();
      if (!(itemStack.getItem() instanceof TravelPackItem)) {
         itemStack = player.getOffhandItem();
         if (!(itemStack.getItem() instanceof TravelPackItem)) {
            return;
         }
      }
      CompoundTag nbt = itemStack.getOrCreateTag();
      if (nbt.contains("BedrollPosX")) {
         BlockPos pos = new BlockPos(nbt.getInt("BedrollPosX"), nbt.getInt("BedrollPosY"), nbt.getInt("BedrollPosZ"));
         Level level = player.level();
         BlockState blockState = level.getBlockState(pos);
         if (blockState.getBlock() instanceof BedrollBlock bedroll) {
            nbt.putInt("Color", bedroll.getColor().getId());
            TravelPackInventory inventory = new TravelPackInventory(itemStack);
            inventory.setItem(0, new ItemStack(bedroll));
            inventory.writeItemStack();
            Direction facing = blockState.getValue(HorizontalDirectionalBlock.FACING);
            BlockState headBlock = level.getBlockState(pos);
            BlockState footBlock = level.getBlockState(pos.relative(facing.getOpposite()));
            level.setBlockAndUpdate(pos, headBlock.getValue(BedrollBlock.WATERLOGGED)
               ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(pos.relative(facing.getOpposite()), footBlock.getValue(BedrollBlock.WATERLOGGED)
               ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
         }
         nbt.remove("BedrollPosX");
         nbt.remove("BedrollPosY");
         nbt.remove("BedrollPosZ");
      }
   }
}
