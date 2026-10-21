package com.vulp.druidcraftrg;

import com.vulp.druidcraftrg.blocks.BeamBlock;
import com.vulp.druidcraftrg.blocks.BedrollBlock;
import com.vulp.druidcraftrg.blocks.BerryBushBlock;
import com.vulp.druidcraftrg.blocks.CeramicLanternBlock;
import com.vulp.druidcraftrg.blocks.CrateBlock;
import com.vulp.druidcraftrg.blocks.DoubleCropBlock;
import com.vulp.druidcraftrg.blocks.ElderFruitBlock;
import com.vulp.druidcraftrg.blocks.ElderLeafLayerBlock;
import com.vulp.druidcraftrg.blocks.ElderLeavesBlock;
import com.vulp.druidcraftrg.blocks.FieryTorchBlock;
import com.vulp.druidcraftrg.blocks.GrowthLampBlock;
import com.vulp.druidcraftrg.blocks.PanelBlock;
import com.vulp.druidcraftrg.blocks.PlatformBlock;
import com.vulp.druidcraftrg.blocks.RopeBlock;
import com.vulp.druidcraftrg.blocks.RopeLanternBlock;
import com.vulp.druidcraftrg.blocks.SoulfireBlock;
import com.vulp.druidcraftrg.blocks.StrippablePillarBlock;
import com.vulp.druidcraftrg.blocks.WallFieryTorchBlock;
import com.vulp.druidcraftrg.blocks.WoodcutterBlock;
import com.vulp.druidcraftrg.blocks.tile.BeamTileEntity;
import com.vulp.druidcraftrg.blocks.tile.CrateTileEntity;
import com.vulp.druidcraftrg.blocks.tile.GrowthLampTileEntity;
import com.vulp.druidcraftrg.blocks.tile.RopeTileEntity;
import com.vulp.druidcraftrg.blocks.trees.DarkwoodTreeGrower;
import com.vulp.druidcraftrg.blocks.trees.ElderTreeGrower;
import com.vulp.druidcraftrg.blocks.WetMudBlock;
import com.vulp.druidcraftrg.blocks.WoodBlock;
import com.vulp.druidcraftrg.blocks.LunarMothJarBlock;
import com.vulp.druidcraftrg.capabilities.TempSpawnCapability;
import com.vulp.druidcraftrg.entities.BeetleEntity;
import com.vulp.druidcraftrg.entities.DreadfishEntity;
import com.vulp.druidcraftrg.entities.LunarMothColors;
import com.vulp.druidcraftrg.entities.LunarMothEntity;
import com.vulp.druidcraftrg.init.ArmorMaterialInit;
import com.vulp.druidcraftrg.init.EntityInit;
import com.vulp.druidcraftrg.init.SoundEventInit;
import com.vulp.druidcraftrg.items.LunarMothEggItem;
import com.vulp.druidcraftrg.items.LunarMothJarItem;
import com.vulp.druidcraftrg.init.BlockEntityInit;
import com.vulp.druidcraftrg.init.BlockInit;
import com.vulp.druidcraftrg.init.ContainerInit;
import com.vulp.druidcraftrg.init.FeatureInit;
import com.vulp.druidcraftrg.init.FoodInit;
import com.vulp.druidcraftrg.init.ItemInit;
import com.vulp.druidcraftrg.init.ParticleInit;
import com.vulp.druidcraftrg.init.RecipeInit;
import com.vulp.druidcraftrg.init.TierInit;
import com.vulp.druidcraftrg.init.WoodTypeInit;
import com.vulp.druidcraftrg.items.BasicShieldItem;
import com.vulp.druidcraftrg.items.DoubleCropSeedItem;
import com.vulp.druidcraftrg.items.DrinkableItem;
import com.vulp.druidcraftrg.items.FuelBlockItem;
import com.vulp.druidcraftrg.items.KnifeItem;
import com.vulp.druidcraftrg.items.SickleItem;
import com.vulp.druidcraftrg.items.TravelPackItem;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class DruidcraftRegrownRegistry {
   @SubscribeEvent
   public static void register(RegisterEvent event) {
      event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
         helper.register(location("hemp_crop"), BlockInit.hemp_crop = new DoubleCropBlock(
            7.0,
            () -> ItemInit.hemp_seeds,
            BlockBehaviour.Properties.of()
               .mapColor(MapColor.PLANT)
               .pushReaction(PushReaction.DESTROY)
               .sound(SoundType.CROP)
               .noCollission()
               .randomTicks()
               .instabreak()
         ));
         helper.register(location("rope"), BlockInit.rope = new RopeBlock(
            2.5, 2.5, BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).sound(SoundType.WOOL).strength(0.4F)
         ));
         helper.register(location("platform"), BlockInit.platform = new PlatformBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1.0F).noOcclusion()
         ));
         helper.register(location("beam"), BlockInit.beam = new BeamBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1.0F)
         ));
         BlockInit.white_bedroll = registerBedroll(helper, "white_bedroll", DyeColor.WHITE);
         BlockInit.orange_bedroll = registerBedroll(helper, "orange_bedroll", DyeColor.ORANGE);
         BlockInit.magenta_bedroll = registerBedroll(helper, "magenta_bedroll", DyeColor.MAGENTA);
         BlockInit.light_blue_bedroll = registerBedroll(helper, "light_blue_bedroll", DyeColor.LIGHT_BLUE);
         BlockInit.yellow_bedroll = registerBedroll(helper, "yellow_bedroll", DyeColor.YELLOW);
         BlockInit.lime_bedroll = registerBedroll(helper, "lime_bedroll", DyeColor.LIME);
         BlockInit.pink_bedroll = registerBedroll(helper, "pink_bedroll", DyeColor.PINK);
         BlockInit.gray_bedroll = registerBedroll(helper, "gray_bedroll", DyeColor.GRAY);
         BlockInit.light_gray_bedroll = registerBedroll(helper, "light_gray_bedroll", DyeColor.LIGHT_GRAY);
         BlockInit.cyan_bedroll = registerBedroll(helper, "cyan_bedroll", DyeColor.CYAN);
         BlockInit.purple_bedroll = registerBedroll(helper, "purple_bedroll", DyeColor.PURPLE);
         BlockInit.blue_bedroll = registerBedroll(helper, "blue_bedroll", DyeColor.BLUE);
         BlockInit.brown_bedroll = registerBedroll(helper, "brown_bedroll", DyeColor.BROWN);
         BlockInit.green_bedroll = registerBedroll(helper, "green_bedroll", DyeColor.GREEN);
         BlockInit.red_bedroll = registerBedroll(helper, "red_bedroll", DyeColor.RED);
         BlockInit.black_bedroll = registerBedroll(helper, "black_bedroll", DyeColor.BLACK);
         helper.register(location("crate"), BlockInit.oak_crate = new CrateBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1.0F)
         ));

         // M1: 1.16.5 materials
         helper.register(location("amber_ore"), BlockInit.amber_ore = new DropExperienceBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F), UniformInt.of(3, 6)));
         helper.register(location("moonstone_ore"), BlockInit.moonstone_ore = new DropExperienceBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F), UniformInt.of(3, 7)));
         helper.register(location("fiery_glass_ore"), BlockInit.fiery_glass_ore = new DropExperienceBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F), UniformInt.of(2, 5)));
         helper.register(location("rockroot_ore"), BlockInit.rockroot_ore = new DropExperienceBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F), UniformInt.of(1, 5)));
         helper.register(location("amber_block"), BlockInit.amber_block = new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(3.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.STONE)));
         helper.register(location("moonstone_block"), BlockInit.moonstone_block = new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(6.0F).requiresCorrectToolForDrops().sound(SoundType.STONE)));
         helper.register(location("fiery_glass_block"), BlockInit.fiery_glass_block = new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(4.0F).requiresCorrectToolForDrops().lightLevel(state -> 15).sound(SoundType.STONE)));
         helper.register(location("rockroot_block"), BlockInit.rockroot_block = new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F).requiresCorrectToolForDrops().sound(SoundType.STONE)));
         helper.register(location("dry_mud_bricks"), BlockInit.dry_mud_bricks = new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 4.0F)));
         helper.register(location("dry_mud_brick_slab"), BlockInit.dry_mud_brick_slab = new SlabBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 4.0F)));
         helper.register(location("dry_mud_brick_stairs"), BlockInit.dry_mud_brick_stairs = new StairBlock(
            () -> BlockInit.dry_mud_bricks.defaultBlockState(),
            BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 4.0F)));
         helper.register(location("dry_mud_brick_wall"), BlockInit.dry_mud_brick_wall = new WallBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 4.0F)));
         helper.register(location("wet_mud_bricks"), BlockInit.wet_mud_bricks = new WetMudBlock(BlockInit.dry_mud_bricks,
            BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(SoundType.SLIME_BLOCK).strength(0.8F).randomTicks()));

         // M2: 1.16.5 wood sets — darkwood
         helper.register(location("darkwood_log"), BlockInit.darkwood_log = new StrippablePillarBlock(() -> BlockInit.stripped_darkwood_log, logProps()));
         helper.register(location("stripped_darkwood_log"), BlockInit.stripped_darkwood_log = new RotatedPillarBlock(logProps()));
         helper.register(location("darkwood_wood"), BlockInit.darkwood_wood = new WoodBlock(() -> ItemInit.darkwood_log, () -> BlockInit.stripped_darkwood_wood, logProps()));
         helper.register(location("stripped_darkwood_wood"), BlockInit.stripped_darkwood_wood = new RotatedPillarBlock(logProps()));
         helper.register(location("darkwood_leaves"), BlockInit.darkwood_leaves = new LeavesBlock(leavesProps()));
         helper.register(location("darkwood_sapling"), BlockInit.darkwood_sapling = new SaplingBlock(new DarkwoodTreeGrower(), saplingProps()));
         helper.register(location("potted_darkwood_sapling"), BlockInit.potted_darkwood_sapling = new FlowerPotBlock(
            () -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> BlockInit.darkwood_sapling,
            BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)));
         helper.register(location("darkwood_planks"), BlockInit.darkwood_planks = new Block(planksProps()));
         helper.register(location("darkwood_slab"), BlockInit.darkwood_slab = new SlabBlock(planksProps()));
         helper.register(location("darkwood_stairs"), BlockInit.darkwood_stairs = new StairBlock(() -> BlockInit.darkwood_planks.defaultBlockState(), planksProps()));
         helper.register(location("darkwood_fence"), BlockInit.darkwood_fence = new FenceBlock(planksProps()));
         helper.register(location("darkwood_fence_gate"), BlockInit.darkwood_fence_gate = new FenceGateBlock(planksProps(), WoodTypeInit.DARKWOOD));
         helper.register(location("darkwood_pressure_plate"), BlockInit.darkwood_pressure_plate = new PressurePlateBlock(
            PressurePlateBlock.Sensitivity.EVERYTHING, plateProps(), WoodTypeInit.DARKWOOD_SET));
         helper.register(location("darkwood_button"), BlockInit.darkwood_button = new ButtonBlock(buttonProps(), WoodTypeInit.DARKWOOD_SET, 30, true));
         helper.register(location("darkwood_trapdoor"), BlockInit.darkwood_trapdoor = new TrapDoorBlock(trapdoorProps(), WoodTypeInit.DARKWOOD_SET));
         helper.register(location("darkwood_door"), BlockInit.darkwood_door = new DoorBlock(doorProps(), WoodTypeInit.DARKWOOD_SET));

         // M2: 1.16.5 wood sets — elder
         helper.register(location("elder_log"), BlockInit.elder_log = new StrippablePillarBlock(() -> BlockInit.stripped_elder_log, logProps()));
         helper.register(location("stripped_elder_log"), BlockInit.stripped_elder_log = new RotatedPillarBlock(logProps()));
         helper.register(location("elder_wood"), BlockInit.elder_wood = new WoodBlock(() -> ItemInit.elder_log, () -> BlockInit.stripped_elder_wood, logProps()));
         helper.register(location("stripped_elder_wood"), BlockInit.stripped_elder_wood = new RotatedPillarBlock(logProps()));
         helper.register(location("elder_leaves"), BlockInit.elder_leaves = new ElderLeavesBlock(leavesProps()));
         helper.register(location("elder_sapling"), BlockInit.elder_sapling = new SaplingBlock(new ElderTreeGrower(), saplingProps()));
         helper.register(location("potted_elder_sapling"), BlockInit.potted_elder_sapling = new FlowerPotBlock(
            () -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> BlockInit.elder_sapling,
            BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)));
         helper.register(location("elder_planks"), BlockInit.elder_planks = new Block(planksProps()));
         helper.register(location("elder_slab"), BlockInit.elder_slab = new SlabBlock(planksProps()));
         helper.register(location("elder_stairs"), BlockInit.elder_stairs = new StairBlock(() -> BlockInit.elder_planks.defaultBlockState(), planksProps()));
         helper.register(location("elder_fence"), BlockInit.elder_fence = new FenceBlock(planksProps()));
         helper.register(location("elder_fence_gate"), BlockInit.elder_fence_gate = new FenceGateBlock(planksProps(), WoodTypeInit.ELDER));
         helper.register(location("elder_pressure_plate"), BlockInit.elder_pressure_plate = new PressurePlateBlock(
            PressurePlateBlock.Sensitivity.EVERYTHING, plateProps(), WoodTypeInit.ELDER_SET));
         helper.register(location("elder_button"), BlockInit.elder_button = new ButtonBlock(buttonProps(), WoodTypeInit.ELDER_SET, 30, true));
         helper.register(location("elder_trapdoor"), BlockInit.elder_trapdoor = new TrapDoorBlock(trapdoorProps(), WoodTypeInit.ELDER_SET));
         helper.register(location("elder_door"), BlockInit.elder_door = new DoorBlock(doorProps(), WoodTypeInit.ELDER_SET));

         // M2: 1.16.5 beams & panels
         BlockInit.oak_beam = registerBeam(helper, "oak_beam");
         BlockInit.spruce_beam = registerBeam(helper, "spruce_beam");
         BlockInit.birch_beam = registerBeam(helper, "birch_beam");
         BlockInit.jungle_beam = registerBeam(helper, "jungle_beam");
         BlockInit.acacia_beam = registerBeam(helper, "acacia_beam");
         BlockInit.dark_oak_beam = registerBeam(helper, "dark_oak_beam");
         BlockInit.darkwood_beam = registerBeam(helper, "darkwood_beam");
         BlockInit.stripped_darkwood_beam = registerBeam(helper, "stripped_darkwood_beam");
         BlockInit.elder_beam = registerBeam(helper, "elder_beam");
         BlockInit.oak_panels = registerPanels(helper, "oak_panels");
         BlockInit.spruce_panels = registerPanels(helper, "spruce_panels");
         BlockInit.birch_panels = registerPanels(helper, "birch_panels");
         BlockInit.jungle_panels = registerPanels(helper, "jungle_panels");
         BlockInit.acacia_panels = registerPanels(helper, "acacia_panels");
         BlockInit.dark_oak_panels = registerPanels(helper, "dark_oak_panels");
         helper.register(location("darkwood_panels"), BlockInit.darkwood_panels = new PanelBlock(planksProps()));
         BlockInit.elder_panels = registerPanels(helper, "elder_panels");

         // M3: 1.16.5 plants & food
         helper.register(location("lavender"), BlockInit.lavender = new FlowerBlock(
            () -> MobEffects.DIG_SPEED, 8,
            BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak()
               .sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));
         helper.register(location("potted_lavender"), BlockInit.potted_lavender = new FlowerPotBlock(
            () -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> BlockInit.lavender,
            BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)));
         helper.register(location("blueberry_bush"), BlockInit.blueberry_bush = new BerryBushBlock(
            () -> ItemInit.blueberries, false,
            BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().noCollission()
               .sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));
         helper.register(location("elder_fruit"), BlockInit.elder_fruit = new ElderFruitBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).instabreak().noCollission().randomTicks()
               .sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));
         helper.register(location("elder_leaf_layer"), BlockInit.elder_leaf_layer = new ElderLeafLayerBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.2F).noCollission().randomTicks()
               .sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));

         // M4: 1.16.5 functional blocks
         BlockInit.white_soulfire = registerSoulfire(helper, "white_soulfire", DyeColor.WHITE);
         BlockInit.orange_soulfire = registerSoulfire(helper, "orange_soulfire", DyeColor.ORANGE);
         BlockInit.magenta_soulfire = registerSoulfire(helper, "magenta_soulfire", DyeColor.MAGENTA);
         BlockInit.light_blue_soulfire = registerSoulfire(helper, "light_blue_soulfire", DyeColor.LIGHT_BLUE);
         BlockInit.yellow_soulfire = registerSoulfire(helper, "yellow_soulfire", DyeColor.YELLOW);
         BlockInit.lime_soulfire = registerSoulfire(helper, "lime_soulfire", DyeColor.LIME);
         BlockInit.pink_soulfire = registerSoulfire(helper, "pink_soulfire", DyeColor.PINK);
         BlockInit.gray_soulfire = registerSoulfire(helper, "gray_soulfire", DyeColor.GRAY);
         BlockInit.light_gray_soulfire = registerSoulfire(helper, "light_gray_soulfire", DyeColor.LIGHT_GRAY);
         BlockInit.cyan_soulfire = registerSoulfire(helper, "cyan_soulfire", DyeColor.CYAN);
         BlockInit.purple_soulfire = registerSoulfire(helper, "purple_soulfire", DyeColor.PURPLE);
         BlockInit.blue_soulfire = registerSoulfire(helper, "blue_soulfire", DyeColor.BLUE);
         BlockInit.brown_soulfire = registerSoulfire(helper, "brown_soulfire", DyeColor.BROWN);
         BlockInit.green_soulfire = registerSoulfire(helper, "green_soulfire", DyeColor.GREEN);
         BlockInit.red_soulfire = registerSoulfire(helper, "red_soulfire", DyeColor.RED);
         BlockInit.black_soulfire = registerSoulfire(helper, "black_soulfire", DyeColor.BLACK);
         helper.register(location("fiery_torch"), BlockInit.fiery_torch = new FieryTorchBlock(torchProps()));
         helper.register(location("wall_fiery_torch"), BlockInit.wall_fiery_torch = new WallFieryTorchBlock(
            torchProps().dropsLike(BlockInit.fiery_torch)));
         helper.register(location("rope_lantern"), BlockInit.rope_lantern = new RopeLanternBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5F).sound(SoundType.LANTERN)
               .lightLevel(state -> 15).noOcclusion().dropsLike(Blocks.LANTERN)));
         helper.register(location("ceramic_lantern"), BlockInit.ceramic_lantern = new CeramicLanternBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F)
               .lightLevel(state -> 13).noOcclusion()));
         helper.register(location("growth_lamp"), BlockInit.growth_lamp = new GrowthLampBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).randomTicks().strength(1.35F)
               .lightLevel(state -> 14).noOcclusion()));
         helper.register(location("woodcutter"), BlockInit.woodcutter = new WoodcutterBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(1.75F)));

         // M5: lunar moth jars (block name is *_lunar_moth_lantern, matching 1.16.5).
         // 1.16.5 only gave the lime jar randomTicks (glowstone fill) — original bug, all six get it here
         BlockInit.turquoise_lunar_moth_jar = registerMothJar(helper, "turquoise_lunar_moth_lantern", 1);
         BlockInit.white_lunar_moth_jar = registerMothJar(helper, "white_lunar_moth_lantern", 2);
         BlockInit.lime_lunar_moth_jar = registerMothJar(helper, "lime_lunar_moth_lantern", 3);
         BlockInit.yellow_lunar_moth_jar = registerMothJar(helper, "yellow_lunar_moth_lantern", 4);
         BlockInit.orange_lunar_moth_jar = registerMothJar(helper, "orange_lunar_moth_lantern", 5);
         BlockInit.pink_lunar_moth_jar = registerMothJar(helper, "pink_lunar_moth_lantern", 6);

         DruidcraftRegrown.LOGGER.info("Blocks Registered!");
      });

      event.register(ForgeRegistries.Keys.ITEMS, helper -> {
         helper.register(location("knife"), ItemInit.knife = new KnifeItem(new Properties().stacksTo(1)));
         helper.register(location("hemp"), ItemInit.hemp = new Item(new Properties()));
         helper.register(location("hemp_seeds"), ItemInit.hemp_seeds = new DoubleCropSeedItem(BlockInit.hemp_crop, new Properties()));
         helper.register(location("rope"), ItemInit.rope = new BlockItem(BlockInit.rope, new Properties()));
         helper.register(location("platform"), ItemInit.platform = new BlockItem(BlockInit.platform, new Properties()));
         helper.register(location("beam"), ItemInit.beam = new BlockItem(BlockInit.beam, new Properties()));
         ItemInit.white_bedroll = registerBedrollItem(helper, "white_bedroll", BlockInit.white_bedroll);
         ItemInit.orange_bedroll = registerBedrollItem(helper, "orange_bedroll", BlockInit.orange_bedroll);
         ItemInit.magenta_bedroll = registerBedrollItem(helper, "magenta_bedroll", BlockInit.magenta_bedroll);
         ItemInit.light_blue_bedroll = registerBedrollItem(helper, "light_blue_bedroll", BlockInit.light_blue_bedroll);
         ItemInit.yellow_bedroll = registerBedrollItem(helper, "yellow_bedroll", BlockInit.yellow_bedroll);
         ItemInit.lime_bedroll = registerBedrollItem(helper, "lime_bedroll", BlockInit.lime_bedroll);
         ItemInit.pink_bedroll = registerBedrollItem(helper, "pink_bedroll", BlockInit.pink_bedroll);
         ItemInit.gray_bedroll = registerBedrollItem(helper, "gray_bedroll", BlockInit.gray_bedroll);
         ItemInit.light_gray_bedroll = registerBedrollItem(helper, "light_gray_bedroll", BlockInit.light_gray_bedroll);
         ItemInit.cyan_bedroll = registerBedrollItem(helper, "cyan_bedroll", BlockInit.cyan_bedroll);
         ItemInit.purple_bedroll = registerBedrollItem(helper, "purple_bedroll", BlockInit.purple_bedroll);
         ItemInit.blue_bedroll = registerBedrollItem(helper, "blue_bedroll", BlockInit.blue_bedroll);
         ItemInit.brown_bedroll = registerBedrollItem(helper, "brown_bedroll", BlockInit.brown_bedroll);
         ItemInit.green_bedroll = registerBedrollItem(helper, "green_bedroll", BlockInit.green_bedroll);
         ItemInit.red_bedroll = registerBedrollItem(helper, "red_bedroll", BlockInit.red_bedroll);
         ItemInit.black_bedroll = registerBedrollItem(helper, "black_bedroll", BlockInit.black_bedroll);
         helper.register(location("crate"), ItemInit.crate = new BlockItem(BlockInit.oak_crate, new Properties()));

         // M1: 1.16.5 materials
         helper.register(location("amber"), ItemInit.amber = new Item(new Properties()));
         helper.register(location("moonstone"), ItemInit.moonstone = new Item(new Properties()));
         helper.register(location("fiery_glass"), ItemInit.fiery_glass = new Item(new Properties()));
         helper.register(location("rockroot"), ItemInit.rockroot = new Item(new Properties()));
         helper.register(location("chitin"), ItemInit.chitin = new Item(new Properties()));
         helper.register(location("bone_sword"), ItemInit.bone_sword = new SwordItem(TierInit.bone, 3, -2.4F, new Properties()));
         helper.register(location("bone_shovel"), ItemInit.bone_shovel = new ShovelItem(TierInit.bone, 1.5F, -3.0F, new Properties()));
         helper.register(location("bone_pickaxe"), ItemInit.bone_pickaxe = new PickaxeItem(TierInit.bone, 1, -2.8F, new Properties()));
         helper.register(location("bone_axe"), ItemInit.bone_axe = new AxeItem(TierInit.bone, 7.0F, -3.2F, new Properties()));
         helper.register(location("bone_hoe"), ItemInit.bone_hoe = new HoeItem(TierInit.bone, 1, -2.0F, new Properties()));
         helper.register(location("bone_sickle"), ItemInit.bone_sickle = new SickleItem(-1.0F, -1.5F, TierInit.bone, 2, new Properties()));
         helper.register(location("bone_helmet"), ItemInit.bone_helmet = new ArmorItem(ArmorMaterialInit.bone, ArmorItem.Type.HELMET, new Properties()));
         helper.register(location("bone_chestplate"), ItemInit.bone_chestplate = new ArmorItem(ArmorMaterialInit.bone, ArmorItem.Type.CHESTPLATE, new Properties()));
         helper.register(location("bone_leggings"), ItemInit.bone_leggings = new ArmorItem(ArmorMaterialInit.bone, ArmorItem.Type.LEGGINGS, new Properties()));
         helper.register(location("bone_boots"), ItemInit.bone_boots = new ArmorItem(ArmorMaterialInit.bone, ArmorItem.Type.BOOTS, new Properties()));
         helper.register(location("chitin_helmet"), ItemInit.chitin_helmet = new ArmorItem(ArmorMaterialInit.chitin, ArmorItem.Type.HELMET, new Properties()));
         helper.register(location("chitin_chestplate"), ItemInit.chitin_chestplate = new ArmorItem(ArmorMaterialInit.chitin, ArmorItem.Type.CHESTPLATE, new Properties()));
         helper.register(location("chitin_leggings"), ItemInit.chitin_leggings = new ArmorItem(ArmorMaterialInit.chitin, ArmorItem.Type.LEGGINGS, new Properties()));
         helper.register(location("chitin_boots"), ItemInit.chitin_boots = new ArmorItem(ArmorMaterialInit.chitin, ArmorItem.Type.BOOTS, new Properties()));
         helper.register(location("moonstone_sword"), ItemInit.moonstone_sword = new SwordItem(TierInit.moonstone, 3, -2.4F, new Properties()));
         helper.register(location("moonstone_shovel"), ItemInit.moonstone_shovel = new ShovelItem(TierInit.moonstone, 1.5F, -3.0F, new Properties()));
         helper.register(location("moonstone_pickaxe"), ItemInit.moonstone_pickaxe = new PickaxeItem(TierInit.moonstone, 1, -2.8F, new Properties()));
         helper.register(location("moonstone_axe"), ItemInit.moonstone_axe = new AxeItem(TierInit.moonstone, 7.0F, -3.2F, new Properties()));
         helper.register(location("moonstone_hoe"), ItemInit.moonstone_hoe = new HoeItem(TierInit.moonstone, 0, -2.0F, new Properties()));
         helper.register(location("moonstone_sickle"), ItemInit.moonstone_sickle = new SickleItem(0.0F, -1.5F, TierInit.moonstone, 4, new Properties()));
         helper.register(location("moonstone_helmet"), ItemInit.moonstone_helmet = new ArmorItem(ArmorMaterialInit.moonstone, ArmorItem.Type.HELMET, new Properties()));
         helper.register(location("moonstone_chestplate"), ItemInit.moonstone_chestplate = new ArmorItem(ArmorMaterialInit.moonstone, ArmorItem.Type.CHESTPLATE, new Properties()));
         helper.register(location("moonstone_leggings"), ItemInit.moonstone_leggings = new ArmorItem(ArmorMaterialInit.moonstone, ArmorItem.Type.LEGGINGS, new Properties()));
         helper.register(location("moonstone_boots"), ItemInit.moonstone_boots = new ArmorItem(ArmorMaterialInit.moonstone, ArmorItem.Type.BOOTS, new Properties()));
         helper.register(location("wooden_sickle"), ItemInit.wooden_sickle = new SickleItem(0.0F, -1.5F, Tiers.WOOD, 1, new Properties()));
         helper.register(location("stone_sickle"), ItemInit.stone_sickle = new SickleItem(0.0F, -1.5F, Tiers.STONE, 2, new Properties()));
         helper.register(location("iron_sickle"), ItemInit.iron_sickle = new SickleItem(0.0F, -1.5F, Tiers.IRON, 3, new Properties()));
         helper.register(location("gold_sickle"), ItemInit.gold_sickle = new SickleItem(0.0F, -1.5F, Tiers.GOLD, 1, new Properties()));
         helper.register(location("diamond_sickle"), ItemInit.diamond_sickle = new SickleItem(0.0F, -1.5F, Tiers.DIAMOND, 4, new Properties()));
         helper.register(location("amber_ore"), ItemInit.amber_ore = new BlockItem(BlockInit.amber_ore, new Properties()));
         helper.register(location("moonstone_ore"), ItemInit.moonstone_ore = new BlockItem(BlockInit.moonstone_ore, new Properties()));
         helper.register(location("fiery_glass_ore"), ItemInit.fiery_glass_ore = new BlockItem(BlockInit.fiery_glass_ore, new Properties()));
         helper.register(location("rockroot_ore"), ItemInit.rockroot_ore = new BlockItem(BlockInit.rockroot_ore, new Properties()));
         helper.register(location("amber_block"), ItemInit.amber_block = new BlockItem(BlockInit.amber_block, new Properties()));
         helper.register(location("moonstone_block"), ItemInit.moonstone_block = new BlockItem(BlockInit.moonstone_block, new Properties()));
         helper.register(location("fiery_glass_block"), ItemInit.fiery_glass_block = new FuelBlockItem(BlockInit.fiery_glass_block, 24000, new Properties()));
         helper.register(location("rockroot_block"), ItemInit.rockroot_block = new BlockItem(BlockInit.rockroot_block, new Properties()));
         helper.register(location("wet_mud_bricks"), ItemInit.wet_mud_bricks = new BlockItem(BlockInit.wet_mud_bricks, new Properties()));
         helper.register(location("dry_mud_bricks"), ItemInit.dry_mud_bricks = new BlockItem(BlockInit.dry_mud_bricks, new Properties()));
         helper.register(location("dry_mud_brick_slab"), ItemInit.dry_mud_brick_slab = new BlockItem(BlockInit.dry_mud_brick_slab, new Properties()));
         helper.register(location("dry_mud_brick_stairs"), ItemInit.dry_mud_brick_stairs = new BlockItem(BlockInit.dry_mud_brick_stairs, new Properties()));
         helper.register(location("dry_mud_brick_wall"), ItemInit.dry_mud_brick_wall = new BlockItem(BlockInit.dry_mud_brick_wall, new Properties()));

         // M2: 1.16.5 wood sets
         ItemInit.darkwood_log = blockItem(helper, "darkwood_log", BlockInit.darkwood_log);
         ItemInit.stripped_darkwood_log = blockItem(helper, "stripped_darkwood_log", BlockInit.stripped_darkwood_log);
         ItemInit.darkwood_wood = blockItem(helper, "darkwood_wood", BlockInit.darkwood_wood);
         ItemInit.stripped_darkwood_wood = blockItem(helper, "stripped_darkwood_wood", BlockInit.stripped_darkwood_wood);
         ItemInit.darkwood_leaves = blockItem(helper, "darkwood_leaves", BlockInit.darkwood_leaves);
         ItemInit.darkwood_sapling = blockItem(helper, "darkwood_sapling", BlockInit.darkwood_sapling);
         ItemInit.darkwood_planks = blockItem(helper, "darkwood_planks", BlockInit.darkwood_planks);
         ItemInit.darkwood_slab = blockItem(helper, "darkwood_slab", BlockInit.darkwood_slab);
         ItemInit.darkwood_stairs = blockItem(helper, "darkwood_stairs", BlockInit.darkwood_stairs);
         ItemInit.darkwood_fence = blockItem(helper, "darkwood_fence", BlockInit.darkwood_fence);
         ItemInit.darkwood_fence_gate = blockItem(helper, "darkwood_fence_gate", BlockInit.darkwood_fence_gate);
         ItemInit.darkwood_pressure_plate = blockItem(helper, "darkwood_pressure_plate", BlockInit.darkwood_pressure_plate);
         ItemInit.darkwood_button = blockItem(helper, "darkwood_button", BlockInit.darkwood_button);
         ItemInit.darkwood_trapdoor = blockItem(helper, "darkwood_trapdoor", BlockInit.darkwood_trapdoor);
         helper.register(location("darkwood_door"), ItemInit.darkwood_door = new DoubleHighBlockItem(BlockInit.darkwood_door, new Properties()));
         ItemInit.elder_log = blockItem(helper, "elder_log", BlockInit.elder_log);
         ItemInit.stripped_elder_log = blockItem(helper, "stripped_elder_log", BlockInit.stripped_elder_log);
         ItemInit.elder_wood = blockItem(helper, "elder_wood", BlockInit.elder_wood);
         ItemInit.stripped_elder_wood = blockItem(helper, "stripped_elder_wood", BlockInit.stripped_elder_wood);
         ItemInit.elder_leaves = blockItem(helper, "elder_leaves", BlockInit.elder_leaves);
         ItemInit.elder_sapling = blockItem(helper, "elder_sapling", BlockInit.elder_sapling);
         ItemInit.elder_planks = blockItem(helper, "elder_planks", BlockInit.elder_planks);
         ItemInit.elder_slab = blockItem(helper, "elder_slab", BlockInit.elder_slab);
         ItemInit.elder_stairs = blockItem(helper, "elder_stairs", BlockInit.elder_stairs);
         ItemInit.elder_fence = blockItem(helper, "elder_fence", BlockInit.elder_fence);
         ItemInit.elder_fence_gate = blockItem(helper, "elder_fence_gate", BlockInit.elder_fence_gate);
         ItemInit.elder_pressure_plate = blockItem(helper, "elder_pressure_plate", BlockInit.elder_pressure_plate);
         ItemInit.elder_button = blockItem(helper, "elder_button", BlockInit.elder_button);
         ItemInit.elder_trapdoor = blockItem(helper, "elder_trapdoor", BlockInit.elder_trapdoor);
         helper.register(location("elder_door"), ItemInit.elder_door = new DoubleHighBlockItem(BlockInit.elder_door, new Properties()));
         ItemInit.oak_beam = blockItem(helper, "oak_beam", BlockInit.oak_beam);
         ItemInit.spruce_beam = blockItem(helper, "spruce_beam", BlockInit.spruce_beam);
         ItemInit.birch_beam = blockItem(helper, "birch_beam", BlockInit.birch_beam);
         ItemInit.jungle_beam = blockItem(helper, "jungle_beam", BlockInit.jungle_beam);
         ItemInit.acacia_beam = blockItem(helper, "acacia_beam", BlockInit.acacia_beam);
         ItemInit.dark_oak_beam = blockItem(helper, "dark_oak_beam", BlockInit.dark_oak_beam);
         ItemInit.darkwood_beam = blockItem(helper, "darkwood_beam", BlockInit.darkwood_beam);
         ItemInit.stripped_darkwood_beam = blockItem(helper, "stripped_darkwood_beam", BlockInit.stripped_darkwood_beam);
         ItemInit.elder_beam = blockItem(helper, "elder_beam", BlockInit.elder_beam);
         ItemInit.oak_panels = blockItem(helper, "oak_panels", BlockInit.oak_panels);
         ItemInit.spruce_panels = blockItem(helper, "spruce_panels", BlockInit.spruce_panels);
         ItemInit.birch_panels = blockItem(helper, "birch_panels", BlockInit.birch_panels);
         ItemInit.jungle_panels = blockItem(helper, "jungle_panels", BlockInit.jungle_panels);
         ItemInit.acacia_panels = blockItem(helper, "acacia_panels", BlockInit.acacia_panels);
         ItemInit.dark_oak_panels = blockItem(helper, "dark_oak_panels", BlockInit.dark_oak_panels);
         ItemInit.darkwood_panels = blockItem(helper, "darkwood_panels", BlockInit.darkwood_panels);
         ItemInit.elder_panels = blockItem(helper, "elder_panels", BlockInit.elder_panels);

         // M3: 1.16.5 plants & food
         ItemInit.lavender = blockItem(helper, "lavender", BlockInit.lavender);
         helper.register(location("blueberries"), ItemInit.blueberries = new ItemNameBlockItem(BlockInit.blueberry_bush,
            new Properties().food(FoodInit.blueberries)));
         helper.register(location("elderberries"), ItemInit.elderberries = new Item(new Properties().food(FoodInit.elderberries)));
         helper.register(location("elderflower"), ItemInit.elderflower = new Item(new Properties()));
         helper.register(location("blueberry_muffin"), ItemInit.blueberry_muffin = new Item(new Properties().food(FoodInit.blueberry_muffin)));
         helper.register(location("apple_elderberry_crumble"), ItemInit.apple_elderberry_crumble = new Item(new Properties().food(FoodInit.apple_elderberry_crumble)));
         helper.register(location("elderflower_cordial"), ItemInit.elderflower_cordial = new DrinkableItem(new Properties().food(FoodInit.elderflower_cordial)));

         // M4: 1.16.5 functional blocks & gear
         ItemInit.white_soulfire = blockItem(helper, "white_soulfire", BlockInit.white_soulfire);
         ItemInit.orange_soulfire = blockItem(helper, "orange_soulfire", BlockInit.orange_soulfire);
         ItemInit.magenta_soulfire = blockItem(helper, "magenta_soulfire", BlockInit.magenta_soulfire);
         ItemInit.light_blue_soulfire = blockItem(helper, "light_blue_soulfire", BlockInit.light_blue_soulfire);
         ItemInit.yellow_soulfire = blockItem(helper, "yellow_soulfire", BlockInit.yellow_soulfire);
         ItemInit.lime_soulfire = blockItem(helper, "lime_soulfire", BlockInit.lime_soulfire);
         ItemInit.pink_soulfire = blockItem(helper, "pink_soulfire", BlockInit.pink_soulfire);
         ItemInit.gray_soulfire = blockItem(helper, "gray_soulfire", BlockInit.gray_soulfire);
         ItemInit.light_gray_soulfire = blockItem(helper, "light_gray_soulfire", BlockInit.light_gray_soulfire);
         ItemInit.cyan_soulfire = blockItem(helper, "cyan_soulfire", BlockInit.cyan_soulfire);
         ItemInit.purple_soulfire = blockItem(helper, "purple_soulfire", BlockInit.purple_soulfire);
         ItemInit.blue_soulfire = blockItem(helper, "blue_soulfire", BlockInit.blue_soulfire);
         ItemInit.brown_soulfire = blockItem(helper, "brown_soulfire", BlockInit.brown_soulfire);
         ItemInit.green_soulfire = blockItem(helper, "green_soulfire", BlockInit.green_soulfire);
         ItemInit.red_soulfire = blockItem(helper, "red_soulfire", BlockInit.red_soulfire);
         ItemInit.black_soulfire = blockItem(helper, "black_soulfire", BlockInit.black_soulfire);
         helper.register(location("fiery_torch"), ItemInit.fiery_torch = new StandingAndWallBlockItem(
            BlockInit.fiery_torch, BlockInit.wall_fiery_torch, new Properties(), Direction.DOWN));
         ItemInit.ceramic_lantern = blockItem(helper, "ceramic_lantern", BlockInit.ceramic_lantern);
         ItemInit.growth_lamp = blockItem(helper, "growth_lamp", BlockInit.growth_lamp);
         ItemInit.woodcutter = blockItem(helper, "woodcutter", BlockInit.woodcutter);
         helper.register(location("travel_pack"), ItemInit.travel_pack = new TravelPackItem(new Properties().stacksTo(1)));
         helper.register(location("bone_shield"), ItemInit.bone_shield = new BasicShieldItem(new Properties().durability(400), Items.BONE));
         helper.register(location("chitin_shield"), ItemInit.chitin_shield = new BasicShieldItem(new Properties().durability(750), ItemInit.chitin));
         helper.register(location("moonstone_shield"), ItemInit.moonstone_shield = new BasicShieldItem(new Properties().durability(1450), ItemInit.moonstone));

         // M5: 1.16.5 entities — spawn eggs, moth jars, moth eggs
         helper.register(location("dreadfish_spawn_egg"), ItemInit.dreadfish_spawn_egg =
            new ForgeSpawnEggItem(() -> EntityInit.dreadfish, 0xE2E2D9, 0xA775FF, new Properties()));
         helper.register(location("beetle_spawn_egg"), ItemInit.beetle_spawn_egg =
            new ForgeSpawnEggItem(() -> EntityInit.beetle, 0x57E5DC, 0x227589, new Properties()));
         helper.register(location("lunar_moth_spawn_egg"), ItemInit.lunar_moth_spawn_egg =
            new ForgeSpawnEggItem(() -> EntityInit.lunar_moth, 0x4AFFC2, 0x00CE89, new Properties()));
         helper.register(location("turquoise_lunar_moth_jar"), ItemInit.lunar_moth_jar_turquoise =
            new LunarMothJarItem(BlockInit.turquoise_lunar_moth_jar, LunarMothColors.TURQUOISE, new Properties()));
         helper.register(location("white_lunar_moth_jar"), ItemInit.lunar_moth_jar_white =
            new LunarMothJarItem(BlockInit.white_lunar_moth_jar, LunarMothColors.WHITE, new Properties()));
         helper.register(location("lime_lunar_moth_jar"), ItemInit.lunar_moth_jar_lime =
            new LunarMothJarItem(BlockInit.lime_lunar_moth_jar, LunarMothColors.LIME, new Properties()));
         helper.register(location("yellow_lunar_moth_jar"), ItemInit.lunar_moth_jar_yellow =
            new LunarMothJarItem(BlockInit.yellow_lunar_moth_jar, LunarMothColors.YELLOW, new Properties()));
         helper.register(location("orange_lunar_moth_jar"), ItemInit.lunar_moth_jar_orange =
            new LunarMothJarItem(BlockInit.orange_lunar_moth_jar, LunarMothColors.ORANGE, new Properties()));
         helper.register(location("pink_lunar_moth_jar"), ItemInit.lunar_moth_jar_pink =
            new LunarMothJarItem(BlockInit.pink_lunar_moth_jar, LunarMothColors.PINK, new Properties()));
         helper.register(location("turquoise_lunar_moth_egg"), ItemInit.lunar_moth_egg_turquoise =
            new LunarMothEggItem(LunarMothColors.TURQUOISE, new Properties()));
         helper.register(location("white_lunar_moth_egg"), ItemInit.lunar_moth_egg_white =
            new LunarMothEggItem(LunarMothColors.WHITE, new Properties()));
         helper.register(location("lime_lunar_moth_egg"), ItemInit.lunar_moth_egg_lime =
            new LunarMothEggItem(LunarMothColors.LIME, new Properties()));
         helper.register(location("yellow_lunar_moth_egg"), ItemInit.lunar_moth_egg_yellow =
            new LunarMothEggItem(LunarMothColors.YELLOW, new Properties()));
         helper.register(location("orange_lunar_moth_egg"), ItemInit.lunar_moth_egg_orange =
            new LunarMothEggItem(LunarMothColors.ORANGE, new Properties()));
         helper.register(location("pink_lunar_moth_egg"), ItemInit.lunar_moth_egg_pink =
            new LunarMothEggItem(LunarMothColors.PINK, new Properties()));

         DruidcraftRegrown.LOGGER.info("Items Registered!");
      });

      event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> {
         helper.register(location("rope"), BlockEntityInit.rope = BlockEntityInit.build(Builder.of(RopeTileEntity::new, BlockInit.rope)));
         helper.register(location("beam"), BlockEntityInit.beam = BlockEntityInit.build(Builder.of(BeamTileEntity::new, BlockInit.beam)));
         helper.register(location("crate"), BlockEntityInit.crate = BlockEntityInit.build(Builder.of(CrateTileEntity::new, BlockInit.oak_crate)));
         helper.register(location("growth_lamp"), BlockEntityInit.growth_lamp = BlockEntityInit.build(Builder.of(GrowthLampTileEntity::new, BlockInit.growth_lamp)));
         DruidcraftRegrown.LOGGER.info("Tile Entities Registered!");
      });

      event.register(ForgeRegistries.Keys.MENU_TYPES, helper -> {
         helper.register(location("crate_9x3"), ContainerInit.CRATE_9x3);
         helper.register(location("crate_9x6"), ContainerInit.CRATE_9x6);
         helper.register(location("crate_9x12"), ContainerInit.CRATE_9x12);
         helper.register(location("crate_9x24"), ContainerInit.CRATE_9x24);
         helper.register(location("woodcutter"), ContainerInit.WOODCUTTER);
         helper.register(location("travel_pack"), ContainerInit.TRAVEL_PACK);
         helper.register(location("beetle_inv"), ContainerInit.BEETLE_INV);
         DruidcraftRegrown.LOGGER.info("Containers Registered!");
      });

      event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> {
         helper.register(location("dreadfish"), EntityInit.dreadfish =
            EntityType.Builder.of(DreadfishEntity::new, MobCategory.MONSTER).sized(0.8F, 0.4F)
               .setShouldReceiveVelocityUpdates(true).setUpdateInterval(3).build("druidcraftrg:dreadfish"));
         helper.register(location("beetle"), EntityInit.beetle =
            EntityType.Builder.of(BeetleEntity::new, MobCategory.MONSTER).sized(1.5F, 1.5F)
               .setShouldReceiveVelocityUpdates(true).setUpdateInterval(3).build("druidcraftrg:beetle"));
         helper.register(location("lunar_moth"), EntityInit.lunar_moth =
            EntityType.Builder.of(LunarMothEntity::new, EntityInit.LUNAR_MOTH_CATEGORY).sized(0.5F, 0.5F)
               .setShouldReceiveVelocityUpdates(true).setUpdateInterval(3).build("druidcraftrg:lunar_moth"));
         DruidcraftRegrown.LOGGER.info("Entities Registered!");
      });

      event.register(ForgeRegistries.Keys.SOUND_EVENTS, helper -> {
         helper.register(location("fill_bottle"), SoundEventInit.fill_bottle);
         helper.register(location("open_crate"), SoundEventInit.open_crate);
         helper.register(location("close_crate"), SoundEventInit.close_crate);
         helper.register(location("woodcutter_take_result"), SoundEventInit.woodcutter_take_result);
      });

      event.register(ForgeRegistries.Keys.PARTICLE_TYPES, helper -> {
         helper.register(location("magic_smoke"), ParticleInit.magic_smoke);
         helper.register(location("fiery_glow"), ParticleInit.fiery_glow);
         helper.register(location("fiery_spark"), ParticleInit.fiery_spark);
         helper.register(location("magic_glitter"), ParticleInit.magic_glitter);
         DruidcraftRegrown.LOGGER.info("Particles Registered!");
      });

      event.register(ForgeRegistries.Keys.RECIPE_TYPES, helper ->
         helper.register(location("woodcutting"), RecipeInit.WOODCUTTING_TYPE));

      event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, helper ->
         helper.register(location("woodcutting"), RecipeInit.WOODCUTTING_SERIALIZER));

      event.register(ForgeRegistries.Keys.FEATURES, helper -> {
         helper.register(location("hemp"), FeatureInit.hemp);
         helper.register(location("elder_tree"), FeatureInit.elder_tree);
         DruidcraftRegrown.LOGGER.info("Features Registered!");
      });

      event.register(Registries.CREATIVE_MODE_TAB, helper -> helper.register(location("druidcraftrg"),
         CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.druidcraftrg"))
            .icon(() -> new ItemStack(ItemInit.hemp_seeds))
            .displayItems((params, output) -> {
               output.accept(ItemInit.knife);
               output.accept(ItemInit.hemp);
               output.accept(ItemInit.hemp_seeds);
               output.accept(ItemInit.rope);
               output.accept(ItemInit.platform);
               output.accept(ItemInit.beam);
               output.accept(ItemInit.white_bedroll);
               output.accept(ItemInit.orange_bedroll);
               output.accept(ItemInit.magenta_bedroll);
               output.accept(ItemInit.light_blue_bedroll);
               output.accept(ItemInit.yellow_bedroll);
               output.accept(ItemInit.lime_bedroll);
               output.accept(ItemInit.pink_bedroll);
               output.accept(ItemInit.gray_bedroll);
               output.accept(ItemInit.light_gray_bedroll);
               output.accept(ItemInit.cyan_bedroll);
               output.accept(ItemInit.purple_bedroll);
               output.accept(ItemInit.blue_bedroll);
               output.accept(ItemInit.brown_bedroll);
               output.accept(ItemInit.green_bedroll);
               output.accept(ItemInit.red_bedroll);
               output.accept(ItemInit.black_bedroll);
               output.accept(ItemInit.crate);
               output.accept(ItemInit.amber);
               output.accept(ItemInit.moonstone);
               output.accept(ItemInit.fiery_glass);
               output.accept(ItemInit.rockroot);
               output.accept(ItemInit.chitin);
               output.accept(ItemInit.bone_sword);
               output.accept(ItemInit.bone_shovel);
               output.accept(ItemInit.bone_pickaxe);
               output.accept(ItemInit.bone_axe);
               output.accept(ItemInit.bone_hoe);
               output.accept(ItemInit.bone_sickle);
               output.accept(ItemInit.bone_helmet);
               output.accept(ItemInit.bone_chestplate);
               output.accept(ItemInit.bone_leggings);
               output.accept(ItemInit.bone_boots);
               output.accept(ItemInit.chitin_helmet);
               output.accept(ItemInit.chitin_chestplate);
               output.accept(ItemInit.chitin_leggings);
               output.accept(ItemInit.chitin_boots);
               output.accept(ItemInit.moonstone_sword);
               output.accept(ItemInit.moonstone_shovel);
               output.accept(ItemInit.moonstone_pickaxe);
               output.accept(ItemInit.moonstone_axe);
               output.accept(ItemInit.moonstone_hoe);
               output.accept(ItemInit.moonstone_sickle);
               output.accept(ItemInit.moonstone_helmet);
               output.accept(ItemInit.moonstone_chestplate);
               output.accept(ItemInit.moonstone_leggings);
               output.accept(ItemInit.moonstone_boots);
               output.accept(ItemInit.wooden_sickle);
               output.accept(ItemInit.stone_sickle);
               output.accept(ItemInit.iron_sickle);
               output.accept(ItemInit.gold_sickle);
               output.accept(ItemInit.diamond_sickle);
               output.accept(ItemInit.amber_ore);
               output.accept(ItemInit.moonstone_ore);
               output.accept(ItemInit.fiery_glass_ore);
               output.accept(ItemInit.rockroot_ore);
               output.accept(ItemInit.amber_block);
               output.accept(ItemInit.moonstone_block);
               output.accept(ItemInit.fiery_glass_block);
               output.accept(ItemInit.rockroot_block);
               output.accept(ItemInit.wet_mud_bricks);
               output.accept(ItemInit.dry_mud_bricks);
               output.accept(ItemInit.dry_mud_brick_slab);
               output.accept(ItemInit.dry_mud_brick_stairs);
               output.accept(ItemInit.dry_mud_brick_wall);
               output.accept(ItemInit.darkwood_log);
               output.accept(ItemInit.stripped_darkwood_log);
               output.accept(ItemInit.darkwood_wood);
               output.accept(ItemInit.stripped_darkwood_wood);
               output.accept(ItemInit.darkwood_leaves);
               output.accept(ItemInit.darkwood_sapling);
               output.accept(ItemInit.darkwood_planks);
               output.accept(ItemInit.darkwood_slab);
               output.accept(ItemInit.darkwood_stairs);
               output.accept(ItemInit.darkwood_fence);
               output.accept(ItemInit.darkwood_fence_gate);
               output.accept(ItemInit.darkwood_pressure_plate);
               output.accept(ItemInit.darkwood_button);
               output.accept(ItemInit.darkwood_trapdoor);
               output.accept(ItemInit.darkwood_door);
               output.accept(ItemInit.elder_log);
               output.accept(ItemInit.stripped_elder_log);
               output.accept(ItemInit.elder_wood);
               output.accept(ItemInit.stripped_elder_wood);
               output.accept(ItemInit.elder_leaves);
               output.accept(ItemInit.elder_sapling);
               output.accept(ItemInit.elder_planks);
               output.accept(ItemInit.elder_slab);
               output.accept(ItemInit.elder_stairs);
               output.accept(ItemInit.elder_fence);
               output.accept(ItemInit.elder_fence_gate);
               output.accept(ItemInit.elder_pressure_plate);
               output.accept(ItemInit.elder_button);
               output.accept(ItemInit.elder_trapdoor);
               output.accept(ItemInit.elder_door);
               output.accept(ItemInit.oak_beam);
               output.accept(ItemInit.spruce_beam);
               output.accept(ItemInit.birch_beam);
               output.accept(ItemInit.jungle_beam);
               output.accept(ItemInit.acacia_beam);
               output.accept(ItemInit.dark_oak_beam);
               output.accept(ItemInit.darkwood_beam);
               output.accept(ItemInit.stripped_darkwood_beam);
               output.accept(ItemInit.elder_beam);
               output.accept(ItemInit.oak_panels);
               output.accept(ItemInit.spruce_panels);
               output.accept(ItemInit.birch_panels);
               output.accept(ItemInit.jungle_panels);
               output.accept(ItemInit.acacia_panels);
               output.accept(ItemInit.dark_oak_panels);
               output.accept(ItemInit.darkwood_panels);
               output.accept(ItemInit.elder_panels);
               output.accept(ItemInit.lavender);
               output.accept(ItemInit.blueberries);
               output.accept(ItemInit.elderberries);
               output.accept(ItemInit.elderflower);
               output.accept(ItemInit.blueberry_muffin);
               output.accept(ItemInit.apple_elderberry_crumble);
               output.accept(ItemInit.elderflower_cordial);
               output.accept(ItemInit.white_soulfire);
               output.accept(ItemInit.orange_soulfire);
               output.accept(ItemInit.magenta_soulfire);
               output.accept(ItemInit.light_blue_soulfire);
               output.accept(ItemInit.yellow_soulfire);
               output.accept(ItemInit.lime_soulfire);
               output.accept(ItemInit.pink_soulfire);
               output.accept(ItemInit.gray_soulfire);
               output.accept(ItemInit.light_gray_soulfire);
               output.accept(ItemInit.cyan_soulfire);
               output.accept(ItemInit.purple_soulfire);
               output.accept(ItemInit.blue_soulfire);
               output.accept(ItemInit.brown_soulfire);
               output.accept(ItemInit.green_soulfire);
               output.accept(ItemInit.red_soulfire);
               output.accept(ItemInit.black_soulfire);
               output.accept(ItemInit.fiery_torch);
               output.accept(ItemInit.ceramic_lantern);
               output.accept(ItemInit.growth_lamp);
               output.accept(ItemInit.woodcutter);
               output.accept(ItemInit.travel_pack);
               output.accept(ItemInit.bone_shield);
               output.accept(ItemInit.chitin_shield);
               output.accept(ItemInit.moonstone_shield);
               output.accept(ItemInit.dreadfish_spawn_egg);
               output.accept(ItemInit.beetle_spawn_egg);
               output.accept(ItemInit.lunar_moth_spawn_egg);
               output.accept(ItemInit.lunar_moth_jar_turquoise);
               output.accept(ItemInit.lunar_moth_jar_white);
               output.accept(ItemInit.lunar_moth_jar_lime);
               output.accept(ItemInit.lunar_moth_jar_yellow);
               output.accept(ItemInit.lunar_moth_jar_orange);
               output.accept(ItemInit.lunar_moth_jar_pink);
               output.accept(ItemInit.lunar_moth_egg_turquoise);
               output.accept(ItemInit.lunar_moth_egg_white);
               output.accept(ItemInit.lunar_moth_egg_lime);
               output.accept(ItemInit.lunar_moth_egg_yellow);
               output.accept(ItemInit.lunar_moth_egg_orange);
               output.accept(ItemInit.lunar_moth_egg_pink);
            })
            .build()));
   }

   private static BlockBehaviour.Properties logProps() {
      return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD).ignitedByLava();
   }

   private static BlockBehaviour.Properties planksProps() {
      return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava();
   }

   private static BlockBehaviour.Properties leavesProps() {
      return BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()
         .isValidSpawn((state, level, pos, type) -> false)
         .isSuffocating((state, level, pos) -> false)
         .isViewBlocking((state, level, pos) -> false)
         .ignitedByLava().pushReaction(PushReaction.DESTROY);
   }

   private static BlockBehaviour.Properties saplingProps() {
      return BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak()
         .sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY);
   }

   private static BlockBehaviour.Properties plateProps() {
      return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).forceSolidOn().noCollission().strength(0.5F)
         .ignitedByLava().pushReaction(PushReaction.DESTROY);
   }

   private static BlockBehaviour.Properties buttonProps() {
      return BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
   }

   private static BlockBehaviour.Properties trapdoorProps() {
      return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(3.0F).noOcclusion()
         .isValidSpawn((state, level, pos, type) -> false).ignitedByLava();
   }

   private static BlockBehaviour.Properties doorProps() {
      return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(3.0F).noOcclusion()
         .ignitedByLava().pushReaction(PushReaction.DESTROY);
   }

   private static Block registerBeam(RegisterEvent.RegisterHelper<Block> helper, String name) {
      Block block = new RotatedPillarBlock(logProps());
      helper.register(location(name), block);
      return block;
   }

   private static Block registerPanels(RegisterEvent.RegisterHelper<Block> helper, String name) {
      Block block = new Block(planksProps());
      helper.register(location(name), block);
      return block;
   }

   private static Item blockItem(RegisterEvent.RegisterHelper<Item> helper, String name, Block block) {
      Item item = new BlockItem(block, new Properties());
      helper.register(location(name), item);
      return item;
   }

   private static Block registerBedroll(RegisterEvent.RegisterHelper<Block> helper, String name, DyeColor color) {
      Block block = new BedrollBlock(color, BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).sound(SoundType.WOOL).strength(1.0F));
      helper.register(location(name), block);
      return block;
   }

   private static Item registerBedrollItem(RegisterEvent.RegisterHelper<Item> helper, String name, Block block) {
      Item item = new BedItem(block, new Properties().stacksTo(1));
      helper.register(location(name), item);
      return item;
   }

   private static Block registerSoulfire(RegisterEvent.RegisterHelper<Block> helper, String name, DyeColor color) {
      Block block = new SoulfireBlock(color, BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable()
         .sound(SoundType.SNOW).instabreak().noCollission().lightLevel(state -> 13).noOcclusion()
         .pushReaction(PushReaction.DESTROY));
      helper.register(location(name), block);
      return block;
   }

   private static BlockBehaviour.Properties torchProps() {
      return BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(state -> 15)
         .sound(SoundType.BAMBOO).pushReaction(PushReaction.DESTROY);
   }

   private static Block registerMothJar(RegisterEvent.RegisterHelper<Block> helper, String name, int mothColor) {
      Block block = new LunarMothJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE)
         .sound(SoundType.GLASS).strength(1.0F).lightLevel(state -> 10).randomTicks().noOcclusion(), mothColor);
      helper.register(location(name), block);
      return block;
   }

   @SubscribeEvent
   public static void entityAttributes(EntityAttributeCreationEvent event) {
      event.put(EntityInit.dreadfish, DreadfishEntity.bakeAttributes().build());
      event.put(EntityInit.beetle, BeetleEntity.bakeAttributes().build());
      event.put(EntityInit.lunar_moth, LunarMothEntity.bakeAttributes().build());
      DruidcraftRegrown.LOGGER.info("Entity Attributes Registered!");
   }

   @SubscribeEvent
   public static void capabilityRegistryEvent(RegisterCapabilitiesEvent event) {
      event.register(TempSpawnCapability.class);
      DruidcraftRegrown.LOGGER.info("Capabilities Registered!");
   }

   public static ResourceLocation location(String name) {
      return new ResourceLocation("druidcraftrg", name);
   }
}
