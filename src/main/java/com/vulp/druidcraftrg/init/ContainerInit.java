package com.vulp.druidcraftrg.init;

import com.vulp.druidcraftrg.inventory.container.BeetleInventoryContainer;
import com.vulp.druidcraftrg.inventory.container.CrateContainer;
import com.vulp.druidcraftrg.inventory.container.TravelPackContainer;
import com.vulp.druidcraftrg.inventory.container.WoodcutterMenu;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class ContainerInit {
   public static MenuType<CrateContainer> CRATE_9x3 = new MenuType(CrateContainer::singleCrate, FeatureFlags.DEFAULT_FLAGS);
   public static MenuType<CrateContainer> CRATE_9x6 = new MenuType(CrateContainer::doubleCrate, FeatureFlags.DEFAULT_FLAGS);
   public static MenuType<CrateContainer> CRATE_9x12 = new MenuType(CrateContainer::quadCrate, FeatureFlags.DEFAULT_FLAGS);
   public static MenuType<CrateContainer> CRATE_9x24 = new MenuType(CrateContainer::octoCrate, FeatureFlags.DEFAULT_FLAGS);
   public static MenuType<WoodcutterMenu> WOODCUTTER = new MenuType<>(WoodcutterMenu::new, FeatureFlags.DEFAULT_FLAGS);
   public static MenuType<TravelPackContainer> TRAVEL_PACK = new MenuType<>(TravelPackContainer::createClientContainer, FeatureFlags.DEFAULT_FLAGS);
   public static MenuType<BeetleInventoryContainer> BEETLE_INV = IForgeMenuType.create((windowId, inv, data) -> {
      int id = data.readInt();
      int size = data.readInt();
      return new BeetleInventoryContainer(windowId, inv, new SimpleContainer(size), id);
   });
}
