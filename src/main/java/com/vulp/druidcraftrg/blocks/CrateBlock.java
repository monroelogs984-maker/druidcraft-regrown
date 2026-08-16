package com.vulp.druidcraftrg.blocks;

import com.vulp.druidcraftrg.DruidcraftRegrown;
import com.vulp.druidcraftrg.blocks.tile.CrateTileEntity;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import com.vulp.druidcraftrg.inventory.MultiSidedInventory;
import com.vulp.druidcraftrg.inventory.container.CrateContainer;
import com.vulp.druidcraftrg.state.properties.CrateType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import com.vulp.druidcraftrg.init.SoundEventInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

public class CrateBlock extends BaseEntityBlock {
   public static final EnumProperty<CrateType> TYPE = EnumProperty.create("type", CrateType.class);
   public static final int[] CRATE_TYPE_START_POINTS = new int[]{0, 8, 12, 16, 20, 22, 24, 26};

   public CrateBlock(Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(TYPE, CrateType.SMALL));
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new CrateTileEntity(pos, state);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
      return createTickerHelper(blockEntity, BlockEntityInit.crate, CrateTileEntity::serverTick);
   }

   public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTrace) {
      if (world.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         MenuProvider menuProvider = this.getMenuProvider(state, world, pos);
         if (menuProvider != null) {
            player.openMenu(new MenuProvider() {
               @Nullable
               private List<CrateTileEntity> findTileList() {
                  List<CrateTileEntity> tileList = new ArrayList<>(Collections.emptyList());
                  BlockEntity tile = world.getBlockEntity(pos);
                  if (tile instanceof CrateTileEntity) {
                     List<BlockPos> array = ((CrateTileEntity)tile).getCrateArray();
                     if (array == null) {
                        array = CrateBlock.getCratePosList(world, pos);
                     }

                     for (BlockPos tempPos : array) {
                        BlockEntity tempTile = world.getBlockEntity(tempPos);
                        if (tempTile instanceof CrateTileEntity) {
                           tileList.add((CrateTileEntity)tempTile);
                        }
                     }
                  }

                  return tileList.isEmpty() ? null : tileList;
               }

               public Component getDisplayName() {
                  List<CrateTileEntity> tileList = this.findTileList();
                  if (tileList != null) {
                     for (CrateTileEntity crateTileEntity : tileList) {
                        if (crateTileEntity.hasCustomName()) {
                           return crateTileEntity.getDisplayName();
                        }
                     }
                  }

                  return Component.translatable("container.druidcraftrg.crate");
               }

               @Nullable
               public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                  List<CrateTileEntity> tileList = this.findTileList();
                  if (tileList == null) {
                     return null;
                  } else {
                     for (int i = 0; i < 2; i++) {
                        for (CrateTileEntity crateTileEntity : tileList) {
                           if (i == 0 && !crateTileEntity.canOpen(player)) {
                              return null;
                           }

                           crateTileEntity.unpackLootTable(playerInventory.player);
                        }
                     }

                     return CrateBlock.getSizedContainer(state, id, playerInventory, new MultiSidedInventory(tileList.toArray(new CrateTileEntity[0])));
                  }
               }
            });
            world.playSound(null, pos, SoundEventInit.open_crate, SoundSource.BLOCKS, 1.0F, 1.0F);
            PiglinAi.angerNearbyPiglins(player, true);
         }

         return InteractionResult.CONSUME;
      }
   }

   public static CrateContainer getSizedContainer(BlockState state, int id, Inventory playerInventory, Container inventory) {
      switch (((CrateType)state.getValue(TYPE)).getCrateSize()) {
         case 2:
            return CrateContainer.doubleCrate(id, playerInventory, inventory);
         case 4:
            return CrateContainer.quadCrate(id, playerInventory, inventory);
         case 8:
            return CrateContainer.octoCrate(id, playerInventory, inventory);
         default:
            return CrateContainer.singleCrate(id, playerInventory, inventory);
      }
   }

   public static List<BlockPos> getCratePosList(Level world, BlockPos pos) {
      List<Direction> directions = getAttachDirectionsFromType((CrateType)world.getBlockState(pos).getValue(TYPE));
      int[] integers = new int[]{0, 0, 0};

      for (Direction dir : directions) {
         integers[dir.getAxis().ordinal()] = dir.getStepX() + dir.getStepY() + dir.getStepZ();
      }

      List<BlockPos> posList = new ArrayList<>(Collections.emptyList());

      for (int y = integers[1] == -1 ? -1 : 0; y < (integers[1] == 1 ? 2 : 1); y++) {
         for (int x = integers[0] == -1 ? -1 : 0; x < (integers[0] == 1 ? 2 : 1); x++) {
            for (int z = integers[2] == -1 ? -1 : 0; z < (integers[2] == 1 ? 2 : 1); z++) {
               posList.add(pos.offset(x, y, z));
            }
         }
      }

      return posList;
   }

   public static boolean isCrateConfigValid(Level world, List<BlockPos> cratePositions) {
      for (BlockPos currentPos : cratePositions) {
         for (Direction dir : getAttachDirectionsFromType((CrateType)world.getBlockState(currentPos).getValue(TYPE))) {
            if (!getAttachDirectionsFromType((CrateType)world.getBlockState(currentPos.relative(dir)).getValue(TYPE)).contains(dir.getOpposite())) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean[] createCrateArray(LevelAccessor world, BlockPos pos) {
      boolean[] boolArray = new boolean[27];
      int ticker = 0;

      for (int y = -1; y < 2; y++) {
         for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
               boolArray[ticker] = world.getBlockState(pos.offset(x, y, z)).getBlock() == this;
               ticker++;
            }
         }
      }

      boolArray[13] = true;
      return boolArray;
   }

   private CrateType detectCrateConfig(LevelAccessor world, BlockPos pos, boolean[] boolArray, boolean updateConfig) {
      int ticker = 0;

      for (int m = 0; m < 7; m++) {
         for (int j = 0; j < (m != 3 && (m <= 3 || m == 6) ? 2 : 1); j++) {
            for (int i = 0; i < (m != 2 && (m <= 3 || m == 5) ? 2 : 1); i++) {
               for (int k = 0; k < (m != 1 && (m <= 3 || m == 4) ? 2 : 1); k++) {
                  boolean latch = true;
                  if (m == 0) {
                     for (int y = -j; y < 2 - j; y++) {
                        for (int x = -i; x < 2 - i; x++) {
                           for (int z = -k; z < 2 - k; z++) {
                              if (!boolArray[13 + x * 3 + y * 9 + z]) {
                                 latch = false;
                                 break;
                              }
                           }
                        }
                     }
                  } else if (m < 4) {
                     for (int y = -j; y < (m == 3 ? 1 : 2 - j); y++) {
                        for (int x = -i; x < (m == 2 ? 1 : 2 - i); x++) {
                           for (int zx = -k; zx < (m == 1 ? 1 : 2 - k); zx++) {
                              if (!boolArray[13 + x * 3 + y * 9 + zx]) {
                                 latch = false;
                                 break;
                              }
                           }
                        }
                     }
                  } else {
                     for (int y = -j; y < (m != 6 ? 1 : 2 - j); y++) {
                        for (int x = -i; x < (m != 5 ? 1 : 2 - i); x++) {
                           for (int zxx = -k; zxx < (m != 4 ? 1 : 2 - k); zxx++) {
                              if (!boolArray[13 + x * 3 + y * 9 + zxx]) {
                                 latch = false;
                                 break;
                              }
                           }
                        }
                     }
                  }

                  if (latch) {
                     CrateType placeType = CrateType.values()[ticker];
                     HashMap<BlockPos, CrateType> map = this.checkConfigValid(world, pos, placeType, ticker);
                     if (map != null) {
                        if (updateConfig) {
                           map.forEach((blockPos, crateType) -> world.setBlock(blockPos, (BlockState)this.defaultBlockState().setValue(TYPE, crateType), 2));
                        }

                        return placeType;
                     }
                  }

                  ticker++;
               }
            }
         }
      }

      return CrateType.SMALL;
   }

   private boolean[] crateTypeToCrateArray(LevelAccessor world, BlockPos pos, CrateType type) {
      List<Direction> directions = getAttachDirectionsFromType(type);
      boolean[] array = this.createCrateArray(world, pos);
      boolean[] arrayMask = new boolean[27];
      int[] integers = new int[]{0, 0, 0};

      for (Direction dir : directions) {
         integers[dir.getAxis().ordinal()] = dir.getStepX() + dir.getStepY() + dir.getStepZ();
      }

      for (int y = integers[1] == -1 ? -1 : 0; y < (integers[1] == 1 ? 2 : 1); y++) {
         for (int x = integers[0] == -1 ? -1 : 0; x < (integers[0] == 1 ? 2 : 1); x++) {
            for (int z = integers[2] == -1 ? -1 : 0; z < (integers[2] == 1 ? 2 : 1); z++) {
               arrayMask[13 + x * 3 + y * 9 + z] = true;
            }
         }
      }

      for (int i = 0; i < arrayMask.length; i++) {
         if (!arrayMask[i]) {
            array[i] = false;
         }
      }

      return array;
   }

   public void onRemove(BlockState state, Level world, BlockPos pos, BlockState replacingState, boolean bool) {
      if (replacingState.getBlock() != this) {
         CrateType type = (CrateType)state.getValue(TYPE);
         List<Direction> directions = getAttachDirectionsFromType(type);
         int[] integers = new int[]{0, 0, 0};

         for (Direction dir : directions) {
            integers[dir.getAxis().ordinal()] = dir.getStepX() + dir.getStepY() + dir.getStepZ();
         }

         List<BlockPos> posList = new ArrayList<>(Collections.emptyList());

         for (int y = integers[1] == -1 ? -1 : 0; y < (integers[1] == 1 ? 2 : 1); y++) {
            for (int x = integers[0] == -1 ? -1 : 0; x < (integers[0] == 1 ? 2 : 1); x++) {
               for (int z = integers[2] == -1 ? -1 : 0; z < (integers[2] == 1 ? 2 : 1); z++) {
                  posList.add(pos.offset(x, y, z));
               }
            }
         }

         posList.remove(pos);
         posList.forEach(
            blockPos -> {
               BlockState currentState = world.getBlockState(blockPos);
               if (currentState.getBlock() instanceof CrateBlock) {
                  world.setBlock(
                     blockPos,
                     (BlockState)currentState.setValue(
                        TYPE,
                        this.detectCrateConfig(world, blockPos, this.crateTypeToCrateArray(world, blockPos, (CrateType)currentState.getValue(TYPE)), true)
                     ),
                     2
                  );
               }
            }
         );
         BlockEntity tileentity = world.getBlockEntity(pos);
         if (tileentity instanceof Container) {
            Containers.dropContents(world, pos, (Container)tileentity);
         }
      }

      super.onRemove(state, world, pos, replacingState, bool);
   }

   public static List<Direction> getAttachDirectionsFromType(CrateType type) {
      List<Direction> directions = new LinkedList<>(Arrays.asList(type.getOpenDirections()));
      List<Direction> oppositeCache = new ArrayList<>(Collections.emptyList());
      directions.removeIf(direction -> {
         Direction opposite = direction.getOpposite();
         oppositeCache.add(opposite);
         return directions.contains(opposite) || oppositeCache.contains(direction);
      });
      return directions;
   }

   @Nullable
   private HashMap<BlockPos, CrateType> checkConfigValid(LevelAccessor world, BlockPos pos, CrateType type, int oldTicker) {
      HashMap<BlockPos, CrateType> map = new HashMap<>();
      List<Direction> directions = getAttachDirectionsFromType(type);
      int[] integers = new int[]{0, 0, 0};

      for (Direction dir : directions) {
         integers[dir.getAxis().ordinal()] = dir.getStepX() + dir.getStepY() + dir.getStepZ();
      }

      for (int i = 0; i < 8; i++) {
         final int currentTicker = oldTicker;
         if (Arrays.stream(CRATE_TYPE_START_POINTS).anyMatch(j -> j == currentTicker)) {
            break;
         }

         oldTicker--;
      }

      int ticker = oldTicker;

      for (int y = 0; y < (integers[1] == 0 ? 1 : 2); y++) {
         for (int x = 0; x < (integers[0] == 0 ? 1 : 2); x++) {
            for (int z = 0; z < (integers[2] == 0 ? 1 : 2); z++) {
               int xPos = pos.getX() + x - (integers[0] == -1 ? 1 : 0);
               int yPos = pos.getY() + y - (integers[1] == -1 ? 1 : 0);
               int zPos = pos.getZ() + z - (integers[2] == -1 ? 1 : 0);
               BlockPos dynamicPos = new BlockPos(xPos, yPos, zPos);
               if (dynamicPos.getX() != pos.getX() || dynamicPos.getY() != pos.getY() || dynamicPos.getZ() != pos.getZ()) {
                  CrateType crateTypeReference = CrateType.values()[ticker];

                  for (Direction dir : getAttachDirectionsFromType(crateTypeReference)) {
                     BlockState dynamicState = world.getBlockState(dynamicPos);
                     CrateType dynamicType = (CrateType)dynamicState.getValue(TYPE);
                     List<Direction> dynamicOpenDirections = Arrays.asList(dynamicType.getOpenDirections());
                     boolean flag = false;
                     if (dynamicType.getCrateSize() == 2 && crateTypeReference.getCrateSize() == 4) {
                        List<Axis> axisList = new ArrayList<>(Collections.emptyList());

                        for (Direction direction : getAttachDirectionsFromType(crateTypeReference)) {
                           axisList.add(direction.getAxis());
                        }

                        if (!axisList.contains(getAttachDirectionsFromType(dynamicType).get(0).getAxis())) {
                           flag = true;
                        }
                     }

                     if (dynamicState.getBlock() != this
                        || dynamicType.getCrateSize() >= crateTypeReference.getCrateSize()
                        || flag
                        || !dynamicOpenDirections.contains(dir)) {
                        return null;
                     }
                  }

                  map.put(dynamicPos, CrateType.values()[ticker]);
               }

               ticker++;
            }
         }
      }

      return map;
   }

   private void debugArrayCreation(boolean[] boolArray) {
      for (int i = 0; i < 3; i++) {
         DruidcraftRegrown.LOGGER.debug("----");

         for (int j = 0; j < 3; j++) {
            int k = i * 9 + j * 3;
            DruidcraftRegrown.LOGGER.debug((boolArray[k] ? "O" : "/") + (boolArray[k + 1] ? "O" : "/") + (boolArray[k + 2] ? "O" : "/"));
         }
      }

      DruidcraftRegrown.LOGGER.debug("----");
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockState state = super.getStateForPlacement(context);
      if (state != null) {
         Level world = context.getLevel();
         BlockPos pos = context.getClickedPos();
         CrateType type = CrateType.SMALL;
         if (context.canPlace()) {
            boolean[] boolArray = this.createCrateArray(world, pos);
            type = this.detectCrateConfig(world, pos, boolArray, true);
         }

         return (BlockState)state.setValue(TYPE, type);
      } else {
         return null;
      }
   }

   public void onPlace(BlockState state, Level world, BlockPos pos, BlockState state1, boolean bool) {
      super.onPlace(state, world, pos, state1, bool);
   }

   public boolean hasAnalogOutputSignal(BlockState state) {
      return true;
   }

   public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
      List<CrateTileEntity> tileList = new ArrayList<>(Collections.emptyList());
      BlockEntity tile = world.getBlockEntity(pos);
      if (tile instanceof CrateTileEntity) {
         List<BlockPos> array = ((CrateTileEntity)tile).getCrateArray();
         if (array == null) {
            array = getCratePosList(world, pos);
         }

         for (BlockPos tempPos : array) {
            BlockEntity tempTile = world.getBlockEntity(tempPos);
            if (tempTile instanceof CrateTileEntity) {
               tileList.add((CrateTileEntity)tempTile);
            }
         }
      }

      return !tileList.isEmpty() ? AbstractContainerMenu.getRedstoneSignalFromContainer(new MultiSidedInventory(tileList.toArray(new CrateTileEntity[0]))) : 0;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{TYPE});
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }
   @Override
   public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
      if (level == null) return;
      if (!Screen.hasShiftDown()) {
         tooltip.add(Component.translatable("block.druidcraftrg.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      } else {
         tooltip.add(Component.translatable("block.druidcraftrg.crate.description1").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      }
   }
}
