package com.vulp.druidcraftrg.inventory.container;

import com.google.common.collect.Lists;
import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.init.SoundEventInit;
import com.vulp.druidcraftrg.init.ContainerInit;
import com.vulp.druidcraftrg.init.RecipeInit;
import com.vulp.druidcraftrg.recipes.WoodcuttingRecipe;
import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// stonecutter menu clone driven by the woodcutting recipe type
public class WoodcutterMenu extends AbstractContainerMenu {
   public static final int INPUT_SLOT = 0;
   public static final int RESULT_SLOT = 1;
   private final ContainerLevelAccess access;
   private final DataSlot selectedRecipeIndex = DataSlot.standalone();
   private final Level level;
   private List<WoodcuttingRecipe> recipes = Lists.newArrayList();
   private ItemStack input = ItemStack.EMPTY;
   long lastSoundTime;
   final Slot inputSlot;
   final Slot resultSlot;
   Runnable slotUpdateListener = () -> {
   };
   public final Container container = new SimpleContainer(1) {
      @Override
      public void setChanged() {
         super.setChanged();
         WoodcutterMenu.this.slotsChanged(this);
         WoodcutterMenu.this.slotUpdateListener.run();
      }
   };
   final ResultContainer resultContainer = new ResultContainer();

   public WoodcutterMenu(int containerId, Inventory inventory) {
      this(containerId, inventory, ContainerLevelAccess.NULL);
   }

   public WoodcutterMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
      super(ContainerInit.WOODCUTTER, containerId);
      this.access = access;
      this.level = inventory.player.level();
      this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
      this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
         @Override
         public boolean mayPlace(ItemStack stack) {
            return false;
         }

         @Override
         public void onTake(Player player, ItemStack stack) {
            stack.onCraftedBy(player.level(), player, stack.getCount());
            WoodcutterMenu.this.resultContainer.awardUsedRecipes(player, List.of(WoodcutterMenu.this.inputSlot.getItem()));
            ItemStack remaining = WoodcutterMenu.this.inputSlot.remove(1);
            if (!remaining.isEmpty()) {
               WoodcutterMenu.this.setupResultSlot();
            }
            access.execute((lvl, pos) -> {
               long gameTime = lvl.getGameTime();
               if (WoodcutterMenu.this.lastSoundTime != gameTime) {
                  lvl.playSound(null, pos, SoundEventInit.woodcutter_take_result, SoundSource.BLOCKS, 1.0F, 1.0F);
                  WoodcutterMenu.this.lastSoundTime = gameTime;
               }
            });
            super.onTake(player, stack);
         }
      });

      for (int row = 0; row < 3; row++) {
         for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
         }
      }
      for (int col = 0; col < 9; col++) {
         this.addSlot(new Slot(inventory, col, 8 + col * 18, 142));
      }

      this.addDataSlot(this.selectedRecipeIndex);
   }

   public int getSelectedRecipeIndex() {
      return this.selectedRecipeIndex.get();
   }

   public List<WoodcuttingRecipe> getRecipes() {
      return this.recipes;
   }

   public int getNumRecipes() {
      return this.recipes.size();
   }

   public boolean hasInputItem() {
      return this.inputSlot.hasItem() && !this.recipes.isEmpty();
   }

   @Override
   public boolean stillValid(Player player) {
      return stillValid(this.access, player, BlockInit.woodcutter);
   }

   @Override
   public boolean clickMenuButton(Player player, int index) {
      if (this.isValidRecipeIndex(index)) {
         this.selectedRecipeIndex.set(index);
         this.setupResultSlot();
      }
      return true;
   }

   private boolean isValidRecipeIndex(int index) {
      return index >= 0 && index < this.recipes.size();
   }

   @Override
   public void slotsChanged(Container container) {
      ItemStack itemstack = this.inputSlot.getItem();
      if (!itemstack.is(this.input.getItem())) {
         this.input = itemstack.copy();
         this.setupRecipeList(container, itemstack);
      }
   }

   private void setupRecipeList(Container container, ItemStack stack) {
      this.recipes.clear();
      this.selectedRecipeIndex.set(-1);
      this.resultSlot.set(ItemStack.EMPTY);
      if (!stack.isEmpty()) {
         this.recipes = this.level.getRecipeManager().getRecipesFor(RecipeInit.WOODCUTTING_TYPE, container, this.level);
      }
   }

   void setupResultSlot() {
      if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
         WoodcuttingRecipe recipe = this.recipes.get(this.selectedRecipeIndex.get());
         ItemStack result = recipe.assemble(this.container, this.level.registryAccess());
         if (result.isItemEnabled(this.level.enabledFeatures())) {
            this.resultContainer.setRecipeUsed(recipe);
            this.resultSlot.set(result);
         } else {
            this.resultSlot.set(ItemStack.EMPTY);
         }
      } else {
         this.resultSlot.set(ItemStack.EMPTY);
      }
      this.broadcastChanges();
   }

   @Override
   public MenuType<?> getType() {
      return ContainerInit.WOODCUTTER;
   }

   public void registerUpdateListener(Runnable listener) {
      this.slotUpdateListener = listener;
   }

   @Override
   public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
      return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
   }

   @Override
   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack slotStack = slot.getItem();
         Item item = slotStack.getItem();
         itemstack = slotStack.copy();
         if (index == 1) {
            item.onCraftedBy(slotStack, player.level(), player);
            if (!this.moveItemStackTo(slotStack, 2, 38, true)) {
               return ItemStack.EMPTY;
            }
            slot.onQuickCraft(slotStack, itemstack);
         } else if (index == 0) {
            if (!this.moveItemStackTo(slotStack, 2, 38, false)) {
               return ItemStack.EMPTY;
            }
         } else if (this.level.getRecipeManager().getRecipeFor(RecipeInit.WOODCUTTING_TYPE, new SimpleContainer(slotStack), this.level).isPresent()) {
            if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
               return ItemStack.EMPTY;
            }
         } else if (index >= 2 && index < 29) {
            if (!this.moveItemStackTo(slotStack, 29, 38, false)) {
               return ItemStack.EMPTY;
            }
         } else if (index >= 29 && index < 38 && !this.moveItemStackTo(slotStack, 2, 29, false)) {
            return ItemStack.EMPTY;
         }

         if (slotStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
         }
         slot.setChanged();
         if (slotStack.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
         }
         slot.onTake(player, slotStack);
         this.broadcastChanges();
      }
      return itemstack;
   }

   @Override
   public void removed(Player player) {
      super.removed(player);
      this.resultContainer.removeItemNoUpdate(1);
      this.access.execute((lvl, pos) -> this.clearContainer(player, this.container));
   }
}
