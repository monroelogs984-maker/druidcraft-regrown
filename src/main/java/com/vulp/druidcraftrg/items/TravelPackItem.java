package com.vulp.druidcraftrg.items;

import com.vulp.druidcraftrg.blocks.BedrollBlock;
import com.vulp.druidcraftrg.entities.TameableMonsterEntity;
import com.vulp.druidcraftrg.inventory.TravelPackInventory;
import com.vulp.druidcraftrg.inventory.container.TravelPackContainer;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.AABB;

public class TravelPackItem extends Item {

   public TravelPackItem(Properties properties) {
      super(properties);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
         serverPlayer.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
               return itemstack.getHoverName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
               return new TravelPackContainer(id, playerInventory,
                  new TravelPackInventory(itemstack, playerEntity.level().registryAccess()), itemstack);
            }
         });
      }
      return InteractionResultHolder.success(itemstack);
   }

   public static void deployBedroll(int entityId, @Nullable Level level) {
      Entity entity = level.getEntity(entityId);
      if (!(entity instanceof Player player)) {
         return;
      }
      ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof TravelPackItem
         ? player.getItemInHand(InteractionHand.MAIN_HAND)
         : player.getItemInHand(InteractionHand.OFF_HAND);
      if (!(itemStack.getItem() instanceof TravelPackItem)) {
         return;
      }
      TravelPackInventory inventory = new TravelPackInventory(itemStack, level.registryAccess());
      if (!(inventory.getItem(0).getItem() instanceof BedItem bedItem) || !(bedItem.getBlock() instanceof BedrollBlock bedrollBlock)) {
         return;
      }
      BlockPos pos = player.blockPosition();
      Direction direction = player.getDirection();
      if (!player.isCreative()) {
         AABB restArea = new AABB((double) pos.getX() - 10.0D, (double) pos.getY() - 6.0D, (double) pos.getZ() - 10.0D,
            (double) pos.getX() + 10.0D, (double) pos.getY() + 6.0D, (double) pos.getZ() + 10.0D);
         List<Monster> monsters = level.getEntitiesOfClass(Monster.class, restArea,
            monster -> monster.isPreventingPlayerRest(player));
         List<TameableMonsterEntity> tameables = level.getEntitiesOfClass(TameableMonsterEntity.class, restArea,
            monster -> monster.preventPlayerSleep(player));
         if (!monsters.isEmpty() || !tameables.isEmpty()) {
            player.displayClientMessage(Component.translatable("block.druidcraftrg.bedroll.fail.monsters"), true);
            return;
         }
      }
      if (!level.dimensionType().bedWorks()) {
         player.displayClientMessage(Component.translatable("block.druidcraftrg.bedroll.fail.dimension"), true);
         return;
      }
      BlockPos bedrollPos = bedrollPlacementHelper(level, pos, direction, bedrollBlock);
      if (bedrollPos != null) {
         // write bedroll pos first so writeItemStack() merges Items on top of it
         CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
         nbt.putInt("BedrollPosX", bedrollPos.getX());
         nbt.putInt("BedrollPosY", bedrollPos.getY());
         nbt.putInt("BedrollPosZ", bedrollPos.getZ());
         itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
         inventory.removeItemNoUpdate(0);
         inventory.writeItemStack();
         // set Color after writeItemStack so existing nbt keys are preserved
         CompoundTag nbt2 = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
         nbt2.putInt("Color", -1);
         itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt2));
         player.startSleepInBed(bedrollPos).ifLeft(problem -> {
            if (problem.getMessage() != null) {
               player.displayClientMessage(problem.getMessage(), true);
            }
         });
         return;
      }
      player.displayClientMessage(Component.translatable("block.druidcraftrg.bedroll.fail.placement"), true);
   }

   @Nullable
   public static BlockPos bedrollPlacementHelper(Level level, BlockPos pos, Direction direction, Block bedroll) {
      BlockPos placed = tryPlaceBedroll(level, pos, direction, bedroll);
      if (placed != null) {
         return placed;
      }
      for (int i = -1; i < 2; i++) {
         for (int j = -1; j < 2; j++) {
            for (int k = -1; k < 2; k++) {
               placed = tryPlaceBedroll(level, pos.offset(i, j, k), direction, bedroll);
               if (placed != null) {
                  return placed;
               }
            }
         }
      }
      return null;
   }

   @Nullable
   private static BlockPos tryPlaceBedroll(Level level, BlockPos foot, Direction direction, Block bedroll) {
      BlockPos head = foot.relative(direction);
      if (level.getBlockState(head).canBeReplaced() && level.getBlockState(foot).canBeReplaced()) {
         level.setBlockAndUpdate(foot, bedroll.defaultBlockState()
            .setValue(HorizontalDirectionalBlock.FACING, direction)
            .setValue(BedBlock.PART, BedPart.FOOT)
            .setValue(BedrollBlock.WATERLOGGED, level.getBlockState(foot).getBlock() == Blocks.WATER));
         level.setBlockAndUpdate(head, bedroll.defaultBlockState()
            .setValue(HorizontalDirectionalBlock.FACING, direction)
            .setValue(BedBlock.PART, BedPart.HEAD)
            .setValue(BedrollBlock.WATERLOGGED, level.getBlockState(head).getBlock() == Blocks.WATER));
         return head;
      }
      return null;
   }

   @Override
   public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean isHeldItem) {
      if (level.isClientSide) return;
      TravelPackInventory inventory = new TravelPackInventory(itemStack, level.registryAccess());
      ItemStack bedroll = inventory.getItem(0);
      int colorId = bedroll.getItem() instanceof BedItem bed && bed.getBlock() instanceof BedrollBlock bedrollBlock
         ? bedrollBlock.getColor().getId()
         : -1;
      CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (nbt.contains("Color") && nbt.getInt("Color") == colorId) return;
      nbt.putInt("Color", colorId);
      itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
   }

   @Override
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
      if (!Screen.hasShiftDown()) {
         tooltip.add(Component.translatable("item.druidcraftrg.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      } else {
         tooltip.add(Component.translatable("item.druidcraftrg.travel_pack.description1").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      }
   }

   @Override
   public boolean onDroppedByPlayer(ItemStack item, Player player) {
      return !(player.containerMenu instanceof TravelPackContainer);
   }
}
