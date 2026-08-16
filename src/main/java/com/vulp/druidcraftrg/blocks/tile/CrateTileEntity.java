package com.vulp.druidcraftrg.blocks.tile;

import com.vulp.druidcraftrg.blocks.CrateBlock;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import com.vulp.druidcraftrg.inventory.MultiSidedInventory;
import com.vulp.druidcraftrg.inventory.container.CrateContainer;
import com.vulp.druidcraftrg.state.properties.CrateType;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CrateTileEntity extends RandomizableContainerBlockEntity {
   private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
   protected int lastOpenCount = 0;
   protected int openCount;
   private int tickInterval;
   private LazyOptional<IItemHandlerModifiable> crateHandler;
   private List<BlockPos> crateArray;

   protected CrateTileEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
      super(blockEntityType, pos, state);
   }

   public CrateTileEntity(BlockPos pos, BlockState state) {
      super(BlockEntityInit.crate, pos, state);
   }

   public static void serverTick(Level level, BlockPos pos, BlockState state, CrateTileEntity blockEntity) {
      int x = blockEntity.worldPosition.getX();
      int y = blockEntity.worldPosition.getY();
      int z = blockEntity.worldPosition.getZ();
      blockEntity.tickInterval++;
      boolean wasOpen = blockEntity.lastOpenCount > 0;
      blockEntity.openCount = getOpenCount(blockEntity.level, blockEntity, blockEntity.tickInterval, x, y, z, blockEntity.openCount);
      if (!wasOpen && blockEntity.openCount > 0) {
         blockEntity.playSound(SoundEvents.CHEST_OPEN);
      } else if (wasOpen && blockEntity.openCount == 0) {
         blockEntity.playSound(SoundEvents.CHEST_CLOSE);
      }

      blockEntity.lastOpenCount = blockEntity.openCount;
   }

   public static int getOpenCount(Level world, RandomizableContainerBlockEntity tile, int tickInterval, int xPos, int yPos, int zPos, int openCount) {
      if (!world.isClientSide && openCount != 0 && (tickInterval + xPos + yPos + zPos) % 200 == 0) {
         openCount = getOpenCount(world, tile, xPos, yPos, zPos);
      }

      return openCount;
   }

   public static int getOpenCount(Level world, RandomizableContainerBlockEntity tile, int xPos, int yPos, int zPos) {
      int ticker = 0;
      float i = 5.0F;

      for (Player playerentity : world.getEntitiesOfClass(
         Player.class,
         new AABB(
            (double)((float)xPos - i),
            (double)((float)yPos - i),
            (double)((float)zPos - i),
            (double)((float)(xPos + 1) + i),
            (double)((float)(yPos + 1) + i),
            (double)((float)(zPos + 1) + i)
         )
      )) {
         if (playerentity.containerMenu instanceof CrateContainer) {
            Container iinventory = ((CrateContainer)playerentity.containerMenu).getContainer();
            if (iinventory == tile || iinventory instanceof MultiSidedInventory && ((MultiSidedInventory)iinventory).contains(tile)) {
               ticker++;
            }
         }
      }

      return ticker;
   }

   private void playSound(SoundEvent soundEvent) {
      CrateType crateType = (CrateType)this.getBlockState().getValue(CrateBlock.TYPE);
      if (Arrays.stream(CrateBlock.CRATE_TYPE_START_POINTS).anyMatch(i -> i == crateType.ordinal())) {
         List<Direction> directions = CrateBlock.getAttachDirectionsFromType(crateType);
         int[] integers = new int[]{0, 0, 0};

         for (Direction dir : directions) {
            integers[dir.getAxis().ordinal()] = dir.getStepX() + dir.getStepY() + dir.getStepZ();
         }

         double d0 = (double)this.worldPosition.getX() + 0.5 + (double)((float)integers[0] / 2.0F);
         double d1 = (double)this.worldPosition.getY() + 0.5 + (double)((float)integers[1] / 2.0F);
         double d2 = (double)this.worldPosition.getZ() + 0.5 + (double)((float)integers[2] / 2.0F);
         this.level
            .playSound(
               null,
               d0,
               d1,
               d2,
               soundEvent,
               SoundSource.BLOCKS,
               0.5F,
               (this.level.random.nextFloat() * 0.1F + 0.9F) * (1.05F - 0.05F * (float)crateType.getCrateSize())
            );
      }
   }

   public boolean triggerEvent(int p_145842_1_, int p_145842_2_) {
      if (p_145842_1_ == 1) {
         this.openCount = p_145842_2_;
         return true;
      } else {
         return super.triggerEvent(p_145842_1_, p_145842_2_);
      }
   }

   public void startOpen(Player player) {
      if (!player.isSpectator()) {
         if (this.openCount < 0) {
            this.openCount = 0;
         }

         this.openCount++;
         this.signalOpenCount();
      }
   }

   public void stopOpen(Player player) {
      if (!player.isSpectator()) {
         this.openCount--;
         this.signalOpenCount();
      }
   }

   protected void signalOpenCount() {
      Block block = this.getBlockState().getBlock();
      if (block instanceof CrateBlock) {
         this.level.blockEvent(this.worldPosition, block, 1, this.openCount);
         this.level.updateNeighborsAt(this.worldPosition, block);
      }
   }

   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      if (!this.tryLoadLootTable(nbt)) {
         ContainerHelper.loadAllItems(nbt, this.items);
      }
   }

   public void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      if (!this.trySaveLootTable(nbt)) {
         ContainerHelper.saveAllItems(nbt, this.items);
      }
   }

   protected NonNullList<ItemStack> getItems() {
      return this.items;
   }

   protected void setItems(NonNullList<ItemStack> p_199721_1_) {
      this.items = p_199721_1_;
   }

   public static int getOpenCount(BlockGetter reader, BlockPos pos) {
      BlockState blockstate = reader.getBlockState(pos);
      if (blockstate.hasBlockEntity()) {
         BlockEntity tileentity = reader.getBlockEntity(pos);
         if (tileentity instanceof CrateTileEntity) {
            return ((CrateTileEntity)tileentity).openCount;
         }
      }

      return 0;
   }

   public static void swapContents(CrateTileEntity crateTile1, CrateTileEntity crateTile2) {
      NonNullList<ItemStack> nonnulllist = crateTile1.getItems();
      crateTile1.setItems(crateTile2.getItems());
      crateTile2.setItems(nonnulllist);
   }

   protected AbstractContainerMenu createMenu(int containerCounter, Inventory playerInventory) {
      return CrateContainer.singleCrate(containerCounter, playerInventory, this);
   }

   public void clearContent() {
      super.clearContent();
      if (this.crateHandler != null) {
         LazyOptional<?> oldHandler = this.crateHandler;
         this.crateHandler = null;
         oldHandler.invalidate();
      }
   }

   public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      if (this.remove || cap != ForgeCapabilities.ITEM_HANDLER) {
         return super.getCapability(cap, side);
      } else if (this.level == null) {
         return LazyOptional.empty().cast();
      } else {
         List<BlockPos> cratePositions = CrateBlock.getCratePosList(this.level, this.worldPosition);
         if (this.crateArray == null) {
            this.crateArray = cratePositions;
         } else if (this.crateArray != cratePositions) {
            this.crateArray = cratePositions;
         }

         if (this.crateHandler == null) {
            this.crateHandler = LazyOptional.of(this::createHandler);
         }

         return this.crateHandler.cast();
      }
   }

   @Nonnull
   private IItemHandlerModifiable createHandler() {
      BlockState state = this.getBlockState();
      if (!(state.getBlock() instanceof CrateBlock)) {
         return new InvWrapper(this);
      } else {
         CrateTileEntity[] tileList = new CrateTileEntity[this.crateArray.size()];
         if (this.level != null) {
            for (int i = 0; i < this.crateArray.size(); i++) {
               tileList[i] = (CrateTileEntity)this.level.getBlockEntity(this.crateArray.get(i));
            }
         }

         Container inv = new MultiSidedInventory(tileList);
         return new InvWrapper(inv);
      }
   }

   public void invalidateCaps() {
      super.invalidateCaps();
      if (this.crateHandler != null) {
         this.crateHandler.invalidate();
      }
   }

   public List<BlockPos> getCrateArray() {
      return this.crateArray;
   }

   public boolean stillValid(Player player) {
      return this.level != null
         && (
            !(this.level.getBlockState(this.worldPosition).getBlock() instanceof CrateBlock)
               || CrateBlock.isCrateConfigValid(this.level, CrateBlock.getCratePosList(this.level, this.worldPosition))
         )
         && super.stillValid(player);
   }

   protected Component getDefaultName() {
      return Component.translatable("container.druidcraftrg.crate");
   }

   public int getContainerSize() {
      return 27;
   }
}
