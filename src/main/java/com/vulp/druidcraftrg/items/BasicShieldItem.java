package com.vulp.druidcraftrg.items;

import com.vulp.druidcraftrg.client.renderer.DruidShieldRenderer;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class BasicShieldItem extends Item {

   private final Item repairMaterial;

   public BasicShieldItem(Properties properties, Item repairItem) {
      super(properties);
      DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
      this.repairMaterial = repairItem;
   }

   @Override
   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.BLOCK;
   }

   @Override
   public int getUseDuration(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
      return 72000;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      player.startUsingItem(hand);
      return InteractionResultHolder.consume(itemstack);
   }

   @Override
   public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
      return this.repairMaterial == repair.getItem() || super.isValidRepairItem(toRepair, repair);
   }

   @Override
   public void initializeClient(Consumer<IClientItemExtensions> consumer) {
      consumer.accept(new IClientItemExtensions() {
         @Override
         public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return DruidShieldRenderer.getInstance();
         }
      });
   }
}
